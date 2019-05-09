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
import pl.agh.cs.io.WindowsListenerRunner;
import pl.agh.cs.io.TimeGuard;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeGuardController {


    public Rules rules;
    TimeCounterController timeCounterController = new TimeCounterController();

    @FXML
    ListView<String> listOfRules;

    Map<String, String> nameToPath = new HashMap<>();



    @FXML
    public void initialize() {
        rules = new Rules();
        WindowsListenerRunner.run(snapshot -> {
            rules.accept(snapshot);
            timeCounterController.accept(snapshot.getForegroundWindow(), rules.getRulesCopy());
        });
        rules.rulesProperty().addListener(
                (MapChangeListener.Change<? extends String, ? extends Rule> change) -> {
                    if (change.wasRemoved()) {
                        listOfRules.getItems().remove(nameFromPath(change.getKey()));
                        nameToPath.remove(nameFromPath(change.getKey()));
                    }
                    if (change.wasAdded()) {
                        listOfRules.getItems().add(nameFromPath(change.getKey()));
                        nameToPath.put(nameFromPath(change.getKey()), change.getKey());
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
        for (File file : files) {
            rules.addRule(new Rule(file.getAbsolutePath()));
        }
    }

    @FXML
    private void removeRule(ActionEvent event) {
        //listOfRules.getItems().remove(listOfRules.getSelectionModel().getSelectedItem());
        rules.removeRule(nameToPath.get(listOfRules.getSelectionModel().getSelectedItem()));
    }

    @FXML
    private void editRule(ActionEvent event) {

    }

    private String nameFromPath(String path) {
        String tokens[] = path.split("/");
        return tokens[tokens.length - 1];
    }

}
