package pl.agh.cs.io;

import java.util.Map;

public class OpenWindowsSnapshot {
    private WindowsPerExe foregroundWindow;
    private Map<String, WindowsPerExe> allWindows;

    public OpenWindowsSnapshot(WindowsPerExe foregroundWindow, Map<String, WindowsPerExe> backgroundWindows) {
        this.foregroundWindow = foregroundWindow;
        this.allWindows = backgroundWindows;
    }

    public WindowsPerExe getForegroundWindow() {
        return foregroundWindow;
    }

    public Map<String, WindowsPerExe> getAllWindows() {
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
