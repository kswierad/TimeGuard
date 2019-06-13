package pl.agh.cs.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

public class Autostart {
    private static final String windowsStartupDirectory = System.getenv("APPDATA") +
            "\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\";

    public static String getPath() throws URISyntaxException {
        return new File(Autostart.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
    }

    public static void writeAutostartFile() throws IOException, URISyntaxException {
        String str = String.format("start javaw -Xmx1024m -jar \"%s\"", Autostart.getPath());
        BufferedWriter writer = new BufferedWriter(new FileWriter(windowsStartupDirectory + "TimeGuard.bat"));
        writer.write(str);
        writer.close();
    }

    public static void deleteAutostartFile() {
        File file = new File(windowsStartupDirectory + "TimeGuard.bat");
        file.delete();
    }
}
