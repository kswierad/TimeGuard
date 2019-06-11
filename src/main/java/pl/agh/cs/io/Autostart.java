package pl.agh.cs.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

public class Autostart {
    private static final String windowsStartupDirectory = System.getenv("APPDATA") +
            "\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\";

    public static String getPath(){
        try {
            return new File(Autostart.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeAutostartFile() throws IOException {
        String str = "javaw -Xmx200m -jar " + Autostart.getPath();
        BufferedWriter writer = new BufferedWriter(new FileWriter(windowsStartupDirectory+"TimeGuard.bat"));
        writer.write(str);
        writer.close();
    }

    public static void deleteAutostartFile() {
        File file = new File(windowsStartupDirectory+"TimeGuard.bat");
        file.delete();
    }
}
