package pl.agh.cs.io;

import pl.agh.cs.io.api.files.FilesListenerRunner;
import pl.agh.cs.io.api.windows.WindowsListenerRunner;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class MainTest {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(2);
        FilesListenerRunner filesListenerRunner = new FilesListenerRunner(scheduledExecutorService);
        filesListenerRunner.run(System.out::println);

        WindowsListenerRunner windowsListenerRunner = new WindowsListenerRunner(scheduledExecutorService);
        windowsListenerRunner.run(System.out::println);
    }
}
