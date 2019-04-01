package pl.agh.cs.io;

import java.util.List;

public class OpenWindowsSnapshot {
    private Window foregroundWindow;
    private List<WindowsPerExe> allWindows;

    public OpenWindowsSnapshot(Window foregroundWindow, List<WindowsPerExe> allWindows) {
        this.foregroundWindow = foregroundWindow;
        this.allWindows = allWindows;
    }

    public Window getForegroundWindow() {
        return foregroundWindow;
    }

    public List<WindowsPerExe> getAllWindows() {
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
