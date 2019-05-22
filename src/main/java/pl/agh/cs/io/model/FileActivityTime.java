package pl.agh.cs.io.model;

public class FileActivityTime{
    private final double amount;
    private final long timestamp;

    public FileActivityTime(double amount, long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
