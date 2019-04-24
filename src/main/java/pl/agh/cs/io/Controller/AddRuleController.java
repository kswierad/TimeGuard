package pl.agh.cs.io.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.agh.cs.io.Rule;
import pl.agh.cs.io.Rules;

public class AddRuleController {

    Rules rules;

    @FXML
    private TextField pathField;

    @FXML
    private Button submitButton;

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        if(pathField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Path");
            alert.setHeaderText("You entered an empty Path");
            alert.setContentText("Please enter a correct path!");
            alert.showAndWait();
        }
        System.out.println(pathField.getText());
        rules.addRule(new Rule(pathField.getText()));
        Stage stage  = (Stage) submitButton.getScene().getWindow();
        stage.close();

    }

    public void setRules(Rules rules){
        this.rules = rules;
    }
}
