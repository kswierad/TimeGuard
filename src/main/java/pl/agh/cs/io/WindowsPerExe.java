package pl.agh.cs.io;

import java.util.List;
import java.util.Set;

public class WindowsPerExe {
    private String exePath;
    private Set<Integer> processIds;

    public WindowsPerExe(String exePath, Set<Integer> processIds) {
        this.exePath = exePath;
        this.processIds = processIds;
    }

    public String getExePath() {
        return exePath;
    }

    public Set<Integer> getProcessIds() {
        return processIds;
    }

    public void close() {
        processIds.forEach(WindowsApiFacade::terminateProces);
    }

    @Override
    public String toString() {
        return "ExeWindows{" +
                "exePath='" + exePath + '\'' +
                ", processIds=" + processIds +
                '}';
    }
}
