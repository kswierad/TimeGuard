package pl.agh.cs.io.controller;

import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.agh.cs.io.Rule;
import pl.agh.cs.io.Rules;
import pl.agh.cs.io.TimeCounterController;
import pl.agh.cs.io.api.windows.WindowsListenerRunner;
import pl.agh.cs.io.TimeGuard;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class TimeGuardController {


    public Rules rules;
    TimeCounterController timeCounterController = new TimeCounterController();

    @FXML
    ListView<String> listOfRules;

    @FXML
    public void initialize() {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(2);
        WindowsListenerRunner windowsListenerRunner = new WindowsListenerRunner(scheduledExecutorService);
        rules = new Rules();
        windowsListenerRunner.run(snapshot -> {
            rules.accept(snapshot);
            timeCounterController.accept(snapshot.getForegroundWindowProcessIdsPerPath(), rules.getRulesCopy());
        });
        rules.rulesProperty().addListener(
                (MapChangeListener.Change<? extends String, ? extends Rule> change) -> {
                    if (change.wasRemoved()) {
                        listOfRules.getItems().remove(change.getKey());
                    }
                    if (change.wasAdded()) {
                        listOfRules.getItems().add(change.getKey());
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
        Scene scene = new Scene(rootLayout, 450, 600);

        statsWindow.setScene(scene);
        statsWindow.show();
    }

    @FXML
    private void addRule(ActionEvent event) throws Exception {
        Stage addRuleWindow = new Stage();
        addRuleWindow.setTitle("Adding new Rule");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TimeGuard.class.getResource("/addRule.fxml"));

        GridPane rootLayout = loader.load();
        ((AddRuleController) loader.getController()).setRules(rules);
        Scene scene = new Scene(rootLayout, 450, 150);

        addRuleWindow.setScene(scene);
        addRuleWindow.show();
    }

    @FXML
    private void deleteRule(ActionEvent event) throws Exception {
        Stage removeRuleWindow = new Stage();
        removeRuleWindow.setTitle("Removing new Rule");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TimeGuard.class.getResource("/removeRule.fxml"));

        GridPane rootLayout = loader.load();
        ((RemoveRuleController) loader.getController()).setRules(rules);
        Scene scene = new Scene(rootLayout, 450, 150);

        removeRuleWindow.setScene(scene);
        removeRuleWindow.show();
    }

}
