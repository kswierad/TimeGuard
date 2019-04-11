package pl.agh.cs.io;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;
import com.sun.jna.ptr.IntByReference;

import java.util.LinkedList;
import java.util.List;

import static com.sun.jna.platform.win32.WinNT.PROCESS_QUERY_INFORMATION;
import static com.sun.jna.platform.win32.WinNT.PROCESS_TERMINATE;

public class WindowsApiFacade {
    private final static int MAX_PATH_BYTES = 1024;

    public static List<Window> getOpenWindows() {
        List<Window> windows = new LinkedList<>();
        WNDENUMPROC proc = (hwnd, data) -> {
            Window window = newWindow(hwnd);
            windows.add(window);
            return true;
        };
        User32.INSTANCE.EnumWindows(proc, null);
        return windows;
    }

    public static Window getForegroundWindow() {
        HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        return newWindow(hwnd);
    }

    public static void terminateProces(int processId) {
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

    private static Window newWindow(HWND hwnd) {
        int processId = getProcessIdForWindow(hwnd);
        String exePath = getExePathForProcess(processId);
        return new Window(processId, exePath);
    }

    private static HWND longToHwnd(long id) {
        Pointer pointer = new Pointer(id);
        return new HWND(pointer);
    }

    private static String getExePathForProcess(int processId) {
        HANDLE process = getProcessHandle(processId);
        char[] buffer = new char[MAX_PATH_BYTES];
        Psapi.INSTANCE.GetModuleFileNameExW(process, null, buffer, buffer.length);
        return Native.toString(buffer);
    }
}
