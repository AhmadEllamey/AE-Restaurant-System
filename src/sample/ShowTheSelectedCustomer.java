package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
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
import static sample.Main.SelectedCustomerStage;
import static sample.MainScreen.SelectedCustomerFromCustomerTable;

public class ShowTheSelectedCustomer implements Initializable {

    private Consumer<String> callback;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setup(Consumer<String> callback) {
        this.callback = callback;
    }


    public ShowTheSelectedCustomer() {

        SelectedCustomerStage.setOnShowing(event -> {
            try {
                OpenConnection();

                Statement GetThatCustomer =  connection.createStatement();
                String Customer = "SELECT * FROM customers_info WHERE phone_number = '"+SelectedCustomerFromCustomerTable+"'";
                ResultSet resultSet = GetThatCustomer.executeQuery(Customer);

                if(resultSet.next()){
                    CustomerNameText.setText(resultSet.getString("customer_name"));
                    CustomerPhoneText.setText(resultSet.getString("phone_number"));
                    CustomerAddressText.setText(resultSet.getString("address"));
                    CustomerNoteText.setText(resultSet.getString("note"));

                    CustomerPhoneText.setEditable(false);
                }

                CloseConnection();

            }catch (Exception e){
                e.printStackTrace();
            }
        });

    }


    @FXML
    private JFXTextField CustomerNameText;

    @FXML
    private JFXTextField CustomerPhoneText;

    @FXML
    private JFXTextArea CustomerAddressText;

    @FXML
    private JFXTextArea CustomerNoteText;

    @FXML
    private JFXButton AddNewCustomerButton;


    public void UpDateTheCurrentCustomerFun(){

        if(!CustomerNameText.getText().trim().isEmpty()&&!CustomerAddressText.getText().trim().isEmpty()){

            try {

                    if(!CustomerNameText.getText().trim().isEmpty()&&!CustomerAddressText.getText().trim().isEmpty()) {
                        OpenConnection();
                        Statement UpdateCustomer = connection.createStatement();
                        String Customer = "UPDATE customers_info SET customer_name = '"+CustomerNameText.getText().trim()+
                                "' , address = '"+CustomerAddressText.getText().trim()+
                                "' , note = '"+CustomerNoteText.getText().trim()+
                                "' WHERE phone_number = '"+CustomerPhoneText.getText().trim()+"'";
                        UpdateCustomer.executeUpdate(Customer);

                        CloseConnection();

                        // close the Stage
                        Stage stage = (Stage) AddNewCustomerButton.getScene().getWindow();
                        stage.close();

                        // ToDo  reload the customers table ?????

                        callback.accept("");

                    }
                }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
            }

        }

        }



}


