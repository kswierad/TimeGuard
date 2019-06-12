package pl.agh.cs.io.model;

import pl.agh.cs.io.api.ProcessIdsPerPath;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Rule implements Serializable {
    private String exePath;
    private List<ActivityTime> times;
    private RuleRestriction restriction;

    private WindowState prevState;
    private long prevTimeStamp;

    public Rule(String path) {
        this.exePath = path;
        this.times = new CopyOnWriteArrayList<>();
        this.prevState = WindowState.CLOSED;
    }

    public void handle(WindowState state, ProcessIdsPerPath processes) {


        if (prevState == WindowState.FOREGROUND || prevState == WindowState.BACKGROUND) {
            if (prevState != state && state != WindowState.CLOSED) {
                createNewTime(prevState);
            }

            if (restriction != null) {
                restriction.checkRestriction(processes);
            }
        }

        if (state != prevState) {
            prevTimeStamp = getTimestamp();
            prevState = state;
        }

        long secondsBetweenSave = 2;
        if (state.equals(prevState) && getTimestamp() - prevTimeStamp > secondsBetweenSave * 1000) {
            createNewTime(prevState);
            prevTimeStamp = getTimestamp();
        }
    }

    private long getTimestamp() {
        //get number of milliseconds since January 1, 1970
        return Timestamp.valueOf(LocalDateTime.now()).getTime();
    }

    private void createNewTime(WindowState state) {
        double amount = (double) (getTimestamp() - prevTimeStamp) / 1000;
        ActivityTime time = new ActivityTime(state, amount, getTimestamp());
        times.add(time);
    }

    public String getExePath() {
        return exePath;
    }

    public List<ActivityTime> getTimes() {
        List<ActivityTime> timesCopy = new CopyOnWriteArrayList<>(times);
        timesCopy.add(new ActivityTime(prevState, (getTimestamp() - prevTimeStamp) / 1000, getTimestamp()));
        return Collections.unmodifiableList(timesCopy);
    }

    public void resetTimes() {
        times.clear();
    }

    @Override
    public String toString() {
        double fg = 0, bg = 0;

        fg = ActivityTime.getActivityTimeFromList(this.getTimes(), WindowState.FOREGROUND);
        bg = ActivityTime.getActivityTimeFromList(this.getTimes(), WindowState.BACKGROUND);

        DecimalFormat df2 = new DecimalFormat("#.##");
        return "Rule " + exePath + "\nFOREGROUND: " + df2.format(fg) + ", BACKGROUND: " + df2.format(bg);
    }

    public void setRestriction(RuleRestriction restriction) {
        restriction.setRule(this);
        this.restriction = restriction;
    }

    public void removeRestriction() {
        this.restriction = null;
    }

    public RuleRestriction getRestriction() {
        return restriction;
    }
}
