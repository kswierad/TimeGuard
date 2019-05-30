package pl.agh.cs.io.controller;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.agh.cs.io.*;
import pl.agh.cs.io.api.windows.WindowsListenerRunner;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.io.File;
import java.util.List;

public class TimeGuardController {


    public Rules rules;
    TimeCounterController timeCounterController = new TimeCounterController();

    @FXML
    ListView<ImgWithPath> listViewOfRules;
    private ObservableList<ImgWithPath> rulesWithIconObservableList;

    @FXML
    public void initialize() {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(2);
        WindowsListenerRunner windowsListenerRunner = new WindowsListenerRunner(scheduledExecutorService);
        rules = new Rules();
        windowsListenerRunner.run(snapshot -> {
            rules.accept(snapshot);
            timeCounterController.accept(snapshot.getForegroundWindowProcessIdsPerPath(), rules.getRulesCopy());
        });

        rulesWithIconObservableList = FXCollections.observableArrayList();
        listViewOfRules.setItems(rulesWithIconObservableList);
        listViewOfRules.setCellFactory(rulesListView -> new RuleListViewCell());
        rules.rulesProperty().addListener(
                (MapChangeListener.Change<? extends String, ? extends Rule> change) -> {
                    if (change.wasRemoved()) {
                        //listOfRules.getItems().remove(NameConverter.nameFromPath(change.getKey()));
                        //NameConverter.nameToPath.remove(NameConverter.nameFromPath(change.getKey()));
                        rulesWithIconObservableList.remove(NameConverter.nameToImgWithPath.get(NameConverter.nameFromPath(change.getKey())));
                        NameConverter.nameToImgWithPath.remove(NameConverter.nameFromPath(change.getKey()));

                    }
                    if (change.wasAdded()) {
                        //listOfRules.getItems().add(NameConverter.nameFromPath(change.getKey()));
                        //NameConverter.nameToPath.put(NameConverter.nameFromPath(change.getKey()), change.getKey());
                        ImgWithPath newRule = new ImgWithPath(change.getKey());
                        //listViewOfRules.getItems().add(newRule);
                        rulesWithIconObservableList.add(newRule);
                        NameConverter.nameToImgWithPath.put(NameConverter.nameFromPath(change.getKey()), newRule);


                    }
                }

        );
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
        File file = fileChooser.showOpenDialog(listViewOfRules.getScene().getWindow());
        if (file != null) {
            rules.addRule(new Rule(file.getAbsolutePath()));
        }
    }

    @FXML
    private void addFiles(ActionEvent event) throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose any file to monitor:");
        List<File> files = fileChooser.showOpenMultipleDialog(listViewOfRules.getScene().getWindow());
        if (files != null) {
            for (File file : files) {
                rules.addRule(new Rule(file.getAbsolutePath()));
            }
        }
    }

    @FXML
    private void removeRule(ActionEvent event) {
        //listOfRules.getItems().remove(listOfRules.getSelectionModel().getSelectedItem());
        //rules.removeRule(NameConverter.nameToPath.get(listOfRules.getSelectionModel().getSelectedItem()));
        rules.removeRule(listViewOfRules.getSelectionModel().getSelectedItem().getPath());
    }

    @FXML
    private void editRule(ActionEvent event) {

    }

}
