package pl.agh.cs.io;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Rules implements Consumer<OpenWindowsSnapshot> {
    private List<Rule> rules;

    public Rules() {
        this.rules = new ArrayList<>();
    }

    public void addRule(Rule rule) {
        this.rules.add(rule);
    }

    @Override
    public void accept(OpenWindowsSnapshot openWindowsSnapshot) {
        for (Rule rule : rules) {
            State ruleState = getRuleState(openWindowsSnapshot, rule.getExePath());
            rule.handle(ruleState);
        }
    }

    private State getRuleState(OpenWindowsSnapshot openWindowsSnapshot, String exePath) {
        ProcessesPerExe foreground = openWindowsSnapshot.getForegroundWindow();
        if (foreground.getExePath().equals(exePath)) {
            return State.FG;
        }

        Map<String, ProcessesPerExe> allWindows = openWindowsSnapshot.getAllWindows();
        for (String key: allWindows.keySet()) {
            if (key.equals(exePath)) {
                return State.BG;
            }
        }
        return State.CLOSED;
    }
}
