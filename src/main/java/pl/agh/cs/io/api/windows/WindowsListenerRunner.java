package pl.agh.cs.io.api.windows;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import pl.agh.cs.io.api.PathProcessId;
import pl.agh.cs.io.api.ProcessIdsPerPath;
import pl.agh.cs.io.api.WindowsApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

public class WindowsListenerRunner {
    private final static long POLLING_DELAY_MILLIS = 1;
    private static Consumer<OpenWindowsProcessesPerExeSnapshot> callback;
    private ScheduledExecutorService executorService;

    public WindowsListenerRunner(ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    public void run(Consumer<OpenWindowsProcessesPerExeSnapshot> callback) {

       ScheduledService<Void> scheduledService = new ScheduledService<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                PathProcessId foregroundWindow = WindowsApi.getForegroundWindowPathProcessId();
                                List<PathProcessId> windows = WindowsApi.getOpenWindowsPathProcessIds();
                                Map<String, ProcessIdsPerPath> exeWindows = toWindowsPerExeMap(windows);
                                ProcessIdsPerPath foregroundWindowPerExe = exeWindows.remove(foregroundWindow.getPath());
                                if (foregroundWindowPerExe == null) {
                                    foregroundWindowPerExe = ProcessIdsPerPath.fromWindow(foregroundWindow);
                                }
                                OpenWindowsProcessesPerExeSnapshot snapshot =
                                        new OpenWindowsProcessesPerExeSnapshot(foregroundWindowPerExe, exeWindows);
                                callback.accept(snapshot);
                            }
                        });
                        return null;
                    }
                };
            }
        };

        scheduledService.setPeriod(Duration.seconds(POLLING_DELAY_MILLIS));
        scheduledService.start();
    }

    private static Map<String, ProcessIdsPerPath> toWindowsPerExeMap(List<PathProcessId> windows) {
        Map<String, ProcessIdsPerPath> perExeWindows = new HashMap<>();
        Map<String, Set<Integer>> ids = PathProcessId.groupByPath(windows);
        ids.forEach((k, v) -> perExeWindows.put(k, new ProcessIdsPerPath(k, v)));
        perExeWindows.remove("");
        return perExeWindows;
    }
}
