package pl.agh.cs.io.model;

import java.util.List;

public class FileActivityTime {
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

    public static double getActivityTimeFromList(List<FileActivityTime> activities) {
        double sum = 0;
        for (FileActivityTime activity : activities) {
            sum += activity.getAmount();
        }
        return sum;
    }
}
