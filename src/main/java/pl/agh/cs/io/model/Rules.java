package pl.agh.cs.io.model;

import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import pl.agh.cs.io.api.ProcessIdsPerPath;
import pl.agh.cs.io.api.windows.OpenWindowsProcessesPerExeSnapshot;
import pl.agh.cs.io.controller.SerializationManager;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Rules implements Consumer<OpenWindowsProcessesPerExeSnapshot>, Serializable {
    private Map<String, Rule> rulesPlainMap;
    private ObservableMap<String, Rule> rulesObservableMap;
    private int timeIntervalInSecondsBetweenBackups = 10;

    public Rules(ScheduledExecutorService executorService) {
        rulesPlainMap = SerializationManager.deserializeApps();
        rulesObservableMap = FXCollections.synchronizedObservableMap(FXCollections.observableMap(rulesPlainMap));

        Runnable serializeRunnable = () -> {
            SerializationManager.serializeApps(rulesPlainMap);
        };
        executorService.scheduleAtFixedRate(serializeRunnable,
                0,
                timeIntervalInSecondsBetweenBackups,
                TimeUnit.SECONDS);
    }

    public MapProperty<String, Rule> rulesProperty() {
        return new SimpleMapProperty<>(rulesObservableMap);
    }

    public boolean addRule(Rule rule) {
        if (rulesObservableMap.containsKey(rule.getExePath())) {
            return false;
        }
        rulesObservableMap.put(rule.getExePath(), rule);
        SerializationManager.serializeApps(rulesPlainMap);
        return true;
    }

    public void removeRule(String path) {
        if (rulesObservableMap.containsKey(path)) {
            rulesObservableMap.remove(path);
            SerializationManager.serializeApps(rulesPlainMap);
        }
    }

    public void removeRule(Rule rule) {
        removeRule(rule.getExePath());
        SerializationManager.serializeApps(rulesPlainMap);
    }

    @Override
    public void accept(OpenWindowsProcessesPerExeSnapshot openWindowsProcessesPerExeSnapshot) {
        HashMap<String, Rule> unchecked = getRulesCopy();

        ProcessIdsPerPath foreground = openWindowsProcessesPerExeSnapshot.getForegroundWindowProcessIdsPerPath();
        Map<String, ProcessIdsPerPath> allWindows =
                openWindowsProcessesPerExeSnapshot.getBackgroundWindowsProcessesPerExe();

        // handle foreground window
        if (unchecked.containsKey(foreground.getPath())) {
            rulesObservableMap.get(foreground.getPath()).handle(WindowState.FOREGROUND, foreground);
            unchecked.remove(foreground.getPath());
            allWindows.remove(foreground.getPath());
        }

        // handle rest of windows
        for (String exePath : allWindows.keySet()) {
            if (unchecked.containsKey(exePath)) {
                rulesObservableMap.get(exePath).handle(WindowState.BACKGROUND, allWindows.get(exePath));
                unchecked.remove(exePath);
            }
        }

        // handle rules that are closed
        for (Rule rule : unchecked.values()) {
            rulesObservableMap.get(rule.getExePath()).handle(WindowState.CLOSED, null);
        }
    }

    public HashMap<String, Rule> getRulesCopy() {
        HashMap<String, Rule> copy = new HashMap<>();
        for (Rule rule : rulesObservableMap.values()) {
            copy.put(rule.getExePath(), rule);
        }
        return copy;
    }

    public ConcurrentHashMap<String, Rule> getRules() {
        return new ConcurrentHashMap<>(rulesObservableMap);
    }

    public void serialize() {
        SerializationManager.serializeApps(rulesPlainMap);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Rule rule : rulesObservableMap.values()) {
            sb.append(rule.toString());
        }
        return sb.toString();
    }
}
