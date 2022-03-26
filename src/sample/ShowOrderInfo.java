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
import static sample.Main.ShowTheOrderInfoStage;
import static sample.MainScreen.SelectedOrderItem;

public class ShowOrderInfo implements Initializable {




    @FXML
    private Label OrderNumberLabel;

    @FXML
    private Label CustomerPhoneLabel;

    @FXML
    private Label CustomerNameLabel;

    @FXML
    private Label TotalLabel;

    @FXML
    private TableColumn<FoodBank,String> DeliveryTableColumnItemName;

    @FXML
    private TableColumn<FoodBank,String> DeliveryTableColumnItemSize;

    @FXML
    private TableColumn<FoodBank,String> DeliveryTableColumnItemCost;

    @FXML
    private TableColumn<FoodBank,String> DeliveryTableColumnNumberOfItems;

    @FXML
    private TableColumn<FoodBank,String> DeliveryTableColumnDisForOne;

    @FXML
    private TableColumn<FoodBank,String> DeliveryTableColumnTotalDis;

    @FXML
    private TableColumn<FoodBank,String> DeliveryTableColumnTotal;

    @FXML
    private TableColumn<FoodBank,String> DeliveryTableColumnCode;

    @FXML
    private JFXButton OkButton;


    @FXML
    private TableView<FoodBank> DeliveryTable;

    private ObservableList<ShowOrderInfo.FoodBank> dataForDeliveryTable = FXCollections.observableArrayList();



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
                        return new ShowOrderInfo.EditingCell();
                    }
                };

        // ToDo FoodBank Table

        DeliveryTableColumnItemName.setCellValueFactory(new PropertyValueFactory<>("foodName"));
        DeliveryTableColumnItemSize.setCellValueFactory(new PropertyValueFactory<>("foodSize"));
        DeliveryTableColumnItemCost.setCellValueFactory(new PropertyValueFactory<>("foodCost"));
        DeliveryTableColumnNumberOfItems.setCellValueFactory(new PropertyValueFactory<>("foodNumItems"));
        DeliveryTableColumnDisForOne.setCellValueFactory(new PropertyValueFactory<>("foodDis"));
        DeliveryTableColumnTotalDis.setCellValueFactory(new PropertyValueFactory<>("foodTotalDis"));
        DeliveryTableColumnTotal.setCellValueFactory(new PropertyValueFactory<>("foodTotal"));
        DeliveryTableColumnCode.setCellValueFactory(new PropertyValueFactory<>("foodCode"));



        DeliveryTable.setItems(dataForDeliveryTable);


    }

    public ShowOrderInfo(){

        ShowTheOrderInfoStage.setOnShowing(windowEvent -> {

            try{

                OpenConnection();


                Statement GetOrderInfo = connection.createStatement();
                String Order = "SELECT * FROM order_detailes WHERE code = "+Integer.parseInt(SelectedOrderItem)+"";
                ResultSet resultSet = GetOrderInfo.executeQuery(Order);

                double totalCash = 0 ;
                while (resultSet.next()){

                    dataForDeliveryTable.add(new ShowOrderInfo.FoodBank(
                             resultSet.getString("item_name")
                            ,resultSet.getString("item_size")
                            ,resultSet.getString("item_cost")
                            ,resultSet.getString("number_of_items")
                            ,resultSet.getString("discount_for_one")
                            ,resultSet.getString("total_discount")
                            ,resultSet.getString("total")
                            ,resultSet.getString("item_code")));

                    totalCash = totalCash + Double.parseDouble(resultSet.getString("total"));

                }

                resultSet.previous();

                OrderNumberLabel.setText(resultSet.getString("code"));
                CustomerPhoneLabel.setText(resultSet.getString("customer_phone"));
                CustomerNameLabel.setText(resultSet.getString("customer_name"));
                TotalLabel.setText(String.valueOf(totalCash));
                CloseConnection();


            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
            }



        });

    }



    public void OkToGo(){
        // close the Stage
        Stage stage = (Stage) OkButton.getScene().getWindow();
        stage.close();
    }




}
