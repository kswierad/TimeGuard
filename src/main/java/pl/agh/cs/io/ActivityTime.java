package pl.agh.cs.io;

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
}
