package pl.agh.cs.io;

import javafx.beans.property.MapProperty;
import pl.agh.cs.io.api.ProcessIdsPerPath;
import pl.agh.cs.io.api.windows.OpenWindowsProcessesPerExeSnapshot;
import pl.agh.cs.io.controller.SerializationManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class Rules implements Consumer<OpenWindowsProcessesPerExeSnapshot>, Serializable {
    private MapProperty<String, Rule> rules;
    private int rulesModificationCounter = 0;
    private int numberOfModsBetweenBackups = 100;

    public Rules() {
        this.rules = SerializationManager.deserialize();/*new SimpleMapProperty<>(FXCollections.observableHashMap())*/;
    }

    public MapProperty<String, Rule> rulesProperty() {
        return rules;
    }

    private void backupIfNeeded(int modCounter){
        if(modCounter < numberOfModsBetweenBackups)
            return;
        SerializationManager.serialize(rules);
        rulesModificationCounter = 0;
    }

    public boolean addRule(Rule rule) {
        if (rules.containsKey(rule.getExePath())) {
            return false;
        }
        this.rules.put(rule.getExePath(), rule);
        rulesModificationCounter++;
        backupIfNeeded(rulesModificationCounter);
        return true;
    }

    public void removeRule(String path) {
        if (rules.containsKey(path)) {
            rules.remove(path);
            rulesModificationCounter++;
            backupIfNeeded(rulesModificationCounter);
        }
    }

    public void removeRule(Rule rule) {
        removeRule(rule.getExePath());
        rulesModificationCounter++;
        backupIfNeeded(rulesModificationCounter);
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
