package pl.agh.cs.io.model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import pl.agh.cs.io.ExceededUsageAction;
import pl.agh.cs.io.Utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public class RuleRestriction {

    public final WindowState state;
    public final long permittedNumSec;
    private int extraTime = 0;

    private ExceededUsageAction action;
    private Rule rule;

    private long lastNotification;
    private long numOfSecBetweenNotifications = 5;


    public RuleRestriction(WindowState state, long permittedNumSec, ExceededUsageAction action) {
        this.permittedNumSec = permittedNumSec;
        this.state = state;
        this.lastNotification = 0;
        this.action = action;
    }

    public void checkRestriction() {
        if (rule != null && permittedNumSec > 0) {
            double usedToday = ActivityTime.getActivityTimeFromList(rule.getTimes(), state);

            if (usedToday > permittedNumSec + extraTime) {
                long currentTime = Timestamp.valueOf(LocalDateTime.now()).getTime();
                //notify or call method every numOfSecBetweenNotifications seconds after time is exceeded
                if (currentTime > lastNotification + (1000 * numOfSecBetweenNotifications)) {
                    lastNotification = currentTime;
                    //TODO Notify user
                    if (action == ExceededUsageAction.CLOSE) {
                        handleClose();
                    }
                    System.out.println("Time used up for " + rule.getExePath() + ", " + action);
                }
            }
        }
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

    private void handleClose(){
        ButtonType buttonExtend = new ButtonType("Please, more time...");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Your time for this application has exceeded",
                buttonExtend,
                ButtonType.OK
        );
        alert.setHeaderText(null);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            // kill app
        }
        else {
            TextInputDialog dialog = new TextInputDialog("01:00");
            dialog.setTitle("Rule time extension");
            dialog.setHeaderText(null);
            dialog.setContentText("Extra time (hh:mm)");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(time -> {
                extraTime += Utils.timeToLong(time);
                System.out.println(String.format("Added extra time: %s", extraTime));
            });
        }
    }

}
