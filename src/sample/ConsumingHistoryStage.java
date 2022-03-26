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
import static sample.DBConnection.CloseConnection;
import static sample.Main.ConsumingHistoryForAnItem;
import static sample.MainScreen.SelectedItemFromKitchenTable;

public class ConsumingHistoryStage implements Initializable {


    private Consumer<String> callback;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setup(Consumer<String> callback) {
        this.callback = callback;
    }



    @FXML
    private Label ItemIDLabel;

    @FXML
    private TableView<ObservableList> ConsumingHistoryTable;

    private ObservableList<ObservableList> dataForConsumingHistoryTable ;

    @FXML
    private TableColumn ItemNameColumn;

    @FXML
    private TableColumn QuantityColumn;

    @FXML
    private TableColumn TypeColumn;

    @FXML
    private TableColumn DateColumn;

    @FXML
    private JFXButton OkButton;




    public ConsumingHistoryStage() {

        ConsumingHistoryForAnItem.setOnShowing(event -> {

            try{
                OpenConnection();

                Statement GetItemsInfo = connection.createStatement();
                String Item = "SELECT * FROM kitchen_info WHERE item_id = '"+SelectedItemFromKitchenTable+"'";
                ResultSet resultSet = GetItemsInfo.executeQuery(Item);

                if(resultSet.next()){

                    ItemIDLabel.setText(resultSet.getString("item_id"));
                    String itemName = resultSet.getString("item_name");

                    CreateTheConsumingDetailsTable(itemName);

                }

                CloseConnection();
            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
            }

        });

    }



    public void OkToGoFun() {
        // close the Stage
        Stage stage = (Stage) OkButton.getScene().getWindow();
        stage.close();
    }


    public void CreateTheConsumingDetailsTable(String name){

        ConsumingHistoryTable.getColumns().clear();

        ItemNameColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(0).toString()));

        QuantityColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(1).toString()));

        TypeColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(2).toString()));

        DateColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(3).toString()));




        ConsumingHistoryTable.getColumns().add(ItemNameColumn);
        ConsumingHistoryTable.getColumns().add(QuantityColumn);
        ConsumingHistoryTable.getColumns().add(TypeColumn);
        ConsumingHistoryTable.getColumns().add(DateColumn);



        LoadTheConsumingDetailsTableFun(0,name);


    }

    public void LoadTheConsumingDetailsTableFun(int mode , String itemName){


        // mode 0 for the normal arrange
        // mode 1 for arrange Items by cost

        try {
            OpenConnection();

            ConsumingHistoryTable.setItems(null);

            dataForConsumingHistoryTable = FXCollections.observableArrayList();

            Statement GetItemsInfo = connection.createStatement();
            String Item = "SELECT * FROM kitchen_consuming_info WHERE item_id = '"+SelectedItemFromKitchenTable+"'";
            ResultSet resultSet = GetItemsInfo.executeQuery(Item);

            while (resultSet.next()){

                ObservableList<String> row = FXCollections.observableArrayList();

                row.add(itemName);
                row.add(resultSet.getString("quantity"));
                row.add(resultSet.getString("consuming_type"));
                row.add(resultSet.getString("last_time_updated"));

                dataForConsumingHistoryTable.add(row);


            }

            ConsumingHistoryTable.setItems(dataForConsumingHistoryTable);

            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }



    }


}
