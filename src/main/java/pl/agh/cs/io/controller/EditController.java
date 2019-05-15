package pl.agh.cs.io.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
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
    public void applyChanges(ActionEvent event) {
        Integer permittedTime = Integer.parseInt(this.permittedTime.getCharacters().toString());
        ExceededUsageAction action = this.action.getSelectionModel().getSelectedItem();
        WindowState state = this.state.getSelectionModel().getSelectedItem();

        if (action != null && state != null) {
            rule.removeRestriction();
            RuleRestriction restriction = new RuleRestriction(state, permittedTime, action);
            rule.setRestriction(restriction);
            close();
        }
    }


    @FXML
    public void removeRestriction(ActionEvent event) {
        rule.removeRestriction();
        close();
    }


    public void setRule(Rule rule) {
        this.rule = rule;
        if (this.rule.getRestriction() != null) {
            RuleRestriction restriction = rule.getRestriction();

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