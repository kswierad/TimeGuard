package pl.agh.cs.io.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PathProcessId {
    private int processId;
    private String path;

    public PathProcessId(int processId, String path) {
        this.processId = processId;
        this.path = path;
    }

    public int getProcessId() {
        return processId;
    }

    public String getPath() {
        return path;
    }

    public static Map<String, Set<Integer>> groupByPath(List<PathProcessId> pathProcessIds) {
        Map<String, ProcessIdsPerPath> perExeWindows = new HashMap<>();
        return pathProcessIds
                .stream()
                .collect(Collectors.groupingBy(PathProcessId::getPath,
                        Collectors.mapping(PathProcessId::getProcessId, Collectors.toSet())));
    }

    @Override
    public String toString() {
        return "PathProcessId{" +
                "processId=" + processId +
                ", path='" + path + '\'' +
                '}';
    }
}
