package pl.agh.cs.io.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OpenFilesPathProcessIdsRetriever {
    private static String systemDrivePrefix = System.getenv("SystemDrive");
    private static String exeLocation = "exe/";
    private static List<String> patternsToIgnoreStrings = Arrays.asList(
            "\\\\Windows.*",
            "\\\\Program Files.*",
            "\\\\ProgramData.*",
            "\\\\Users\\\\.*?\\\\\\..*",
            "\\\\Users\\\\.*?\\\\AppData.*"
    );
    private List<Pattern> toIgnore;
    private Scanner scanner;

    public OpenFilesPathProcessIdsRetriever() {
        toIgnore = patternsToIgnoreStrings
                .stream()
                .map(OpenFilesPathProcessIdsRetriever::addSystemPrefix)
                .map(Pattern::compile)
                .collect(Collectors.toList());
    }

    public List<PathProcessId> getPathProcessIds() {
        execAndLoadResults();
        List<PathProcessId> pathProcessIds = new LinkedList<>();
        skipIrrelevantLines();
        while (scanner.hasNext()) {
            int pid = readPid();
            pathProcessIds.addAll(readPathsPerProcess(pid));
        }
        return pathProcessIds;
    }

    private void execAndLoadResults() {
        try {
            Runtime runtime = Runtime.getRuntime();
            String command =
                    String.format("%s\\System32\\WindowsPowershell\\v1.0\\powershell.exe %sHandle64.exe /accepteula",
                            System.getenv("SYSTEMROOT"),
                            exeLocation);
            Process process = runtime.exec(command);
            InputStream is = process.getInputStream();
            scanner = new Scanner(is);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to getPathProcessIds file handles");
        }
    }

    private void skipIrrelevantLines() {
        String line = scanner.nextLine();
        while (!line.startsWith("-")) {
            line = scanner.nextLine();
        }
    }

    private int readPid() {
        String next;
        do {
            next = scanner.next();
        }
        while (!next.equals("pid:"));
        return scanner.nextInt();
    }

    private List<PathProcessId> readPathsPerProcess(int pid) {
        List<PathProcessId> paths = new LinkedList<>();
        scanner.nextLine(); //skip owner
        String next;
        while (scanner.hasNext()) {
            next = scanner.next();
            if (next.startsWith("-")) {
                return paths;
            }
            String type = scanner.next();
            if (!type.equals("File")) {
                scanner.nextLine();
                continue;
            }
            String path = scanner.nextLine().trim();
            if (!matchesAnyPattern(path)) {
                paths.add(new PathProcessId(pid, path));
            }
        }
        return paths;
    }

    private boolean matchesAnyPattern(String path) {
        for (Pattern p : toIgnore) {
            if (p.matcher(path).matches()) {
                return true;
            }
        }
        return false;
    }

    private static String addSystemPrefix(String path) {
        return systemDrivePrefix + path;
    }
}
