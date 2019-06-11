package pl.agh.cs.io.model;

import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import pl.agh.cs.io.api.ProcessIdsPerPath;
import pl.agh.cs.io.api.windows.OpenWindowsProcessesPerExeSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class Rules implements Consumer<OpenWindowsProcessesPerExeSnapshot> {
    private MapProperty<String, Rule> rules;

    public Rules() {
        this.rules = new SimpleMapProperty<>(FXCollections.observableHashMap());
    }

    public MapProperty<String, Rule> rulesProperty() {
        return rules;
    }


    public boolean addRule(Rule rule) {
        if (rules.containsKey(rule.getExePath())) {
            return false;
        }
//  fixme remove
//        if (rule.getExePath().contains("dupa")) {
//            rule.handle(WindowState.FOREGROUND);
//            Thread.sleep(2000);
//            rule.handle(WindowState.FOREGROUND);
//        }
        this.rules.put(rule.getExePath(), rule);
        return true;
    }

    public void removeRule(String path) {
        if (rules.containsKey(path)) {
            rules.remove(path);
        }
    }

    public void removeRule(Rule rule) {
        removeRule(rule.getExePath());
    }

    @Override
    public void accept(OpenWindowsProcessesPerExeSnapshot openWindowsProcessesPerExeSnapshot) {
        HashMap<String, Rule> unchecked = getRulesCopy();

        ProcessIdsPerPath foreground = openWindowsProcessesPerExeSnapshot.getForegroundWindowProcessIdsPerPath();
        Map<String, ProcessIdsPerPath> allWindows =
                openWindowsProcessesPerExeSnapshot.getBackgroundWindowsProcessesPerExe();

        // handle foreground window
        if (unchecked.containsKey(foreground.getPath())) {
            rules.get(foreground.getPath()).handle(WindowState.FOREGROUND);
            unchecked.remove(foreground.getPath());
            allWindows.remove(foreground.getPath());
        }

        // handle rest of windows
        for (String exePath : allWindows.keySet()) {
            if (unchecked.containsKey(exePath)) {
                rules.get(exePath).handle(WindowState.BACKGROUND);
                unchecked.remove(exePath);
            }
        }

        // handle rules that are closed
        for (Rule rule : unchecked.values()) {
            rules.get(rule.getExePath()).handle(WindowState.CLOSED);
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
        return new ConcurrentHashMap<>(rules);
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
