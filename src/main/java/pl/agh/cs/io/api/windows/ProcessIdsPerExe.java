package pl.agh.cs.io.api.windows;

import pl.agh.cs.io.api.ProcessIdsPerPath;

import java.util.Set;

public class ProcessIdsPerExe extends ProcessIdsPerPath {
    public ProcessIdsPerExe(String path, Set<Integer> processIds) {
        super(path, processIds);
    }

    public void terminateProcessesWithExePath() {
        terminateProcesses();
    }
}
