package pl.agh.cs.io;

import javafx.application.Platform;

import java.util.HashMap;

public class TimeCounterController {

    TimeCounter timeCounter;

    public TimeCounterController() {
        timeCounter = new TimeCounter();
        timeCounter.start();
    }

    public void accept(WindowsPerExe foregroundWindow, HashMap<String, Rule> rulesCopy) {
        if (rulesCopy.containsKey(foregroundWindow.getExePath())) {
            Platform.runLater(() -> {
                timeCounter.setText(rulesCopy.get(foregroundWindow.getExePath()).toString());
                timeCounter.setWidthAndHeight();
            });
        }
    }

}
