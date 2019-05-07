package pl.agh.cs.io.api;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Psapi;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;
import com.sun.jna.ptr.IntByReference;

import java.util.LinkedList;
import java.util.List;

import static com.sun.jna.platform.win32.WinNT.PROCESS_QUERY_INFORMATION;
import static com.sun.jna.platform.win32.WinNT.PROCESS_TERMINATE;

public class WindowsApi {
    private final static int MAX_PATH_BYTES = 1024;

    public static List<PathProcessId> getOpenWindowsPathProcessIds() {
        List<PathProcessId> windows = new LinkedList<>();
        WNDENUMPROC proc = (hwnd, data) -> {
            PathProcessId pathProcessId = newPathProcessId(hwnd);
            windows.add(pathProcessId);
            return true;
        };
        User32.INSTANCE.EnumWindows(proc, null);
        return windows;
    }

    public static PathProcessId getForegroundWindowPathProcessId() {
        HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        return newPathProcessId(hwnd);
    }

    public static void terminateProcess(int processId) {
        HANDLE process = getProcessHandle(processId);
        Kernel32.INSTANCE.TerminateProcess(process, 0);
    }

    private static int getProcessIdForWindow(HWND hwnd) {
        IntByReference processId = new IntByReference();
        User32.INSTANCE.GetWindowThreadProcessId(hwnd, processId);
        return processId.getValue();
    }

    private static HANDLE getProcessHandle(int processId) {
        return Kernel32.INSTANCE.OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_TERMINATE, false, processId);
    }

    private static PathProcessId newPathProcessId(HWND hwnd) {
        int processId = getProcessIdForWindow(hwnd);
        String exePath = getExePathForProcess(processId);
        return new PathProcessId(processId, exePath);
    }

    private static String getExePathForProcess(int processId) {
        HANDLE process = getProcessHandle(processId);
        char[] buffer = new char[MAX_PATH_BYTES];
        Psapi.INSTANCE.GetModuleFileNameExW(process, null, buffer, buffer.length);
        return Native.toString(buffer);
    }
}
