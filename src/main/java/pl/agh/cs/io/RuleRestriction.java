package pl.agh.cs.io;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class RuleRestriction {

    WindowState state;
    long permittedNamSec;
    Rule rule;
    ExceededUsageAction action;

    private long lastNotification;
    private long numOfSecBetweenNotifications = 5;


    public RuleRestriction(WindowState state, long permittedNamSec, ExceededUsageAction action) {
        this.permittedNamSec = permittedNamSec;
        this.state = state;
        this.lastNotification = 0;
        this.action = action;
    }

    public void checkRestriction() {
        if (rule != null && permittedNamSec > 0) {
            double usedToday = ActivityTime.getActivityTimeFromList(rule.getTimes(), state);

            if (usedToday > permittedNamSec) {
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


    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
