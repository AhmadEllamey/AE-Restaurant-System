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
import static sample.Main.StatisticsShiftsInfoDetails;
import static sample.MainScreen.*;

public class StatisticsShiftsInfo implements Initializable {



    @FXML
    private Label HeaderLineLabel;

    @FXML
    private TableView<ShiftsInfo> ShiftsDetailsTable;

    private ObservableList<ShiftsInfo> dataListForShiftsDetailsTable = FXCollections.observableArrayList();

    @FXML
    private TableColumn<ShiftsInfo,String> ShiftNumberColumn;

    @FXML
    private TableColumn<ShiftsInfo,String> DateColumn;

    @FXML
    private TableColumn<ShiftsInfo,String> UserNameColumn;

    @FXML
    private TableColumn<ShiftsInfo,String> UserShiftColumn;

    @FXML
    private TableColumn<ShiftsInfo,String> CashColumn;

    @FXML
    private TableColumn<ShiftsInfo,String> EarnedColumn;

    @FXML
    private JFXButton DoneButton;

    public StatisticsShiftsInfo() {

        StatisticsShiftsInfoDetails.setOnShowing(event -> {

            LoadTheOrders();

        });

    }


    public class ShiftsInfo {

        private final SimpleStringProperty NumberOfShift;
        private final SimpleStringProperty DateOfTheShift;
        private final SimpleStringProperty UserName;
        private final SimpleStringProperty UserShiftNumber;
        private final SimpleStringProperty TotalMoneyOfTheShift;
        private final SimpleStringProperty TotalEarnedOfTheShift;


        public ShiftsInfo(String numberOfShifts, String dateOfTheShift,
                         String userName, String userShiftNumber,
                         String totalMoney, String totalEarned ) {
            NumberOfShift = new SimpleStringProperty(numberOfShifts);
            DateOfTheShift = new SimpleStringProperty(dateOfTheShift);
            UserName = new SimpleStringProperty(userName);
            UserShiftNumber = new SimpleStringProperty(userShiftNumber);
            TotalMoneyOfTheShift = new SimpleStringProperty(totalMoney);
            TotalEarnedOfTheShift = new SimpleStringProperty(totalEarned);
        }


        public String getNumberOfShift() {
            return NumberOfShift.get();
        }

        public SimpleStringProperty numberOfShiftProperty() {
            return NumberOfShift;
        }

        public void setNumberOfShift(String numberOfShift) {
            this.NumberOfShift.set(numberOfShift);
        }

        public String getDateOfTheShift() {
            return DateOfTheShift.get();
        }

        public SimpleStringProperty dateOfTheShiftProperty() {
            return DateOfTheShift;
        }

        public void setDateOfTheShift(String dateOfTheShift) {
            this.DateOfTheShift.set(dateOfTheShift);
        }

        public String getUserName() {
            return UserName.get();
        }

        public SimpleStringProperty userNameProperty() {
            return UserName;
        }

        public void setUserName(String userName) {
            this.UserName.set(userName);
        }

        public String getUserShiftNumber() {
            return UserShiftNumber.get();
        }

        public SimpleStringProperty userShiftNumberProperty() {
            return UserShiftNumber;
        }

        public void setUserShiftNumber(String userShiftNumber) {
            this.UserShiftNumber.set(userShiftNumber);
        }

        public String getTotalMoneyOfTheShift() {
            return TotalMoneyOfTheShift.get();
        }

        public SimpleStringProperty totalMoneyOfTheShiftProperty() {
            return TotalMoneyOfTheShift;
        }

        public void setTotalMoneyOfTheShift(String totalMoneyOfTheShift) {
            this.TotalMoneyOfTheShift.set(totalMoneyOfTheShift);
        }

        public String getTotalEarnedOfTheShift() {
            return TotalEarnedOfTheShift.get();
        }

        public SimpleStringProperty totalEarnedOfTheShiftProperty() {
            return TotalEarnedOfTheShift;
        }

        public void setTotalEarnedOfTheShift(String totalEarnedOfTheShift) {
            this.TotalEarnedOfTheShift.set(totalEarnedOfTheShift);
        }
    }

    class EditingCell extends TableCell<ShiftsInfo, String> {

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
                        return new StatisticsShiftsInfo.EditingCell();
                    }
                };





        ShiftNumberColumn.setCellValueFactory(new PropertyValueFactory<ShiftsInfo, String>("NumberOfShift"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<ShiftsInfo, String>("DateOfTheShift"));
        UserNameColumn.setCellValueFactory(new PropertyValueFactory<ShiftsInfo, String>("UserName"));
        UserShiftColumn.setCellValueFactory(new PropertyValueFactory<ShiftsInfo, String>("UserShiftNumber"));
        CashColumn.setCellValueFactory(new PropertyValueFactory<ShiftsInfo, String>("TotalMoneyOfTheShift"));
        EarnedColumn.setCellValueFactory(new PropertyValueFactory<ShiftsInfo, String>("TotalEarnedOfTheShift"));



        ShiftsDetailsTable.setItems(dataListForShiftsDetailsTable);

    }

    public void LoadTheOrders(){


        try {
            OpenConnection();

            Statement GetTheOrders =  connection.createStatement();

            String Orders = "" ;

            // mode 0 (show all shifts) , mode 1 (show all shifts for cashier)

            if(StatisticsKeyValue == 0){

                HeaderLineLabel.setText("All Shifts");
                Orders = "SELECT * FROM shifts";

            }else if(StatisticsKeyValue == 1){

                HeaderLineLabel.setText("All Shifts For "+ StatisticsValue1);
                Orders = "SELECT * FROM shifts WHERE user_name = '"+StatisticsValue1+"'";

            }

            ResultSet resultSet = GetTheOrders.executeQuery(Orders);


            while (resultSet.next()){


                dataListForShiftsDetailsTable.add(new ShiftsInfo(
                        resultSet.getString("number_of_shift"),
                        resultSet.getString("date_of_day"),
                        resultSet.getString("user_name"),
                        resultSet.getString("user_shift_number"),
                        resultSet.getString("total_money_of_the_shift"),
                        resultSet.getString("total_earned_of_the_shift")));

            }



            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }



    }


    public void DoneButtonFun(){
        // close the Stage
        Stage stage = (Stage) DoneButton.getScene().getWindow();
        stage.close();
    }













}
