package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import static sample.DBConnection.*;
import static sample.Main.ConsumingHistoryForAnItem;
import static sample.Main.SelectedKitchenItemConsumingStage;
import static sample.MainScreen.SelectedItemFromKitchenTable;

public class ShowKitchenItemConsumingManagement implements Initializable {


    private Consumer<String> callback;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setup(Consumer<String> callback) {
        this.callback = callback;
    }





    @FXML
    private Label ItemNameLabel;

    @FXML
    private Label ItemIDLabel;

    @FXML
    private JFXButton OpenTheHistoryButton;

    @FXML
    private JFXTextField QuantityText;

    @FXML
    private JFXTextField QuantityUnitText;

    @FXML
    private JFXRadioButton ConsumedRB;

    @FXML
    private JFXRadioButton RuinedRB;

    @FXML
    private JFXRadioButton LoanedRB;

    @FXML
    private JFXButton DoneButton;




    public ShowKitchenItemConsumingManagement() {

        SelectedKitchenItemConsumingStage.setOnShowing(event -> {

            try{
                OpenConnection();

                Statement GetItemsInfo = connection.createStatement();
                String ItemInfo = "SELECT * FROM kitchen_info WHERE item_id = '"+SelectedItemFromKitchenTable+"'";
                ResultSet resultSet = GetItemsInfo.executeQuery(ItemInfo);

                if(resultSet.next()){
                    ItemNameLabel.setText(resultSet.getString("item_name"));
                    ItemIDLabel.setText(resultSet.getString("item_id"));
                    QuantityUnitText.setText(resultSet.getString("quantity_unit"));

                    QuantityUnitText.setEditable(false);


                    Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

                    UnaryOperator<TextFormatter.Change> filter = c -> {
                        String text = c.getControlNewText();
                        if (validEditingState.matcher(text).matches()) {
                            return c ;
                        } else {
                            return null ;
                        }
                    };

                    StringConverter<Double> converter = new StringConverter<Double>() {

                        @Override
                        public Double fromString(String s) {
                            if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                                return 0.0 ;
                            } else {
                                return Double.valueOf(s);
                            }
                        }


                        @Override
                        public String toString(Double d) {
                            return d.toString();
                        }
                    };

                    TextFormatter<Double> textFormatter = new TextFormatter<>(converter, 0.0, filter);

                    QuantityText.setTextFormatter(textFormatter);



                }

                CloseConnection();
            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
            }

        });

    }

    public void CalculateAndUpdateConsumingHistoryFun(){

        if(!QuantityText.getText().trim().isEmpty() && Double.parseDouble(QuantityText.getText().trim())>0){

            try {
                OpenConnection();

                // ToDo Update The Kitchen Consuming History

                String type ;

                if(RuinedRB.isSelected()){
                    type = "Ruined" ;
                }else if(LoanedRB.isSelected()){
                    type = "Loaned" ;
                }else {
                    type = "Consumed" ;
                }

                Statement UpdateKitchenConsumingInfo = connection.createStatement();
                String UpdateTheKitchenConsumingInfo = "INSERT INTO kitchen_consuming_info (item_id, quantity, consuming_type) VALUES " +
                        "('"+SelectedItemFromKitchenTable+"',"+Double.parseDouble(QuantityText.getText().trim())+",'"+type+"')" ;
                UpdateKitchenConsumingInfo.executeUpdate(UpdateTheKitchenConsumingInfo);


                // ToDo Update The Kitchen Info


                Statement UpdateKitchenInfo = connection.createStatement();
                String UpdateTheKitchenInfo = "UPDATE kitchen_info SET" +
                        " quantity = quantity - "+Double.parseDouble(QuantityText.getText().trim())+
                        " WHERE item_id = '"+SelectedItemFromKitchenTable+"' " ;
                UpdateKitchenInfo.executeUpdate(UpdateTheKitchenInfo);

                // close the Stage
                Stage stage = (Stage) DoneButton.getScene().getWindow();
                stage.close();

                // ToDo  reload the Kitchen table ?????

                callback.accept("");



                CloseConnection();
            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
            }



        }













    }

    public void OpenTheConsumingHistory(){

        try {
            OpenConnection();

            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ConsumingHistoryStage.fxml"));
            loader.load();
            ConsumingHistoryStage controller = loader.getController();
            Parent popup = loader.getRoot();

            // Give popup a callback method
            controller.setup(
                    (value)->{
                         // Do Nothing
                    }
            );

            try {
                ConsumingHistoryForAnItem.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            ConsumingHistoryForAnItem.setTitle("Consumption Information  !!!");
            ConsumingHistoryForAnItem.setScene(new Scene(popup,800 ,600));
            ConsumingHistoryForAnItem.show();


            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }







    }


}
