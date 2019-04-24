package pl.agh.cs.io;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class Rules implements Consumer<OpenWindowsSnapshot> {
    private ConcurrentHashMap<String, Rule> rules;

    public Rules() {
        this.rules = new ConcurrentHashMap<>();
    }

    public boolean addRule(Rule rule) {
        if (rules.containsKey(rule.getExePath())) {
            return false;
        }
        this.rules.put(rule.getExePath(), rule);
        return true;
    }

    public boolean removeRule(String path) {
        if (rules.containsKey(path)) {
            rules.remove(path);
            return true;
        }
        return false;
    }

    public boolean removeRule(Rule rule) {
        return removeRule(rule.getExePath());
    }

    @Override
    public void accept(OpenWindowsSnapshot openWindowsSnapshot) {
        HashMap<String, Rule> unchecked = getRulesCopy();

        WindowsPerExe foreground = openWindowsSnapshot.getForegroundWindow();
        Map<String, WindowsPerExe> allWindows = openWindowsSnapshot.getAllWindows();

        // handle foreground window
        if (unchecked.containsKey(foreground.getExePath())) {
            rules.get(foreground.getExePath()).handle(State.FG);
            unchecked.remove(foreground.getExePath());
            allWindows.remove(foreground.getExePath());
        }

        // handle rest of windows
        for (String exePath : allWindows.keySet()) {
            if (unchecked.containsKey(exePath)) {
                rules.get(exePath).handle(State.BG);
                unchecked.remove(exePath);
            }
        }

        // handle rules that are closed
        for (Rule rule : unchecked.values()) {
            rules.get(rule.getExePath()).handle(State.CLOSED);
        }
    }

    public HashMap<String, Rule> getRulesCopy() {
        HashMap<String, Rule> copy = new HashMap<>();
        for (Rule rule : rules.values()) {
            copy.put(rule.getExePath(), rule);
        }
        return copy;
    }

    public ConcurrentHashMap<String, Rule> getRules() {
        return rules;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Rule rule : rules.values()) {
            sb.append(rule.toString());
        }
        return sb.toString();
    }
}
