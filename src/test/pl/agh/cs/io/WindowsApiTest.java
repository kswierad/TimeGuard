package pl.agh.cs.io;

import org.junit.Test;
import pl.agh.cs.io.api.WindowsApi;

import static org.junit.Assert.assertFalse;


public class WindowsApiTest {


    @Test
    public void getOpenWindows() {
        String openedAppExePath = "exePath='C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe";
        String notOpenedAppExePath = "...";
        String[] openedWindows = WindowsApi.getOpenWindowsPathProcessIds().toString().split(",");

        boolean checkIfOpenFileExePathInList = false;
        for (String string: openedWindows){
            if (string.contains(openedAppExePath)) checkIfOpenFileExePathInList = true;
            assertFalse(string.contains(notOpenedAppExePath));
        }
        //assertTrue(checkIfOpenFileExePathInList);
    }


}