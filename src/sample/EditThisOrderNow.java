package sample;

import com.jfoenix.controls.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PrinterName;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Consumer;

import static sample.DBConnection.*;
import static sample.DBConnection.CloseConnection;
import static sample.Main.*;
import static sample.Main.CurrentDelivery;
import static sample.MainScreen.*;

public class EditThisOrderNow implements Initializable {


    String PreviousPoints ;
    String PreviousCash ;
    String OrderCashierNameKey;
    String OrderCash ;
    double currentPoints;




    @FXML
    private AnchorPane SpecialTab;

    @FXML
    private AnchorPane StartTab;

    @FXML
    private AnchorPane PizzaTab;

    @FXML
    private JFXButton PizzaButton;

    @FXML
    private JFXButton SpecialButton;

    @FXML
    private JFXTextField DeliveryPhoneNumberText;

    @FXML
    private JFXTextField DeliveryNameText;

    @FXML
    private JFXTextArea DeliveryAddressText;

    @FXML
    private JFXTextArea DeliveryNoteText;

    @FXML
    private JFXButton DeliverySaveButton;

    @FXML
    private JFXButton DeliveryInfoButton;


    public static int ToggleFlagX = 0 ;



    @FXML
    private TableView<EditThisOrderNow.FoodBank> DeliveryTable;

    private ObservableList<EditThisOrderNow.FoodBank> dataListForD = FXCollections.observableArrayList();

    @FXML
    private TableColumn<EditThisOrderNow.FoodBank, String> DeliveryTableColumnItemName;

    @FXML
    private TableColumn <EditThisOrderNow.FoodBank, String> DeliveryTableColumnItemSize;

    @FXML
    private TableColumn <EditThisOrderNow.FoodBank, String> DeliveryTableColumnItemCost;

    @FXML
    private TableColumn <EditThisOrderNow.FoodBank, String> DeliveryTableColumnNumberOfItems;

    @FXML
    private TableColumn <EditThisOrderNow.FoodBank, String> DeliveryTableColumnDisForOne;

    @FXML
    private TableColumn <EditThisOrderNow.FoodBank, String> DeliveryTableColumnTotalDis;

    @FXML
    private TableColumn <EditThisOrderNow.FoodBank, String> DeliveryTableColumnTotal;

    @FXML
    private TableColumn <EditThisOrderNow.FoodBank, String> DeliveryTableColumnCode;

    @FXML
    private TableColumn <EditThisOrderNow.FoodBank, String> DeliveryTableColumnOrderCounter;

    @FXML
    private Label CashierNameLabel;

    @FXML
    private JFXTextField cashMoneyText;

    @FXML
    private JFXTextField savedMoneyText;

    @FXML
    private JFXTextField pointsForThisOrderText;

    @FXML
    private JFXTextField yourTotalPointsText;

    @FXML
    private JFXComboBox<EditThisOrderNow.DeliveryClassForComboBox> DeliveryManComboBox;

    private ObservableList<EditThisOrderNow.DeliveryClassForComboBox> DataForDeliveryManComboBox = FXCollections.observableArrayList();

    @FXML
    private Label DateLabel;

    @FXML
    private JFXTextField PaidText;

    @FXML
    private JFXTextField ReturnText;

    @FXML
    private JFXRadioButton PayWithPointsRB;

    @FXML
    private JFXButton DoneButton;

    @FXML
    private JFXRadioButton DeliveryTypeRB;

    @FXML
    private JFXRadioButton TakeAWayTypeRB;

    @FXML
    private JFXRadioButton LobbyTypeRB;

    @FXML
    private JFXTextField EstimatedTimeText;






    @FXML
    public void ManageTheSelections(ActionEvent event) {


        if(event.getSource() == PizzaButton){

            PizzaTab.toFront();

        }else if(event.getSource() == SpecialButton){

            SpecialTab.toFront();

        }

    }


    @FXML
    public void ManageTheSelectionOptions(ActionEvent event) {

        // get the id of the clicked button
        JFXButton btn = (JFXButton) event.getSource();
        FoodItemName = btn.getId();


        try {

            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChooseTheSize.fxml"));
            loader.load();
            ChooseTheSize controller = loader.getController();
            Parent popup = loader.getRoot();

            // Give popup a callback method
            controller.setup(
                    (value) -> {
                        LoadTheFoodTable(FoodItemName,ItemSize);
                        GetTheFoodTableCalculations();
                    }
            );

            try {
                SelectTheSizeStage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            SelectTheSizeStage.setTitle("Size Information !!!");
            SelectTheSizeStage.setScene(new Scene(popup, 800, 200));
            SelectTheSizeStage.show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void ShowCustomerInfoFun(){


        if(!DeliveryPhoneNumberText.getText().trim().isEmpty()){

            try {

                SearchForThatCustomer = DeliveryPhoneNumberText.getText().trim();

                // Load the popup
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowCustomerInfo.fxml"));
                loader.load();
                ShowCustomerInfo controller = loader.getController();
                Parent popup = loader.getRoot();

                // Give popup a callback method
                controller.setup(
                        (value)->{
                            new MainScreen().LoadTheCurrentCustomerTable(0);
                        }
                );

                try {
                    ShowTheCustomerInfoStage.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

                ShowTheCustomerInfoStage.setTitle("Customer Information !!!");
                ShowTheCustomerInfoStage.setScene(new Scene(popup,800 ,600));
                ShowTheCustomerInfoStage.show();

            }catch (Exception e){
                e.printStackTrace();
            }

        }



    }

    public class FoodBank {
        private final SimpleStringProperty foodName;
        private final SimpleStringProperty foodSize;
        private final SimpleStringProperty foodCost;
        private final SimpleStringProperty foodNumItems;
        private final SimpleStringProperty foodDis;
        private final SimpleStringProperty foodTotalDis;
        private final SimpleStringProperty foodTotal;
        private final SimpleStringProperty foodCode;

        FoodBank(String foodName, String foodSize, String foodCost, String foodNumItems, String foodDis, String foodTotalDis, String foodTotal, String foodCode){
            this.foodName = new SimpleStringProperty(foodName);
            this.foodSize = new SimpleStringProperty(foodSize);
            this.foodCost = new SimpleStringProperty(foodCost);
            this.foodNumItems = new SimpleStringProperty(foodNumItems);
            this.foodDis = new SimpleStringProperty(foodDis);
            this.foodTotalDis = new SimpleStringProperty(foodTotalDis);
            this.foodTotal = new SimpleStringProperty(foodTotal);
            this.foodCode = new SimpleStringProperty(foodCode);
        }

        public String getFoodName() {
            return foodName.get();
        }

        public SimpleStringProperty foodNameProperty() {
            return foodName;
        }

        public void setFoodName(String foodName) {
            this.foodName.set(foodName);
        }

        public String getFoodSize() {
            return foodSize.get();
        }

        public SimpleStringProperty foodSizeProperty() {
            return foodSize;
        }

        public void setFoodSize(String foodSize) {
            this.foodSize.set(foodSize);
        }

        public String getFoodCost() {
            return foodCost.get();
        }

        public SimpleStringProperty foodCostProperty() {
            return foodCost;
        }

        public void setFoodCost(String foodCost) {
            this.foodCost.set(foodCost);
        }

        public String getFoodNumItems() {
            return foodNumItems.get();
        }

        public SimpleStringProperty foodNumItemsProperty() {
            return foodNumItems;
        }

        public void setFoodNumItems(String foodNumItems) {
            this.foodNumItems.set(foodNumItems);
        }

        public String getFoodDis() {
            return foodDis.get();
        }

        public SimpleStringProperty foodDisProperty() {
            return foodDis;
        }

        public void setFoodDis(String foodDis) {
            this.foodDis.set(foodDis);
        }

        public String getFoodTotalDis() {
            return foodTotalDis.get();
        }

        public SimpleStringProperty foodTotalDisProperty() {
            return foodTotalDis;
        }

        public void setFoodTotalDis(String foodTotalDis) {
            this.foodTotalDis.set(foodTotalDis);
        }

        public String getFoodTotal() {
            return foodTotal.get();
        }

        public SimpleStringProperty foodTotalProperty() {
            return foodTotal;
        }

        public void setFoodTotal(String foodTotal) {
            this.foodTotal.set(foodTotal);
        }

        public String getFoodCode() {
            return foodCode.get();
        }

        public SimpleStringProperty foodCodeProperty() {
            return foodCode;
        }

        public void setFoodCode(String foodCode) {
            this.foodCode.set(foodCode);
        }

    }

    class EditingCell extends TableCell<MainScreen.FoodBank, String> {

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
                        return new EditThisOrderNow.EditingCell();
                    }
                };

        // ToDo FoodBank Table

        DeliveryTableColumnItemName.setCellValueFactory(new PropertyValueFactory<EditThisOrderNow.FoodBank, String>("foodName"));
        DeliveryTableColumnItemSize.setCellValueFactory(new PropertyValueFactory<EditThisOrderNow.FoodBank, String>("foodSize"));
        DeliveryTableColumnItemCost.setCellValueFactory(new PropertyValueFactory<EditThisOrderNow.FoodBank, String>("foodCost"));
        DeliveryTableColumnNumberOfItems.setCellValueFactory(new PropertyValueFactory<EditThisOrderNow.FoodBank, String>("foodNumItems"));
        DeliveryTableColumnDisForOne.setCellValueFactory(new PropertyValueFactory<EditThisOrderNow.FoodBank, String>("foodDis"));
        DeliveryTableColumnTotalDis.setCellValueFactory(new PropertyValueFactory<EditThisOrderNow.FoodBank, String>("foodTotalDis"));
        DeliveryTableColumnTotal.setCellValueFactory(new PropertyValueFactory<EditThisOrderNow.FoodBank, String>("foodTotal"));
        DeliveryTableColumnCode.setCellValueFactory(new PropertyValueFactory<EditThisOrderNow.FoodBank, String>("foodCode"));

        DeliveryTableColumnNumberOfItems.setCellFactory(TextFieldTableCell.forTableColumn());

        DeliveryTableColumnNumberOfItems.setOnEditCommit(

                (TableColumn.CellEditEvent<EditThisOrderNow.FoodBank, String> t) -> {
                    (t.getTableView().getItems().get(t.getTablePosition().getRow())).setFoodNumItems(t.getNewValue());

                    String counter = (t.getTableView().getItems().get(t.getTablePosition().getRow())).getFoodNumItems();

                    String discountForOne = (t.getTableView().getItems().get(t.getTablePosition().getRow())).getFoodDis();

                    double totalDiscountForTheRow = Double.parseDouble(counter)*Double.parseDouble(discountForOne);

                    (t.getTableView().getItems().get(t.getTablePosition().getRow())).setFoodTotalDis(String.valueOf(totalDiscountForTheRow));

                    String costForOne = (t.getTableView().getItems().get(t.getTablePosition().getRow())).getFoodCost();

                    double total = Double.parseDouble(counter) * Double.parseDouble(costForOne) - totalDiscountForTheRow ;

                    (t.getTableView().getItems().get(t.getTablePosition().getRow())).setFoodTotal(String.valueOf(total));

                    GetTheFoodTableCalculations();

                    System.out.println(counter);

                }

        );

        DeliveryTable.setItems(dataListForD);


    }

    public void LoadTheFoodTable(String ItemName , String SizeCost){


        try {
            OpenConnection();

            Statement GetTheFoodTableRow =  connection.createStatement();
            String Row = "SELECT * FROM menu WHERE code = '"+ItemName+"'";
            ResultSet resultSet = GetTheFoodTableRow.executeQuery(Row);


            if (resultSet.next()){


                String DisType = "dis_"+SizeCost;

                double total_dis = Double.parseDouble(resultSet.getString("item_unit")) *
                        Double.parseDouble(resultSet.getString(DisType));

                double total = (Double.parseDouble(resultSet.getString("item_unit")) *
                        Double.parseDouble(resultSet.getString(SizeCost)) - total_dis);




                dataListForD.add(new EditThisOrderNow.FoodBank(
                        resultSet.getString("item_name")
                        ,SizeCost
                        ,resultSet.getString(SizeCost)
                        ,resultSet.getString("item_unit")
                        ,resultSet.getString(DisType)
                        ,String.valueOf(total_dis)
                        ,String.valueOf(total)
                        ,resultSet.getString("code")));


                PayWithPointsRB.setSelected(false);

            }



            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }



    }

    public void DeleteRowFromFoodTableFun(){

        EditThisOrderNow.FoodBank selectedItem = DeliveryTable.getSelectionModel().getSelectedItem();
        DeliveryTable.getItems().remove(selectedItem);
        GetTheFoodTableCalculations();
        if(DeliveryTable.getItems().isEmpty()){
            yourTotalPointsText.setText("0");
        }
    }

    public void ClearAllRowsFromFoodTableFun(){

        DeliveryTable.getItems().clear();
        GetTheFoodTableCalculations();
        PaidText.setText("0");
        ReturnText.setText("0");
    }

    public void GetTheFoodTableCalculations(){

        try {

            double total_discount = 0 ;
            for (EditThisOrderNow.FoodBank item : DeliveryTable.getItems()) {
                total_discount = total_discount + Double.parseDouble(item.getFoodTotalDis());
            }
            savedMoneyText.setText(String.valueOf(total_discount));

            double total_cash = 0 ;
            for (EditThisOrderNow.FoodBank item : DeliveryTable.getItems()) {
                total_cash = total_cash + Double.parseDouble(item.getFoodTotal());
            }
            cashMoneyText.setText(String.valueOf(total_cash));

            double total_points = 0 ;
            total_points = total_cash / 20 ;
            pointsForThisOrderText.setText(String.valueOf(total_points));

            if(!DeliveryPhoneNumberText.getText().trim().isEmpty()) {

                OpenConnection();

                Statement GetUserInfo = connection.createStatement();
                String UserInfo = "Select * FROM customers_info WHERE phone_number = '" + DeliveryPhoneNumberText.getText().trim() + "'";
                ResultSet resultSet = GetUserInfo.executeQuery(UserInfo);

                System.out.println(PreviousPoints);

                if (resultSet.next()) {
                    double Points = Double.parseDouble(resultSet.getString("customer_points")) - Double.parseDouble(PreviousPoints);
                    yourTotalPointsText.setText(String.valueOf(Points));
                    System.out.println(Points);
                } else {

                    yourTotalPointsText.setText("0");

                }

                PayWithPointsFun();

                ManagePaidAndReturn();

                CloseConnection();

            }

        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }


    }

    public void ManagePaidAndReturn(){

        if(!PaidText.getText().trim().isEmpty()){
            try{

                double returnMoney = Double.parseDouble(PaidText.getText().trim()) - Double.parseDouble(cashMoneyText.getText().trim());

                ReturnText.setText(String.valueOf(returnMoney));


            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public void LoadTheDeliveryComboBoxFun(){
        try{
            OpenConnection();

            Statement GetUserInfo = connection.createStatement();
            String UserInfo = "Select * FROM users_info WHERE worker_type = '4'";
            ResultSet resultSet = GetUserInfo.executeQuery(UserInfo);

            DeliveryManComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
                System.out.println("Delivery Name : " + newVal.getNameX() + " his user name : " + newVal.getUser_nameX());
                CurrentDelivery = newVal.getNameX() ;
            });

            DeliveryManComboBox.setConverter(new StringConverter<EditThisOrderNow.DeliveryClassForComboBox>() {
                @Override
                public String toString(EditThisOrderNow.DeliveryClassForComboBox object) {
                    return object.getNameX();
                }

                @Override
                public EditThisOrderNow.DeliveryClassForComboBox fromString(String string) {
                    return null;
                }
            });

            DataForDeliveryManComboBox.clear();

            DataForDeliveryManComboBox.add(new EditThisOrderNow.DeliveryClassForComboBox("Delivery Man","No One"));


            while (resultSet.next()){

                DataForDeliveryManComboBox.add(new EditThisOrderNow.DeliveryClassForComboBox(resultSet.getString("worker_name"),resultSet.getString("user_name")));

            }

            DeliveryManComboBox.setItems(DataForDeliveryManComboBox);
            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }
    }


    // ToDo Edit The Colors And Try This Function .

    public void PayWithPointsFun(){

        if(ToggleFlagX!=0){
            try{
                OpenConnection();


                Statement GetCustomerInfo = connection.createStatement();
                String CustomerInfo = "Select * FROM customers_info WHERE phone_number = '" + DeliveryPhoneNumberText.getText().trim() + "'";
                ResultSet resultSet = GetCustomerInfo.executeQuery(CustomerInfo);

                if(resultSet.next()){

                    if(PayWithPointsRB.isSelected()){

                        pointsForThisOrderText.setText("0");

                        if(Double.parseDouble(cashMoneyText.getText().trim())
                                > Double.parseDouble(resultSet.getString("customer_points")) + Double.parseDouble(PreviousPoints)){

                            double restMoney = Double.parseDouble(cashMoneyText.getText().trim())
                                    - Double.parseDouble(resultSet.getString("customer_points")) + Double.parseDouble(PreviousPoints) ;

                            double restPoints = 0 ;


                            cashMoneyText.setText(String.valueOf(restMoney));
                            cashMoneyText.setFocusColor(Color.GREEN);
                            cashMoneyText.setUnFocusColor(Color.GREEN);


                            yourTotalPointsText.setText(String.valueOf(restPoints));
                            yourTotalPointsText.setFocusColor(Color.RED);
                            yourTotalPointsText.setUnFocusColor(Color.RED);

                        }
                        else if(Double.parseDouble(cashMoneyText.getText().trim())
                                <= (Double.parseDouble(resultSet.getString("customer_points")) - Double.parseDouble(PreviousPoints)) ) {

                            double restMoney = 0 ;

                            double restPoints = Double.parseDouble(resultSet.getString("customer_points"))
                                    - Double.parseDouble(PreviousPoints)
                                    - Double.parseDouble(cashMoneyText.getText().trim());


                            cashMoneyText.setText(String.valueOf(restMoney));
                            cashMoneyText.setFocusColor(Color.GREEN);
                            cashMoneyText.setUnFocusColor(Color.GREEN);


                            yourTotalPointsText.setText(String.valueOf(restPoints));
                            yourTotalPointsText.setFocusColor(Color.RED);
                            yourTotalPointsText.setUnFocusColor(Color.RED);

                        }

                    }
                    else {

                        ToggleFlagX = 0 ;

                        GetTheFoodTableCalculations();

                        cashMoneyText.setFocusColor(Color.valueOf("#4059a9"));
                        cashMoneyText.setUnFocusColor(Color.valueOf("#4d4d4d"));


                        double CPoints = Double.parseDouble(resultSet.getString("customer_points")) - Double.parseDouble(PreviousPoints);
                        yourTotalPointsText.setText(String.valueOf(CPoints));
                        yourTotalPointsText.setFocusColor(Color.valueOf("#4059a9"));
                        yourTotalPointsText.setUnFocusColor(Color.valueOf("#4d4d4d"));

                    }

                }else {

                    yourTotalPointsText.setText("0");
                    ToggleFlagX = 0 ;

                    GetTheFoodTableCalculations();

                    cashMoneyText.setFocusColor(Color.valueOf("#4059a9"));
                    cashMoneyText.setUnFocusColor(Color.valueOf("#4d4d4d"));


                    yourTotalPointsText.setFocusColor(Color.valueOf("#4059a9"));
                    yourTotalPointsText.setUnFocusColor(Color.valueOf("#4d4d4d"));

                }

                CloseConnection();

            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
            }
        }

    }

    public void PayWithPointsRBFun(){
        ToggleFlagX = 1 ;
        PayWithPointsFun();
    }

    class DeliveryClassForComboBox {
        private String name;
        private String user_name;

        public DeliveryClassForComboBox(String name, String user_name) {
            this.name = name;
            this.user_name = user_name;
        }

        public String getNameX() {
            return name;
        }

        public void setNameX(String name) {
            this.name = name;
        }

        public String getUser_nameX() {
            return user_name;
        }

        public void setUser_nameX(String user_name) {
            this.user_name = user_name;
        }
    }



    public void ClearTheLastOrder(){
        try {
            OpenConnection();


            // ToDo update the user rate

            Statement UpdateUserRate =  connection.createStatement();
            String UserRate = "UPDATE users_info SET rate = rate - "+(Double.parseDouble(OrderCash)/20)+
                    " WHERE user_name = '"+OrderCashierNameKey+"'";
            UpdateUserRate.executeUpdate(UserRate);



            // ToDo Update The Customer Info Including Points And Cash .

            double Reduced_Points ;

            Statement getThePointFlag = connection.createStatement();
            String PointFlag = "SELECT * FROM general_order WHERE code = '"+SelectedOrderItem+"'";
            ResultSet PointFlagRS = getThePointFlag.executeQuery(PointFlag);
            PointFlagRS.next();

            if(Double.parseDouble(PointFlagRS.getString("points")) == 0){
                Statement getOrderInfo = connection.createStatement();
                String OrderInfo = "SELECT * FROM order_detailes WHERE code = '"+SelectedOrderItem+"'";
                ResultSet resultSet = getOrderInfo.executeQuery(OrderInfo);

                double TotalCash = 0 ;
                double PaidCash ;
                while (resultSet.next()){
                    TotalCash = TotalCash + Double.parseDouble(resultSet.getString("total"));
                }
                PaidCash = Double.parseDouble(PointFlagRS.getString("total"));

                double YourPointsForThisOrder = (TotalCash - PaidCash);

                Statement UpdateCashAndPoints = connection.createStatement();
                String PointsAndCash = "UPDATE customers_info SET" +
                        " customer_cash = customer_cash - "+Double.parseDouble(SelectedOrderItemCash)+" ," +
                        " customer_points = customer_points + "+YourPointsForThisOrder+" WHERE phone_number = '"+SelectedOrderItemCustomerPhone+"'";
                UpdateCashAndPoints.executeUpdate(PointsAndCash);

            }else {
                Reduced_Points = Double.parseDouble(PointFlagRS.getString("points"));

                System.out.println(Reduced_Points);
                System.out.println(SelectedOrderItemCash);

                Statement UpdateCashAndPoints = connection.createStatement();
                String PointsAndCash = "UPDATE customers_info SET" +
                        " customer_cash = customer_cash - "+Double.parseDouble(SelectedOrderItemCash)+" ," +
                        " customer_points = customer_points - "+Reduced_Points+" WHERE phone_number = '"+SelectedOrderItemCustomerPhone+"'";
                UpdateCashAndPoints.executeUpdate(PointsAndCash);
            }





            // ToDo Update The Order general_order and general_order_temp Table by remove the order  .

            Statement UpdateGeneralOrder = connection.createStatement();
            String RemoveGeneralOrder = "Delete FROM general_order WHERE code = '"+SelectedOrderItem+"'";
            UpdateGeneralOrder.executeUpdate(RemoveGeneralOrder);

            Statement UpdateGeneralOrderTemp = connection.createStatement();
            String RemoveGeneralOrderTemp = "Delete FROM general_order_temp WHERE code = '"+SelectedOrderItem+"'";
            UpdateGeneralOrderTemp.executeUpdate(RemoveGeneralOrderTemp);


            // ToDo Update Items Rate In Menu Table .


            // load order info using the order number

            Statement GetTheOrder = connection.createStatement();
            String Order = "SELECT * FROM order_detailes WHERE code = '" + SelectedOrderItem + "'";
            ResultSet resultSet = GetTheOrder.executeQuery(Order);

            while (resultSet.next()){

                // update the item rate ...
                Statement UpdateTheItemRate = connection.createStatement();
                String ItemRate = "UPDATE menu Set rate = rate - "+Double.parseDouble(resultSet.getString("number_of_items"))
                        +" WHERE code = '"+resultSet.getString("item_code")+"'";
                UpdateTheItemRate.executeUpdate(ItemRate);

            }

            // ToDo Update The Order Details .
            Statement RemoveItemsFromOrderDetails = connection.createStatement();
            String DeleteOrder = "DELETE FROM order_detailes WHERE code = '"+SelectedOrderItem+"'";
            RemoveItemsFromOrderDetails.executeUpdate(DeleteOrder);

            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }
    }

    private void PrintReportToPrinter(JasperPrint jp) throws JRException {

        try {
            // TODO Auto-generated method stub
            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
            // printRequestAttributeSet.add(MediaSizeName.ISO_A4); //setting page size
            printRequestAttributeSet.add(new Copies(1));

            // set the printer name
            PrinterName printerName = new PrinterName(PrinterNameFromTheComputer, null); //gets printer

            PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
            printServiceAttributeSet.add(printerName);

            JRPrintServiceExporter exporter = new JRPrintServiceExporter();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
            exporter.exportReport();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void PrintKitchenReportToPrinter(JasperPrint jp) throws JRException {

        try {
            // TODO Auto-generated method stub
            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
            // printRequestAttributeSet.add(MediaSizeName.ISO_A4); //setting page size
            printRequestAttributeSet.add(new Copies(1));

            // set the printer name
            PrinterName printerName = new PrinterName(PrinterNameFromTheComputerForKitchen, null); //gets printer

            PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
            printServiceAttributeSet.add(printerName);

            JRPrintServiceExporter exporter = new JRPrintServiceExporter();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
            exporter.exportReport();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void FinishTheOrder(){


        if(!DeliveryPhoneNumberText.getText().trim().isEmpty() &&  !DeliveryNameText.getText().trim().isEmpty() && !DeliveryTable.getItems().isEmpty()){

            try {

                ClearTheLastOrder();

                OpenConnection();

                List<Map<String,?>> dataSource = new ArrayList();

                List<Map<String,?>> dataSource2 = new ArrayList();

                System.out.println("We Are In The Finish The Order Function Editing");



                Statement GetTheTotalPoints =  connection.createStatement();
                String TotalPoints = "SELECT * From customers_info WHERE phone_number = '"+DeliveryPhoneNumberText.getText().trim()+"'";
                ResultSet RS = GetTheTotalPoints.executeQuery(TotalPoints);

                if(RS.next()){
                    currentPoints = Double.parseDouble(RS.getString("customer_points"));
                }

                // ToDo Update The Customer Info Including Points And Cash .


                double newPoints ;
                double InnerConsumed ;
                if(PayWithPointsRB.isSelected()){
                    newPoints = 0 ;

                    // loop to get the total
                    double total = 0 ;

                    for (EditThisOrderNow.FoodBank item : DeliveryTable.getItems()) {
                        total = total + Double.parseDouble(item.getFoodTotal());
                    }
                    InnerConsumed = total - Double.parseDouble(cashMoneyText.getText().trim()) ;


                }else {
                    newPoints = Double.parseDouble(pointsForThisOrderText.getText().trim());
                    InnerConsumed = 0 ;
                }


                double points = newPoints + currentPoints ;

                double cash = Double.parseDouble(cashMoneyText.getText().trim());

                    Statement UpdateTheUserInfo =  connection.createStatement();
                    String CustomerInfo = "UPDATE customers_info SET customer_name = '"+DeliveryNameText.getText().trim()+
                            "' , address = '"+DeliveryAddressText.getText().trim()+
                            "' , note = '"+DeliveryNoteText.getText().trim()+
                            "' , customer_points = "+points+
                            "  , customer_cash = customer_cash +"+cash+
                            " WHERE phone_number = '"+DeliveryPhoneNumberText.getText().trim()+"'";
                    UpdateTheUserInfo.executeUpdate(CustomerInfo);


                // ToDo Update Insert The Order Into general_order and general_order_temp Table   .



                // get order Type

                String OrderType ;

                if(DeliveryTypeRB.isSelected()){
                    OrderType = "Delivery";
                }else if(LobbyTypeRB.isSelected()){
                    OrderType = "Lobby";
                }else {
                    OrderType = "Take Away";
                }



                OpenConnection();
                Statement UpdateTheOrderTable =  connection.createStatement();
                String OrderTable = "INSERT INTO general_order (customer_phone, customer_name, cashier_name, estimated_time, delivery_man, total, order_type,points,consumed,shift_number) " +
                        "VALUES (" +
                        "'"+DeliveryPhoneNumberText.getText().trim()+
                        "','"+DeliveryNameText.getText().trim()+
                        "','"+CurrentUserNameKey+
                        "','"+EstimatedTimeText.getText().trim()+
                        "','"+CurrentDelivery+
                        "','"+cashMoneyText.getText().trim()+
                        "','"+OrderType+
                        "','"+newPoints+
                        "','"+InnerConsumed+
                        "','"+UserShiftNumber+"')";
                UpdateTheOrderTable.executeUpdate(OrderTable);



                Statement UpdateTheOrderTempTable =  connection.createStatement();
                String OrderTempTable = "INSERT INTO general_order_temp (customer_phone, customer_name, cashier_name, estimated_time, delivery_man, total, order_type,points,consumed,shift_number) " +
                        "VALUES (" +
                        "'"+DeliveryPhoneNumberText.getText().trim()+
                        "','"+DeliveryNameText.getText().trim()+
                        "','"+CurrentUserNameKey+
                        "','"+EstimatedTimeText.getText().trim()+
                        "','"+CurrentDelivery+
                        "','"+cashMoneyText.getText().trim()+
                        "','"+OrderType+
                        "','"+newPoints+
                        "','"+InnerConsumed+
                        "','"+UserShiftNumber+"')";
                UpdateTheOrderTempTable.executeUpdate(OrderTempTable);


                String code ;
                String CustomerPhone = DeliveryPhoneNumberText.getText().trim();
                String CustomerName = DeliveryNameText.getText().trim();

                // ToDo Update Insert The Order Table Into The Order Details Table  .

                // get the order code
                Statement GetTheOrderCode =  connection.createStatement();
                String OrderCode = "SELECT * FROM general_order WHERE code=(SELECT max(code) FROM general_order)";
                ResultSet resultSet = GetTheOrderCode.executeQuery(OrderCode);

                if(resultSet.next()) {
                    code = resultSet.getString("code");


                    // ToDo update the user rate

                    Statement UpdateUserRate =  connection.createStatement();
                    String UserRate = "UPDATE users_info SET rate = rate + "+(Double.parseDouble(resultSet.getString("total"))/20)+" WHERE user_name = '"+CurrentUserNameKey+"'";
                    UpdateUserRate.executeUpdate(UserRate);



                    Statement UpdateTheOrderDetails = connection.createStatement();
                    Statement UpdateTheItemRate = connection.createStatement();

                    for (EditThisOrderNow.FoodBank item : DeliveryTable.getItems()) {

                        String OrderDetails = "INSERT INTO order_detailes (code, customer_phone, customer_name, item_name, item_size, item_cost, number_of_items, discount_for_one, total_discount, total,item_code) VALUES " +
                                "('" +code+
                                "','"+CustomerPhone+
                                "','"+CustomerName+
                                "','"+item.getFoodName()+
                                "','"+item.getFoodSize()+
                                "','"+item.getFoodCost()+
                                "','"+item.getFoodNumItems()+
                                "','"+item.getFoodDis()+
                                "','"+item.getFoodTotalDis()+
                                "','"+item.getFoodTotal()+
                                "','"+item.getFoodCode()+"')";
                        UpdateTheOrderDetails.executeUpdate(OrderDetails);


                        // update the item rate ...
                        String ItemRate = "UPDATE menu Set rate = rate + "+Double.parseDouble(item.getFoodNumItems())+" WHERE code = '"+item.getFoodCode()+"'";
                        UpdateTheItemRate.executeUpdate(ItemRate);


                        // load the table to use the jasper report for printing the bill.

                        Map<String, Object> m = new HashMap();
                        m.put("item_name",item.getFoodName());
                        m.put("item_size",item.getFoodSize());
                        m.put("item_cost",item.getFoodCost());
                        m.put("item_nums",item.getFoodNumItems());
                        m.put("item_dis",item.getFoodDis());
                        m.put("item_dis_total",item.getFoodTotalDis());
                        m.put("item_total",item.getFoodTotal());
                        dataSource.add(m);

                        // load the table to use the jasper report for printing the bill for the Kitchen.

                        Map<String, Object> m2 = new HashMap();
                        m2.put("ItemName",item.getFoodName());
                        m2.put("ItemSize",item.getFoodSize());
                        m2.put("ItemNumber",item.getFoodNumItems());

                        dataSource2.add(m2);



                    }

                    UpDateTheShift();


                    // ToDo  reload the General Order Temp Table .

                    callback.accept("");

                    // close the Stage
                    Stage stage = (Stage) DoneButton.getScene().getWindow();
                    stage.close();


                    HashMap parameter = new HashMap();
                    parameter.put("BillNumber", code);
                    parameter.put("TotalCash",cashMoneyText.getText().trim());
                    parameter.put("Saved",savedMoneyText.getText().trim());
                    parameter.put("TotalPoints",String.valueOf(points));
                    parameter.put("CashierName",CurrentUserName);
                    parameter.put("DeliveryManName",CurrentDelivery );
                    parameter.put("CustomerPhone",DeliveryPhoneNumberText.getText().trim());
                    parameter.put("CustomerName",DeliveryNameText.getText().trim());
                    parameter.put("CustomerAddress",DeliveryAddressText.getText().trim() );

                    HashMap parameter2 = new HashMap();
                    parameter.put("CustomerNote", DeliveryNoteText.getText().trim());



                    JRBeanCollectionDataSource dataSource1 = new JRBeanCollectionDataSource(dataSource);
                    System.out.println(BillUrlPath);
                    JasperDesign jd = JRXmlLoader.load(BillUrlPath);
                    JasperReport jp = JasperCompileManager.compileReport(jd);
                    JasperPrint print = JasperFillManager.fillReport(jp, parameter, dataSource1);
                    JasperViewer.viewReport(print, false);
                    PrintReportToPrinter(print);



                    JRBeanCollectionDataSource dataSource3 = new JRBeanCollectionDataSource(dataSource2);
                    System.out.println(BillUrlPathForKitchen);
                    JasperDesign jd2 = JRXmlLoader.load(BillUrlPathForKitchen);
                    JasperReport jp2 = JasperCompileManager.compileReport(jd2);
                    JasperPrint print2 = JasperFillManager.fillReport(jp2, parameter2, dataSource3);
                    JasperViewer.viewReport(print2, false);
                    PrintKitchenReportToPrinter(print2);



                }



                CloseConnection();

            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
            }


        }

    }

    public void UpDateTheShift(){

        double totalBillsForShift = 0 ;

        // ***** in this function the shift is calculated for the user about his shift and what happened in it , in the last 24 hours of his shift

        try{

            OpenConnection();

            // check if the user is already in the shifts or not yet
            Statement CheckIfTheShiftExist = connection.createStatement();
            String FindTheShift = "SELECT * FROM shifts WHERE user_name = '"+CurrentUserNameKey+"'  AND user_shift_number = "+UserShiftNumber+"";
            ResultSet resultSetForTheShift = CheckIfTheShiftExist.executeQuery(FindTheShift);


            // get all bills for the user in this shift
            Statement GetUserInfo = connection.createStatement();
            String TransactionsOfTheDay = "SELECT * FROM general_order WHERE cashier_name = '"+CurrentUserNameKey+"'  AND shift_number = "+UserShiftNumber+"";
            ResultSet resultSet = GetUserInfo.executeQuery(TransactionsOfTheDay);

            if(resultSetForTheShift.next()){

                while (resultSet.next()){
                    totalBillsForShift = totalBillsForShift + Double.parseDouble(resultSet.getString("total"));
                }

                Statement UpDateShiftTable = connection.createStatement();


                String SetTheShift = "UPDATE shifts Set total_money_of_the_shift = "+totalBillsForShift+", total_earned_of_the_shift = "+0+" WHERE " +
                        "user_name = '"+CurrentUserNameKey+"' AND user_shift_number = "+UserShiftNumber+"";

                UpDateShiftTable.executeUpdate(SetTheShift);


            }
            else{

                while (resultSet.next()){
                    totalBillsForShift = totalBillsForShift + Double.parseDouble(resultSet.getString("total"));

                }

                Statement UpDateShiftTable = connection.createStatement();


                String SetTheShift = "INSERT INTO shifts (user_name,user_shift_number,total_money_of_the_shift, total_earned_of_the_shift) VALUES " +
                        "('"+CurrentUserNameKey+"',"+UserShiftNumber+","+totalBillsForShift+","+0+")";

                UpDateShiftTable.executeUpdate(SetTheShift);
            }



            CloseConnection();

        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }


    private Consumer<String> callback;


    public void setup(Consumer<String> callback) {
        this.callback = callback;
    }


    public EditThisOrderNow(){

        EditThisOrderStage.setOnShowing(windowEvent -> {


            if(PayWithPointsForEdit.equals("No")){
                PayWithPointsRB.setDisable(true);
            }


            LoadTheDeliveryComboBoxFun();

            // manage the date function to keep the date right .

            Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
                LocalTime currentTime = LocalTime.now();
                DateLabel.setText(currentTime.getHour() + " : " + currentTime.getMinute() + " : " + currentTime.getSecond());
            }),
                    new KeyFrame(Duration.seconds(1))
            );
            clock.setCycleCount(Animation.INDEFINITE);
            clock.play();


            // set The Cashier Name

            CashierNameLabel.setText(CurrentUserName);




            // load the text fields information

            try {


                OpenConnection();

                // ToDO Get Important Info


                Statement GetTheUserInfoRow =  connection.createStatement();
                String UserRow = "SELECT * FROM customers_info WHERE phone_number = '"+SelectedOrderItemCustomerPhone+"'";
                ResultSet resultSet = GetTheUserInfoRow.executeQuery(UserRow);

                if(resultSet.next()){
                    DeliveryNoteText.setText(resultSet.getString("note"));
                    DeliveryAddressText.setText(resultSet.getString("address"));
                    DeliveryNameText.setText(resultSet.getString("customer_name"));
                    DeliveryPhoneNumberText.setText(SelectedOrderItemCustomerPhone);
                    yourTotalPointsText.setText(resultSet.getString("customer_points"));

                    DeliveryPhoneNumberText.setEditable(false);
                    yourTotalPointsText.setEditable(false);
                }


                Statement GetTheOrderInfoRow =  connection.createStatement();
                String OrderRow = "SELECT * FROM general_order WHERE code = '"+SelectedOrderItem+"'";
                ResultSet resultSetForOrderInfo = GetTheOrderInfoRow.executeQuery(OrderRow);

                if(resultSetForOrderInfo.next()){

                    OrderCashierNameKey = resultSetForOrderInfo.getString("cashier_name");
                    OrderCash = resultSetForOrderInfo.getString("total");

                    PreviousCash =  resultSetForOrderInfo.getString("total");
                    PreviousPoints = resultSetForOrderInfo.getString("points");

                    PayWithPointsRB.setSelected(false);
                    if(Double.parseDouble(resultSetForOrderInfo.getString("points")) == 0){
                        PayWithPointsRB.setSelected(true);
                    }



                    cashMoneyText.setText(resultSetForOrderInfo.getString("total"));
                    cashMoneyText.setEditable(false);

                    ReturnText.setText(resultSetForOrderInfo.getString("total"));
                    ReturnText.setEditable(false);
                    PaidText.setText("0");

                    pointsForThisOrderText.setText(resultSetForOrderInfo.getString("points"));
                    pointsForThisOrderText.setEditable(false);

                    double CurrentPoints = Double.parseDouble(yourTotalPointsText.getText().trim()) - Double.parseDouble(pointsForThisOrderText.getText().trim()) ;

                    yourTotalPointsText.setText(String.valueOf(CurrentPoints));


                    EstimatedTimeText.setText(resultSetForOrderInfo.getString("estimated_time"));


                    // load the radio button selection information

                    if(resultSetForOrderInfo.getString("order_type").equals("Delivery")){
                        DeliveryTypeRB.setSelected(true);
                    }else if(resultSetForOrderInfo.getString("order_type").equals("Take Away")){
                        TakeAWayTypeRB.setSelected(true);
                    }else {
                        LobbyTypeRB.setSelected(true);
                    }

                    // load the combo box selection

                    for (DeliveryClassForComboBox X : DataForDeliveryManComboBox)
                    {
                        if (X.getNameX().equals(resultSetForOrderInfo.getString("delivery_man")))
                        {
                            DeliveryManComboBox.setValue(X);
                        }
                    }


                }


                // load the food table raws

                Statement GetOrderRows =  connection.createStatement();
                String OrderRows = "SELECT * FROM order_detailes WHERE code = '"+SelectedOrderItem+"'";
                ResultSet resultSetForOrderRows = GetOrderRows.executeQuery(OrderRows);

                DeliveryTable.getItems().clear();

                while (resultSetForOrderRows.next()){

                    dataListForD.add(new EditThisOrderNow.FoodBank(
                             resultSetForOrderRows.getString("item_name")
                            ,resultSetForOrderRows.getString("item_size")
                            ,resultSetForOrderRows.getString("item_cost")
                            ,resultSetForOrderRows.getString("number_of_items")
                            ,resultSetForOrderRows.getString("discount_for_one")
                            ,resultSetForOrderRows.getString("total_discount")
                            ,resultSetForOrderRows.getString("total")
                            ,resultSetForOrderRows.getString("item_code")
                    ));

                }


                double total_discount = 0 ;
                for (EditThisOrderNow.FoodBank item : DeliveryTable.getItems()) {
                    total_discount = total_discount + Double.parseDouble(item.getFoodTotalDis());
                }

                savedMoneyText.setText(String.valueOf(total_discount));
                savedMoneyText.setEditable(false);


                CloseConnection();
            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
            }




        });

    }





}
