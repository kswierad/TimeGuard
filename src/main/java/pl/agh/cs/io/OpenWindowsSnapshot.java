package pl.agh.cs.io;

import java.util.Map;

public class OpenWindowsSnapshot {
    private ProcessesPerExe foregroundWindow;
    private Map<String, ProcessesPerExe> allWindows;

    public OpenWindowsSnapshot(ProcessesPerExe foregroundWindow, Map<String, ProcessesPerExe> backgroundWindows) {
        this.foregroundWindow = foregroundWindow;
        this.allWindows = backgroundWindows;
    }

    public ProcessesPerExe getForegroundWindow() {
        return foregroundWindow;
    }

    public Map<String, ProcessesPerExe> getAllWindows() {
        return allWindows;
    }

    @Override
    public String toString() {
        return "OpenWindowsSnapshot{" +
                "foregroundWindow=" + foregroundWindow +
                ", allWindows=" + allWindows +
                '}';
    }
}
