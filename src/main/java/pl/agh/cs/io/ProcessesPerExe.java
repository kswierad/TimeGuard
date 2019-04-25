package pl.agh.cs.io;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProcessesPerExe {
    private String exePath;
    private Set<Integer> processIds;

    public ProcessesPerExe(String exePath, Set<Integer> processIds) {
        this.exePath = exePath;
        this.processIds = processIds;
    }

    public static ProcessesPerExe fromWindow(Window window) {
        int processId = window.getProcessId();
        List<Integer> processIdList = Collections.singletonList(processId);
        return new ProcessesPerExe(window.getExePath(),
                new HashSet<>(processIdList));
    }

    public String getExePath() {
        return exePath;
    }

    public Set<Integer> getProcessIds() {
        return processIds;
    }

    public void terminate() {
        processIds.forEach(WindowsApi::terminateProces);
    }

    @Override
    public String toString() {
        return "ExeWindows{" +
                "exePath='" + exePath + '\'' +
                ", processIds=" + processIds +
                '}';
    }
}
