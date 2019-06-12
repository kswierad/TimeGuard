package pl.agh.cs.io.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pl.agh.cs.io.ExceededUsageAction;
import pl.agh.cs.io.model.FileRestriction;
import pl.agh.cs.io.model.FileRule;

import static pl.agh.cs.io.Utils.timeToLong;


public class EditFileController {


    private FileRule rule;
    @FXML
    private CheckBox enable;
    @FXML
    private TextField permittedTime;
    @FXML
    private ChoiceBox<ExceededUsageAction> action;
    @FXML
    private Text errorInfo;

    @FXML
    public void okAction(ActionEvent event) {
        if (enable.isSelected()) {
            try {
                Long permittedTime = timeToLong(this.permittedTime.getCharacters().toString());
                ExceededUsageAction action = this.action.getSelectionModel().getSelectedItem();

                if (action != null) {
                    rule.removeRestriction();
                    FileRestriction restriction = new FileRestriction(permittedTime, action, rule);
                    rule.setRestriction(restriction);
                    close();
                } else {
                    errorInfo.setText("Every field have to be filled");
                }
            } catch (NumberFormatException e) {
                errorInfo.setText("Invalid allowed time");
            }
        } else {
            removeRestriction();
            close();
        }
    }

    @FXML
    public void resetTimeInRestriction(ActionEvent event) {
        rule.resetTimes();
        close();
    }

    @FXML
    public void cancelAction(ActionEvent event) {
        close();
    }

    @FXML
    public void enableAction(ActionEvent event) {
        changeInputFieldsStatus();
    }

    private void removeRestriction() {
        rule.removeRestriction();
        close();
    }

    public void setRule(FileRule rule) {
        this.rule = rule;
        if (this.rule.getRestriction().isPresent()) {
            enable.setSelected(true);
            FileRestriction restriction = rule.getRestriction().get();

            action.setValue(restriction.getAction());
            permittedTime.insertText(0, EditProgramController.longToString(restriction.getPermittedNumSec()));
        } else {
            enable.setSelected(false);
            permittedTime.insertText(0, "00:00");
        }
        changeInputFieldsStatus();
    }

    private void close() {
        Stage stage = (Stage) action.getScene().getWindow();
        stage.close();
    }

    private void changeInputFieldsStatus() {
        if (enable.isSelected()) {
            permittedTime.setDisable(false);
            action.setDisable(false);
        } else {
            permittedTime.setDisable(true);
            action.setDisable(true);
        }
    }

}
