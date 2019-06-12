package pl.agh.cs.io.controller;

import pl.agh.cs.io.ImgWithPath;

import java.util.HashMap;
import java.util.Map;

public class NameConverter {

    public static Map<String, ImgWithPath> nameToImgWithPath = new HashMap<>();
    public static Map<String, String> nameToPath = new HashMap<>();

    public static String nameFromPath(String path) {
        String tokens[] = path.split("\\\\");
        return tokens[tokens.length - 1];
    }
}
