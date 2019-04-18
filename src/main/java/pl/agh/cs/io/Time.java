package pl.agh.cs.io;

// holds type of activity (FG, BG), when started, and how long lasted
public class Time {
    private final State type;
    private final double amount;
    private final long timestamp;

    public Time(State type, double amount, long timestamp) {
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public State getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
