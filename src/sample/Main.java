package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {



    public static Stage SelectedCustomerStage = new Stage() ;
    public static Stage SelectedWorkerStage = new Stage() ;
    public static Stage SelectedKitchenItemStage = new Stage() ;

    public static Stage AlertStage = new Stage() ;
    public static Stage AlertAboutUserNameStage = new Stage() ;

    public static Stage TheMainStage = new Stage() ;

    public static Stage ShowTheCustomerInfoStage = new Stage() ;

    public static Stage SelectTheSizeStage = new Stage() ;
    public static Stage ShowTheFoodInfoStage = new Stage() ;
    public static Stage SelectedKitchenItemDetailsStage = new Stage() ;
    public static Stage SelectedKitchenItemConsumingStage = new Stage() ;
    public static Stage ConsumingHistoryForAnItem = new Stage() ;
    public static Stage DeleteAnOrderQuestion = new Stage() ;
    public static Stage ShowTheOrderInfoStage = new Stage() ;
    public static Stage ShowQuestionAboutTheOrder = new Stage() ;
    public static Stage ShowQuestionAboutTheOrdersRemoving = new Stage() ;
    public static Stage EditThisOrderStage = new Stage() ;
    public static Stage StatisticsCustomerInfo = new Stage() ;
    public static Stage StatisticsWorkerInfo = new Stage() ;
    public static Stage StatisticsOrdersInfo = new Stage() ;
    public static Stage StatisticsOrdersInfoDetails = new Stage() ;
    public static Stage StatisticsShiftsInfoDetails = new Stage() ;


    public static String CurrentUserName ;
    public static String CurrentUserNameKey ;
    public static String CurrentDelivery ;

    public static int UserShiftNumber ;

    public static String BillUrlPath ;
    public static String PrinterNameFromTheComputer ;

    public static String PrinterNameFromTheComputerForKitchen ;
    public static String BillUrlPathForKitchen;

    public static String PayWithPointsForEdit ;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LogInScreen.fxml")));
        primaryStage.setTitle("AE Restaurant System !");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);

    }
}
