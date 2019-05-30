package pl.agh.cs.io.model;

import java.util.List;

public class ActivityTime {
    private final WindowState state;
    private final double amount;
    private final long timestamp;

    public ActivityTime(WindowState state, double amount, long timestamp) {
        this.state = state;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public WindowState getState() {
        return state;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public static double getActivityTimeFromList(List<ActivityTime> activities, WindowState state) {
        double fg = 0, bg = 0;
        for (ActivityTime activity : activities) {
            switch (activity.getState()) {
                case FOREGROUND:
                    fg += activity.getAmount();
                    break;
                case BACKGROUND:
                    bg += activity.getAmount();
                    break;
            }
        }
        switch (state) {
            case FOREGROUND:
                return fg;
            case BACKGROUND:
                return fg + bg;
        }
        return 0;
    }
}
