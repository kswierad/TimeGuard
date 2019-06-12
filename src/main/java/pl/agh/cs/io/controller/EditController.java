package pl.agh.cs.io.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pl.agh.cs.io.ExceededUsageAction;
import pl.agh.cs.io.Rule;
import pl.agh.cs.io.RuleRestriction;
import pl.agh.cs.io.WindowState;

public class EditController {


    public Rule rule;

    @FXML
    private TextField permittedTime;
    @FXML
    private ChoiceBox<ExceededUsageAction> action;
    @FXML
    private ChoiceBox<WindowState> state;
    @FXML
    private Text errorInfo;

    @FXML
    public void applyChanges(ActionEvent event) {
        try {
            Integer permittedTime = Integer.parseInt(this.permittedTime.getCharacters().toString());
            ExceededUsageAction action = this.action.getSelectionModel().getSelectedItem();
            WindowState state = this.state.getSelectionModel().getSelectedItem();

            if (action != null && state != null) {
                rule.removeRestriction();
                RuleRestriction restriction = new RuleRestriction(state, permittedTime, action);
                rule.setRestriction(restriction);
                close();
            }
        } catch (NumberFormatException e) {
            this.errorInfo.setText("Invalid permitted time format");
        }
    }

    @FXML
    public void removeRestriction(ActionEvent event) {
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
            RuleRestriction restriction = rule.getRestriction().get();

            action.setValue(restriction.getAction());
            state.setValue(restriction.getState());
            permittedTime.insertText(0, String.valueOf(restriction.getPermittedNumSec()));
        }
    }

    private void close() {
        Stage stage = (Stage) action.getScene().getWindow();
        stage.close();
    }
}
