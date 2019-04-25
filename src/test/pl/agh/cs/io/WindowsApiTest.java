package pl.agh.cs.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


public class WindowsApiTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getOpenWindows() {
        String openedAppExePath = "exePath='C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe";
        String notOpenedAppExePath = "...";
        String[] openedWindows = WindowsApi.getOpenWindows().toString().split(",");

        boolean checkIfOpenFileExePathInList = false;
        for (String string: openedWindows){
            if (string.contains(openedAppExePath)) checkIfOpenFileExePathInList = true;
            assertFalse(string.contains(notOpenedAppExePath));
        }
        assertTrue(checkIfOpenFileExePathInList);
    }


}