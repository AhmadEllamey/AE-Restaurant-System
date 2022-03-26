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
import static sample.Main.StatisticsOrdersInfoDetails;
import static sample.MainScreen.*;

public class StatisticsOrderDetails implements Initializable {



    @FXML
    private Label HeaderLineLabel;

    @FXML
    private TableView<OrderDetails> OrderDetailsTable;

    private ObservableList<OrderDetails> dataListForOrderDetailsTable = FXCollections.observableArrayList();

    @FXML
    private TableColumn<OrderDetails,String> CodeColumn;

    @FXML
    private TableColumn<OrderDetails,String> InternalCounterColumn;

    @FXML
    private TableColumn<OrderDetails,String> PhoneColumn;

    @FXML
    private TableColumn<OrderDetails,String> NameColumn;

    @FXML
    private TableColumn<OrderDetails,String> ItemColumn;

    @FXML
    private TableColumn<OrderDetails,String> SizeColumn;

    @FXML
    private TableColumn<OrderDetails,String> CostColumn;

    @FXML
    private TableColumn<OrderDetails,String> NumberOfItemsColumn;

    @FXML
    private TableColumn<OrderDetails,String> DisForOneColumn;

    @FXML
    private TableColumn<OrderDetails,String> TotalDisColumn;

    @FXML
    private TableColumn<OrderDetails,String> TotalColumn;

    @FXML
    private TableColumn<OrderDetails,String> ItemCodeColumn;

    @FXML
    private TableColumn<OrderDetails,String> LTUColumn;

    @FXML
    private JFXButton DoneButton;



    public StatisticsOrderDetails() {

        StatisticsOrdersInfoDetails.setOnShowing(event -> {

            HeaderLineLabel.setText("Order Number "+ StatisticsValue3);
            LoadTheOrderDetails();

        });

    }


    public class OrderDetails {

        private final SimpleStringProperty OrderCode;
        private final SimpleStringProperty OrderCounter;
        private final SimpleStringProperty OrderCustomerPhone;
        private final SimpleStringProperty OrderCustomerName;
        private final SimpleStringProperty OrderItemName;
        private final SimpleStringProperty OrderItemSize;
        private final SimpleStringProperty OrderItemCost;
        private final SimpleStringProperty OrderNumberOfItems;
        private final SimpleStringProperty OrderDiscountForOne;
        private final SimpleStringProperty OrderTotalDiscount;
        private final SimpleStringProperty OrderTotal;
        private final SimpleStringProperty OrderItemCode;
        private final SimpleStringProperty OrderLTU;

        public OrderDetails(String orderCode,String orderCounter, String orderCustomerPhone, String orderCustomerName, String orderItemName, String orderItemSize, String orderItemCost
                ,  String orderNumberOfItems, String orderDiscountForOne, String orderTotalDiscount,String orderTotal, String orderItemCode ,String orderLTU) {
            OrderCode = new SimpleStringProperty(orderCode);
            OrderCounter = new SimpleStringProperty(orderCounter);
            OrderCustomerPhone = new SimpleStringProperty(orderCustomerPhone);
            OrderCustomerName = new SimpleStringProperty(orderCustomerName);
            OrderItemName = new SimpleStringProperty(orderItemName);
            OrderItemSize = new SimpleStringProperty(orderItemSize);
            OrderItemCost = new SimpleStringProperty(orderItemCost);
            OrderNumberOfItems = new SimpleStringProperty(orderNumberOfItems);
            OrderDiscountForOne = new SimpleStringProperty(orderDiscountForOne);
            OrderTotalDiscount = new SimpleStringProperty(orderTotalDiscount);
            OrderTotal = new SimpleStringProperty(orderTotal);
            OrderItemCode = new SimpleStringProperty(orderItemCode);
            OrderLTU = new SimpleStringProperty(orderLTU);
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

        public String getOrderCounter() {
            return OrderCounter.get();
        }

        public SimpleStringProperty orderCounterProperty() {
            return OrderCounter;
        }

        public void setOrderCounter(String orderCounter) {
            this.OrderCounter.set(orderCounter);
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

        public String getOrderItemName() {
            return OrderItemName.get();
        }

        public SimpleStringProperty orderItemNameProperty() {
            return OrderItemName;
        }

        public void setOrderItemName(String orderItemName) {
            this.OrderItemName.set(orderItemName);
        }

        public String getOrderItemSize() {
            return OrderItemSize.get();
        }

        public SimpleStringProperty orderItemSizeProperty() {
            return OrderItemSize;
        }

        public void setOrderItemSize(String orderItemSize) {
            this.OrderItemSize.set(orderItemSize);
        }

        public String getOrderItemCost() {
            return OrderItemCost.get();
        }

        public SimpleStringProperty orderItemCostProperty() {
            return OrderItemCost;
        }

        public void setOrderItemCost(String orderItemCost) {
            this.OrderItemCost.set(orderItemCost);
        }

        public String getOrderNumberOfItems() {
            return OrderNumberOfItems.get();
        }

        public SimpleStringProperty orderNumberOfItemsProperty() {
            return OrderNumberOfItems;
        }

        public void setOrderNumberOfItems(String orderNumberOfItems) {
            this.OrderNumberOfItems.set(orderNumberOfItems);
        }

        public String getOrderDiscountForOne() {
            return OrderDiscountForOne.get();
        }

        public SimpleStringProperty orderDiscountForOneProperty() {
            return OrderDiscountForOne;
        }

        public void setOrderDiscountForOne(String orderDiscountForOne) {
            this.OrderDiscountForOne.set(orderDiscountForOne);
        }

        public String getOrderTotalDiscount() {
            return OrderTotalDiscount.get();
        }

        public SimpleStringProperty orderTotalDiscountProperty() {
            return OrderTotalDiscount;
        }

        public void setOrderTotalDiscount(String orderTotalDiscount) {
            this.OrderTotalDiscount.set(orderTotalDiscount);
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

        public String getOrderItemCode() {
            return OrderItemCode.get();
        }

        public SimpleStringProperty orderItemCodeProperty() {
            return OrderItemCode;
        }

        public void setOrderItemCode(String orderItemCode) {
            this.OrderItemCode.set(orderItemCode);
        }

        public String getOrderLTU() {
            return OrderLTU.get();
        }

        public SimpleStringProperty orderLTUProperty() {
            return OrderLTU;
        }

        public void setOrderLTU(String orderLTU) {
            this.OrderLTU.set(orderLTU);
        }
    }


    class EditingCell extends TableCell<OrderDetails, String> {

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
                        return new StatisticsOrderDetails.EditingCell();
                    }
                };





        CodeColumn.setCellValueFactory(new PropertyValueFactory<OrderDetails, String>("OrderCode"));
        InternalCounterColumn.setCellValueFactory(new PropertyValueFactory<OrderDetails, String>("OrderCounter"));
        PhoneColumn.setCellValueFactory(new PropertyValueFactory<OrderDetails, String>("OrderCustomerPhone"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<OrderDetails, String>("OrderCustomerName"));
        ItemColumn.setCellValueFactory(new PropertyValueFactory<OrderDetails, String>("OrderItemName"));
        SizeColumn.setCellValueFactory(new PropertyValueFactory<OrderDetails, String>("OrderItemSize"));
        CostColumn.setCellValueFactory(new PropertyValueFactory<OrderDetails, String>("OrderItemCost"));
        NumberOfItemsColumn.setCellValueFactory(new PropertyValueFactory<OrderDetails, String>("OrderNumberOfItems"));
        DisForOneColumn.setCellValueFactory(new PropertyValueFactory<OrderDetails, String>("OrderDiscountForOne"));
        TotalDisColumn.setCellValueFactory(new PropertyValueFactory<OrderDetails, String>("OrderTotalDiscount"));
        TotalColumn.setCellValueFactory(new PropertyValueFactory<OrderDetails, String>("OrderTotal"));
        ItemCodeColumn.setCellValueFactory(new PropertyValueFactory<OrderDetails, String>("OrderItemCode"));
        LTUColumn.setCellValueFactory(new PropertyValueFactory<OrderDetails, String>("OrderLTU"));



        OrderDetailsTable.setItems(dataListForOrderDetailsTable);

    }


    public void LoadTheOrderDetails(){


        try {
            OpenConnection();

            Statement GetTheOrders =  connection.createStatement();
            String Orders = "SELECT * FROM order_detailes WHERE code = "+Integer.parseInt(StatisticsValue3)+"" ;
            ResultSet resultSet = GetTheOrders.executeQuery(Orders);


            while (resultSet.next()){


                dataListForOrderDetailsTable.add(new OrderDetails(
                        resultSet.getString("code"),
                        resultSet.getString("order_counter"),
                        resultSet.getString("customer_phone"),
                        resultSet.getString("customer_name"),
                        resultSet.getString("item_name"),
                        resultSet.getString("item_size"),
                        resultSet.getString("item_cost"),
                        resultSet.getString("number_of_items"),
                        resultSet.getString("discount_for_one"),
                        resultSet.getString("total_discount"),
                        resultSet.getString("total"),
                        resultSet.getString("item_code"),
                        resultSet.getString("last_time_updated")));

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
