package pl.agh.cs.io.api.files;

import pl.agh.cs.io.api.ProcessIdsPerPath;

import java.util.Set;

public class ProcessIdsPerFilepath extends ProcessIdsPerPath {
    public ProcessIdsPerFilepath(String path, Set<Integer> processIds) {
        super(path, processIds);
    }

    public void terminateProcessesWithHandlesToPath() {
        terminateProcesses();
    }
}
