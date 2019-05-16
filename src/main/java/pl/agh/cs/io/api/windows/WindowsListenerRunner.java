package pl.agh.cs.io.api.windows;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import pl.agh.cs.io.api.PathProcessId;
import pl.agh.cs.io.api.ProcessIdsPerPath;
import pl.agh.cs.io.api.WindowsApi;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class WindowsListenerRunner {
    private final static Duration POLLING_DURATION = Duration.seconds(1);
    private static Consumer<OpenWindowsProcessesPerExeSnapshot> callback;
    private ScheduledExecutorService executorService;

    public WindowsListenerRunner(ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    public void run(Consumer<OpenWindowsProcessesPerExeSnapshot> callback) {
      /*  WindowsListenerRunner.callback = callback;
        executorService.scheduleAtFixedRate(newGetOpenWindowsProcessesTask(),
                POLLING_DELAY_MILLIS, POLLING_DELAY_MILLIS,
                TimeUnit.MILLISECONDS);*/

       ScheduledService<Integer> scheduledService = new ScheduledService<Integer>() {

            @Override
            protected Task<Integer> createTask() {

                return new Task<Integer>() {
                    @Override
                    protected Integer call() throws Exception {
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
                                System.out.println("miau");
                                callback.accept(snapshot);
                            }
                        });
                        return 1;
                    }
                };
            }
        };

        scheduledService.setPeriod(POLLING_DURATION);
        scheduledService.start();

        System.out.println("after");
    }

    private static Runnable newGetOpenWindowsProcessesTask() {
        return () -> {
            // !!! THIS PLACE STARTS NOT WORKING ALERT FREEZES
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

        };
    }

    private static Map<String, ProcessIdsPerPath> toWindowsPerExeMap(List<PathProcessId> windows) {
        Map<String, ProcessIdsPerPath> perExeWindows = new HashMap<>();
        Map<String, Set<Integer>> ids = PathProcessId.groupByPath(windows);
        ids.forEach((k, v) -> perExeWindows.put(k, new ProcessIdsPerPath(k, v)));
        perExeWindows.remove("");
        return perExeWindows;
    }
}
