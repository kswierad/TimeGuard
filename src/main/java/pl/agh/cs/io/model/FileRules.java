package pl.agh.cs.io.model;

import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import pl.agh.cs.io.api.files.ProcessIdsPerFilepath;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class FileRules implements Consumer<Map<String, ProcessIdsPerFilepath>> {
    private MapProperty<String, FileRule> fileRules;
    private Map<String, ProcessIdsPerFilepath> activeFiles;

    public FileRules() {
        this.fileRules = new SimpleMapProperty<>(FXCollections.observableHashMap());
        activeFiles = new ConcurrentHashMap<>();
    }

    public MapProperty<String, FileRule> fileRulesProperty() {
        return fileRules;
    }


    public boolean addFileRule(FileRule fileRule) {
        if (fileRules.containsKey(fileRule.getPath())) {
            return false;
        }
        this.fileRules.put(fileRule.getPath(), fileRule);
        return true;
    }

    public void removeFileRule(String path) {
        if (fileRules.containsKey(path)) {
            fileRules.remove(path);
        }
    }

    public void removeFileRule(FileRule fileRule) {
        removeFileRule(fileRule.getPath());
    }

    @Override
    public void accept(Map<String, ProcessIdsPerFilepath> newActiveFiles) {
        for (FileRule fileRule: fileRules.values()) {
            if (newActiveFiles.containsKey(fileRule.getPath())) {
                fileRule.activate(newActiveFiles.get(fileRule.getPath()));
            }
            Set<String>  deactivationSet = activeFiles.keySet();
            deactivationSet.removeAll(newActiveFiles.keySet());
            for (String filePath: deactivationSet) {
                fileRules.get(filePath).deactivate();
            }
            activeFiles = newActiveFiles;
        }
    }

    public HashMap<String, FileRule> getFileRulesCopy() {
        HashMap<String, FileRule> copy = new HashMap<>();
        for (FileRule fileRule : fileRules.values()) {
            copy.put(fileRule.getPath(), fileRule);
        }
        return copy;
    }

    public ConcurrentHashMap<String, FileRule> getFileRules() {
        return new ConcurrentHashMap<>(fileRules);
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
