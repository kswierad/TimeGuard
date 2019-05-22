package pl.agh.cs.io.counter;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TimeCounter {

    private double xOffset = 0, yOffset = 0;

    private Label timeLabel = new Label("0:00");
    private StackPane stackPane = new StackPane();
    private Group root = new Group();
    private Stage stage = new Stage();


    public void start() {

        root.getChildren().addAll(timeLabel);
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.show();
    }

    public void setText(String text) {
        timeLabel.setText(text);
    }

    public void setWidthAndHeight() {
        root.applyCss();
        root.layout();
        stage.setWidth(timeLabel.getWidth());
        stage.setHeight(timeLabel.getHeight());
    }

}
