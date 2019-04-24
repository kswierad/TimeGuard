package pl.agh.cs.io;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class WindowsListenerRunner {
    private final static long POLLING_DELAY_MILLIS = 1000;
    private static Consumer<OpenWindowsSnapshot> callback;
    private static ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

    public static void run(Consumer<OpenWindowsSnapshot> callback) {
        WindowsListenerRunner.callback = callback;
        executorService.scheduleAtFixedRate(newGetOpenWindowsTask(),
                POLLING_DELAY_MILLIS, POLLING_DELAY_MILLIS,
                TimeUnit.MILLISECONDS);
    }

    private static Runnable newGetOpenWindowsTask() {
        return () -> {
            Window foregroundWindow = WindowsApi.getForegroundWindow();
            List<Window> windows = WindowsApi.getOpenWindows();
            Map<String, ProcessesPerExe> exeWindows = toWindowsPerExeMap(windows);
            ProcessesPerExe foregroundWindowPerExe = exeWindows.remove(foregroundWindow.getExePath());
            if (foregroundWindowPerExe == null) {
                foregroundWindowPerExe = ProcessesPerExe.fromWindow(foregroundWindow);
            }
            OpenWindowsSnapshot snapshot = new OpenWindowsSnapshot(foregroundWindowPerExe, exeWindows);
            callback.accept(snapshot);
        };
    }

    private static Map<String, ProcessesPerExe> toWindowsPerExeMap(List<Window> windows) {
        Map<String, ProcessesPerExe> perExeWindows = new HashMap<>();
        Map<String, Set<Integer>> ids = windows
                .stream()
                .collect(Collectors.groupingBy(Window::getExePath,
                        Collectors.mapping(Window::getProcessId, Collectors.toSet())));
        ids.forEach((k, v) -> perExeWindows.put(k, new ProcessesPerExe(k, v)));
        perExeWindows.remove("");
        return perExeWindows;
    }

}
