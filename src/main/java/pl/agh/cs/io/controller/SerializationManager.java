package pl.agh.cs.io.controller;

import pl.agh.cs.io.Rule;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SerializationManager {

    public static String filePath = "rules.ser";

    public static void serialize(Map<String, Rule> o) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            // Method for serialization of object
            out.writeObject(o);
            System.out.println("Objects have been serialized");

        } catch (Exception e) {
        }
    }

    public static Map<String, Rule> deserialize() {
        FileInputStream file;
        try {
            file = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            Map<String, Rule> result = (Map<String, Rule>) in.readObject();

            in.close();
            file.close();

            System.out.println("Objects have been deserialized ");
            return result;
        } catch (IOException | ClassNotFoundException e) {

        }
        return new HashMap<>();
    }
}
