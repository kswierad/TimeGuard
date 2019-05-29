package pl.agh.cs.io.controller;

import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import pl.agh.cs.io.Rule;

import java.io.*;

public class SerializationManager {

    public static String filePath = "main/resources/rules.ser";

    private static MapProperty<String, Rule> emptyRules = new SimpleMapProperty<>(FXCollections.observableHashMap());

    public static void serialize(MapProperty<String, Rule> o){

        FileOutputStream file;
        try {
            file = new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(o);

            out.close();
            file.close();

            System.out.println("Objects have been serialized");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MapProperty<String, Rule> deserialize(){

        FileInputStream file;
        try {
            file = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            MapProperty<String, Rule> result = (MapProperty<String, Rule>) in.readObject();

            in.close();
            file.close();

            System.out.println("Objects have been deserialized ");
            return result;
        } catch (EOFException e){
            return emptyRules;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return emptyRules;
    }
}