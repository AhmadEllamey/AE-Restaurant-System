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
import static sample.Main.StatisticsOrdersInfo;
import static sample.MainScreen.*;


public class OrdersInfoForStatistics implements Initializable {


    public OrdersInfoForStatistics() {

        StatisticsOrdersInfo.setOnShowing(event -> {

            LoadTheOrders();

        });

    }


    public class OrderInfo {

        private final SimpleStringProperty OrderCode;
        private final SimpleStringProperty OrderCustomerPhone;
        private final SimpleStringProperty OrderCustomerName;
        private final SimpleStringProperty OrderCashier;
        private final SimpleStringProperty OrderEstTime;
        private final SimpleStringProperty OrderDelivery;
        private final SimpleStringProperty OrderTotal;
        private final SimpleStringProperty OrderType;
        private final SimpleStringProperty OrderPoint;
        private final SimpleStringProperty OrderConsumed;
        private final SimpleStringProperty OrderShift;
        private final SimpleStringProperty WorkerLTU;

        public OrderInfo(String orderCode, String orderCustomerPhone, String orderCustomerName, String orderCashierName, String orderEstTime, String orderDelivery, String orderTotal, String orderType, String orderPoint, String orderConsumed, String orderShift ,String orderLTU) {
            OrderCode = new SimpleStringProperty(orderCode);
            OrderCustomerPhone = new SimpleStringProperty(orderCustomerPhone);
            OrderCustomerName = new SimpleStringProperty(orderCustomerName);
            OrderCashier = new SimpleStringProperty(orderCashierName);
            OrderEstTime = new SimpleStringProperty(orderEstTime);
            OrderDelivery = new SimpleStringProperty(orderDelivery);
            OrderTotal = new SimpleStringProperty(orderTotal);
            OrderType = new SimpleStringProperty(orderType);
            OrderPoint = new SimpleStringProperty(orderPoint);
            OrderConsumed = new SimpleStringProperty(orderConsumed);
            OrderShift = new SimpleStringProperty(orderShift);
            WorkerLTU = new SimpleStringProperty(orderLTU);
        }


        public String getOrderCode() {
            return OrderCode.get();
        }

        public SimpleStringProperty orderCodeProperty() {
            return OrderCode;
        }

        public void setOrderCode(String orderCode) {
            this.OrderCode.set(orderCode);
        }

        public String getOrderCustomerPhone() {
            return OrderCustomerPhone.get();
        }

        public SimpleStringProperty orderCustomerPhoneProperty() {
            return OrderCustomerPhone;
        }

        public void setOrderCustomerPhone(String orderCustomerPhone) {
            this.OrderCustomerPhone.set(orderCustomerPhone);
        }

        public String getOrderCustomerName() {
            return OrderCustomerName.get();
        }

        public SimpleStringProperty orderCustomerNameProperty() {
            return OrderCustomerName;
        }

        public void setOrderCustomerName(String orderCustomerName) {
            this.OrderCustomerName.set(orderCustomerName);
        }

        public String getOrderCashier() {
            return OrderCashier.get();
        }

        public SimpleStringProperty orderCashierProperty() {
            return OrderCashier;
        }

        public void setOrderCashier(String orderCashier) {
            this.OrderCashier.set(orderCashier);
        }

        public String getOrderEstTime() {
            return OrderEstTime.get();
        }

        public SimpleStringProperty orderEstTimeProperty() {
            return OrderEstTime;
        }

        public void setOrderEstTime(String orderEstTime) {
            this.OrderEstTime.set(orderEstTime);
        }

        public String getOrderDelivery() {
            return OrderDelivery.get();
        }

        public SimpleStringProperty orderDeliveryProperty() {
            return OrderDelivery;
        }

        public void setOrderDelivery(String orderDelivery) {
            this.OrderDelivery.set(orderDelivery);
        }

        public String getOrderTotal() {
            return OrderTotal.get();
        }

        public SimpleStringProperty orderTotalProperty() {
            return OrderTotal;
        }

        public void setOrderTotal(String orderTotal) {
            this.OrderTotal.set(orderTotal);
        }

        public String getOrderType() {
            return OrderType.get();
        }

        public SimpleStringProperty orderTypeProperty() {
            return OrderType;
        }

        public void setOrderType(String orderType) {
            this.OrderType.set(orderType);
        }

        public String getOrderPoint() {
            return OrderPoint.get();
        }

        public SimpleStringProperty orderPointProperty() {
            return OrderPoint;
        }

        public void setOrderPoint(String orderPoint) {
            this.OrderPoint.set(orderPoint);
        }

        public String getOrderConsumed() {
            return OrderConsumed.get();
        }

        public SimpleStringProperty orderConsumedProperty() {
            return OrderConsumed;
        }

        public void setOrderConsumed(String orderConsumed) {
            this.OrderConsumed.set(orderConsumed);
        }

        public String getOrderShift() {
            return OrderShift.get();
        }

        public SimpleStringProperty orderShiftProperty() {
            return OrderShift;
        }

        public void setOrderShift(String orderShift) {
            this.OrderShift.set(orderShift);
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

    class EditingCell extends TableCell<OrderInfo, String> {

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
                        return new OrdersInfoForStatistics.EditingCell();
                    }
                };






        CodeColumn.setCellValueFactory(new PropertyValueFactory<OrderInfo, String>("OrderCode"));
        CustomerPhoneColumn.setCellValueFactory(new PropertyValueFactory<OrderInfo, String>("OrderCustomerPhone"));
        CustomerNameColumn.setCellValueFactory(new PropertyValueFactory<OrderInfo, String>("OrderCustomerName"));
        CashierColumn.setCellValueFactory(new PropertyValueFactory<OrderInfo, String>("OrderCashier"));
        EstimatedColumn.setCellValueFactory(new PropertyValueFactory<OrderInfo, String>("OrderEstTime"));
        DeliveryColumn.setCellValueFactory(new PropertyValueFactory<OrderInfo, String>("OrderDelivery"));
        TotalColumn.setCellValueFactory(new PropertyValueFactory<OrderInfo, String>("OrderTotal"));
        OrderColumn.setCellValueFactory(new PropertyValueFactory<OrderInfo, String>("OrderType"));
        PointsColumn.setCellValueFactory(new PropertyValueFactory<OrderInfo, String>("OrderPoint"));
        ConsumedColumn.setCellValueFactory(new PropertyValueFactory<OrderInfo, String>("OrderConsumed"));
        ShiftColumn.setCellValueFactory(new PropertyValueFactory<OrderInfo, String>("OrderShift"));
        LTUColumn.setCellValueFactory(new PropertyValueFactory<OrderInfo, String>("WorkerLTU"));



        OrdersInfoTable.setItems(dataListForOrdersInfoTable);

    }

    public void LoadTheOrders(){


        try {
            OpenConnection();

            Statement GetTheOrders =  connection.createStatement();

            String Orders = "" ;


            // mode 0 (show all orders) , mode 1 (show all orders for cashier) , mode 2 (show all orders for delivery man)
            // mode 3 (show all orders for customer) , mode 4 (show all orders by order type) , mode 5 (show all orders by shifts)

            if(StatisticsKeyValue == 0){

                 Orders = "SELECT * FROM general_order";

            }else if(StatisticsKeyValue == 1){

                Orders = "SELECT * FROM general_order WHERE cashier_name = '"+StatisticsValue1+"'";

            }else if(StatisticsKeyValue == 2){

                Orders = "SELECT * FROM general_order WHERE delivery_man = '"+StatisticsValue1+"'";

            }else if(StatisticsKeyValue == 3){

                Orders = "SELECT * FROM general_order WHERE customer_phone = '"+StatisticsValue1+"'";

            }else if(StatisticsKeyValue == 4){

                Orders = "SELECT * FROM general_order WHERE order_type ='"+StatisticsValue1+"'";

            }else if(StatisticsKeyValue == 5){

                Orders = "SELECT * FROM general_order WHERE cashier_name = '"+StatisticsValue1+"' AND shift_number ='"+StatisticsValue2+"'";

            }



            ResultSet resultSet = GetTheOrders.executeQuery(Orders);


            while (resultSet.next()){


                dataListForOrdersInfoTable.add(new OrderInfo(
                        resultSet.getString("code"),
                        resultSet.getString("customer_phone"),
                        resultSet.getString("customer_name"),
                        resultSet.getString("cashier_name"),
                        resultSet.getString("estimated_time"),
                        resultSet.getString("delivery_man"),
                        resultSet.getString("total"),
                        resultSet.getString("order_type"),
                        resultSet.getString("consumed"),
                        resultSet.getString("points"),
                        resultSet.getString("shift_number"),
                        resultSet.getString("last_time_updated")));


            }



            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }



    }

    @FXML
    private TableView<OrderInfo> OrdersInfoTable;

    private ObservableList<OrderInfo> dataListForOrdersInfoTable = FXCollections.observableArrayList();

    @FXML
    private TableColumn<OrderInfo,String> CodeColumn;

    @FXML
    private TableColumn<OrderInfo,String> CustomerPhoneColumn;

    @FXML
    private TableColumn<OrderInfo,String> CustomerNameColumn;

    @FXML
    private TableColumn<OrderInfo,String> CashierColumn;

    @FXML
    private TableColumn<OrderInfo,String> EstimatedColumn;

    @FXML
    private TableColumn<OrderInfo,String> DeliveryColumn;

    @FXML
    private TableColumn<OrderInfo,String> TotalColumn;

    @FXML
    private TableColumn<OrderInfo,String> OrderColumn;

    @FXML
    private TableColumn<OrderInfo,String> PointsColumn;

    @FXML
    private TableColumn<OrderInfo,String> ConsumedColumn;

    @FXML
    private TableColumn<OrderInfo,String> ShiftColumn;

    @FXML
    private TableColumn<OrderInfo,String> LTUColumn;

    @FXML
    private JFXButton DoneButton;



    public void DoneButtonFun(){
        // close the Stage
        Stage stage = (Stage) DoneButton.getScene().getWindow();
        stage.close();
    }









}
