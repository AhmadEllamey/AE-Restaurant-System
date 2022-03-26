package sample;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static sample.DBConnection.*;
import static sample.Main.CurrentUserNameKey;
import static sample.Main.UserShiftNumber;
import static sample.MainScreen.*;


public class DeleteOrderConfirmation implements Initializable {


    private Consumer<String> callback;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setup(Consumer<String> callback) {
        this.callback = callback;
    }

    public DeleteOrderConfirmation() {

    }


    @FXML
    private JFXButton DeleteButton;

    @FXML
    private JFXButton CancelButton;

    @FXML
    public void CancelFun() {
        // close the Stage
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
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

    @FXML
    public void DeleteAnOrderFun() {
        try {
            OpenConnection();


            // ToDo Update The Customer Info Including Points And Cash .

            double Reduced_Points ;

            Statement getThePointFlag = connection.createStatement();
            String PointFlag = "SELECT * FROM general_order WHERE code = '"+SelectedOrderItem+"'";
            ResultSet PointFlagRS = getThePointFlag.executeQuery(PointFlag);
            PointFlagRS.next();


            // ToDo update the user rate

            Statement UpdateUserRate =  connection.createStatement();
            String UserRate = "UPDATE users_info SET rate = rate - "+(Double.parseDouble(PointFlagRS.getString("total"))/20)+
                    " WHERE user_name = '"+PointFlagRS.getString("cashier_name")+"'";
            UpdateUserRate.executeUpdate(UserRate);



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
                        " customer_points = customer_points + "+YourPointsForThisOrder+" WHERE phone_number = '"+SelectedOrderItemPhone+"'";
                UpdateCashAndPoints.executeUpdate(PointsAndCash);

            }else {
                Reduced_Points = Double.parseDouble(PointFlagRS.getString("points"));

                Statement UpdateCashAndPoints = connection.createStatement();
                String PointsAndCash = "UPDATE customers_info SET" +
                        " customer_cash = customer_cash - "+Double.parseDouble(SelectedOrderItemCash)+" ," +
                        " customer_points = customer_points - "+Reduced_Points+" WHERE phone_number = '"+SelectedOrderItemPhone+"'";
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




            UpDateTheShift();

            // ToDo  reload the General Order Temp Table .

            callback.accept("");

            // close the Stage
            Stage stage = (Stage) DeleteButton.getScene().getWindow();
            stage.close();




            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }
    }



}

