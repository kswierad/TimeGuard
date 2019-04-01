package pl.agh.cs.io;

import java.util.List;

public class TimeGuard {
    public static void main(String args[]){
        WindowsListenerRunner.run(snapshot -> {
            System.out.println(snapshot);

            Window foregroundWindow = snapshot.getForegroundWindow();
            if (foregroundWindow.getExePath().contains("mspaint.exe"))
                foregroundWindow.close();

            List<WindowsPerExe> windows = snapshot.getAllWindows();
            windows.forEach(window -> {
                if (window.getExePath().contains("notepad.exe"))
                    window.close();
            });

        });
    }
}
