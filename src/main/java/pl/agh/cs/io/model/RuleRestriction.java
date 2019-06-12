package pl.agh.cs.io.model;

import pl.agh.cs.io.ExceededUsageAction;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class RuleRestriction {

    public final WindowState state;
    public final long permittedNumSec;

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

            if (usedToday > permittedNumSec) {
                long currentTime = Timestamp.valueOf(LocalDateTime.now()).getTime();
                //notify or call method every numOfSecBetweenNotifications seconds after time is exceeded
                if (currentTime > lastNotification + (1000 * numOfSecBetweenNotifications)) {
                    lastNotification = currentTime;
                    //TODO Notify user
                    System.out.println("Time used up for " + rule.getExePath() + ", " + action);
                }
            }
        }
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
