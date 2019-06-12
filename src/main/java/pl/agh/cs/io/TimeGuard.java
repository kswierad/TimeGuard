package pl.agh.cs.io;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.agh.cs.io.controller.TimeGuardController;
import pl.agh.cs.io.counter.TimeCounterController;

import javax.imageio.ImageIO;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

public class TimeGuard extends Application {
    public static Stage primaryStage;
    private SystemTray tray;
    private TrayIcon icon;

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
        Platform.setImplicitExit(false);
        launch(args);
    }


    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TimeGuard.class.getResource("/TimeGuard.fxml"));
            VBox rootLayout = loader.load();

            Scene scene = new Scene(rootLayout, 800, 600);

            primaryStage.setScene(scene);
            primaryStage.setIconified(false);
            primaryStage.show();
            primaryStage.setOnCloseRequest(closeEvent -> {
                boolean closed = this.close();
                if (!closed) {
                    closeEvent.consume();
                }
            });
            primaryStage.getIcons().add(new javafx.scene.image.Image(TimeGuard.class.getResourceAsStream("/icon.png")));

            TimeGuardController timeGuardController = loader.getController();
            TimeCounterController timeCounterController = timeGuardController.getTimeCounterController();
            timeCounterController.setAlwaysOnTop();
            timeCounterController.setWindowPosition((int) primaryStage.getX(), (int) primaryStage.getY());

            try {
                BufferedImage bufferedImage = ImageIO.read(TimeGuard.class.getResource("/icon.png"));
                Image image = bufferedImage.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                icon = new TrayIcon(image);
                icon.addActionListener(e -> {
                    Platform.runLater(primaryStage::show);
                });
                tray = SystemTray.getSystemTray();
                tray.add(icon);
            } catch (IOException | AWTException e) {
            }
            primaryStage.iconifiedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    primaryStage.hide();
                    primaryStage.setIconified(false);
                }
            });
        } catch (IOException e) {

        }
    }

    private boolean close() {
        Optional<ButtonType> optionalResult = new Alert(Alert.AlertType.WARNING,
                "Are you sure you want to close the app? " +
                "To keep the counters running, minimize the window. You can bring it back from the system tray",
                ButtonType.CLOSE, ButtonType.CANCEL).showAndWait();
        ButtonType result = optionalResult.orElse(ButtonType.CANCEL);
        if (result == ButtonType.CLOSE) {
            tray.remove(icon);
            Platform.exit();
            System.exit(0);
            return true;
        }
        return false;
    }
}
