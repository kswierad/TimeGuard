package pl.agh.cs.io;

import javafx.application.Platform;
import pl.agh.cs.io.api.ProcessIdsPerPath;

import java.util.HashMap;

public class TimeCounterController {

    TimeCounter timeCounter;

    public TimeCounterController() {
        timeCounter = new TimeCounter();
        timeCounter.start();
    }

    public void accept(ProcessIdsPerPath foregroundWindow, HashMap<String, Rule> rulesCopy) {
        if (rulesCopy.containsKey(foregroundWindow.getPath())) {
            Platform.runLater(() -> {
                timeCounter.setText(rulesCopy.get(foregroundWindow.getPath()).toString());
                timeCounter.setWidthAndHeight();
            });
        }
    }

}
