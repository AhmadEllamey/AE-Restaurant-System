package sample;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static sample.DBConnection.*;
import static sample.DBConnection.CloseConnection;
import static sample.MainScreen.SelectedOrderItem;

public class IsTheOrderReady implements Initializable {


    private Consumer<String> callback;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setup(Consumer<String> callback) {
        this.callback = callback;
    }


    @FXML
    private JFXButton DeleteButton;

    @FXML
    private JFXButton CancelButton;


    public void CancelFun() {
        // close the Stage
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }

    public void DeleteAnOrderFun() {
        try {
            OpenConnection();

            Statement UpdateGeneralOrderTemp = connection.createStatement();
            String RemoveGeneralOrderTemp = "Delete FROM general_order_temp WHERE code = '"+SelectedOrderItem+"'";
            UpdateGeneralOrderTemp.executeUpdate(RemoveGeneralOrderTemp);

            callback.accept("");

            // close the Stage
            Stage stage = (Stage) DeleteButton.getScene().getWindow();
            stage.close();

            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }
    }




}
