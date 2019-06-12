package pl.agh.cs.io.model;

import javafx.scene.control.Alert;
import pl.agh.cs.io.ExceededUsageAction;
import pl.agh.cs.io.controller.NameConverter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class RuleRestriction {

    public final WindowState state;
    public final long permittedNumSec;

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

    public void checkRestriction() {
        if (rule != null && rule.getRestriction().get().permittedNumSec > 0) {
            double usedToday = ActivityTime.getActivityTimeFromList(rule.getTimes(), rule.getRestriction().get().state);

            if (usedToday > rule.getRestriction().get().permittedNumSec) {
                ;
                long currentTime = Timestamp.valueOf(LocalDateTime.now()).getTime();
                //notify or call method every numOfSecBetweenNotifications seconds after time is exceeded
                if (!isDisplayed &&
                        currentTime >
                        rule.getRestriction().get().lastNotification +
                        (1000 * rule.getRestriction().get().numOfSecBetweenNotifications)) {

                    rule.getRestriction().get().lastNotification = currentTime;
                    this.isDisplayed = true;
                    showAlert(rule);
                    this.isDisplayed = false;
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

}
