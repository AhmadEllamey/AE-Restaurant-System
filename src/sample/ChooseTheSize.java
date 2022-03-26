package sample;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static sample.MainScreen.ItemSize;

public class ChooseTheSize implements Initializable {


    private Consumer<String> callback;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setup(Consumer<String> callback) {
        this.callback = callback;
    }


    @FXML
    public void ManageTheSelectionOptions(ActionEvent event) {

        // get the id of the clicked button
        JFXButton btn = (JFXButton) event.getSource();
        ItemSize = btn.getId();

        // close the Stage
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();

        // ToDo  reload the customers table ?????

        callback.accept("");

    }


}
