package sample;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import static sample.DBConnection.*;
import static sample.DBConnection.CloseConnection;

import static sample.Main.StatisticsWorkerInfo;

public class WorkerInfoForStatistics implements Initializable {



    public WorkerInfoForStatistics() {

        StatisticsWorkerInfo.setOnShowing(event -> {

            LoadTheWorker();

        });

    }

    public class WorkerInfo {

        private final SimpleStringProperty WorkerUsername;
        private final SimpleStringProperty WorkerPassword;
        private final SimpleStringProperty WorkerName;
        private final SimpleStringProperty WorkerNationalID;
        private final SimpleStringProperty WorkerAge;
        private final SimpleStringProperty WorkerPhone;
        private final SimpleStringProperty WorkerAddress;
        private final SimpleStringProperty WorkerJob;
        private final SimpleStringProperty WorkerSalary;
        private final SimpleStringProperty WorkerRate;
        private final SimpleStringProperty WorkerLTU;

        public WorkerInfo(String workerUsername, String workerPassword, String workerName, String workerNationalID, String workerAge, String workerPhone, String workerAddress, String workerJob, String workerSalary, String workerRate, String workerLTU) {
            WorkerUsername = new SimpleStringProperty(workerUsername);
            WorkerPassword = new SimpleStringProperty(workerPassword);
            WorkerName = new SimpleStringProperty(workerName);
            WorkerNationalID = new SimpleStringProperty(workerNationalID);
            WorkerAge = new SimpleStringProperty(workerAge);
            WorkerPhone = new SimpleStringProperty(workerPhone);
            WorkerAddress = new SimpleStringProperty(workerAddress);
            WorkerJob = new SimpleStringProperty(workerJob);
            WorkerSalary = new SimpleStringProperty(workerSalary);
            WorkerRate = new SimpleStringProperty(workerRate);
            WorkerLTU = new SimpleStringProperty(workerLTU);
        }

        public String getWorkerUsername() {
            return WorkerUsername.get();
        }

        public SimpleStringProperty workerUsernameProperty() {
            return WorkerUsername;
        }

        public void setWorkerUsername(String workerUsername) {
            this.WorkerUsername.set(workerUsername);
        }

        public String getWorkerPassword() {
            return WorkerPassword.get();
        }

        public SimpleStringProperty workerPasswordProperty() {
            return WorkerPassword;
        }

        public void setWorkerPassword(String workerPassword) {
            this.WorkerPassword.set(workerPassword);
        }

        public String getWorkerName() {
            return WorkerName.get();
        }

        public SimpleStringProperty workerNameProperty() {
            return WorkerName;
        }

        public void setWorkerName(String workerName) {
            this.WorkerName.set(workerName);
        }

        public String getWorkerNationalID() {
            return WorkerNationalID.get();
        }

        public SimpleStringProperty workerNationalIDProperty() {
            return WorkerNationalID;
        }

        public void setWorkerNationalID(String workerNationalID) {
            this.WorkerNationalID.set(workerNationalID);
        }

        public String getWorkerAge() {
            return WorkerAge.get();
        }

        public SimpleStringProperty workerAgeProperty() {
            return WorkerAge;
        }

        public void setWorkerAge(String workerAge) {
            this.WorkerAge.set(workerAge);
        }

        public String getWorkerPhone() {
            return WorkerPhone.get();
        }

        public SimpleStringProperty workerPhoneProperty() {
            return WorkerPhone;
        }

        public void setWorkerPhone(String workerPhone) {
            this.WorkerPhone.set(workerPhone);
        }

        public String getWorkerAddress() {
            return WorkerAddress.get();
        }

        public SimpleStringProperty workerAddressProperty() {
            return WorkerAddress;
        }

        public void setWorkerAddress(String workerAddress) {
            this.WorkerAddress.set(workerAddress);
        }

        public String getWorkerJob() {
            return WorkerJob.get();
        }

        public SimpleStringProperty workerJobProperty() {
            return WorkerJob;
        }

        public void setWorkerJob(String workerJob) {
            this.WorkerJob.set(workerJob);
        }

        public String getWorkerSalary() {
            return WorkerSalary.get();
        }

        public SimpleStringProperty workerSalaryProperty() {
            return WorkerSalary;
        }

        public void setWorkerSalary(String workerSalary) {
            this.WorkerSalary.set(workerSalary);
        }

        public String getWorkerRate() {
            return WorkerRate.get();
        }

        public SimpleStringProperty workerRateProperty() {
            return WorkerRate;
        }

        public void setWorkerRate(String workerRate) {
            this.WorkerRate.set(workerRate);
        }

        public String getWorkerLTU() {
            return WorkerLTU.get();
        }

        public SimpleStringProperty workerLTUProperty() {
            return WorkerLTU;
        }

        public void setWorkerLTU(String workerLTU) {
            this.WorkerLTU.set(workerLTU);
        }
    }




    class EditingCell extends TableCell<WorkerInfo, String> {

        private TextField textField;

        public EditingCell() {
        }

        @Override
        public void startEdit() {
            super.startEdit();

            if (textField == null) {
                createTextField();
            }

            setGraphic(textField);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(String.valueOf(getItem()));
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(textField);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setGraphic(textField);
                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                } else {
                    setText(getString());
                    setContentDisplay(ContentDisplay.TEXT_ONLY);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.setOnKeyPressed(t -> {
                if (t.getCode() == KeyCode.ENTER) {
                    commitEdit(textField.getText());
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Callback<TableColumn, TableCell> cellFactory =
                new Callback<TableColumn, TableCell>() {
                    public TableCell call(TableColumn p) {
                        return new WorkerInfoForStatistics.EditingCell();
                    }
                };



        UsernameColumn.setCellValueFactory(new PropertyValueFactory<WorkerInfo, String>("WorkerUsername"));
        PasswordColumn.setCellValueFactory(new PropertyValueFactory<WorkerInfo, String>("WorkerPassword"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<WorkerInfo, String>("WorkerName"));
        NationalIDColumn.setCellValueFactory(new PropertyValueFactory<WorkerInfo, String>("WorkerNationalID"));
        AgeColumn.setCellValueFactory(new PropertyValueFactory<WorkerInfo, String>("WorkerAge"));
        PhoneColumn.setCellValueFactory(new PropertyValueFactory<WorkerInfo, String>("WorkerPhone"));
        AddressColumn.setCellValueFactory(new PropertyValueFactory<WorkerInfo, String>("WorkerAddress"));
        JobColumn.setCellValueFactory(new PropertyValueFactory<WorkerInfo, String>("WorkerJob"));
        SalaryColumn.setCellValueFactory(new PropertyValueFactory<WorkerInfo, String>("WorkerSalary"));
        RateColumn.setCellValueFactory(new PropertyValueFactory<WorkerInfo, String>("WorkerRate"));
        LTUColumn.setCellValueFactory(new PropertyValueFactory<WorkerInfo, String>("WorkerLTU"));



        WorkerInfoTable.setItems(dataListForWorkerInfoTable);

    }




    public void LoadTheWorker(){


        try {
            OpenConnection();

            Statement GetWorkersInfo =  connection.createStatement();
            String Workers = "SELECT * FROM users_info";
            ResultSet resultSet = GetWorkersInfo.executeQuery(Workers);


            while (resultSet.next()){
                String WorkerType ;

                if(resultSet.getString("worker_type").equals("1")){
                    WorkerType = "Manager";
                }else if(resultSet.getString("worker_type").equals("2")){
                    WorkerType = "Cashier";
                }else if(resultSet.getString("worker_type").equals("3")){
                    WorkerType = "Chef";
                }else {
                    WorkerType = "Delivery";
                }

                dataListForWorkerInfoTable.add(new WorkerInfo(
                        resultSet.getString("user_name"),
                        resultSet.getString("password"),
                        resultSet.getString("worker_name"),
                        resultSet.getString("national_id"),
                        resultSet.getString("age"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("address"),
                        WorkerType,
                        resultSet.getString("salary"),
                        resultSet.getString("rate"),
                        resultSet.getString("last_time_updated")));


            }



            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }



    }



    @FXML
    private TableView<WorkerInfo> WorkerInfoTable;

    private ObservableList<WorkerInfo> dataListForWorkerInfoTable = FXCollections.observableArrayList();



    @FXML
    private TableColumn<WorkerInfo,String> UsernameColumn;

    @FXML
    private TableColumn<WorkerInfo,String> PasswordColumn;

    @FXML
    private TableColumn<WorkerInfo,String> NameColumn;

    @FXML
    private TableColumn<WorkerInfo,String> NationalIDColumn;

    @FXML
    private TableColumn<WorkerInfo,String> AgeColumn;

    @FXML
    private TableColumn<WorkerInfo,String> PhoneColumn;

    @FXML
    private TableColumn<WorkerInfo,String> AddressColumn;

    @FXML
    private TableColumn<WorkerInfo,String> JobColumn;

    @FXML
    private TableColumn<WorkerInfo,String> SalaryColumn;

    @FXML
    private TableColumn<WorkerInfo,String> RateColumn;

    @FXML
    private TableColumn<WorkerInfo,String> LTUColumn;

    @FXML
    private JFXButton DoneButton;



    public void DoneButtonFun(){
        // close the Stage
        Stage stage = (Stage) DoneButton.getScene().getWindow();
        stage.close();
    }




}
