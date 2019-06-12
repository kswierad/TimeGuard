package pl.agh.cs.io.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pl.agh.cs.io.ExceededUsageAction;
import pl.agh.cs.io.model.Rule;
import pl.agh.cs.io.model.RuleRestriction;
import pl.agh.cs.io.model.WindowState;

import static pl.agh.cs.io.Utils.timeToLong;

public class EditProgramController {


    private Rule rule;
    @FXML
    private CheckBox enable;
    @FXML
    private TextField permittedTime;
    @FXML
    private ChoiceBox<ExceededUsageAction> action;
    @FXML
    private ChoiceBox<WindowState> state;
    @FXML
    private Text errorInfo;

    @FXML
    public void okAction(ActionEvent event) {
        if (enable.isSelected()) {
            try {
                Long permittedTime = timeToLong(this.permittedTime.getCharacters().toString());
                ExceededUsageAction action = this.action.getSelectionModel().getSelectedItem();
                WindowState state = this.state.getSelectionModel().getSelectedItem();

                if (action != null && state != null) {
                    rule.removeRestriction();
                    RuleRestriction restriction = new RuleRestriction(state, permittedTime, action);
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

    @FXML
    public void resetTimeInRestriction(ActionEvent event) {
        rule.resetTimes();
        close();
    }


    public void setRule(Rule rule) {
        this.rule = rule;
        if (this.rule.getRestriction() != null) {
            enable.setSelected(true);
            RuleRestriction restriction = rule.getRestriction();

            action.setValue(restriction.getAction());
            state.setValue(restriction.getState());
            permittedTime.insertText(0, longToString(restriction.getPermittedNumSec()));
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
            state.setDisable(false);
        } else {
            permittedTime.setDisable(true);
            action.setDisable(true);
            state.setDisable(true);
        }
    }

    public static String longToString(Long secondsOrigin) {
        Long seconds = secondsOrigin % 60;
        secondsOrigin = secondsOrigin / 60;
        Long minutes = secondsOrigin % 60;
        secondsOrigin = secondsOrigin / 60;
        Long hours = secondsOrigin % 60;

        if (seconds > 0) {
            return hours + ":" + minutes + ":" + seconds;
        } else {
            return hours + ":" + minutes;
        }
    }


}
