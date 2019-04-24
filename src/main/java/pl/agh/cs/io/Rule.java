package pl.agh.cs.io;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Rule {
    private String exePath;
    private List<Time> times;
    
    private State prevState;
    private long prevTimeStamp;

    public Rule(String path) {
        this.exePath = path;
        this.times = new CopyOnWriteArrayList<>();
        this.prevState = State.CLOSED;
    }

    public void handle(State state) {
        if (prevState == State.FG || prevState == State.BG) {
            if (prevState != state && prevState != State.CLOSED) {
                createNewTime(prevState);
            }
        }

        if (state != prevState) {
            prevTimeStamp = getTimeStamp();
            prevState = state;
        }
    }

    private long getTimeStamp() {
        //get number of milliseconds since January 1, 1970
        return Timestamp.valueOf(LocalDateTime.now()).getTime();
    }

    private void createNewTime(State type) {
        double amount = (double) (getTimeStamp() - prevTimeStamp) / 1000;
        Time time = new Time(type, amount, getTimeStamp());
        times.add(time);
    }

    public String getExePath() {
        return exePath;
    }

    public List<Time> getTimes() {
        return Collections.unmodifiableList(times);
    }

    public void resetTimes() {
        times.clear();
    }

    @Override
    public String toString() {
        double fg = 0, bg = 0;
        for (Time time : times) {
            switch (time.getType()) {
                case FG:
                    fg += time.getAmount();
                    break;
                case BG:
                    bg += time.getAmount();
                    break;
            }
        }
        switch (prevState) {
            case FG:
                fg += (getTimeStamp() - prevTimeStamp) / 1000;
                break;
            case BG:
                bg += (getTimeStamp() - prevTimeStamp) / 1000;
                break;
        }
        bg += fg;
        DecimalFormat df2 = new DecimalFormat("#.##");
        return "\nRule " + exePath + "\nFG: " + df2.format(fg) + ", BG: " + df2.format(bg);
    }
}
