package pl.agh.cs.io.model;

import pl.agh.cs.io.api.files.ProcessIdsPerFilepath;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FileRule implements Serializable {

    private String path;
    private List<FileActivityTime> times;
    private FileRestriction restriction;

    private Boolean active;
    private long prevTimeStamp;

    private ProcessIdsPerFilepath fileProcesses;

    public FileRule(String path) {
        this.path = path;
        this.times = new CopyOnWriteArrayList<>();
        active = false;
    }

    public void activate(ProcessIdsPerFilepath fileProcesses) {
        if (restriction != null) {
            restriction.checkRestriction();
        }
        this.fileProcesses = fileProcesses;
        if (!active) {
            active = true;
            prevTimeStamp = getTimestamp();
        }
    }

    public void deactivate() {
        if (active) {
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

    public void setRestriction(FileRestriction restriction) {
        restriction.setRule(this);
        this.restriction = restriction;
    }

    public void removeRestriction() {
        this.restriction = null;
    }

    public FileRestriction getRestriction() {
        return restriction;
    }
}
