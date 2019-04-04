package pl.agh.cs.io;

import java.util.List;
import java.util.Map;

public class OpenWindowsSnapshot {
    private Window foregroundWindow;
    private Map<String, WindowsPerExe> allWindows;

    public OpenWindowsSnapshot(Window foregroundWindow, Map<String, WindowsPerExe> allWindows) {
        this.foregroundWindow = foregroundWindow;
        this.allWindows = allWindows;
    }

    public Window getForegroundWindow() {
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
