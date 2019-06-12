package pl.agh.cs.io.controller;

import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.agh.cs.io.Rule;
import pl.agh.cs.io.Rules;
import pl.agh.cs.io.TimeCounterController;
import pl.agh.cs.io.api.windows.WindowsListenerRunner;
import pl.agh.cs.io.TimeGuard;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.io.File;
import java.util.List;

public class TimeGuardController {


    public Rules rules;
    TimeCounterController timeCounterController = new TimeCounterController();

    @FXML
    ListView<String> listOfRules;

    @FXML
    public void initialize() {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(3);
        WindowsListenerRunner windowsListenerRunner = new WindowsListenerRunner(scheduledExecutorService);
        rules = new Rules(scheduledExecutorService);
        windowsListenerRunner.run(snapshot -> {
            rules.accept(snapshot);
            timeCounterController.accept(snapshot.getForegroundWindowProcessIdsPerPath(), rules.getRulesCopy());
        });
        rules.rulesProperty().addListener(
                (MapChangeListener.Change<? extends String, ? extends Rule> change) -> {
                    if (change.wasRemoved()) {
                        listOfRules.getItems().remove(NameConverter.nameFromPath(change.getKey()));
                        NameConverter.nameToPath.remove(NameConverter.nameFromPath(change.getKey()));
                    }
                    if (change.wasAdded()) {
                        listOfRules.getItems().add(NameConverter.nameFromPath(change.getKey()));
                        NameConverter.nameToPath.put(NameConverter.nameFromPath(change.getKey()), change.getKey());
                    }
                }
        );
        rules.getRules().keySet().forEach(key -> {
            listOfRules.getItems().add(NameConverter.nameFromPath(key));
            NameConverter.nameToPath.put(NameConverter.nameFromPath(key), key);
        });


    }

    @FXML
    private void showStats(ActionEvent event) throws Exception {
        Stage statsWindow = new Stage();
        statsWindow.setTitle("Statistics");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TimeGuard.class.getResource("/statistics.fxml"));

        VBox rootLayout = loader.load();
        ((StatsController) loader.getController()).setRules(rules);
        Scene scene = new Scene(rootLayout, 800, 600);

        statsWindow.setScene(scene);
        statsWindow.show();
    }

    @FXML
    private void addExe(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a .exe file to monitor:");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("executable", "*.exe"));
        File file = fileChooser.showOpenDialog(listOfRules.getScene().getWindow());
        if (file != null) {
            rules.addRule(new Rule(file.getAbsolutePath()));
        }
    }

    @FXML
    private void addFiles(ActionEvent event) throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose any file to monitor:");
        List<File> files = fileChooser.showOpenMultipleDialog(listOfRules.getScene().getWindow());
        if (files != null) {
            for (File file : files) {
                rules.addRule(new Rule(file.getAbsolutePath()));
            }
        }
    }

    @FXML
    private void removeRule(ActionEvent event) {
        //listOfRules.getItems().remove(listOfRules.getSelectionModel().getSelectedItem());
        rules.removeRule(NameConverter.nameToPath.get(listOfRules.getSelectionModel().getSelectedItem()));
    }

    @FXML
    private void editRule(ActionEvent event) throws Exception {
        String path = NameConverter.nameToPath.get(listOfRules.getSelectionModel().getSelectedItem());
        Rule toEdit = rules.rulesProperty().get(path);
        if (toEdit != null) {
            Stage editWindow = new Stage();
            editWindow.setTitle("Rule");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TimeGuard.class.getResource("/editRestriction.fxml"));

            VBox rootLayout = loader.load();
            ((EditController) loader.getController()).setRule(toEdit);
            Scene scene = new Scene(rootLayout, 450, 250);

            editWindow.setScene(scene);
            editWindow.show();
        }
    }

}
