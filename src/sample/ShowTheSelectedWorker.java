package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static sample.DBConnection.*;
import static sample.Main.SelectedWorkerStage;
import static sample.MainScreen.SelectedWorkerFromWorkerTable;

public class ShowTheSelectedWorker implements Initializable {


    private Consumer<String> callback;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setup(Consumer<String> callback) {
        this.callback = callback;
    }

    public ShowTheSelectedWorker() {

        SelectedWorkerStage.setOnShowing(event -> {
            try {
                OpenConnection();

                Statement GetThatWorker = connection.createStatement();
                String Worker = "SELECT * FROM users_info WHERE user_name = '" + SelectedWorkerFromWorkerTable + "'";
                ResultSet resultSet = GetThatWorker.executeQuery(Worker);

                if (resultSet.next()) {

                    WorkerNameText.setText(resultSet.getString("worker_name"));
                    WorkerNationalIDText.setText(resultSet.getString("national_id"));
                    WorkerAgeText.setText(resultSet.getString("age"));
                    WorkerPhoneText.setText(resultSet.getString("phone_number"));
                    WorkerAddressText.setText(resultSet.getString("address"));
                    WorkerSalaryText.setText(resultSet.getString("salary"));
                    WorkerUserNameText.setText(resultSet.getString("user_name"));
                    WorkerPasswordText.setText(resultSet.getString("password"));

                    if (resultSet.getString("worker_type").equals("1")) {

                        WorkerManagerRB.setSelected(true);

                    } else if (resultSet.getString("worker_type").equals("2")) {

                        WorkerCashierRB.setSelected(true);

                    } else if (resultSet.getString("worker_type").equals("3")){

                        WorkerChefRB.setSelected(true);

                    } else {

                        WorkerDeliveryRB.setSelected(true);

                    }


                    WorkerUserNameText.setEditable(false);
                }

                CloseConnection();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }


    @FXML
    private JFXTextField WorkerNameText;

    @FXML
    private JFXTextField WorkerNationalIDText;

    @FXML
    private JFXTextField WorkerAgeText;

    @FXML
    private JFXTextField WorkerPhoneText;

    @FXML
    private JFXTextField WorkerAddressText;

    @FXML
    private JFXRadioButton WorkerManagerRB;

    @FXML
    private ToggleGroup WorkerTypeGroup;

    @FXML
    private JFXRadioButton WorkerCashierRB;

    @FXML
    private JFXRadioButton WorkerChefRB;

    @FXML
    private JFXRadioButton WorkerDeliveryRB;

    @FXML
    private JFXTextField WorkerSalaryText;

    @FXML
    private JFXTextField WorkerUserNameText;

    @FXML
    private JFXTextField WorkerPasswordText;

    @FXML
    private JFXButton UpdateTheWorkerButton;


    public void UpDateTheCurrentWorkerFun() {

        if (!WorkerNameText.getText().trim().isEmpty()
                && !WorkerNationalIDText.getText().trim().isEmpty()
                && !WorkerAgeText.getText().trim().isEmpty()
                && !WorkerPhoneText.getText().trim().isEmpty()
                && !WorkerAddressText.getText().trim().isEmpty()
                && !WorkerSalaryText.getText().trim().isEmpty()
                && !WorkerUserNameText.getText().trim().isEmpty()
                && !WorkerPasswordText.getText().trim().isEmpty()
        ) {

            try {

                // load the worker type .

                String WorkerTypeFlag ;

                if(WorkerManagerRB.isSelected()){

                    WorkerTypeFlag = "1" ;

                }else if(WorkerCashierRB.isSelected()){

                    WorkerTypeFlag = "2" ;

                }else if(WorkerChefRB.isSelected()) {

                    WorkerTypeFlag = "3" ;

                }else {

                    WorkerTypeFlag = "4" ;

                }

                OpenConnection();
                Statement UpdateWorker = connection.createStatement();
                String Worker = "UPDATE users_info SET password = '" + WorkerPasswordText.getText().trim() +
                        "' , worker_name = '" + WorkerNameText.getText().trim() +
                        "' , national_id = '" + WorkerNationalIDText.getText().trim() +
                        "' , age = '"+ WorkerAgeText.getText().trim() +
                        "' , phone_number = '"+WorkerPhoneText.getText().trim()+
                        "' , address = '"+WorkerAddressText.getText().trim()+
                        "' , worker_type = '"+WorkerTypeFlag+
                        "' , salary = '"+WorkerSalaryText.getText().trim()+
                        "' WHERE user_name = '" + WorkerUserNameText.getText().trim() + "'";
                UpdateWorker.executeUpdate(Worker);

                CloseConnection();

                // close the Stage
                Stage stage = (Stage) UpdateTheWorkerButton.getScene().getWindow();
                stage.close();

                // ToDo  reload the customers table ?????

                callback.accept("");

            } catch (Exception e) {
                e.printStackTrace();
                CloseConnection();
            }


        }


    }

}
