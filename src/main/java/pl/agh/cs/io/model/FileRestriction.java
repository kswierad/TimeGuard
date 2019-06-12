package pl.agh.cs.io.model;

import pl.agh.cs.io.ExceededUsageAction;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class FileRestriction {

    private final long permittedNumSec;

    private ExceededUsageAction action;
    private FileRule rule;

    private long lastNotification;
    private static long numOfSecBetweenNotifications = 5;

    public FileRestriction(long permittedNumSec, ExceededUsageAction action, FileRule rule) {
        this.permittedNumSec = permittedNumSec;
        this.action = action;
        this.lastNotification = 0;
    }

    public void checkRestriction() {
        if (rule != null && permittedNumSec > 0) {
            double usedToday = FileActivityTime.getActivityTimeFromList(rule.getTimes());
            if (usedToday > permittedNumSec) {
                long currentTime = Timestamp.valueOf(LocalDateTime.now()).getTime();
                //notify or call method every numOfSecBetweenNotifications seconds after time is exceeded
                if (currentTime > lastNotification + (1000 * numOfSecBetweenNotifications)) {
                    lastNotification = currentTime;
                    //TODO Notify user
                    System.out.println("Time used up for " + rule.getPath() + ", " + action);
                }
            }
        }
    }

    public ExceededUsageAction getAction() {
        return action;
    }

    public long getPermittedNumSec() {
        return permittedNumSec;
    }

    public void setRule(FileRule rule) {
        this.rule = rule;
    }

}
