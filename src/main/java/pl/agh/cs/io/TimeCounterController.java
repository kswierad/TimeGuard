package pl.agh.cs.io;

import java.util.HashMap;

public class TimeCounterController {

    TimeCounter timeCounter;

    public TimeCounterController() {
        timeCounter = new TimeCounter();
        timeCounter.start();
    }

    public void accept(WindowsPerExe foregroundWindow, HashMap<String, Rule> rulesCopy) {
        if(rulesCopy.containsKey(foregroundWindow.getExePath())) {
            timeCounter.setText(rulesCopy.get(foregroundWindow.getExePath()).toString());
        }
    }

}
