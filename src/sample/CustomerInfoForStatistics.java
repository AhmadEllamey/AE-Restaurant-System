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
import static sample.Main.*;

public class CustomerInfoForStatistics implements Initializable {


    public CustomerInfoForStatistics() {

        StatisticsCustomerInfo.setOnShowing(event -> {

            LoadTheCustomer();

        });

    }

    public class CustomerInfo {

        private final SimpleStringProperty CustomerName;
        private final SimpleStringProperty CustomerPhone;
        private final SimpleStringProperty CustomerAddress;
        private final SimpleStringProperty CustomerNote;
        private final SimpleStringProperty CustomerCash;
        private final SimpleStringProperty CustomerPoint;
        private final SimpleStringProperty CustomerLTU;


        CustomerInfo(String CustomerName, String CustomerPhone, String CustomerAddress, String CustomerNote, String CustomerCash, String CustomerPoint, String CustomerLTU ){
            this.CustomerName = new SimpleStringProperty(CustomerName);
            this.CustomerPhone = new SimpleStringProperty(CustomerPhone);
            this.CustomerAddress = new SimpleStringProperty(CustomerAddress);
            this.CustomerNote = new SimpleStringProperty(CustomerNote);
            this.CustomerCash = new SimpleStringProperty(CustomerCash);
            this.CustomerPoint = new SimpleStringProperty(CustomerPoint);
            this.CustomerLTU = new SimpleStringProperty(CustomerLTU);

        }

        public String getCustomerName() {
            return CustomerName.get();
        }

        public SimpleStringProperty customerNameProperty() {
            return CustomerName;
        }

        public void setCustomerName(String customerName) {
            this.CustomerName.set(customerName);
        }

        public String getCustomerPhone() {
            return CustomerPhone.get();
        }

        public SimpleStringProperty customerPhoneProperty() {
            return CustomerPhone;
        }

        public void setCustomerPhone(String customerPhone) {
            this.CustomerPhone.set(customerPhone);
        }

        public String getCustomerAddress() {
            return CustomerAddress.get();
        }

        public SimpleStringProperty customerAddressProperty() {
            return CustomerAddress;
        }

        public void setCustomerAddress(String customerAddress) {
            this.CustomerAddress.set(customerAddress);
        }

        public String getCustomerNote() {
            return CustomerNote.get();
        }

        public SimpleStringProperty customerNoteProperty() {
            return CustomerNote;
        }

        public void setCustomerNote(String customerNote) {
            this.CustomerNote.set(customerNote);
        }

        public String getCustomerCash() {
            return CustomerCash.get();
        }

        public SimpleStringProperty customerCashProperty() {
            return CustomerCash;
        }

        public void setCustomerCash(String customerCash) {
            this.CustomerCash.set(customerCash);
        }

        public String getCustomerPoint() {
            return CustomerPoint.get();
        }

        public SimpleStringProperty customerPointProperty() {
            return CustomerPoint;
        }

        public void setCustomerPoint(String customerPoint) {
            this.CustomerPoint.set(customerPoint);
        }

        public String getCustomerLTU() {
            return CustomerLTU.get();
        }

        public SimpleStringProperty customerLTUProperty() {
            return CustomerLTU;
        }

        public void setCustomerLTU(String customerLTU) {
            this.CustomerLTU.set(customerLTU);
        }
    }




    class EditingCell extends TableCell<CustomerInfo, String> {

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
                        return new CustomerInfoForStatistics.EditingCell();
                    }
                };

        // ToDo FoodBank Table

        NameColumn.setCellValueFactory(new PropertyValueFactory<CustomerInfo, String>("CustomerName"));
        PhoneColumn.setCellValueFactory(new PropertyValueFactory<CustomerInfo, String>("CustomerPhone"));
        AddressColumn.setCellValueFactory(new PropertyValueFactory<CustomerInfo, String>("CustomerAddress"));
        NoteColumn.setCellValueFactory(new PropertyValueFactory<CustomerInfo, String>("CustomerNote"));
        CashColumn.setCellValueFactory(new PropertyValueFactory<CustomerInfo, String>("CustomerCash"));
        PointsColumn.setCellValueFactory(new PropertyValueFactory<CustomerInfo, String>("CustomerPoint"));
        LTUColumn.setCellValueFactory(new PropertyValueFactory<CustomerInfo, String>("CustomerLTU"));



        CustomerInfoTable.setItems(dataListForCustomerInfoTable);

    }




    public void LoadTheCustomer(){


        try {
            OpenConnection();

            Statement GetCustomerInfo =  connection.createStatement();
            String Customers = "SELECT * FROM customers_info";
            ResultSet resultSet = GetCustomerInfo.executeQuery(Customers);


            while (resultSet.next()){

                dataListForCustomerInfoTable.add(new CustomerInfo(
                         resultSet.getString("customer_name")
                        ,resultSet.getString("phone_number")
                        ,resultSet.getString("address")
                        ,resultSet.getString("note")
                        ,resultSet.getString("customer_cash")
                        ,resultSet.getString("customer_points")
                        ,resultSet.getString("last_time_updated")));


            }



            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }



    }



    @FXML
    private TableView<CustomerInfo> CustomerInfoTable;

    private ObservableList<CustomerInfo> dataListForCustomerInfoTable = FXCollections.observableArrayList();


    @FXML
    private TableColumn<CustomerInfo,String> NameColumn;

    @FXML
    private TableColumn<CustomerInfo,String> PhoneColumn;

    @FXML
    private TableColumn<CustomerInfo,String> AddressColumn;

    @FXML
    private TableColumn<CustomerInfo,String> NoteColumn;

    @FXML
    private TableColumn<CustomerInfo,String> CashColumn;

    @FXML
    private TableColumn<CustomerInfo,String> PointsColumn;

    @FXML
    private TableColumn<CustomerInfo,String> LTUColumn;

    @FXML
    private JFXButton DoneButton;



    public void DoneButtonFun(){
        // close the Stage
        Stage stage = (Stage) DoneButton.getScene().getWindow();
        stage.close();
    }







}
