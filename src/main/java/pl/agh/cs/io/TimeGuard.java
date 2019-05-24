package pl.agh.cs.io;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class TimeGuard extends Application {
    private Stage primaryStage;

    @Override
    public void init() throws Exception {
        super.init();
        //empty method provided for future use with persistence
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        //empty method provided for future use with persistence
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("TimeGuard");

        initRootLayout();
    }

    public static void main(String[] args) {
        String exeLocation = "exe/";
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec("PowerShell.exe " + exeLocation + "/Handle64.exe");
            InputStream is = process.getInputStream();
            Scanner scanner = new Scanner(is);
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //launch(args);
    }


    private void initRootLayout() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TimeGuard.class.getResource("/TimeGuard.fxml"));
            VBox rootLayout = loader.load();

            Scene scene = new Scene(rootLayout, 800, 600);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {

        }
    }
}
