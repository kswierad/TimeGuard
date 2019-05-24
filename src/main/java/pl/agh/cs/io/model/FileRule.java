package pl.agh.cs.io.model;

import pl.agh.cs.io.api.files.ProcessIdsPerFilepath;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FileRule {

    private String path;
    private List<FileActivityTime> times;

    private Boolean active;
    private long prevTimeStamp;

    private ProcessIdsPerFilepath fileProcesses;

    public FileRule(String path) {
        this.path = path;
        this.times = new CopyOnWriteArrayList<>();
        active = false;
    }

    public void activate(ProcessIdsPerFilepath fileProcesses) {
        this.fileProcesses = fileProcesses;
        System.out.println("activating " + path);
        if (!active) {
            active = true;
            prevTimeStamp = getTimestamp();
        }
    }

    public void deactivate() {
        if (active) {
            System.out.println("deactivating " + path);
            active = false;
            createNewTime();
        }
    }

    private long getTimestamp() {
        //get number of milliseconds since January 1, 1970
        return Timestamp.valueOf(LocalDateTime.now()).getTime();
    }

    private void createNewTime() {
        double amount = (double) (getTimestamp() - prevTimeStamp) / 1000;
        FileActivityTime time = new FileActivityTime(amount, getTimestamp());
        times.add(time);
    }

    public String getPath() {
        return path;
    }

    public List<FileActivityTime> getTimes() {
        List<FileActivityTime> timesCopy = new CopyOnWriteArrayList<>(times);
        if (active) {
            timesCopy.add(new FileActivityTime((getTimestamp() - prevTimeStamp) / 1000, getTimestamp()));
        }
        return Collections.unmodifiableList(timesCopy);
    }

    public void resetTimes() {
        times.clear();
    }

//    public void setFileProcesses(ProcessIdsPerFilepath fileProcesses) {
//        this.fileProcesses = fileProcesses;
//    }
//
//    @Override
//    public String toString() {
//        double fg = 0, bg = 0;
//        for (ActivityTime time : times) {
//            switch (time.getState()) {
//                case FOREGROUND:
//                    fg += time.getAmount();
//                    break;
//                case BACKGROUND:
//                    bg += time.getAmount();
//                    break;
//            }
//        }
//        switch (prevState) {
//            case FOREGROUND:
//                fg += (getTimestamp() - prevTimeStamp) / 1000;
//                break;
//            case BACKGROUND:
//                bg += (getTimestamp() - prevTimeStamp) / 1000;
//                break;
//        }
//        bg += fg;
//        DecimalFormat df2 = new DecimalFormat("#.##");
//        return "Rule " + exePath + "\nFOREGROUND: " + df2.format(fg) + ", BACKGROUND: " + df2.format(bg);
//    }
}
