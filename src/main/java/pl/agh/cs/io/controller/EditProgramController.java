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
        if (this.rule.getRestriction().isPresent()) {
            enable.setSelected(true);
            RuleRestriction restriction = rule.getRestriction().get();

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

    public static Long timeToLong(String input) throws NumberFormatException {
        Long hours;
        Long minutes;
        Long seconds;

        String[] parts = input.split(":");

        if (parts.length < 2 || parts.length > 3) {
            throw new NumberFormatException();
        }

        hours = Long.parseLong(parts[0]);
        if (hours < 0 || hours > 24) {
            throw new NumberFormatException();
        }

        minutes = Long.parseLong(parts[1]);
        if (minutes < 0 || minutes >= 60) {
            throw new NumberFormatException();
        }

        seconds = new Long(0);
        if (parts.length == 3) {
            seconds = Long.parseLong(parts[2]);
            if (seconds < 0 || seconds >= 60) {
                throw new NumberFormatException();
            }
        }

        Long result = seconds + minutes * 60 + hours * 3600;

        //if more time than it is in one day
        if (result > 24 * 3600 || result <= 0) {
            throw new NumberFormatException();
        }

        return result;
    }

}
