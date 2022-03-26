package sample;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class AlertAboutPhoneNumber {


    @FXML
    private JFXButton OkButton;


    public void ToGoFunction() {

        // close the Stage
        Stage stage = (Stage) OkButton.getScene().getWindow();
        stage.close();

    }


}
