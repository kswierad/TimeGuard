package pl.agh.cs.io.model;

import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import pl.agh.cs.io.api.files.ProcessIdsPerFilepath;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class FileRules implements Consumer<Map<String, ProcessIdsPerFilepath>> {
    private ObservableMap<String, FileRule> fileRules;
    private Map<String, ProcessIdsPerFilepath> activeFiles;

    public FileRules() {
        fileRules = FXCollections.synchronizedObservableMap(FXCollections.observableHashMap());
        activeFiles = new HashMap<>();
    }

    public MapProperty<String, FileRule> fileRulesProperty() {
        return new SimpleMapProperty<>(fileRules);
    }

    public boolean addFileRule(FileRule fileRule) {
            if (fileRules.containsKey(fileRule.getPath())) {
                return false;
            }
            this.fileRules.put(fileRule.getPath(), fileRule);
            return true;
    }

    public void removeFileRule(String path) {
        fileRules.remove(path);
    }

    public void removeFileRule(FileRule fileRule) {
        removeFileRule(fileRule.getPath());
    }

    @Override
    public void accept(Map<String, ProcessIdsPerFilepath> newActiveFiles) {
        fileRules.forEach((rulePath, rule) -> {
            if (newActiveFiles.containsKey(rulePath)) {
                rule.activate(newActiveFiles.get(rulePath));
            }
            Set<String> deactivationSet = activeFiles.keySet();
            deactivationSet.removeAll(newActiveFiles.keySet());
            deactivationSet.retainAll(fileRules.keySet());
            deactivationSet.forEach(path -> fileRules.get(path).deactivate());
        });
        activeFiles = newActiveFiles;
    }

    public Map<String, FileRule> getFileRules() {
        return fileRules;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (FileRule rule : fileRules.values()) {
            sb.append(rule.toString());
        }
        return sb.toString();
    }
}
