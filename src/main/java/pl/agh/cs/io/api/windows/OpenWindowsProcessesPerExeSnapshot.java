package pl.agh.cs.io.api.windows;

import pl.agh.cs.io.api.ProcessIdsPerPath;

import java.util.Map;

public class OpenWindowsProcessesPerExeSnapshot {
    private ProcessIdsPerPath foregroundWindowProcessIdsPerPath;
    private Map<String, ProcessIdsPerPath> backgroundWindowsProcessesPerExe;

    public OpenWindowsProcessesPerExeSnapshot(ProcessIdsPerPath foregroundWindowProcessIdsPerPath,
                                              Map<String, ProcessIdsPerPath> backgroundWindows) {
        this.foregroundWindowProcessIdsPerPath = foregroundWindowProcessIdsPerPath;
        this.backgroundWindowsProcessesPerExe = backgroundWindows;
    }

    public ProcessIdsPerPath getForegroundWindowProcessIdsPerPath() {
        return foregroundWindowProcessIdsPerPath;
    }

    public Map<String, ProcessIdsPerPath> getBackgroundWindowsProcessesPerExe() {
        return backgroundWindowsProcessesPerExe;
    }

    @Override
    public String toString() {
        return "OpenWindowsSnapshot{" +
                "foregroundWindowProcessesPerExe=" + foregroundWindowProcessIdsPerPath +
                ", backgroundWindowsProcessesPerExe=" + backgroundWindowsProcessesPerExe +
                '}';
    }
}
