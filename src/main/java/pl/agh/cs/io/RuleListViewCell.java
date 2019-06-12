package pl.agh.cs.io;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import pl.agh.cs.io.controller.NameConverter;
import pl.agh.cs.io.model.Rules;


import java.io.IOException;

public class RuleListViewCell extends ListCell<ImgWithPath> {

    private final Rules rules;
    @FXML
    Label exeName;

    @FXML
    ImageView imageView;

    @FXML
    ImageView imageR;

    @FXML
    GridPane gridPane;

    private FXMLLoader mLLoader;

    public RuleListViewCell(Rules rules) {
        this.rules = rules;
    }

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
            if (rules.getRules().get(imgWithPath.getPath()).getRestriction().isPresent()) {
                imageR.setVisible(true);
            } else {
                imageR.setVisible(false);
            }
            setText(null);
            setGraphic(gridPane);
        }

    }


}
