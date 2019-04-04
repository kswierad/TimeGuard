package pl.agh.cs.io;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class WindowsListenerRunner {
    private final static long POLLING_DELAY_MILLIS = 1000;
    private static Consumer<OpenWindowsSnapshot> callback;
    private static ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

    public static void run(Consumer<OpenWindowsSnapshot> callback) {
        WindowsListenerRunner.callback = callback;
        executorService.scheduleAtFixedRate(newGetOpenWindowsTask(), POLLING_DELAY_MILLIS, POLLING_DELAY_MILLIS,  TimeUnit.MILLISECONDS);
    }

    private static Runnable newGetOpenWindowsTask() {
        return () -> {
            Window foregroundWindow = WindowsApiFacade.getForegroundWindow();
            List<Window> windows = WindowsApiFacade.getOpenWindows();
            Map<String, WindowsPerExe> exeWindows = new HashMap<>();
            Map<String, Set<Integer>> ids = windows
                    .stream()
                    .collect(Collectors.groupingBy(Window::getExePath,
                            Collectors.mapping(Window::getProcessId, Collectors.toSet())));
            ids.forEach((k, v) -> exeWindows.put(k, new WindowsPerExe(k, v)));
            WindowsPerExe foregroundWindowPerExe = exeWindows.remove(foregroundWindow.getExePath());
            OpenWindowsSnapshot snapshot = new OpenWindowsSnapshot(foregroundWindowPerExe, exeWindows);
            callback.accept(snapshot);
        };
    }
}
