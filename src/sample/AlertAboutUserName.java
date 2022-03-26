package sample;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class AlertAboutUserName {


    @FXML
    private JFXButton OkButton;

    public void OkToGOFun(){
        // close the Stage
        Stage stage = (Stage) OkButton.getScene().getWindow();
        stage.close();
    }


}
