package pl.agh.cs.io.counter;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import pl.agh.cs.io.controller.StatsController;
import pl.agh.cs.io.model.ActivityTime;
import pl.agh.cs.io.model.Rule;
import pl.agh.cs.io.api.ProcessIdsPerPath;
import pl.agh.cs.io.model.WindowState;

import java.util.HashMap;
import java.util.List;

public class TimeCounterController {

    TimeCounter timeCounter;

    public TimeCounterController() {
        timeCounter = new TimeCounter();
        timeCounter.start();
    }

    public void accept(ProcessIdsPerPath foregroundWindow, HashMap<String, Rule> rulesCopy) {
        if (rulesCopy.containsKey(foregroundWindow.getPath())) {
            Platform.runLater(() -> {
                List<ActivityTime> atList = rulesCopy.get(foregroundWindow.getPath()).getTimes();
                double foregroundSeconds = ActivityTime.getActivityTimeFromList(atList, WindowState.FOREGROUND);
                String fgString = new SimpleStringProperty(StatsController.secondsToString(foregroundSeconds)).get();
                timeCounter.setText(fgString);
            });
        }
    }

    public void setAlwaysOnTop() {
        timeCounter.setAlwaysOnTop();
    }

    public void setWindowPosition(int x, int y) {
        timeCounter.setWindowPosition(x, y);
    }

}
