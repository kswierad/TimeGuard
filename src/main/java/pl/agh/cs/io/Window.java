package pl.agh.cs.io;

public class Window {
    private int processId;
    private String exePath;

    public Window(int processId, String exePath) {
        this.processId = processId;
        this.exePath = exePath;
    }

    public int getProcessId() {
        return processId;
    }

    public String getExePath() {
        return exePath;
    }

    @Override
    public String toString() {
        return "Window{" +
                "processId=" + processId +
                ", exePath='" + exePath + '\'' +
                '}';
    }
}
