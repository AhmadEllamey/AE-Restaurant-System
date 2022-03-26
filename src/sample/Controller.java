package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.Statement;

import static sample.DBConnection.*;
import static sample.Main.*;

public class Controller {




    @FXML
    private JFXTextField UserNameText;

    @FXML
    private JFXPasswordField PasswordText;

    @FXML
    private JFXButton LogInButton;

    @FXML
    private Label ContactUsButtonLink;



    public void LogInFun(){
        try {
            OpenConnection();

            Statement CheckTheUser =  connection.createStatement();
            String Check = "SELECT * FROM users_info WHERE user_name = '"+UserNameText.getText().trim()+
                    "' AND password = '"+PasswordText.getText().trim()+"'";
            ResultSet resultSet = CheckTheUser.executeQuery(Check);

            if (resultSet.next()){

                if(PasswordText.getText().trim().equals(resultSet.getString("password"))){
                    CurrentUserName = resultSet.getString("worker_name");
                    CurrentUserNameKey = resultSet.getString("user_name");

                    // close the Stage
                    Stage stage = (Stage) LogInButton.getScene().getWindow();
                    stage.close();

                    // open the main screen
                    FXMLLoader loader = new FXMLLoader(Main.class.getResource("MainScreen.fxml"));
                    Parent mainCallWindowFXML = loader.load();
                    TheMainStage.setTitle("AE Restaurant Management System !");
                    TheMainStage.setScene(new Scene(mainCallWindowFXML,800,600));
                    //TheMainStage.setFullScreen(true);

                    GetTheShiftNumber();


                    TheMainStage.show();
                }

            }

            CloseConnection();

        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }
    }

    public void GetTheShiftNumber(){

        try{

            Statement ScanShifts =  connection.createStatement();
            String GetTheShiftNumber = "SELECT shift_number FROM general_order WHERE cashier_name = '"+CurrentUserNameKey+"'";
            ResultSet resultSet = ScanShifts.executeQuery(GetTheShiftNumber);

            if(resultSet.next()){
                resultSet.last();
                UserShiftNumber = Integer.parseInt(resultSet.getString("shift_number")) + 1;
            }
            else{
                UserShiftNumber = 1 ;
            }

        }

        catch (Exception e){
            e.printStackTrace();
        }


    }

    public void HowToContactUsFun(){
        //ToDO How to contact us ???

    }




}
