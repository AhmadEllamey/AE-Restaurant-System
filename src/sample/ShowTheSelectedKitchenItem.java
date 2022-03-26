package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static sample.DBConnection.*;
import static sample.Main.SelectedKitchenItemStage;
import static sample.MainScreen.SelectedItemFromKitchenTable;


public class ShowTheSelectedKitchenItem implements Initializable {


    private Consumer<String> callback;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setup(Consumer<String> callback) {
        this.callback = callback;
    }



    @FXML
    private JFXButton UpDateTheKitchenItemButton;

    @FXML
    private JFXTextField KitchenItemNameText;

    @FXML
    private JFXTextField KitchenItemIDText;

    @FXML
    private JFXTextField KitchenItemCostText;

    @FXML
    private JFXTextField KitchenItemQuantityText;

    @FXML
    private JFXTextField KitchenItemQuantityUnitText;

    @FXML
    private JFXTextField KitchenItemSupplierText;

    @FXML
    private JFXTextField KitchenItemSupplierPhoneText;

    @FXML
    private JFXTextField KitchenItemNoteText;



    public ShowTheSelectedKitchenItem() {

        SelectedKitchenItemStage.setOnShowing(event -> {
            try {
                OpenConnection();

                Statement GetItemInfo = connection.createStatement();
                String ItemInfo = "SELECT * FROM kitchen_info WHERE item_id = '" + SelectedItemFromKitchenTable + "'";
                ResultSet resultSet = GetItemInfo.executeQuery(ItemInfo);

                if (resultSet.next()) {

                    KitchenItemNameText.setText(resultSet.getString("item_name"));
                    KitchenItemIDText.setText(resultSet.getString("item_id"));
                    KitchenItemCostText.setText(resultSet.getString("cost"));
                    KitchenItemQuantityText.setText(resultSet.getString("quantity"));
                    KitchenItemQuantityUnitText.setText(resultSet.getString("quantity_unit"));
                    KitchenItemSupplierText.setText(resultSet.getString("supplier"));
                    KitchenItemSupplierPhoneText.setText(resultSet.getString("supplier_number"));
                    KitchenItemNoteText.setText(resultSet.getString("note"));

                    KitchenItemIDText.setEditable(false);
                    KitchenItemCostText.setEditable(false);
                    KitchenItemQuantityText.setEditable(false);
                    KitchenItemQuantityUnitText.setEditable(false);
                }

                CloseConnection();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }


    public void UpDateTheKitchenItemFun(){


        try {

            OpenConnection();

            Statement UpdateKitchenItem = connection.createStatement();
            String ItemInfo = "UPDATE kitchen_info SET item_name = '"+KitchenItemNameText.getText().trim()+
                    "' , supplier = '"+KitchenItemSupplierText.getText().trim()+
                    "' , supplier_number = '"+KitchenItemSupplierPhoneText.getText().trim()+
                    "' , note = '"+KitchenItemNoteText.getText().trim()+"' WHERE item_id = '"+KitchenItemIDText.getText().trim()+"'";
            UpdateKitchenItem.executeUpdate(ItemInfo);


            // close the Stage
            Stage stage = (Stage) UpDateTheKitchenItemButton.getScene().getWindow();
            stage.close();


            // ToDo  reload the customers table ?????

            callback.accept("");

            CloseConnection();

        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }






    }


}
