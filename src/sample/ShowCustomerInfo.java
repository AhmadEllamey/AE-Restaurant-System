package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static sample.DBConnection.*;
import static sample.Main.ShowTheCustomerInfoStage;
import static sample.MainScreen.SearchForThatCustomer;

public class ShowCustomerInfo implements Initializable {



    @FXML
    private JFXTextField Name;

    @FXML
    private JFXTextField phone;

    @FXML
    private JFXTextField address;

    @FXML
    private JFXTextField note;

    @FXML
    private JFXTextField cash;

    @FXML
    private JFXTextField points;

    @FXML
    private JFXButton OkButton;


    public void OKFun() {

        // close the Stage
        Stage stage = (Stage) OkButton.getScene().getWindow();
        stage.close();

    }


    private Consumer<String> callback;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setup(Consumer<String> callback) {
        this.callback = callback;
    }


    public ShowCustomerInfo() {

        ShowTheCustomerInfoStage.setOnShowing(event -> {
            try {
                OpenConnection();

                Statement GetThatCustomer =  connection.createStatement();
                String Customer = "SELECT * FROM customers_info WHERE phone_number = '"+SearchForThatCustomer+"'";
                ResultSet resultSet = GetThatCustomer.executeQuery(Customer);

                if(resultSet.next()){

                    Name.setText(resultSet.getString("customer_name"));
                    phone.setText(resultSet.getString("phone_number"));
                    address.setText(resultSet.getString("address"));
                    note.setText(resultSet.getString("note"));
                    cash.setText(resultSet.getString("customer_cash"));
                    points.setText(resultSet.getString("customer_points"));

                }

                CloseConnection();

            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
            }
        });

    }






}
