package pl.agh.cs.io.api.files;


import pl.agh.cs.io.api.OpenFilesPathProcessIdsRetriever;
import pl.agh.cs.io.api.PathProcessId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class FilesListenerRunner {
    private final static long POLLING_DELAY_SECONDS = 10;
    private ScheduledExecutorService scheduledExecutorService;

    public FilesListenerRunner(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }

    public void run(Consumer<Map<String, ProcessIdsPerFilepath>> callback) {
        Runnable runnable = newGetOpenFilesProcessesTask(callback);
        scheduledExecutorService.scheduleAtFixedRate(runnable,
                0,
                POLLING_DELAY_SECONDS,
                TimeUnit.SECONDS);
    }

    private Runnable newGetOpenFilesProcessesTask(Consumer<Map<String, ProcessIdsPerFilepath>> callback) {
        return () -> {
            OpenFilesPathProcessIdsRetriever openFilesPathProcessIdsRetriever = new OpenFilesPathProcessIdsRetriever();
            List<PathProcessId> pathProcessIds = openFilesPathProcessIdsRetriever.getPathProcessIds();
            Map<String, Set<Integer>> groupedProcessIds = PathProcessId.groupByPath(pathProcessIds);
            Map<String, ProcessIdsPerFilepath> processIdsPerFilepath = new HashMap<>();
            groupedProcessIds.forEach((k, v) -> processIdsPerFilepath.put(k, new ProcessIdsPerFilepath(k, v)));
            callback.accept(processIdsPerFilepath);
        };
    }
}
