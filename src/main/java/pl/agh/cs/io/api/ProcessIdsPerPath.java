package pl.agh.cs.io.api;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProcessIdsPerPath implements Serializable {
    private String path;
    private Set<Integer> processIds;

    public ProcessIdsPerPath(String path, Set<Integer> processIds) {
        this.path = path;
        this.processIds = processIds;
    }

    public static ProcessIdsPerPath fromWindow(PathProcessId window) {
        int processId = window.getProcessId();
        List<Integer> processIdList = Collections.singletonList(processId);
        return new ProcessIdsPerPath(window.getPath(),
                new HashSet<>(processIdList));
    }

    public String getPath() {
        return path;
    }

    public Set<Integer> getProcessIds() {
        return processIds;
    }

    protected void terminateProcesses() {
        processIds.forEach(WindowsApi::terminateProcess);
    }

    @Override
    public String toString() {
        return "ProcessIdsPerPath{" +
                "path='" + path + '\'' +
                ", processIds=" + processIds +
                '}';
    }
}
