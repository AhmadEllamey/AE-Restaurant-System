package sample;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static sample.DBConnection.*;
import static sample.Main.SelectedKitchenItemDetailsStage;

import static sample.MainScreen.SelectedItemFromKitchenTable;


public class ShowTheSelectedKitchenItemDetails implements Initializable {



    private Consumer<String> callback;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setup(Consumer<String> callback) {
        this.callback = callback;
    }

    public ShowTheSelectedKitchenItemDetails() {

        SelectedKitchenItemDetailsStage.setOnShowing(event -> {
            CreateTheKitchenItemsDetailsTable();
        });

    }

    @FXML
    private Label ItemIDLabel;

    @FXML
    private TableView<ObservableList> ItemInfoTable;

    private ObservableList<ObservableList> dataForItemInfoTable ;

    @FXML
    private TableColumn ItemNameColumn;

    @FXML
    private TableColumn ItemCostColumn;

    @FXML
    private TableColumn ItemQuantityColumn;

    @FXML
    private TableColumn ItemQuantityUnitColumn;

    @FXML
    private TableColumn ItemSingleCostColumn;

    @FXML
    private TableColumn ItemSupplierColumn;

    @FXML
    private TableColumn ItemSupplierPhoneColumn;

    @FXML
    private TableColumn ItemNOteColumn;

    @FXML
    private TableColumn ItemDateColumn;

    @FXML
    private JFXButton OkButton;


    public void OkToGoFun() {
        // close the Stage
        Stage stage = (Stage) OkButton.getScene().getWindow();
        stage.close();
    }



    public void CreateTheKitchenItemsDetailsTable(){

        ItemInfoTable.getColumns().clear();

        ItemNameColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(0).toString()));

        ItemCostColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(1).toString()));

        ItemQuantityColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(2).toString()));

        ItemQuantityUnitColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(3).toString()));

        ItemSingleCostColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(4).toString()));

        ItemSupplierColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(5).toString()));

        ItemSupplierPhoneColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(6).toString()));

        ItemNOteColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(7).toString()));

        ItemDateColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(8).toString()));



        ItemInfoTable.getColumns().add(ItemNameColumn);
        ItemInfoTable.getColumns().add(ItemCostColumn);
        ItemInfoTable.getColumns().add(ItemQuantityColumn);
        ItemInfoTable.getColumns().add(ItemQuantityUnitColumn);
        ItemInfoTable.getColumns().add(ItemSingleCostColumn);
        ItemInfoTable.getColumns().add(ItemSupplierColumn);
        ItemInfoTable.getColumns().add(ItemSupplierPhoneColumn);
        ItemInfoTable.getColumns().add(ItemNOteColumn);
        ItemInfoTable.getColumns().add(ItemDateColumn);


        LoadTheKitchenItemsDetailsTableFun(0);


    }

    public void LoadTheKitchenItemsDetailsTableFun(int mode){


        // mode 0 for the normal arrange
        // mode 1 for arrange Items by cost

        int flag = 1 ;

        try {
            OpenConnection();

            ItemInfoTable.setItems(null);

            dataForItemInfoTable = FXCollections.observableArrayList();

            Statement GetItemsInfo = connection.createStatement();
            String Item = "SELECT * FROM kitchen_details_info WHERE item_id = '"+SelectedItemFromKitchenTable+"'";
            ResultSet resultSet = GetItemsInfo.executeQuery(Item);

            while (resultSet.next()){

                if(flag == 1){
                    ItemIDLabel.setText(resultSet.getString("item_id"));
                    flag = 0 ;
                }

                ObservableList<String> row = FXCollections.observableArrayList();

                row.add(resultSet.getString("item_name"));
                row.add(resultSet.getString("cost"));
                row.add(resultSet.getString("quantity"));
                row.add(resultSet.getString("quantity_unit"));
                row.add(String.valueOf(Double.parseDouble(resultSet.getString("cost")) / Double.parseDouble(resultSet.getString("quantity"))));
                row.add(resultSet.getString("supplier"));
                row.add(resultSet.getString("supplier_number"));
                row.add(resultSet.getString("note"));
                row.add(resultSet.getString("last_time_updated"));

                dataForItemInfoTable.add(row);


            }

            ItemInfoTable.setItems(dataForItemInfoTable);

            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }



    }



}



