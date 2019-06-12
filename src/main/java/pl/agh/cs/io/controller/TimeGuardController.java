package pl.agh.cs.io.controller;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.agh.cs.io.Autostart;
import pl.agh.cs.io.TimeGuard;
import pl.agh.cs.io.RuleListViewCell;
import pl.agh.cs.io.ImgWithPath;
import pl.agh.cs.io.api.files.FilesListenerRunner;
import pl.agh.cs.io.api.windows.WindowsListenerRunner;
import pl.agh.cs.io.counter.TimeCounterController;
import pl.agh.cs.io.model.FileRule;
import pl.agh.cs.io.model.FileRules;
import pl.agh.cs.io.model.Rule;
import pl.agh.cs.io.model.Rules;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.io.File;
import java.util.List;

public class TimeGuardController {


    public Rules rules;
    public FileRules fileRules;
    TimeCounterController timeCounterController = new TimeCounterController();

    @FXML
    ListView<ImgWithPath> listViewOfRules;
    private ObservableList<ImgWithPath> rulesWithIconObservableList;

    @FXML
    Tab filesTab;
    @FXML
    Tab programsTab;
    @FXML
    ListView<String> listOfFileRules;

    @FXML
    public void initialize() {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(3);
        WindowsListenerRunner windowsListenerRunner = new WindowsListenerRunner(scheduledExecutorService);
        rules = new Rules(scheduledExecutorService);
        FilesListenerRunner filesListenerRunner = new FilesListenerRunner(scheduledExecutorService);
        fileRules = new FileRules();
        windowsListenerRunner.run(snapshot -> {
            rules.accept(snapshot);
            timeCounterController.accept(snapshot.getForegroundWindowProcessIdsPerPath(), rules.getRulesCopy());
        });

        rulesWithIconObservableList = FXCollections.observableArrayList();
        listViewOfRules.setItems(rulesWithIconObservableList);
        listViewOfRules.setCellFactory(rulesListView -> new RuleListViewCell());
        filesListenerRunner.run(fileRules::accept);
        rules.rulesProperty().addListener(
                (MapChangeListener.Change<? extends String, ? extends Rule> change) -> {
                    if (change.wasRemoved()) {
                        rulesWithIconObservableList.remove(NameConverter.nameToImgWithPath.get(
                                NameConverter.nameFromPath(change.getKey())
                        ));
                        NameConverter.nameToImgWithPath.remove(NameConverter.nameFromPath(change.getKey()));

                    }
                    if (change.wasAdded()) {
                        ImgWithPath newRule = new ImgWithPath(change.getKey());
                        rulesWithIconObservableList.add(newRule);
                        NameConverter.nameToImgWithPath.put(NameConverter.nameFromPath(change.getKey()), newRule);


                    }
                }

        );
        fileRules.fileRulesProperty().addListener(
                (MapChangeListener.Change<? extends String, ? extends FileRule> change) -> {
                    if (change.wasRemoved()) {
                        listOfFileRules.getItems().remove(NameConverter.nameFromPath(change.getKey()));
                        NameConverter.nameToPath.remove(NameConverter.nameFromPath(change.getKey()));
                    }
                    if (change.wasAdded()) {

                        listOfFileRules.getItems().add(NameConverter.nameFromPath(change.getKey()));
                        NameConverter.nameToPath.put(NameConverter.nameFromPath(change.getKey()), change.getKey());
                    }
                }
        );
        rules.getRules().keySet().forEach(key -> {
            listOfFileRules.getItems().add(NameConverter.nameFromPath(key));
            NameConverter.nameToPath.put(NameConverter.nameFromPath(key), key);
        });


    }

    public TimeCounterController getTimeCounterController() {
        return timeCounterController;
    }

    @FXML
    private void showStats(ActionEvent event) throws Exception {
        Stage statsWindow = new Stage();
        statsWindow.setTitle("Statistics");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TimeGuard.class.getResource("/statistics.fxml"));

        VBox rootLayout = loader.load();
        ((StatsController) loader.getController()).setRules(fileRules, rules);
        Scene scene = new Scene(rootLayout, 800, 600);

        statsWindow.setScene(scene);
        statsWindow.show();
    }

    @FXML
    private void addExe(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a .exe file to monitor:");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("executable", "*.exe"));
        File file = fileChooser.showOpenDialog(listViewOfRules.getScene().getWindow());
        if (file != null) {
            if (NameConverter.nameToPath.containsValue(file.getAbsolutePath())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Wrong program");
                alert.setHeaderText("The program you were trying to add is already tracked");
                alert.setContentText("If you want to change the path for the program edit it, \n" +
                        "or delete it and add it again");
                alert.showAndWait();
            } else {
                rules.addRule(new Rule(file.getAbsolutePath()));
            }
        }
    }

    @FXML
    private void addFiles(ActionEvent event) throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose any file to monitor:");
        List<File> files = fileChooser.showOpenMultipleDialog(listViewOfRules.getScene().getWindow());
        if (files != null) {
            for (File file : files) {
                if (NameConverter.nameToPath.containsValue(file.getAbsolutePath())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Wrong file");
                    alert.setHeaderText("The file you were trying to add is already tracked");
                    alert.setContentText("If you want to change the path for the file edit it, \n" +
                            "or delete it and add it again");
                    alert.showAndWait();
                } else {
                    fileRules.addFileRule(new FileRule(file.getAbsolutePath()));
                }
            }
        }
    }

    @FXML
    private void removeRule(ActionEvent event) {
        if (programsTab.isSelected()) {
            rules.removeRule(listViewOfRules.getSelectionModel().getSelectedItem().getPath());
        }

        if (filesTab.isSelected()) {
            fileRules.removeFileRule(
                    NameConverter.nameToPath.get(listOfFileRules.getSelectionModel().getSelectedItem())
            );
        }
    }

    @FXML
    private void help(ActionEvent event) throws IOException, URISyntaxException {
        Desktop.getDesktop()
                .browse(new URI("mailto:kam.swierad@gmail.com"));

    }

    @FXML
    private void editRule(ActionEvent event) throws Exception {
        if (programsTab.isSelected()) {
            String path = listViewOfRules.getSelectionModel().getSelectedItem().getPath();
            Rule toEdit = rules.rulesProperty().get(path);
            if (toEdit != null) {
                Stage editWindow = new Stage();
                editWindow.setTitle("Rule");
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(TimeGuard.class.getResource("/editProgramRestriction.fxml"));

                VBox rootLayout = loader.load();
                ((EditProgramController) loader.getController()).setRule(toEdit);
                Scene scene = new Scene(rootLayout);

                editWindow.setScene(scene);
                editWindow.show();
            }
        } else if (filesTab.isSelected()) {
            String path = NameConverter.nameToPath.get(listOfFileRules.getSelectionModel().getSelectedItem());
            FileRule toEdit = fileRules.fileRulesProperty().get(path);
            if (toEdit != null) {
                Stage editWindow = new Stage();
                editWindow.setTitle("Rule");
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(TimeGuard.class.getResource("/editFileRestriction.fxml"));

                VBox rootLayout = loader.load();
                ((EditFileController) loader.getController()).setRule(toEdit);
                Scene scene = new Scene(rootLayout);

                editWindow.setScene(scene);
                editWindow.show();
            }
        }
    }

    @FXML
    private void addToAutostart(ActionEvent event) throws Exception {
        if (((CheckMenuItem) event.getSource()).isSelected()) {
            Autostart.writeAutostartFile();
        } else {
            Autostart.deleteAutostartFile();
        }
    }
}
