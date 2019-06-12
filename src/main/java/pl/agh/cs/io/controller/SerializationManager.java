package pl.agh.cs.io.controller;

import pl.agh.cs.io.model.FileRule;
import pl.agh.cs.io.model.Rule;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SerializationManager {

    public static String appFilePath = "apps.ser";
    public static String filesFilePath = "files.ser";

    public static void serializeApps(Map<String, Rule> o) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(appFilePath))) {
            // Method for serialization of object
            out.writeObject(o);

        } catch (Exception e) {
        }
    }

    public static void serializeFiles(Map<String, FileRule> o) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filesFilePath))) {
            // Method for serialization of object
            out.writeObject(o);

        } catch (Exception e) {
        }
    }

    public static Map<String, Rule> deserializeApps() {
        FileInputStream file;
        try {
            file = new FileInputStream(appFilePath);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            Map<String, Rule> result = (Map<String, Rule>) in.readObject();

            in.close();
            file.close();

            return result;
        } catch (IOException | ClassNotFoundException e) {

        }
        return new HashMap<>();
    }

    public static Map<String, FileRule> deserializeFiles() {
        FileInputStream file;
        try {
            file = new FileInputStream(filesFilePath);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            Map<String, FileRule> result = (Map<String, FileRule>) in.readObject();

            in.close();
            file.close();

            return result;
        } catch (IOException | ClassNotFoundException e) {
        }
        return new HashMap<>();
    }
}
