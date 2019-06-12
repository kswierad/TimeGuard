package pl.agh.cs.io;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import pl.agh.cs.io.controller.NameConverter;


import java.io.IOException;

public class RuleListViewCell extends ListCell<ImgWithPath> {

    @FXML
    Label exeName;

    @FXML
    ImageView imageView;

    @FXML
    GridPane gridPane;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(ImgWithPath imgWithPath, boolean empty) {
        super.updateItem(imgWithPath, empty);

        if (empty || imgWithPath == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/listViewCell.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    //do nothing :v
                }

            }

            exeName.setText(NameConverter.nameFromPath(imgWithPath.getPath()));
            imageView.setImage(imgWithPath.getImg());

            setText(null);
            setGraphic(gridPane);
        }

    }


}
