package pl.agh.cs.io.model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import pl.agh.cs.io.ExceededUsageAction;
import pl.agh.cs.io.Utils;
import pl.agh.cs.io.api.ProcessIdsPerPath;
import pl.agh.cs.io.controller.NameConverter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class RuleRestriction {

    public final WindowState state;
    public final long permittedNumSec;
    private int extraTime = 0;

    private ExceededUsageAction action;
    private Rule rule;

    private long lastNotification;
    private long numOfSecBetweenNotifications = 5;
    private boolean isDisplayed = false;


    public RuleRestriction(WindowState state, long permittedNumSec, ExceededUsageAction action) {
        this.permittedNumSec = permittedNumSec;
        this.state = state;
        this.lastNotification = 0;
        this.action = action;
    }

    public void checkRestriction(ProcessIdsPerPath processes) {
        if (rule != null && permittedNumSec > 0) {
            double usedToday = ActivityTime.getActivityTimeFromList(rule.getTimes(), state);

            if (usedToday > permittedNumSec + extraTime) {
                long currentTime = Timestamp.valueOf(LocalDateTime.now()).getTime();
                //notify or call method every numOfSecBetweenNotifications seconds after time is exceeded
                if (currentTime > lastNotification + (1000 * numOfSecBetweenNotifications)) {
                    lastNotification = currentTime;
                    //TODO Notify user
                    if (action == ExceededUsageAction.CLOSE) {
                        handleClose(processes);
                    }
                    else {
                        if(!isDisplayed) {
                            this.isDisplayed = true;
                            showAlert(rule);
                            this.isDisplayed = false;
                        }
                    }
                    System.out.println("Time used up for " + rule.getExePath() + ", " + action);
                }
            }
        }
    }

    public static void showAlert(Rule rule) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("WARNING");
        alert.setContentText("Time on " + NameConverter.nameFromPath(rule.getExePath()) + " exceeded");
        alert.showAndWait();
    }


    public ExceededUsageAction getAction() {
        return action;
    }

    public void clearExtraTime() {
        extraTime = 0;
    }

    public void setAction(ExceededUsageAction action) {
        this.action = action;
    }

    public WindowState getState() {
        return state;
    }

    public long getPermittedNumSec() {
        return permittedNumSec;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    private void handleClose(ProcessIdsPerPath processes) {
//        ButtonType buttonExtend = new ButtonType("Please, more time...");
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
//                String.format("Your time for application %s has exceeded", rule.getExePath()),
//                buttonExtend,
//                ButtonType.OK
//        );
//        alert.setHeaderText(null);
//        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//        stage.setAlwaysOnTop(true);
//        alert.showAndWait();

//        if (alert.getResult() == ButtonType.OK) {
            processes.terminateProcesses();
//        } else {
//            TextInputDialog dialog = new TextInputDialog("01:00");
//            dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK);
//            dialog.setTitle("Rule time extension");
//            dialog.setHeaderText(null);
//            dialog.setContentText("Extra time (hh:mm)");
//            stage = (Stage) dialog.getDialogPane().getScene().getWindow();
//            stage.setAlwaysOnTop(true);
//            Optional<String> result = dialog.showAndWait();
//            result.ifPresent(time -> extraTime += Utils.timeToLong(time));
//        }
    }

}
