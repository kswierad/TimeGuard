package pl.agh.cs.io;

import javafx.scene.image.Image;

public class ImgWithPath {

    private String path;
    private Image img;

    public ImgWithPath(String path) {
        this.path = path;
        IconImageRetriever iconImageRetriever = new IconImageRetriever(path);
        this.img = iconImageRetriever.getIconImage();
    }


    public Image getImg() {
        return img;
    }

    public String getPath() {
        return path;
    }
}
