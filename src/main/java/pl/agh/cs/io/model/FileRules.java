package pl.agh.cs.io.model;

import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import pl.agh.cs.io.api.files.ProcessIdsPerFilepath;
import pl.agh.cs.io.controller.SerializationManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class FileRules implements Consumer<Map<String, ProcessIdsPerFilepath>> {
    private Map<String, FileRule> fileRulesPlainMap;
    private ObservableMap<String, FileRule> fileRulesObservableMap;
    private Map<String, ProcessIdsPerFilepath> activeFiles;
    private int timeIntervalInSecondsBetweenBackups = 10;

    public FileRules(ScheduledExecutorService executorService) {
        activeFiles = new HashMap<>();
        fileRulesPlainMap = SerializationManager.deserializeFiles();
        fileRulesObservableMap =
                FXCollections.synchronizedObservableMap(FXCollections.observableMap(fileRulesPlainMap));

        Runnable serializeRunnable = () -> {
            SerializationManager.serializeFiles(fileRulesPlainMap);
        };
        executorService.scheduleAtFixedRate(serializeRunnable,
                0,
                timeIntervalInSecondsBetweenBackups,
                TimeUnit.SECONDS);
    }

    public MapProperty<String, FileRule> fileRulesObservableMapProperty() {
        return new SimpleMapProperty<>(fileRulesObservableMap);
    }

    public boolean addFileRule(FileRule fileRule) {
            if (fileRulesObservableMap.containsKey(fileRule.getPath())) {
                return false;
            }
            this.fileRulesObservableMap.put(fileRule.getPath(), fileRule);
            SerializationManager.serializeFiles(fileRulesPlainMap);
            return true;
    }

    public void removeFileRule(String path) {
        fileRulesObservableMap.remove(path);
        SerializationManager.serializeFiles(fileRulesPlainMap);
    }

    public void removeFileRule(FileRule fileRule) {
        removeFileRule(fileRule.getPath());
        SerializationManager.serializeFiles(fileRulesPlainMap);
    }

    @Override
    public void accept(Map<String, ProcessIdsPerFilepath> newActiveFiles) {
        fileRulesObservableMap.forEach((rulePath, rule) -> {
            if (newActiveFiles.containsKey(rulePath)) {
                rule.activate(newActiveFiles.get(rulePath));
            }
            Set<String> deactivationSet = activeFiles.keySet();
            deactivationSet.removeAll(newActiveFiles.keySet());
            deactivationSet.retainAll(fileRulesObservableMap.keySet());
            deactivationSet.forEach(path -> fileRulesObservableMap.get(path).deactivate());
        });
        activeFiles = newActiveFiles;
    }

    public Map<String, FileRule> getFileRulesObservableMap() {
        return fileRulesObservableMap;
    }

    public void serialize() {
        SerializationManager.serializeFiles(fileRulesPlainMap);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (FileRule rule : fileRulesObservableMap.values()) {
            sb.append(rule.toString());
        }
        return sb.toString();
    }
}
