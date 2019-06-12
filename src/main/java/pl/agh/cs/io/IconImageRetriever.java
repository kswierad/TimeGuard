package pl.agh.cs.io;

import com.sun.jna.Memory;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.Shell32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HICON;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinGDI.BITMAPINFO;
import com.sun.jna.platform.win32.WinGDI.ICONINFO;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

import static com.sun.jna.platform.win32.WinGDI.BI_RGB;
import static com.sun.jna.platform.win32.WinGDI.DIB_RGB_COLORS;


public class IconImageRetriever {
    private String path;
    private HICON iconHandle;
    private int width;
    private int height;

    public IconImageRetriever(String path) {
        this.path = path;
    }

    public Image getIconImage() {
        try {
            ICONINFO iconInfo = getIconInfo();
            height = iconInfo.xHotspot * 2;
            width = iconInfo.yHotspot * 2;
            BITMAPINFO bitmapInfo = setupBitmapInfo(iconInfo);
            int[] colourBits = getColourBits(bitmapInfo, iconInfo);
            User32.INSTANCE.DestroyIcon(iconHandle);
            return convertToImage(colourBits);
        } catch (IllegalStateException e) {
            Image image = new Image("/default_icon.png");
            return image;
        }
    }

    private ICONINFO getIconInfo() {
        HICON[] largeHicons = new HICON[1];
        Shell32.INSTANCE.ExtractIconEx(path, 0, largeHicons, null, 1);
        iconHandle = largeHicons[0];
        if (iconHandle == null) {
            throw new IllegalStateException("Icon cannot be retrieved");
        }
        ICONINFO iconInfo = new ICONINFO();
        User32.INSTANCE.GetIconInfo(largeHicons[0], iconInfo);
        return iconInfo;
    }

    private BITMAPINFO setupBitmapInfo(ICONINFO iconInfo) {
        BITMAPINFO bitmapInfo = new BITMAPINFO();
        bitmapInfo.bmiHeader.biWidth = width;
        bitmapInfo.bmiHeader.biHeight = -height;
        bitmapInfo.bmiHeader.biPlanes = 1;
        bitmapInfo.bmiHeader.biBitCount = 32;
        bitmapInfo.bmiHeader.biCompression = BI_RGB;
        return bitmapInfo;
    }

    private int[] getColourBits(BITMAPINFO bitmapInfo, ICONINFO iconInfo) {
        Memory memory = new Memory(height * width * 4);
        HWND hwnd = new HWND();
        HDC hdc = User32.INSTANCE.GetDC(hwnd);
        GDI32.INSTANCE.GetDIBits(hdc, iconInfo.hbmColor, 0, height, memory, bitmapInfo, DIB_RGB_COLORS);
       return memory.getIntArray(0, width * height);
    }

    private Image convertToImage(int[] colourBits) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.setRGB(0, 0, width, height, colourBits, 0,  height);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }


}
