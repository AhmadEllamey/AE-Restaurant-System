package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import static sample.Main.ShowTheFoodInfoStage;
import static sample.MainScreen.SelectedFoodItem;

public class ShowTheSelectedFoodItem implements Initializable {


    private Consumer<String> callback;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setup(Consumer<String> callback) {
        this.callback = callback;
    }



    @FXML
    private Label CodeLabel;

    @FXML
    private Label RateLabel;

    @FXML
    private JFXTextField ItemNameText;

    @FXML
    private JFXTextField ItemCategoryText;

    @FXML
    private JFXTextField SCostText;

    @FXML
    private JFXTextField MCostText;

    @FXML
    private JFXTextField LCostText;

    @FXML
    private JFXTextField XLCostText;

    @FXML
    private JFXTextField XXLCostText;

    @FXML
    private JFXTextField SDIsText;

    @FXML
    private JFXTextField MDisText;

    @FXML
    private JFXTextField LDisText;

    @FXML
    private JFXTextField XLDisText;

    @FXML
    private JFXTextField XXLDisText;

    @FXML
    private JFXButton UpdateTheItemButton;



    public ShowTheSelectedFoodItem() {

        ShowTheFoodInfoStage.setOnShowing(event -> {
            try {
                OpenConnection();

                Statement GetItemInfo = connection.createStatement();
                String ItemInfo = "SELECT * FROM menu WHERE code = '"+SelectedFoodItem+"'";
                ResultSet resultSet = GetItemInfo.executeQuery(ItemInfo);

                if (resultSet.next()) {

                    // make all of these texts accepts only double

                    Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");

                    UnaryOperator<TextFormatter.Change> filter = c -> {
                        String text = c.getControlNewText();
                        if (validEditingState.matcher(text).matches()) {
                            return c ;
                        } else {
                            return null ;
                        }
                    };

                    StringConverter<Double> converter = new StringConverter<>() {

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
                    TextFormatter<Double> textFormatter2 = new TextFormatter<>(converter, 0.0, filter);
                    TextFormatter<Double> textFormatter3 = new TextFormatter<>(converter, 0.0, filter);
                    TextFormatter<Double> textFormatter4 = new TextFormatter<>(converter, 0.0, filter);
                    TextFormatter<Double> textFormatter5 = new TextFormatter<>(converter, 0.0, filter);
                    TextFormatter<Double> textFormatter6 = new TextFormatter<>(converter, 0.0, filter);
                    TextFormatter<Double> textFormatter7 = new TextFormatter<>(converter, 0.0, filter);
                    TextFormatter<Double> textFormatter8 = new TextFormatter<>(converter, 0.0, filter);
                    TextFormatter<Double> textFormatter9 = new TextFormatter<>(converter, 0.0, filter);
                    TextFormatter<Double> textFormatter10 = new TextFormatter<>(converter, 0.0, filter);


                    SCostText.setTextFormatter(textFormatter);
                    MCostText.setTextFormatter(textFormatter2);
                    LCostText.setTextFormatter(textFormatter3);
                    XLCostText.setTextFormatter(textFormatter4);
                    XXLCostText.setTextFormatter(textFormatter5);
                    SDIsText.setTextFormatter(textFormatter6);
                    MDisText.setTextFormatter(textFormatter7);
                    LDisText.setTextFormatter(textFormatter8);
                    XLDisText.setTextFormatter(textFormatter9);
                    XXLDisText.setTextFormatter(textFormatter10);



                    CodeLabel.setText(resultSet.getString("code"));
                    RateLabel.setText(resultSet.getString("rate"));
                    ItemNameText.setText(resultSet.getString("item_name"));
                    ItemCategoryText.setText(resultSet.getString("category"));
                    SCostText.setText(String.valueOf(Double.parseDouble(resultSet.getString("s"))));
                    MCostText.setText(resultSet.getString("m"));
                    LCostText.setText(resultSet.getString("l"));
                    XLCostText.setText(resultSet.getString("xl"));
                    XXLCostText.setText(resultSet.getString("xxl"));
                    SDIsText.setText(resultSet.getString("dis_s"));
                    MDisText.setText(resultSet.getString("dis_m"));
                    LDisText.setText(resultSet.getString("dis_l"));
                    XLDisText.setText(resultSet.getString("dis_xl"));
                    XXLDisText.setText(resultSet.getString("dis_xxl"));

                }

                CloseConnection();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public void UpdateTheFoodItem(){

        if(!ItemNameText.getText().trim().isEmpty() &&
                !ItemCategoryText.getText().trim().isEmpty() &&
                !SCostText.getText().trim().isEmpty() &&
                !MCostText.getText().trim().isEmpty() &&
                !LCostText.getText().trim().isEmpty() &&
                !XLCostText.getText().trim().isEmpty() &&
                !XXLCostText.getText().trim().isEmpty() &&
                !SDIsText.getText().trim().isEmpty() &&
                !MDisText.getText().trim().isEmpty() &&
                !LDisText.getText().trim().isEmpty() &&
                !XLDisText.getText().trim().isEmpty() &&
                !XXLDisText.getText().trim().isEmpty()){

            try {
                OpenConnection();

                Statement UpdateTheFoodItem =  connection.createStatement();
                String FoodItem = "UPDATE menu SET item_name = '"+ItemNameText.getText().trim()+"' ," +
                        " category = '"+ItemCategoryText.getText().trim()+"' ," +
                        " s = "+Double.parseDouble(SCostText.getText().trim())+" ," +
                        " m = "+Double.parseDouble(MCostText.getText().trim())+"," +
                        " l = "+Double.parseDouble(LCostText.getText().trim())+" ," +
                        " xl = "+Double.parseDouble(XLCostText.getText().trim())+"," +
                        " xxl = "+Double.parseDouble(XXLCostText.getText().trim())+"," +
                        " dis_s = "+Double.parseDouble(SDIsText.getText().trim())+"," +
                        " dis_m = "+Double.parseDouble(MDisText.getText().trim())+"," +
                        " dis_l = "+Double.parseDouble(LDisText.getText().trim())+"," +
                        " dis_xl = "+Double.parseDouble(XLDisText.getText().trim())+"," +
                        " dis_xxl = "+Double.parseDouble(XXLDisText.getText().trim())+" " +
                        "WHERE code = '"+CodeLabel.getText().trim()+"'";
                UpdateTheFoodItem.executeUpdate(FoodItem);

                // close the Stage
                Stage stage = (Stage) UpdateTheItemButton.getScene().getWindow();
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


}
