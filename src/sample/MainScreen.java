package sample;

import com.jfoenix.controls.*;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;

import javafx.beans.value.ObservableValue;
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
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;


import static sample.DBConnection.*;
import static sample.Main.*;

public class MainScreen implements Initializable {



    public static String SelectedCustomerFromCustomerTable;

    public static String SelectedWorkerFromWorkerTable;

    public static String SelectedItemFromKitchenTable;

    public static String SelectedFoodItem;

    public static String SelectedOrderItem ;

    public static String SelectedOrderItemCash;

    public static String SelectedOrderItemPhone;

    public static String SearchForThatCustomer;

    public static String LastButtonIDForSpecialMenu ;

    public static String SelectedOrderItemCustomerPhone ;

    public static int StatisticsKeyValue ;
    public static String StatisticsValue1 ;
    public static String StatisticsValue2 ;
    public static String StatisticsValue3 ;

    public static String FoodItemName;
    public static String ItemSize;

    public static int ToggleFlag = 0 ;


    public static String CurrentCategory ;

    String [] btn_names = new String[10];




    public MainScreen() {

        TheMainStage.setOnShowing(event -> {


            CreateTheCurrentKitchenItemsSearchTable();
            CreateTheKitchenCurrentItemsTable();
            CreateTheCurrentWorkerSearchTable();
            CreateTheCurrentWorkerTable();
            CreateTheCurrentCustomerTable();
            CreateTheCurrentCustomerSearchTable();
            CreateTheSearchFoodTable();
            //CreateTheFoodTable();
            UpdateTheBillPath();
            UpdateTheOrderBillPath();

            UpdateTheButtonsName();


            CheckPayWithPointsPermission();

            LoadTheOrderInfoTable("",0,0);


            yourTotalPointsText.setText("0");
            pointsForThisOrderText.setText("0");
            cashMoneyText.setText("0");
            savedMoneyText.setText("0");
            PaidText.setText("0");



            Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
                LocalTime currentTime = LocalTime.now();
                DateLabel.setText(currentTime.getHour() + " : " + currentTime.getMinute() + " : " + currentTime.getSecond());
            }),
                    new KeyFrame(Duration.seconds(1))
            );
            clock.setCycleCount(Animation.INDEFINITE);
            clock.play();

            CashierNameLabel.setText(CurrentUserName);

            LoadTheDeliveryComboBoxFun();
            LoadTheFoodCategoryCBComboBoxFun();
            LoadTheCurrentCustomerTable(0);
            LoadTheCurrentWorkerTable(0);
            LoadTheKitchenCurrentItemsTableFun(0);




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
            TextFormatter<Double> textFormatter2 = new TextFormatter<>(converter, 0.0, filter);
            TextFormatter<Double> textFormatter3 = new TextFormatter<>(converter, 0.0, filter);
            TextFormatter<Double> textFormatter4 = new TextFormatter<>(converter, 0.0, filter);
            TextFormatter<Double> textFormatter5 = new TextFormatter<>(converter, 0.0, filter);
            TextFormatter<Double> textFormatter6 = new TextFormatter<>(converter, 0.0, filter);
            TextFormatter<Double> textFormatter7 = new TextFormatter<>(converter, 0.0, filter);
            TextFormatter<Double> textFormatter8 = new TextFormatter<>(converter, 0.0, filter);
            TextFormatter<Double> textFormatter9 = new TextFormatter<>(converter, 0.0, filter);
            TextFormatter<Double> textFormatter10 = new TextFormatter<>(converter, 0.0, filter);


            SpecialSCostText.setTextFormatter(textFormatter);
            SpecialMCostText.setTextFormatter(textFormatter2);
            SpecialLCostText.setTextFormatter(textFormatter3);
            SpecialXLCostText.setTextFormatter(textFormatter4);
            SpecialXXLCostText.setTextFormatter(textFormatter5);
            SpecialSDIsText.setTextFormatter(textFormatter6);
            SpecialMDisText.setTextFormatter(textFormatter7);
            SpecialLDisText.setTextFormatter(textFormatter8);
            SpecialXLDisText.setTextFormatter(textFormatter9);
            SpecialXXLDisText.setTextFormatter(textFormatter10);


            TextFormatter<Double> textFormatter11 = new TextFormatter<>(converter, 0.0, filter);
            TextFormatter<Double> textFormatter12 = new TextFormatter<>(converter, 0.0, filter);

            KitchenItemCostText.setTextFormatter(textFormatter11);
            KitchenItemQuantityText.setTextFormatter(textFormatter12);
        });

    }



    // ToDO Start Of The Customer Tab *************************


    // ToDo >>>>>> Vars


    // ToDo >>> For ( Add New Customer ) Sub Tab

    @FXML
    private JFXTextField CustomerNameText;

    @FXML
    private JFXTextField CustomerPhoneText;

    @FXML
    private JFXTextArea CustomerAddressText;

    @FXML
    private JFXTextArea CustomerNoteText;

    @FXML
    private JFXButton AddNewCustomerButton;

    @FXML
    private JFXButton ClearTheFormButton;


    // ToDo >>> For ( Manage Current Customer ) Sub Tab

    @FXML
    private TableView<ObservableList> CurrentCustomersTable;

    private ObservableList<ObservableList> dataForCurrentCustomersTable ;


    @FXML
    private TableColumn NameColumn;

    @FXML
    private TableColumn PhoneColumn;

    @FXML
    private TableColumn AddressColumn;

    @FXML
    private TableColumn NoteColumn;

    @FXML
    private TableColumn CashColumn;

    @FXML
    private TableColumn RateColumn;




    @FXML
    private JFXButton ShowAndUpdateCustomerButton;

    @FXML
    private JFXButton RemoveCustomerButton;

    @FXML
    private JFXButton ShowCustomersByRateButton;



    // ToDo >>> For ( Search For Customer ) Sub Tab

    @FXML
    private TableView<ObservableList> SearchInCustomerTable;

    private ObservableList<ObservableList> dataForSearchInCustomerTable ;

    @FXML
    private TableColumn NameSearchColumn;

    @FXML
    private TableColumn PhoneSearchColumn;

    @FXML
    private TableColumn AddressSearchColumn;

    @FXML
    private TableColumn NoteSearchColumn;

    @FXML
    private TableColumn CashSearchColumn;

    @FXML
    private TableColumn RateSearchColumn;

    @FXML
    private JFXTextField NameSearchText;

    @FXML
    private JFXTextField PhoneSearchText;

    @FXML
    private JFXButton ShowAndUpdateCustomerSearchButton;

    @FXML
    private JFXButton RemoveCustomerSearchButton;

    @FXML
    private JFXButton ShowByRateCustomerSearchButton;





    // ToDo >>>>>> Functions


    // ToDo >>> Functions For ( Add New Customer ) Sub Tab


    public void AddNewCustomerFun() {

        // Add new Customer
        try {

            OpenConnection();

            Statement CheckTheNewCustomer =  connection.createStatement();
            String Check = "SELECT phone_number FROM customers_info WHERE phone_number = '"+CustomerPhoneText.getText().trim()+"'";
            ResultSet resultSet = CheckTheNewCustomer.executeQuery(Check);

            if(resultSet.next()){
                // ToDo >>> Show That This Number Is Already Used , So Try Another Number .

                try {
                    AlertStage.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

                FXMLLoader loader = new FXMLLoader(Main.class.getResource("AlertAboutPhoneNumber.fxml"));
                Parent mainCallWindowFXML = loader.load();
                AlertStage.setTitle("Alert !!!");
                AlertStage.setScene(new Scene(mainCallWindowFXML,500 ,400));
                AlertStage.show();

            }
            else {

                if(!CustomerNameText.getText().trim().isEmpty()&&!CustomerAddressText.getText().trim().isEmpty()&&!CustomerPhoneText.getText().trim().isEmpty()) {
                    Statement AddNewCustomer = connection.createStatement();
                    String Customer = "INSERT INTO customers_info (customer_name, phone_number, address, note) VALUES " +
                            "('" + CustomerNameText.getText().trim()
                            + "','" + CustomerPhoneText.getText().trim()
                            + "','" + CustomerAddressText.getText().trim()
                            + "','" + CustomerNoteText.getText().trim() + "')";
                    AddNewCustomer.executeUpdate(Customer);


                    // Clear The Add Customer Form

                    ClearTheFormFun();

                    // update the customers table

                    LoadTheCurrentCustomerTable(0);

                }
            }


            CloseConnection();


        }
        catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }

    public void ClearTheFormFun() {

        // Clear The Add Customer Form

        CustomerNameText.setText("");
        CustomerPhoneText.setText("");
        CustomerAddressText.setText("");
        CustomerNoteText.setText("");

    }


    // ToDo >>> Functions For ( Manage Current Customer ) Sub Tab


    public void CreateTheCurrentCustomerTable(){

        CurrentCustomersTable.getColumns().clear();

        NameColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(0).toString()));

        PhoneColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(1).toString()));

        AddressColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(2).toString()));

        NoteColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(3).toString()));

        CashColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(4).toString()));

        RateColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(5).toString()));


        CurrentCustomersTable.getColumns().add(NameColumn);
        CurrentCustomersTable.getColumns().add(PhoneColumn);
        CurrentCustomersTable.getColumns().add(AddressColumn);
        CurrentCustomersTable.getColumns().add(NoteColumn);
        CurrentCustomersTable.getColumns().add(CashColumn);
        CurrentCustomersTable.getColumns().add(RateColumn);


    }

    public void LoadTheCurrentCustomerTable(int mode){

        // mode 0 for the normal arrange
        // mode 1 for arrange customers by rate

        try {
            OpenConnection();

            CurrentCustomersTable.setItems(null);

            dataForCurrentCustomersTable = FXCollections.observableArrayList();

            Statement GetAllCustomers = connection.createStatement();
            String Customers ;
            if(mode==0) {

                Customers = "SELECT * FROM customers_info";

            }
            else {

                Customers = "SELECT * FROM customers_info order by customer_points DESC ";
            }
            ResultSet resultSet = GetAllCustomers.executeQuery(Customers);






            while (resultSet.next()){

                ObservableList<String> row = FXCollections.observableArrayList();

                row.add(resultSet.getString("customer_name"));
                row.add(resultSet.getString("phone_number"));
                row.add(resultSet.getString("address"));
                row.add(resultSet.getString("note"));
                row.add(resultSet.getString("customer_cash"));
                row.add(resultSet.getString("customer_points"));


                dataForCurrentCustomersTable.add(row);


            }

            CurrentCustomersTable.setItems(dataForCurrentCustomersTable);




            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }


    public void ShowAndUpdateTheSelectedCustomerFun(){

        try {

            SelectedCustomerFromCustomerTable = CurrentCustomersTable.getSelectionModel().getSelectedItems().get(0).toString().split(",")[1].substring(1);

            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowTheSelectedCustomer.fxml"));
            loader.load();
            ShowTheSelectedCustomer controller = loader.getController();
            Parent popup = loader.getRoot();

            // Give popup a callback method
            controller.setup(
                    (value)->{
                        LoadTheCurrentCustomerTable(0);
                    }
            );


            try {
                SelectedCustomerStage.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            SelectedCustomerStage.setTitle("UpDate The Customer !!!");
            SelectedCustomerStage.setScene(new Scene(popup,800 ,600));
            SelectedCustomerStage.show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void RemoveFromTheSelectedCustomerFun(){

        try {
            OpenConnection();

            String selectedCustomer = CurrentCustomersTable.getSelectionModel().getSelectedItems().get(0).toString().split(",")[1].substring(1);

            Statement RemoveCustomer =  connection.createStatement();
            String Customer = "DELETE FROM customers_info WHERE phone_number = '"+selectedCustomer+"'";
            RemoveCustomer.executeUpdate(Customer);

            LoadTheCurrentCustomerTable(0);

            CloseConnection();

        }
        catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }


    public void ReArrangeTheCustomersByRateFun(){
        LoadTheCurrentCustomerTable(1);
    }


    // ToDo >>> Functions For ( Search For Customer ) Sub Tab


    public void CreateTheCurrentCustomerSearchTable(){

        SearchInCustomerTable.getColumns().clear();

        NameSearchColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(0).toString()));

        PhoneSearchColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(1).toString()));

        AddressSearchColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(2).toString()));

        NoteSearchColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(3).toString()));

        CashSearchColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(4).toString()));

        RateSearchColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(5).toString()));


        SearchInCustomerTable.getColumns().add(NameSearchColumn);
        SearchInCustomerTable.getColumns().add(PhoneSearchColumn);
        SearchInCustomerTable.getColumns().add(AddressSearchColumn);
        SearchInCustomerTable.getColumns().add(NoteSearchColumn);
        SearchInCustomerTable.getColumns().add(CashSearchColumn);
        SearchInCustomerTable.getColumns().add(RateSearchColumn);


    }

    public void LoadTheCurrentCustomerSearchTable(int mode ,int kind , String text){

        // mode 0 for the normal arrange
        // mode 1 for arrange customers by rate
        // kind 0 for phone , 1 for name .

        try {
            OpenConnection();

            SearchInCustomerTable.setItems(null);

            dataForSearchInCustomerTable = FXCollections.observableArrayList();


            Statement GetAllCustomers = connection.createStatement();
            String Customers ;
            if(mode==0) {

                if(kind==0){
                    Customers = "SELECT * FROM customers_info WHERE phone_number LIKE '%"+text+"%'";
                }else {
                    Customers = "SELECT * FROM customers_info WHERE customer_name LIKE '%"+text+"%'";
                }



            }
            else {

                if(kind==0){
                    Customers = "SELECT * FROM customers_info WHERE phone_number LIKE '%"+text+"%' order by customer_points DESC ";
                }else {
                    Customers = "SELECT * FROM customers_info WHERE customer_name LIKE '%"+text+"%' order by customer_points DESC ";
                }


            }
            ResultSet resultSet = GetAllCustomers.executeQuery(Customers);






            while (resultSet.next()){

                ObservableList<String> row = FXCollections.observableArrayList();

                row.add(resultSet.getString("customer_name"));
                row.add(resultSet.getString("phone_number"));
                row.add(resultSet.getString("address"));
                row.add(resultSet.getString("note"));
                row.add(resultSet.getString("customer_cash"));
                row.add(resultSet.getString("customer_points"));


                dataForSearchInCustomerTable.add(row);


            }

            SearchInCustomerTable.setItems(dataForSearchInCustomerTable);




            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }


    public void ShowAndUpdateTheSelectedCustomerSearchFun(){

        try {

            SelectedCustomerFromCustomerTable = SearchInCustomerTable.getSelectionModel().getSelectedItems().get(0).toString().split(",")[1].substring(1);

            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowTheSelectedCustomer.fxml"));
            loader.load();
            ShowTheSelectedCustomer controller = loader.getController();
            Parent popup = loader.getRoot();

            // Give popup a callback method
            controller.setup(
                    (value)->{
                        LoadTheCurrentCustomerTable(0);
                    }
            );

            try {
                SelectedCustomerStage.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            SelectedCustomerStage.setTitle("UpDate The Customer !!!");
            SelectedCustomerStage.setScene(new Scene(popup,800 ,600));
            SelectedCustomerStage.show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void RemoveFromTheSelectedCustomerSearchFun(){

        try {
            OpenConnection();

            String selectedCustomer = SearchInCustomerTable.getSelectionModel().getSelectedItems().get(0).toString().split(",")[1].substring(1);

            Statement RemoveCustomer =  connection.createStatement();
            String Customer = "DELETE FROM customers_info WHERE phone_number = '"+selectedCustomer+"'";
            RemoveCustomer.executeUpdate(Customer);


            if(PhoneSearchText.getText().trim().isEmpty()&&NameSearchText.getText().trim().isEmpty()){

                SearchInCustomerTable.setItems(null);

            }else if(!PhoneSearchText.getText().trim().isEmpty()&&NameSearchText.getText().trim().isEmpty()){

                LoadTheCurrentCustomerSearchTable(1,0,PhoneSearchText.getText().trim());

            }else if(PhoneSearchText.getText().trim().isEmpty()&&!NameSearchText.getText().trim().isEmpty()){

                LoadTheCurrentCustomerSearchTable(1,1,NameSearchText.getText().trim());

            }

            LoadTheCurrentCustomerTable(0);

            CloseConnection();

        }
        catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }


    public void ReArrangeTheCustomersSearchByRateFun(){

        if(PhoneSearchText.getText().trim().isEmpty()&&NameSearchText.getText().trim().isEmpty()){

            SearchInCustomerTable.setItems(null);

        }else if(!PhoneSearchText.getText().trim().isEmpty()&&NameSearchText.getText().trim().isEmpty()){

            LoadTheCurrentCustomerSearchTable(1,0,PhoneSearchText.getText().trim());

        }else if(PhoneSearchText.getText().trim().isEmpty()&&!NameSearchText.getText().trim().isEmpty()){

            LoadTheCurrentCustomerSearchTable(1,1,NameSearchText.getText().trim());

        }

    }


    public void CustomerSearchByPhone(){

        if(!PhoneSearchText.getText().trim().isEmpty()){
            NameSearchText.setText("");
            LoadTheCurrentCustomerSearchTable(0,0,PhoneSearchText.getText().trim());
        }else {
            SearchInCustomerTable.setItems(null);
        }
    }


    public void CustomerSearchByName(){
        if(!NameSearchText.getText().trim().isEmpty()){
            PhoneSearchText.setText("");
            LoadTheCurrentCustomerSearchTable(0,1,NameSearchText.getText().trim());
        }else {
            SearchInCustomerTable.setItems(null);
        }

    }




    // ToDO End Of The Customer Tab *************************


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // ToDO Start Of The Workers Tab *************************




    // ToDo >>>>>> Vars


    // ToDo >>> For ( Add New Worker ) Sub Tab


    @FXML
    private JFXTextField WorkerNameText;

    @FXML
    private JFXTextField WorkerNationalIDText;

    @FXML
    private JFXTextField WorkerAgeText;

    @FXML
    private JFXTextField WorkerPhoneText;

    @FXML
    private JFXTextField WorkerAddressText;

    @FXML
    private JFXRadioButton WorkerManagerRB;

    @FXML
    private JFXRadioButton WorkerCashierRB;

    @FXML
    private JFXRadioButton WorkerChefRB;

    @FXML
    private JFXRadioButton WorkerDeliveryRB;

    @FXML
    private JFXTextField WorkerSalaryText;

    @FXML
    private JFXTextField WorkerUserNameText;

    @FXML
    private JFXTextField WorkerPasswordText;

    @FXML
    private JFXButton AddNewWorkerButton;

    @FXML
    private JFXButton ClearAddNewWorkerButton;


    // ToDo >>> For ( Manage Current Workers ) Sub Tab

    @FXML
    private TableView<ObservableList> CurrentWorkersTable;

    private ObservableList<ObservableList> dataForCurrentWorkersTable ;

    @FXML
    private TableColumn WorkerNameColumn;

    @FXML
    private TableColumn WorkerNationalIDColumn;

    @FXML
    private TableColumn WorkerAgeColumn;

    @FXML
    private TableColumn WorkerPhoneColumn;

    @FXML
    private TableColumn WorkerAddressColumn;

    @FXML
    private TableColumn WorkerTypeColumn;

    @FXML
    private TableColumn WorkerSalaryColumn;

    @FXML
    private TableColumn WorkerRateColumn;

    @FXML
    private TableColumn WorkerUserNameColumn;

    @FXML
    private TableColumn WorkerPasswordColumn;

    @FXML
    private JFXButton ShowSelectedWorkerButton;

    @FXML
    private JFXButton RemoverTheSelectedButton;

    @FXML
    private JFXButton ShowWorkersByRateButton;


    // ToDo >>> For ( Search For Workers ) Sub Tab



    @FXML
    private TableView<ObservableList> SearchForWorkersTable;

    private ObservableList<ObservableList> dataForSearchForWorkersTable ;

    @FXML
    private TableColumn WorkerNameSearchColumn;

    @FXML
    private TableColumn WorkerNationalIDSearchColumn;

    @FXML
    private TableColumn WorkerAgeSearchColumn;

    @FXML
    private TableColumn WorkerPhoneSearchColumn;

    @FXML
    private TableColumn WorkerAddressSearchColumn;

    @FXML
    private TableColumn WorkerSalarySearchColumn;

    @FXML
    private TableColumn WorkerRateSearchColumn;

    @FXML
    private TableColumn WorkerUserNameSearchColumn;

    @FXML
    private TableColumn WorkerPasswordSearchColumn;

    @FXML
    private TableColumn WorkerTypeSearchColumn;

    @FXML
    private JFXTextField SearchForWorkersByNameText;

    @FXML
    private JFXTextField SearchForWorkersByPhoneText;

    @FXML
    private JFXButton ShowTheSelectedWorkerFromWorkerSearchButton;

    @FXML
    private JFXButton RemoveTheSelectedWorkerFromWorkerSearchButton;

    @FXML
    private JFXButton ShowByRateFromWorkerSearchButton;











    // ToDo >>>>>> Functions


    // ToDo >>> Functions For ( Add New Worker ) Sub Tab

    public void AddNewWorkerFun(){

        try {

            OpenConnection();


            Statement CheckTheNewWorker =  connection.createStatement();
            String Check = "SELECT user_name FROM users_info WHERE user_name = '"+WorkerUserNameText.getText().trim()+"'";
            ResultSet resultSet = CheckTheNewWorker.executeQuery(Check);

            if(resultSet.next()){
                // ToDo >>> Show That This User Is Already Exist , So Try Another User Name .

                try {
                    AlertAboutUserNameStage.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

                FXMLLoader loader = new FXMLLoader(Main.class.getResource("AlertAboutUserName.fxml"));
                Parent mainCallWindowFXML = loader.load();
                AlertAboutUserNameStage.setTitle("Alert !!!");
                AlertAboutUserNameStage.setScene(new Scene(mainCallWindowFXML,500 ,400));
                AlertAboutUserNameStage.show();

            }
            else {

                if(!WorkerNameText.getText().trim().isEmpty()
                        &&!WorkerNationalIDText.getText().trim().isEmpty()
                        &&!WorkerAgeText.getText().trim().isEmpty()
                        &&!WorkerPhoneText.getText().trim().isEmpty()
                        &&!WorkerAddressText.getText().trim().isEmpty()
                        &&!WorkerSalaryText.getText().trim().isEmpty()
                        &&!WorkerUserNameText.getText().trim().isEmpty()
                        &&!WorkerPasswordText.getText().trim().isEmpty()
                ) {

                    // load the worker type .

                    String WorkerTypeFlag ;

                    if(WorkerManagerRB.isSelected()){

                        WorkerTypeFlag = "1" ;

                    }else if(WorkerCashierRB.isSelected()){

                        WorkerTypeFlag = "2" ;

                    }else if(WorkerChefRB.isSelected()) {

                        WorkerTypeFlag = "3" ;

                    }else {

                        WorkerTypeFlag = "4" ;

                    }


                    Statement AddNewWorker = connection.createStatement();
                    String Worker = "INSERT INTO users_info (user_name, password, worker_name, national_id, age, phone_number, address, worker_type, salary) VALUES (" +
                            "'"+WorkerUserNameText.getText().trim()+
                            "','"+WorkerPasswordText.getText().trim()+
                            "','"+WorkerNameText.getText().trim()+
                            "','"+WorkerNationalIDText.getText().trim()+
                            "','"+WorkerAgeText.getText().trim()+
                            "','"+WorkerPhoneText.getText().trim()+
                            "','"+WorkerAddressText.getText().trim()+
                            "','"+WorkerTypeFlag+
                            "','"+WorkerSalaryText.getText().trim()+"')";
                    AddNewWorker.executeUpdate(Worker);


                    if(WorkerDeliveryRB.isSelected()){
                        LoadTheDeliveryComboBoxFun();
                    }

                    // Clear The Add Worker Form

                    ClearAddNewWorkerFormFun();

                    // update the Worker table

                    LoadTheCurrentWorkerTable(0);

                }
            }


            CloseConnection();

        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }




    }

    public void ClearAddNewWorkerFormFun(){

        WorkerNameText.setText("");
        WorkerNationalIDText.setText("");
        WorkerAgeText.setText("");
        WorkerPhoneText.setText("");
        WorkerAddressText.setText("");
        WorkerSalaryText.setText("");
        WorkerUserNameText.setText("");
        WorkerPasswordText.setText("");

        WorkerManagerRB.setSelected(false);
        WorkerCashierRB.setSelected(false);
        WorkerChefRB.setSelected(false);
        WorkerDeliveryRB.setSelected(false);

    }


    // ToDo >>> Functions For ( Manage Current Workers ) Sub Tab


    public void CreateTheCurrentWorkerTable(){

        CurrentWorkersTable.getColumns().clear();

        WorkerNameColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(0).toString()));

        WorkerNationalIDColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(1).toString()));

        WorkerAgeColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(2).toString()));

        WorkerPhoneColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(3).toString()));

        WorkerAddressColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(4).toString()));

        WorkerTypeColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(5).toString()));

        WorkerSalaryColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(6).toString()));

        WorkerRateColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(7).toString()));

        WorkerUserNameColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(8).toString()));

        WorkerPasswordColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(9).toString()));


        CurrentWorkersTable.getColumns().add(WorkerNameColumn);
        CurrentWorkersTable.getColumns().add(WorkerNationalIDColumn);
        CurrentWorkersTable.getColumns().add(WorkerAgeColumn);
        CurrentWorkersTable.getColumns().add(WorkerPhoneColumn);
        CurrentWorkersTable.getColumns().add(WorkerAddressColumn);
        CurrentWorkersTable.getColumns().add(WorkerTypeColumn);
        CurrentWorkersTable.getColumns().add(WorkerSalaryColumn);
        CurrentWorkersTable.getColumns().add(WorkerRateColumn);
        CurrentWorkersTable.getColumns().add(WorkerUserNameColumn);
        CurrentWorkersTable.getColumns().add(WorkerPasswordColumn);

    }


    public void LoadTheCurrentWorkerTable(int mode){

        // mode 0 for the normal arrange
        // mode 1 for arrange Workers by rate

        try {
            OpenConnection();

            CurrentWorkersTable.setItems(null);

            dataForCurrentWorkersTable = FXCollections.observableArrayList();

            Statement GetAllWorkers = connection.createStatement();
            String Workers ;
            if(mode==0) {

                Workers = "SELECT * FROM users_info";

            }
            else {

                Workers = "SELECT * FROM users_info order by rate DESC ";

            }
            ResultSet resultSet = GetAllWorkers.executeQuery(Workers);



            while (resultSet.next()){

                ObservableList<String> row = FXCollections.observableArrayList();

                row.add(resultSet.getString("worker_name"));
                row.add(resultSet.getString("national_id"));
                row.add(resultSet.getString("age"));
                row.add(resultSet.getString("phone_number"));
                row.add(resultSet.getString("address"));

                switch (resultSet.getString("worker_type")) {
                    case "1":

                        row.add("Manager");

                        break;
                    case "2":

                        row.add("Cashier");

                        break;
                    case "3":

                        row.add("Chef");

                        break;

                    case "4":

                        row.add("Delivery");

                        break;
                }
                row.add(resultSet.getString("salary"));
                row.add(resultSet.getString("rate"));
                row.add(resultSet.getString("user_name"));
                row.add(resultSet.getString("password"));

                dataForCurrentWorkersTable.add(row);


            }

            CurrentWorkersTable.setItems(dataForCurrentWorkersTable);

            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }


    public void ShowAndUpdateTheSelectedWorkerFun(){

        try {

            SelectedWorkerFromWorkerTable = CurrentWorkersTable.getSelectionModel().getSelectedItems().get(0).toString().split(",")[8].substring(1);

            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowTheSelectedWorker.fxml"));
            loader.load();
            ShowTheSelectedWorker controller = loader.getController();
            Parent popup = loader.getRoot();

            // Give popup a callback method
            controller.setup(
                    (value)->{
                        LoadTheCurrentWorkerTable(0);
                    }
            );


            try {
                SelectedWorkerStage.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            SelectedWorkerStage.setTitle("UpDate The Worker !!!");
            SelectedWorkerStage.setScene(new Scene(popup,800 ,600));
            SelectedWorkerStage.show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void RemoveFromTheSelectedWorkerFun(){

        try {
            OpenConnection();

            String selectedWorker = CurrentWorkersTable.getSelectionModel().getSelectedItems().get(0).toString().split(",")[8].substring(1);

            if(!selectedWorker.equals("Super Admin")){
                Statement RemoveWorker =  connection.createStatement();
                String Worker = "DELETE FROM users_info WHERE user_name = '"+selectedWorker+"'";
                RemoveWorker.executeUpdate(Worker);

                LoadTheCurrentWorkerTable(0);
                LoadTheDeliveryComboBoxFun();
            }

            CloseConnection();

        }
        catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }


    public void ReArrangeTheWorkersByRateFun(){
        LoadTheCurrentWorkerTable(1);
    }




    // ToDo >>> Functions For ( Search For Workers ) Sub Tab

    public void CreateTheCurrentWorkerSearchTable(){

        SearchForWorkersTable.getColumns().clear();

        WorkerNameSearchColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(0).toString()));

        WorkerNationalIDSearchColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(1).toString()));

        WorkerAgeSearchColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(2).toString()));

        WorkerPhoneSearchColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(3).toString()));

        WorkerAddressSearchColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(4).toString()));

        WorkerSalarySearchColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(5).toString()));

        WorkerRateSearchColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(6).toString()));

        WorkerUserNameSearchColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(7).toString()));

        WorkerPasswordSearchColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(8).toString()));

        WorkerTypeSearchColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(9).toString()));


        SearchForWorkersTable.getColumns().add(WorkerNameSearchColumn);
        SearchForWorkersTable.getColumns().add(WorkerNationalIDSearchColumn);
        SearchForWorkersTable.getColumns().add(WorkerAgeSearchColumn);
        SearchForWorkersTable.getColumns().add(WorkerPhoneSearchColumn);
        SearchForWorkersTable.getColumns().add(WorkerAddressSearchColumn);
        SearchForWorkersTable.getColumns().add(WorkerSalarySearchColumn);
        SearchForWorkersTable.getColumns().add(WorkerRateSearchColumn);
        SearchForWorkersTable.getColumns().add(WorkerUserNameSearchColumn);
        SearchForWorkersTable.getColumns().add(WorkerPasswordSearchColumn);
        SearchForWorkersTable.getColumns().add(WorkerTypeSearchColumn);

    }

    public void LoadTheCurrentWorkerSearchTable(int mode ,int kind , String text){

        // mode 0 for the normal arrange
        // mode 1 for arrange workers by rate
        // kind 0 for phone , 1 for name .

        try {
            OpenConnection();

            SearchForWorkersTable.setItems(null);

            dataForSearchForWorkersTable = FXCollections.observableArrayList();


            Statement GetAllCustomers = connection.createStatement();
            String Customers ;
            if(mode==0) {

                if(kind==0){
                    Customers = "SELECT * FROM users_info WHERE phone_number LIKE '%"+text+"%'";
                }else {
                    Customers = "SELECT * FROM users_info WHERE worker_name LIKE '%"+text+"%'";
                }



            }
            else {

                if(kind==0){
                    Customers = "SELECT * FROM users_info WHERE phone_number LIKE '%"+text+"%' order by rate DESC ";
                }else {
                    Customers = "SELECT * FROM users_info WHERE worker_name LIKE '%"+text+"%' order by rate DESC ";
                }


            }
            ResultSet resultSet = GetAllCustomers.executeQuery(Customers);



            while (resultSet.next()){

                ObservableList<String> row = FXCollections.observableArrayList();

                row.add(resultSet.getString("worker_name"));
                row.add(resultSet.getString("national_id"));
                row.add(resultSet.getString("age"));
                row.add(resultSet.getString("phone_number"));
                row.add(resultSet.getString("address"));
                row.add(resultSet.getString("salary"));
                row.add(resultSet.getString("rate"));
                row.add(resultSet.getString("user_name"));
                row.add(resultSet.getString("password"));

                switch (resultSet.getString("worker_type")) {

                    case "1":

                        row.add("Manager");



                        break;
                    case "2":

                        row.add("Cashier");


                        break;
                    case "3":

                        row.add("Chef");


                        break;
                    case "4":

                        row.add("Delivery");

                        break;
                }

                dataForSearchForWorkersTable.add(row);


            }

            SearchForWorkersTable.setItems(dataForSearchForWorkersTable);




            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }


    public void ShowAndUpdateTheSelectedWorkerSearchFun(){

        try {

            SelectedWorkerFromWorkerTable = SearchForWorkersTable.getSelectionModel().getSelectedItems().get(0).toString().split(",")[8].substring(1);

            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowTheSelectedWorker.fxml"));
            loader.load();
            ShowTheSelectedWorker controller = loader.getController();
            Parent popup = loader.getRoot();

            // Give popup a callback method
            controller.setup(
                    (value)->{
                        LoadTheCurrentWorkerTable(0);

                        if(SearchForWorkersByPhoneText.getText().trim().isEmpty()&&SearchForWorkersByNameText.getText().trim().isEmpty()){

                            SearchForWorkersTable.setItems(null);

                        }else if(!SearchForWorkersByPhoneText.getText().trim().isEmpty()&&SearchForWorkersByNameText.getText().trim().isEmpty()){

                            LoadTheCurrentWorkerSearchTable(0,0,SearchForWorkersByPhoneText.getText().trim());

                        }else if(SearchForWorkersByPhoneText.getText().trim().isEmpty()&&!SearchForWorkersByNameText.getText().trim().isEmpty()){

                            LoadTheCurrentWorkerSearchTable(0,1,SearchForWorkersByNameText.getText().trim());

                        }
                    }
            );

            try {
                SelectedWorkerStage.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            SelectedWorkerStage.setTitle("Update The Worker !!!");
            SelectedWorkerStage.setScene(new Scene(popup,800 ,600));
            SelectedWorkerStage.show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void RemoveFromTheSelectedWorkersSearchFun(){

        try {
            OpenConnection();


            String selectedWorker = SearchForWorkersTable.getSelectionModel().getSelectedItems().get(0).toString().split(",")[8].substring(1);


            if(!selectedWorker.equals("Super Admin")){

                Statement RemoveWorker =  connection.createStatement();
                String Worker = "DELETE FROM users_info WHERE user_name = '"+selectedWorker+"'";
                RemoveWorker.executeUpdate(Worker);


                if(SearchForWorkersByPhoneText.getText().trim().isEmpty()&&SearchForWorkersByNameText.getText().trim().isEmpty()){

                    SearchForWorkersTable.setItems(null);

                }else if(!SearchForWorkersByPhoneText.getText().trim().isEmpty()&&SearchForWorkersByNameText.getText().trim().isEmpty()){

                    LoadTheCurrentWorkerSearchTable(0,0,SearchForWorkersByPhoneText.getText().trim());

                }else if(SearchForWorkersByPhoneText.getText().trim().isEmpty()&&!SearchForWorkersByNameText.getText().trim().isEmpty()){

                    LoadTheCurrentWorkerSearchTable(0,1,SearchForWorkersByNameText.getText().trim());

                }

                LoadTheDeliveryComboBoxFun();

                LoadTheCurrentWorkerTable(0);

            }


            CloseConnection();

        }
        catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }


    public void ReArrangeTheWorkersSearchByRateFun(){

        if(SearchForWorkersByPhoneText.getText().trim().isEmpty()&&SearchForWorkersByNameText.getText().trim().isEmpty()){

            SearchForWorkersTable.setItems(null);

        }else if(!SearchForWorkersByPhoneText.getText().trim().isEmpty()&&SearchForWorkersByNameText.getText().trim().isEmpty()){

            LoadTheCurrentWorkerSearchTable(1,0,SearchForWorkersByPhoneText.getText().trim());

        }else if(SearchForWorkersByPhoneText.getText().trim().isEmpty()&&!SearchForWorkersByNameText.getText().trim().isEmpty()){

            LoadTheCurrentWorkerSearchTable(1,1,SearchForWorkersByNameText.getText().trim());

        }

    }


    public void WorkerSearchByPhone(){

        if(!SearchForWorkersByPhoneText.getText().trim().isEmpty()){
            SearchForWorkersByNameText.setText("");
            LoadTheCurrentWorkerSearchTable(0,0,SearchForWorkersByPhoneText.getText().trim());
        }else {
            SearchForWorkersTable.setItems(null);
        }
    }


    public void WorkerSearchByName(){
        if(!SearchForWorkersByNameText.getText().trim().isEmpty()){
            SearchForWorkersByPhoneText.setText("");
            LoadTheCurrentWorkerSearchTable(0,1,SearchForWorkersByNameText.getText().trim());
        }else {
            SearchForWorkersTable.setItems(null);
        }
    }



    // ToDO End Of The Workers Tab *************************


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // ToDO Start Of The Kitchen Tab *************************




    // ToDo >>>>>> Vars


    // ToDo >>> For ( Add New Item ) Sub Tab

    @FXML
    private JFXButton KitchenAddOrUpdateNewItemToKitchenButton;

    @FXML
    private JFXButton KitchenClearAddKitchenItemFormButton;

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


    // ToDo >>> For ( Manage The Current Items ) Sub Tab



    @FXML
    private TableView<ObservableList> KitchenCurrentItemsTable;

    private ObservableList<ObservableList> dataForKitchenCurrentItemsTable ;


    @FXML
    private TableColumn KitchenColumnItemName;

    @FXML
    private TableColumn KitchenColumnItemID;

    @FXML
    private TableColumn KitchenColumnItemCost;

    @FXML
    private TableColumn KitchenColumnItemQuantity;

    @FXML
    private TableColumn KitchenColumnItemQuantityUnit;

    @FXML
    private TableColumn KitchenColumnItemSupplier;

    @FXML
    private TableColumn KitchenColumnItemSupplierPhone;

    @FXML
    private TableColumn KitchenColumnItemNote;

    @FXML
    private TableColumn KitchenColumnItemAddDate;

    @FXML
    private JFXButton KitchenShowAndUpdateItemButton;

    @FXML
    private JFXButton KitchenRemoveItemButton;

    @FXML
    private JFXButton KitchenShowByCostButton;



    // ToDo >>> For ( Search For Kitchen Items ) Sub Tab


    @FXML
    private TableView<ObservableList> KitchenSearchItemTable;

    private ObservableList<ObservableList> dataForKitchenSearchItemTable ;

    @FXML
    private TableColumn KitchenSearchItemNameColumn;

    @FXML
    private TableColumn KitchenSearchItemIDColumn;

    @FXML
    private TableColumn KitchenSearchItemCostColumn;

    @FXML
    private TableColumn KitchenSearchItemQuantityColumn;

    @FXML
    private TableColumn KitchenSearchItemQuantityUnitColumn;

    @FXML
    private TableColumn KitchenSearchItemSupplierColumn;

    @FXML
    private TableColumn KitchenSearchItemSupplierPhoneColumn;

    @FXML
    private TableColumn KitchenSearchItemNoteColumn;

    @FXML
    private TableColumn KitchenSearchItemDateColumn;

    @FXML
    private JFXTextField KitchenItemSearchText;

    @FXML
    private JFXTextField KitchenItemIDSearchText;

    @FXML
    private JFXButton KitchenSearchShowAndUpdateItemButton;

    @FXML
    private JFXButton KitchenSearchRemoveItemButton;

    @FXML
    private JFXButton KitchenSearchShowItemByCostButton;




    // ToDo >>>>>> Functions


    // ToDo >>> For ( Add New Item ) Sub Tab

    public void AddNewItemToTheKitchenFun(){

        if(!KitchenItemNameText.getText().trim().isEmpty()&&
                !KitchenItemIDText.getText().trim().isEmpty()&&
                !KitchenItemCostText.getText().trim().isEmpty()&&
                !KitchenItemQuantityText.getText().trim().isEmpty()&&
                !KitchenItemQuantityUnitText.getText().trim().isEmpty()&&
                !KitchenItemSupplierText.getText().trim().isEmpty()&&
                !KitchenItemSupplierPhoneText.getText().trim().isEmpty()&&
                Double.parseDouble(KitchenItemCostText.getText().trim())>0&&
                Double.parseDouble(KitchenItemQuantityText.getText().trim())>0){


            try {

                OpenConnection();

                Statement AddNewItemToKitchenDetails =  connection.createStatement();
                String AddItem = "INSERT INTO kitchen_details_info (item_name, item_id, cost, quantity, quantity_unit, supplier, supplier_number, note) " +
                        "VALUES ('"+KitchenItemNameText.getText().trim()+
                        "','"+KitchenItemIDText.getText().trim()+
                        "',"+KitchenItemCostText.getText().trim()+
                        ","+KitchenItemQuantityText.getText().trim()+
                        ",'"+KitchenItemQuantityUnitText.getText().trim()+
                        "','"+KitchenItemSupplierText.getText().trim()+
                        "','"+KitchenItemSupplierPhoneText.getText().trim()+
                        "','"+KitchenItemNoteText.getText().trim()+"')";
                AddNewItemToKitchenDetails.executeUpdate(AddItem);


                Statement GetItemInfo =  connection.createStatement();
                String ItemInfo = "SELECT * FROM kitchen_details_info WHERE item_id = '"+KitchenItemIDText.getText().trim()+"'";
                ResultSet resultSet = GetItemInfo.executeQuery(ItemInfo);

                double totalCost = 0 ;
                double totalQuantity = 0 ;
                double meanCostForQuantityUnit ;


                while (resultSet.next()){

                    totalCost = totalCost + Double.parseDouble(resultSet.getString("cost"));
                    totalQuantity = totalQuantity + Double.parseDouble(resultSet.getString("quantity"));

                }

                meanCostForQuantityUnit = totalCost / totalQuantity ;


                Statement CheckAboutPK =  connection.createStatement();
                String PK = "SELECT * FROM kitchen_info WHERE item_id = '"+KitchenItemIDText.getText().trim()+"'";
                ResultSet resultSet2 = CheckAboutPK.executeQuery(PK);

                if(resultSet2.next()){
                    Statement UpdateItemToKitchenInfo =  connection.createStatement();
                    String UpdateItem = "UPDATE kitchen_info SET item_name = '"+KitchenItemNameText.getText().trim()+"' ," +
                            " cost = "+meanCostForQuantityUnit+" ," +
                            " quantity = quantity + "+Double.parseDouble(KitchenItemQuantityText.getText().trim())+" ," +
                            "supplier = '"+KitchenItemSupplierText.getText().trim()+"'," +
                            "supplier_number = '"+KitchenItemSupplierPhoneText.getText().trim()+"' ," +
                            "note = '"+KitchenItemNoteText.getText().trim()+
                            "' WHERE item_id = '"+KitchenItemIDText.getText().trim()+"'";
                    UpdateItemToKitchenInfo.executeUpdate(UpdateItem);
                }else {
                    Statement AddNewItemToKitchenInfo =  connection.createStatement();
                    String AddNewItem = "INSERT INTO kitchen_info (item_name, item_id, cost, quantity, quantity_unit, supplier, supplier_number, note) " +
                            "VALUES ('"+KitchenItemNameText.getText().trim()+
                            "','"+KitchenItemIDText.getText().trim()+
                            "',"+meanCostForQuantityUnit+
                            ","+KitchenItemQuantityText.getText().trim()+
                            ",'"+KitchenItemQuantityUnitText.getText().trim()+
                            "','"+KitchenItemSupplierText.getText().trim()+
                            "','"+KitchenItemSupplierPhoneText.getText().trim()+
                            "','"+KitchenItemNoteText.getText().trim()+"')";
                    AddNewItemToKitchenInfo.executeUpdate(AddNewItem);
                }

                ClearAddNewItemToTheKitchenFormFun();

                LoadTheKitchenCurrentItemsTableFun(0);

                CloseConnection();

            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
            }

        }

    }

    public void ClearAddNewItemToTheKitchenFormFun(){
        KitchenItemNameText.setText("");
        KitchenItemIDText.setText("");
        KitchenItemCostText.setText("");
        KitchenItemQuantityText.setText("");
        KitchenItemQuantityUnitText.setText("");
        KitchenItemSupplierText.setText("");
        KitchenItemSupplierPhoneText.setText("");
        KitchenItemNoteText.setText("");
    }

    public void LoadItemInfoWhileTypingFun(){

        try {
            OpenConnection();



            Statement statement = connection.createStatement();
            String getIDs = "SELECT * FROM kitchen_info WHERE item_id = '"+KitchenItemIDText.getText().trim()+"'" ;
            ResultSet resultSet = statement.executeQuery(getIDs);

            if(resultSet.next()){
                KitchenItemNameText.setText(resultSet.getString("item_name"));
                KitchenItemIDText.setText(resultSet.getString("item_id"));
                KitchenItemQuantityUnitText.setText(resultSet.getString("quantity_unit"));
                KitchenItemSupplierText.setText(resultSet.getString("supplier"));
                KitchenItemSupplierPhoneText.setText(resultSet.getString("supplier_number"));
                KitchenItemNoteText.setText(resultSet.getString("note"));

                KitchenItemIDText.positionCaret(KitchenItemIDText.getText().length());

                KitchenItemQuantityUnitText.setEditable(false);
            }else {

                KitchenItemCostText.setText("");
                KitchenItemQuantityText.setText("");
                KitchenItemQuantityUnitText.setText("");
                KitchenItemSupplierText.setText("");
                KitchenItemSupplierPhoneText.setText("");
                KitchenItemNoteText.setText("");

                KitchenItemQuantityUnitText.setEditable(true);
            }


            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }

    // ToDo >>> For ( Manage The Current Items ) Sub Tab


    public void CreateTheKitchenCurrentItemsTable(){

        KitchenCurrentItemsTable.getColumns().clear();

        KitchenColumnItemName.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(0).toString()));

        KitchenColumnItemID.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(1).toString()));

        KitchenColumnItemCost.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(2).toString()));

        KitchenColumnItemQuantity.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(3).toString()));

        KitchenColumnItemQuantityUnit.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(4).toString()));

        KitchenColumnItemSupplier.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(5).toString()));

        KitchenColumnItemSupplierPhone.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(6).toString()));

        KitchenColumnItemNote.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(7).toString()));

        KitchenColumnItemAddDate.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(8).toString()));



        KitchenCurrentItemsTable.getColumns().add(KitchenColumnItemName);
        KitchenCurrentItemsTable.getColumns().add(KitchenColumnItemID);
        KitchenCurrentItemsTable.getColumns().add(KitchenColumnItemCost);
        KitchenCurrentItemsTable.getColumns().add(KitchenColumnItemQuantity);
        KitchenCurrentItemsTable.getColumns().add(KitchenColumnItemQuantityUnit);
        KitchenCurrentItemsTable.getColumns().add(KitchenColumnItemSupplier);
        KitchenCurrentItemsTable.getColumns().add(KitchenColumnItemSupplierPhone);
        KitchenCurrentItemsTable.getColumns().add(KitchenColumnItemNote);
        KitchenCurrentItemsTable.getColumns().add(KitchenColumnItemAddDate);


    }

    public void LoadTheKitchenCurrentItemsTableFun(int mode){


        // mode 0 for the normal arrange
        // mode 1 for arrange Items by cost

        try {
            OpenConnection();

            KitchenCurrentItemsTable.setItems(null);

            dataForKitchenCurrentItemsTable = FXCollections.observableArrayList();

            Statement GetAllItems = connection.createStatement();
            String Items ;
            if(mode==0) {

                Items = "SELECT * FROM kitchen_info";

            }
            else {

                Items = "SELECT * FROM kitchen_info order by cost DESC ";
            }
            ResultSet resultSet = GetAllItems.executeQuery(Items);






            while (resultSet.next()){

                ObservableList<String> row = FXCollections.observableArrayList();

                row.add(resultSet.getString("item_name"));
                row.add(resultSet.getString("item_id"));
                row.add(resultSet.getString("cost"));
                row.add(resultSet.getString("quantity"));
                row.add(resultSet.getString("quantity_unit"));
                row.add(resultSet.getString("supplier"));
                row.add(resultSet.getString("supplier_number"));
                row.add(resultSet.getString("note"));
                row.add(resultSet.getString("last_time_updated"));

                dataForKitchenCurrentItemsTable.add(row);


            }

            KitchenCurrentItemsTable.setItems(dataForKitchenCurrentItemsTable);




            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }



    }

    public void KitchenItemsShowAndUpdateFun(){

        try {

            SelectedItemFromKitchenTable = KitchenCurrentItemsTable.getSelectionModel().getSelectedItems().get(0).toString().split(",")[1].substring(1);

            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowTheSelectedKitchenItem.fxml"));
            loader.load();
            ShowTheSelectedKitchenItem controller = loader.getController();
            Parent popup = loader.getRoot();

            // Give popup a callback method
            controller.setup(
                    (value)->{
                        LoadTheKitchenCurrentItemsTableFun(0);
                    }
            );

            try {
                SelectedKitchenItemStage.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            SelectedKitchenItemStage.setTitle("UpDate The Item !!!");
            SelectedKitchenItemStage.setScene(new Scene(popup,800 ,600));
            SelectedKitchenItemStage.show();

        }catch (Exception e){
            e.printStackTrace();
        }



    }

    public void KitchenItemsDetailsFun(){

        try {

            SelectedItemFromKitchenTable = KitchenCurrentItemsTable.getSelectionModel().getSelectedItems().get(0).toString().split(",")[1].substring(1);

            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowTheSelectedKitchenItemDetails.fxml"));
            loader.load();
            ShowTheSelectedKitchenItemDetails controller = loader.getController();
            Parent popup = loader.getRoot();

            // Give popup a callback method
            controller.setup(
                    (value)->{
                         // Do Nothing ...
                    }
            );

            try {
                SelectedKitchenItemDetailsStage.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            SelectedKitchenItemDetailsStage.setTitle("Item Details !!!");
            SelectedKitchenItemDetailsStage.setScene(new Scene(popup,800 ,600));
            SelectedKitchenItemDetailsStage.show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void KitchenItemsConsumingFun(){

        try {

            SelectedItemFromKitchenTable = KitchenCurrentItemsTable.getSelectionModel().getSelectedItems().get(0).toString().split(",")[1].substring(1);

            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowKitchenItemConsumingManagement.fxml"));
            loader.load();
            ShowKitchenItemConsumingManagement controller = loader.getController();
            Parent popup = loader.getRoot();

            // Give popup a callback method
            controller.setup(
                    (value)->{
                        LoadTheKitchenCurrentItemsTableFun(0);
                    }
            );

            try {
                SelectedKitchenItemConsumingStage.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            SelectedKitchenItemConsumingStage.setTitle("Consuming Management !!!");
            SelectedKitchenItemConsumingStage.setScene(new Scene(popup,800 ,600));
            SelectedKitchenItemConsumingStage.show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void KitchenRemoveItemFun(){

        try {
            OpenConnection();

            SelectedItemFromKitchenTable = KitchenCurrentItemsTable.getSelectionModel().getSelectedItems().get(0).toString().split(",")[1].substring(1);

            Statement RemoveItem =  connection.createStatement();
            String Item = "DELETE FROM kitchen_info WHERE item_id = '"+SelectedItemFromKitchenTable+"'";
            RemoveItem.executeUpdate(Item);

            Statement RemoveItemFromInfo =  connection.createStatement();
            String ItemInfo = "DELETE FROM kitchen_details_info WHERE item_id = '"+SelectedItemFromKitchenTable+"'";
            RemoveItemFromInfo.executeUpdate(ItemInfo);

            Statement RemoveItemFromConsuming =  connection.createStatement();
            String ItemConsumingInfo = "DELETE FROM kitchen_consuming_info WHERE item_id = '"+SelectedItemFromKitchenTable+"'";
            RemoveItemFromConsuming.executeUpdate(ItemConsumingInfo);

            LoadTheKitchenCurrentItemsTableFun(0);

            SelectedItemFromKitchenTable = "";
            CloseConnection();

        }
        catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }

    public void KitchenItemsOrderByCostFun(){
        LoadTheKitchenCurrentItemsTableFun(1);
    }

    // ToDo >>> For ( Search For Kitchen Items ) Sub Tab


    public void CreateTheCurrentKitchenItemsSearchTable(){

        KitchenSearchItemTable.getColumns().clear();

        KitchenSearchItemNameColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(0).toString()));

        KitchenSearchItemIDColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(1).toString()));

        KitchenSearchItemCostColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(2).toString()));

        KitchenSearchItemQuantityColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(3).toString()));

        KitchenSearchItemQuantityUnitColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(4).toString()));

        KitchenSearchItemSupplierColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(5).toString()));

        KitchenSearchItemSupplierPhoneColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(6).toString()));

        KitchenSearchItemNoteColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(7).toString()));

        KitchenSearchItemDateColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(8).toString()));



        KitchenSearchItemTable.getColumns().add(KitchenSearchItemNameColumn);
        KitchenSearchItemTable.getColumns().add(KitchenSearchItemIDColumn);
        KitchenSearchItemTable.getColumns().add(KitchenSearchItemCostColumn);
        KitchenSearchItemTable.getColumns().add(KitchenSearchItemQuantityColumn);
        KitchenSearchItemTable.getColumns().add(KitchenSearchItemQuantityUnitColumn);
        KitchenSearchItemTable.getColumns().add(KitchenSearchItemSupplierColumn);
        KitchenSearchItemTable.getColumns().add(KitchenSearchItemSupplierPhoneColumn);
        KitchenSearchItemTable.getColumns().add(KitchenSearchItemNoteColumn);
        KitchenSearchItemTable.getColumns().add(KitchenSearchItemDateColumn);


    }


    public void LoadTheCurrentKitchenItemsSearchTable(int mode ,int kind , String text){

        // mode 0 for the normal arrange
        // mode 1 for arrange Items by rate
        // kind 0 for Item Name  , 1 for Item ID .

        try {
            OpenConnection();


            KitchenSearchItemTable.setItems(null);

            dataForKitchenSearchItemTable = FXCollections.observableArrayList();


            Statement GetAllItems = connection.createStatement();
            String Items ;
            if(mode==0) {

                if(kind==0){
                    Items = "SELECT * FROM kitchen_info WHERE item_name LIKE '%"+text+"%'";
                }else {
                    Items = "SELECT * FROM kitchen_info WHERE item_id LIKE '%"+text+"%'";
                }



            }
            else {

                if(kind==0){
                    Items = "SELECT * FROM kitchen_info WHERE item_name LIKE '%"+text+"%' order by cost DESC ";
                }else {
                    Items = "SELECT * FROM kitchen_info WHERE item_id LIKE '%"+text+"%' order by cost DESC ";
                }


            }
            ResultSet resultSet = GetAllItems.executeQuery(Items);



            while (resultSet.next()){

                ObservableList<String> row = FXCollections.observableArrayList();

                row.add(resultSet.getString("item_name"));
                row.add(resultSet.getString("item_id"));
                row.add(resultSet.getString("cost"));
                row.add(resultSet.getString("quantity"));
                row.add(resultSet.getString("quantity_unit"));
                row.add(resultSet.getString("supplier"));
                row.add(resultSet.getString("supplier_number"));
                row.add(resultSet.getString("note"));
                row.add(resultSet.getString("last_time_updated"));


                dataForKitchenSearchItemTable.add(row);


            }

            KitchenSearchItemTable.setItems(dataForKitchenSearchItemTable);


            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }


    public void ShowAndUpdateTheSelectedItemSearchFun(){

        try {

            SelectedItemFromKitchenTable = KitchenSearchItemTable.getSelectionModel().getSelectedItems().get(0).toString().split(",")[1].substring(1);

            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowTheSelectedKitchenItem.fxml"));
            loader.load();
            ShowTheSelectedKitchenItem controller = loader.getController();
            Parent popup = loader.getRoot();

            // Give popup a callback method
            controller.setup(
                    (value)->{
                        LoadTheKitchenCurrentItemsTableFun(0);
                    }
            );

            try {
                SelectedKitchenItemStage.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            SelectedKitchenItemStage.setTitle("UpDate The Item !!!");
            SelectedKitchenItemStage.setScene(new Scene(popup,800 ,600));
            SelectedKitchenItemStage.show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void KitchenItemsDetailsForSearchFun(){

        try {

            SelectedItemFromKitchenTable = KitchenSearchItemTable.getSelectionModel().getSelectedItems().get(0).toString().split(",")[1].substring(1);

            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowTheSelectedKitchenItemDetails.fxml"));
            loader.load();
            ShowTheSelectedKitchenItemDetails controller = loader.getController();
            Parent popup = loader.getRoot();

            // Give popup a callback method
            controller.setup(
                    (value)->{
                        // Do Nothing ...
                    }
            );

            try {
                SelectedKitchenItemDetailsStage.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            SelectedKitchenItemDetailsStage.setTitle("Item Details !!!");
            SelectedKitchenItemDetailsStage.setScene(new Scene(popup,800 ,600));
            SelectedKitchenItemDetailsStage.show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void KitchenItemsConsumingForSearchFun(){

        try {

            SelectedItemFromKitchenTable = KitchenSearchItemTable.getSelectionModel().getSelectedItems().get(0).toString().split(",")[1].substring(1);

            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowKitchenItemConsumingManagement.fxml"));
            loader.load();
            ShowKitchenItemConsumingManagement controller = loader.getController();
            Parent popup = loader.getRoot();

            // Give popup a callback method
            controller.setup(
                    (value)->{
                        LoadTheKitchenCurrentItemsTableFun(0);
                    }
            );

            try {
                SelectedKitchenItemConsumingStage.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            SelectedKitchenItemConsumingStage.setTitle("Consuming Management !!!");
            SelectedKitchenItemConsumingStage.setScene(new Scene(popup,800 ,600));
            SelectedKitchenItemConsumingStage.show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void RemoveFromTheSelectedItemsSearchFun(){

        try {
            OpenConnection();


            SelectedItemFromKitchenTable = KitchenSearchItemTable.getSelectionModel().getSelectedItems().get(0).toString().split(",")[1].substring(1);

            Statement RemoveItem =  connection.createStatement();
            String Item = "DELETE FROM kitchen_info WHERE item_id = '"+SelectedItemFromKitchenTable+"'";
            RemoveItem.executeUpdate(Item);

            Statement RemoveItemFromInfo =  connection.createStatement();
            String ItemInfo = "DELETE FROM kitchen_details_info WHERE item_id = '"+SelectedItemFromKitchenTable+"'";
            RemoveItemFromInfo.executeUpdate(ItemInfo);

            Statement RemoveItemFromConsuming =  connection.createStatement();
            String ItemConsumingInfo = "DELETE FROM kitchen_consuming_info WHERE item_id = '"+SelectedItemFromKitchenTable+"'";
            RemoveItemFromConsuming.executeUpdate(ItemConsumingInfo);


            LoadTheKitchenCurrentItemsTableFun(0);

            if(KitchenItemSearchText.getText().trim().isEmpty()&&KitchenItemIDSearchText.getText().trim().isEmpty()){

                KitchenSearchItemTable.setItems(null);

            }else if(!KitchenItemSearchText.getText().trim().isEmpty()&&KitchenItemIDSearchText.getText().trim().isEmpty()){

                LoadTheCurrentKitchenItemsSearchTable(0,0,KitchenItemSearchText.getText().trim());

            }else if(KitchenItemSearchText.getText().trim().isEmpty()&&!KitchenItemIDSearchText.getText().trim().isEmpty()){

                LoadTheCurrentKitchenItemsSearchTable(0,1,KitchenItemIDSearchText.getText().trim());

            }

            SelectedItemFromKitchenTable = "";

            CloseConnection();

        }
        catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }


    public void ReArrangeTheItemsSearchByCostFun(){

        if(KitchenItemSearchText.getText().trim().isEmpty()&&KitchenItemIDSearchText.getText().trim().isEmpty()){

            KitchenCurrentItemsTable.setItems(null);

        }else if(!KitchenItemSearchText.getText().trim().isEmpty()&&KitchenItemIDSearchText.getText().trim().isEmpty()){

            LoadTheCurrentWorkerSearchTable(1,0,KitchenItemSearchText.getText().trim());

        }else if(KitchenItemSearchText.getText().trim().isEmpty()&&!KitchenItemIDSearchText.getText().trim().isEmpty()){

            LoadTheCurrentWorkerSearchTable(1,1,KitchenItemIDSearchText.getText().trim());

        }

    }


    public void KitchenSearchByItemName(){

        if(!KitchenItemSearchText.getText().trim().isEmpty()){
            KitchenItemIDSearchText.setText("");
            LoadTheCurrentKitchenItemsSearchTable(0,0,KitchenItemSearchText.getText().trim());
        }else {
            KitchenSearchItemTable.setItems(null);
        }
    }


    public void KitchenSearchByItemID(){
        if(!KitchenItemIDSearchText.getText().trim().isEmpty()){
            KitchenItemSearchText.setText("");
            LoadTheCurrentKitchenItemsSearchTable(0,1,KitchenItemIDSearchText.getText().trim());
        }else {
            KitchenSearchItemTable.setItems(null);
        }

    }



    // ToDO End Of The Kitchen Tab *************************


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // ToDO Start Of The Orders Tab *************************




    // ToDo >>>>>> Vars


    // ToDo >>> For ( Manage Orders ) Sub Tab


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

    int SaveOrUpdate = 0 ; // 1 for update ...// 2 for insert







    @FXML
    private TableView<FoodBank> DeliveryTable;

    private ObservableList<FoodBank> dataListForD = FXCollections.observableArrayList();

    @FXML
    private TableColumn <FoodBank, String> DeliveryTableColumnItemName;

    @FXML
    private TableColumn <FoodBank, String> DeliveryTableColumnItemSize;

    @FXML
    private TableColumn <FoodBank, String> DeliveryTableColumnItemCost;

    @FXML
    private TableColumn <FoodBank, String> DeliveryTableColumnNumberOfItems;

    @FXML
    private TableColumn <FoodBank, String> DeliveryTableColumnDisForOne;

    @FXML
    private TableColumn <FoodBank, String> DeliveryTableColumnTotalDis;

    @FXML
    private TableColumn <FoodBank, String> DeliveryTableColumnTotal;

    @FXML
    private TableColumn <FoodBank, String> DeliveryTableColumnCode;

    @FXML
    private TableColumn <FoodBank, String> DeliveryTableColumnOrderCounter;


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
    private JFXComboBox<DeliveryClassForComboBox> DeliveryManComboBox;

    private ObservableList<DeliveryClassForComboBox> DataForDeliveryManComboBox = FXCollections.observableArrayList();

    @FXML
    private Label DateLabel;

    @FXML
    private JFXTextField PaidText;

    @FXML
    private JFXTextField ReturnText;

    @FXML
    private JFXRadioButton PayWithPointsRB;



    @FXML
    private JFXRadioButton DeliveryTypeRB;

    @FXML
    private JFXRadioButton TakeAWayTypeRB;

    @FXML
    private JFXRadioButton LobbyTypeRB;

    @FXML
    private JFXTextField EstimatedTimeText;



    // ToDo >>> For ( Manage Menu ) Sub Tab


    @FXML
    private JFXTextField FoodItemNameForSearch;

    @FXML
    private JFXComboBox<FoodCategoryCBClassForComboBox> FoodCategoryCB;

    private ObservableList<FoodCategoryCBClassForComboBox> DataForFoodCategoryCB = FXCollections.observableArrayList();



    @FXML
    private TableView<ObservableList> MenuFoodTable;

    private ObservableList<ObservableList> dataForMenuFoodTable ;

    @FXML
    private TableColumn SearchMenuColumnCode;

    @FXML
    private TableColumn SearchMenuColumnItemName;

    @FXML
    private TableColumn SearchMenuColumnItemUnit;

    @FXML
    private TableColumn SearchMenuColumnS;

    @FXML
    private TableColumn SearchMenuColumnM;

    @FXML
    private TableColumn SearchMenuColumnL;

    @FXML
    private TableColumn SearchMenuColumnXL;

    @FXML
    private TableColumn SearchMenuColumnXXL;

    @FXML
    private TableColumn SearchMenuColumnDiscountS;

    @FXML
    private TableColumn SearchMenuColumnDiscountM;

    @FXML
    private TableColumn SearchMenuColumnDiscountL;

    @FXML
    private TableColumn SearchMenuColumnDiscountXL;

    @FXML
    private TableColumn SearchMenuColumnDiscountXXL;

    @FXML
    private TableColumn SearchMenuColumnRate;

    @FXML
    private TableColumn SearchMenuColumnLastUpdate;

    @FXML
    private TableColumn SearchMenuColumnCategory;

    @FXML
    private TableColumn SearchMenuColumnType;

    @FXML
    private JFXButton ShowAndUpdateFoodMenuItemButton;

    @FXML
    private JFXButton ShowFoodByRateButton;



    // ToDo >>> For ( Special Menu ) Sub Tab



    @FXML
    private Label BtnCodeLabel;

    @FXML
    private JFXTextField SpecialBtnText;

    @FXML
    private JFXTextField SpecialItemNameText;

    @FXML
    private JFXTextField SpecialItemCategoryText;

    @FXML
    private JFXTextField SpecialSCostText;

    @FXML
    private JFXTextField SpecialMCostText;

    @FXML
    private JFXTextField SpecialLCostText;

    @FXML
    private JFXTextField SpecialXLCostText;

    @FXML
    private JFXTextField SpecialXXLCostText;

    @FXML
    private JFXTextField SpecialSDIsText;

    @FXML
    private JFXTextField SpecialMDisText;

    @FXML
    private JFXTextField SpecialLDisText;

    @FXML
    private JFXTextField SpecialXLDisText;

    @FXML
    private JFXTextField SpecialXXLDisText;

    @FXML
    private JFXButton SpecialUpdateTheItemButton;


    @FXML
    private AnchorPane UpdateSpecialMenuCoverPanel;

    @FXML
    private AnchorPane UpdateSpecialMenuPanel;


    @FXML
    private JFXButton S1;

    @FXML
    private JFXButton S2;

    @FXML
    private JFXButton S3;

    @FXML
    private JFXButton S4;

    @FXML
    private JFXButton S5;

    @FXML
    private JFXButton S6;

    @FXML
    private JFXButton S7;

    @FXML
    private JFXButton S8;

    @FXML
    private JFXButton S9;

    @FXML
    private JFXButton S10;

    @FXML
    private JFXButton Specialb1;

    @FXML
    private JFXButton Specialb2;

    @FXML
    private JFXButton Specialb3;

    @FXML
    private JFXButton Specialb4;

    @FXML
    private JFXButton Specialb5;

    @FXML
    private JFXButton Specialb6;

    @FXML
    private JFXButton Specialb7;

    @FXML
    private JFXButton Specialb8;

    @FXML
    private JFXButton Specialb9;

    @FXML
    private JFXButton Specialb10;



    // ToDo >>>>>> Functions


    // ToDo >>> For ( Manage Orders ) Sub Tab


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
        Button btn = (Button) event.getSource();
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




    public void LoadTheUserInfoFun(){

        try{

            OpenConnection();
            Statement GetUserInfo =  connection.createStatement();
            String UserInfo = "Select * FROM customers_info WHERE phone_number = '"+DeliveryPhoneNumberText.getText().trim()+"'";
            ResultSet resultSet = GetUserInfo.executeQuery(UserInfo);

            if(resultSet.next()){
                SaveOrUpdate = 1 ;

                DeliveryNameText.setText(resultSet.getString("customer_name"));
                DeliveryAddressText.setText(resultSet.getString("address"));
                DeliveryNoteText.setText(resultSet.getString("note"));
                yourTotalPointsText.setText(resultSet.getString("customer_points"));


            }else {
                SaveOrUpdate = 2 ;
                DeliveryNameText.setText("");
                DeliveryAddressText.setText("");
                DeliveryNoteText.setText("");
                yourTotalPointsText.setText("0");

            }
            PayWithPointsRB.setSelected(false);
            GetTheFoodTableCalculations();

            CloseConnection();

        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }




    }

    public void LoadTheUserInfoOnAddingFun(){

        try{

            OpenConnection();
            Statement GetUserInfo =  connection.createStatement();
            String UserInfo = "Select * FROM customers_info WHERE phone_number = '"+CustomerPhoneText.getText().trim()+"'";
            ResultSet resultSet = GetUserInfo.executeQuery(UserInfo);

            if(resultSet.next()){


                CustomerNameText.setText(resultSet.getString("customer_name"));
                CustomerAddressText.setText(resultSet.getString("address"));
                CustomerNoteText.setText(resultSet.getString("note"));



            }else {
                CustomerNameText.setText("");
                CustomerAddressText.setText("");
                CustomerNoteText.setText("");

            }


            CloseConnection();

        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }




    }

    public void UpdateOrInsertTheCustomerFun(){

        if(SaveOrUpdate == 1){
            if(!DeliveryNoteText.getText().trim().isEmpty()&&!DeliveryAddressText.getText().trim().isEmpty()&&!DeliveryNameText.getText().trim().isEmpty()&&!DeliveryPhoneNumberText.getText().trim().isEmpty()){

                try {

                    OpenConnection();
                    Statement UpdateTheUserInfo =  connection.createStatement();
                    String CustomerInfo = "UPDATE customers_info SET customer_name = '"+DeliveryNameText.getText().trim()+
                            "' , address = '"+DeliveryAddressText.getText().trim()+
                            "' , note = '"+DeliveryNoteText.getText().trim()+
                            "' WHERE phone_number = '"+DeliveryPhoneNumberText.getText().trim()+"'";
                    UpdateTheUserInfo.executeUpdate(CustomerInfo);


                    LoadTheCurrentCustomerTable(0);

                    SaveOrUpdate = 0 ;

                    CloseConnection();

                }catch (Exception e){
                    e.printStackTrace();
                    CloseConnection();
                }


            }

        } else if(SaveOrUpdate==2){

            if(!DeliveryNoteText.getText().trim().isEmpty()&&!DeliveryAddressText.getText().trim().isEmpty()&&!DeliveryNameText.getText().trim().isEmpty()&&!DeliveryPhoneNumberText.getText().trim().isEmpty()){

                try {

                    OpenConnection();
                    Statement InsetNewCustomer =  connection.createStatement();
                    String CustomerInfo = "INSERT INTO customers_info (customer_name, phone_number, address, note) VALUES ('"+DeliveryNameText.getText().trim()+
                            "','"+DeliveryPhoneNumberText.getText().trim()+
                            "','"+DeliveryAddressText.getText().trim()+
                            "','"+DeliveryNoteText.getText().trim()+"')";
                    InsetNewCustomer.executeUpdate(CustomerInfo);


                    LoadTheCurrentCustomerTable(0);

                    SaveOrUpdate = 0 ;

                    CloseConnection();

                }catch (Exception e){
                    e.printStackTrace();
                    CloseConnection();
                }


            }


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
                            LoadTheCurrentCustomerTable(0);
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

    public void ClearTheCustomerInfoInOrderFun(){

        DeliveryNameText.setText("");
        DeliveryAddressText.setText("");
        DeliveryNoteText.setText("");
        DeliveryPhoneNumberText.setText("");
        yourTotalPointsText.setText("0");


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

    class EditingCell extends TableCell<FoodBank, String> {

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
                        return new EditingCell();
                    }
                };

        // ToDo FoodBank Table

        DeliveryTableColumnItemName.setCellValueFactory(new PropertyValueFactory<FoodBank, String>("foodName"));
        DeliveryTableColumnItemSize.setCellValueFactory(new PropertyValueFactory<FoodBank, String>("foodSize"));
        DeliveryTableColumnItemCost.setCellValueFactory(new PropertyValueFactory<FoodBank, String>("foodCost"));
        DeliveryTableColumnNumberOfItems.setCellValueFactory(new PropertyValueFactory<FoodBank, String>("foodNumItems"));
        DeliveryTableColumnDisForOne.setCellValueFactory(new PropertyValueFactory<FoodBank, String>("foodDis"));
        DeliveryTableColumnTotalDis.setCellValueFactory(new PropertyValueFactory<FoodBank, String>("foodTotalDis"));
        DeliveryTableColumnTotal.setCellValueFactory(new PropertyValueFactory<FoodBank, String>("foodTotal"));
        DeliveryTableColumnCode.setCellValueFactory(new PropertyValueFactory<FoodBank, String>("foodCode"));

        DeliveryTableColumnNumberOfItems.setCellFactory(TextFieldTableCell.forTableColumn());

        DeliveryTableColumnNumberOfItems.setOnEditCommit(

                (TableColumn.CellEditEvent<FoodBank, String> t) -> {
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

        // ToDo Order Info Table

        Callback<TableColumn, TableCell> cellFactory2 =
                new Callback<TableColumn, TableCell>() {
                    public TableCell call(TableColumn p) {
                        return new EditingCell2();
                    }
                };

        MainGeneralOrderColumnOrderNumber.setCellValueFactory(new PropertyValueFactory<OrderInfo, String>("OrderNumber"));
        MainGeneralOrderColumnCustomerPhone.setCellValueFactory(new PropertyValueFactory<OrderInfo, String>("CustomerPhone"));
        MainGeneralOrderColumnCustomerName.setCellValueFactory(new PropertyValueFactory<OrderInfo, String>("CustomerName"));
        MainGeneralOrderColumnOrderType.setCellValueFactory(new PropertyValueFactory<OrderInfo, String>("OrderType"));
        MainGeneralOrderColumnEstimatedTime.setCellValueFactory(new PropertyValueFactory<OrderInfo, String>("EstimatedTime"));
        MainGeneralOrderColumnTotal.setCellValueFactory(new PropertyValueFactory<OrderInfo, String>("Total"));
        MainGeneralOrderColumnOrderTime.setCellValueFactory(new PropertyValueFactory<OrderInfo, String>("OrderTime"));


        OrderInfoTable.setItems(dataForOrderInfoTable);

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




                dataListForD.add(new FoodBank(
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

        FoodBank selectedItem = DeliveryTable.getSelectionModel().getSelectedItem();
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
            for (FoodBank item : DeliveryTable.getItems()) {
                total_discount = total_discount + Double.parseDouble(item.getFoodTotalDis());
            }
            savedMoneyText.setText(String.valueOf(total_discount));

            double total_cash = 0 ;
            for (FoodBank item : DeliveryTable.getItems()) {
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

                if (resultSet.next()) {
                    yourTotalPointsText.setText(resultSet.getString("customer_points"));
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

            DeliveryManComboBox.setConverter(new StringConverter<DeliveryClassForComboBox>() {
                @Override
                public String toString(DeliveryClassForComboBox object) {
                    return object.getNameX();
                }

                @Override
                public DeliveryClassForComboBox fromString(String string) {
                    return null;
                }
            });

            DataForDeliveryManComboBox.clear();

            DataForDeliveryManComboBox.add(new DeliveryClassForComboBox("Delivery Man","No One"));


            while (resultSet.next()){

                DataForDeliveryManComboBox.add(new DeliveryClassForComboBox(resultSet.getString("worker_name"),resultSet.getString("user_name")));

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

        if(ToggleFlag!=0){
            try{
                OpenConnection();

                Statement GetCustomerInfo = connection.createStatement();
                String CustomerInfo = "Select customer_points FROM customers_info WHERE phone_number = '" + DeliveryPhoneNumberText.getText().trim() + "'";
                ResultSet resultSet = GetCustomerInfo.executeQuery(CustomerInfo);

                if(resultSet.next()){

                    if(PayWithPointsRB.isSelected()){

                        pointsForThisOrderText.setText("0");

                        if(Double.parseDouble(cashMoneyText.getText().trim()) > Double.parseDouble(yourTotalPointsText.getText().trim())){

                            double restMoney = Double.parseDouble(cashMoneyText.getText().trim()) - Double.parseDouble(yourTotalPointsText.getText().trim());

                            double restPoints = 0 ;


                            cashMoneyText.setText(String.valueOf(restMoney));
                            cashMoneyText.setFocusColor(Color.GREEN);
                            cashMoneyText.setUnFocusColor(Color.GREEN);


                            yourTotalPointsText.setText(String.valueOf(restPoints));
                            yourTotalPointsText.setFocusColor(Color.RED);
                            yourTotalPointsText.setUnFocusColor(Color.RED);

                        }
                        else if(Double.parseDouble(cashMoneyText.getText().trim()) <= Double.parseDouble(yourTotalPointsText.getText().trim())) {

                            double restMoney = 0 ;

                            double restPoints = Double.parseDouble(yourTotalPointsText.getText().trim()) - Double.parseDouble(cashMoneyText.getText().trim());


                            cashMoneyText.setText(String.valueOf(restMoney));
                            cashMoneyText.setFocusColor(Color.GREEN);
                            cashMoneyText.setUnFocusColor(Color.GREEN);


                            yourTotalPointsText.setText(String.valueOf(restPoints));
                            yourTotalPointsText.setFocusColor(Color.RED);
                            yourTotalPointsText.setUnFocusColor(Color.RED);

                        }

                    }
                    else {

                        ToggleFlag = 0 ;

                        GetTheFoodTableCalculations();

                        cashMoneyText.setFocusColor(Color.valueOf("#4059a9"));
                        cashMoneyText.setUnFocusColor(Color.valueOf("#4d4d4d"));


                        yourTotalPointsText.setText(resultSet.getString("customer_points"));
                        yourTotalPointsText.setFocusColor(Color.valueOf("#4059a9"));
                        yourTotalPointsText.setUnFocusColor(Color.valueOf("#4d4d4d"));

                    }

                }else {

                    yourTotalPointsText.setText("0");
                    ToggleFlag = 0 ;

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
        ToggleFlag = 1 ;
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


    public void UpdateTheBillPath(){
        try {
            OpenConnection();

            Statement getTheBillUrl = connection.createStatement();
            String BillUrl = "SELECT * FROM bill_path ";
            ResultSet resultSet = getTheBillUrl.executeQuery(BillUrl);

            if (resultSet.next()){
                resultSet.last();
                BillUrlPath = String.valueOf(resultSet.getString("path_url"));
                PrinterNameFromTheComputer = String.valueOf(resultSet.getString("printer_name"));
            }

            CloseConnection();

        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }
    }

    public void UpdateTheOrderBillPath(){
        try {
            OpenConnection();

            Statement getTheBillUrl = connection.createStatement();
            String BillUrl = "SELECT * FROM order_bill_path ";
            ResultSet resultSet = getTheBillUrl.executeQuery(BillUrl);

            if (resultSet.next()){
                resultSet.last();
                BillUrlPathForKitchen = String.valueOf(resultSet.getString("path_url"));
                PrinterNameFromTheComputerForKitchen = String.valueOf(resultSet.getString("printer_name"));
            }

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
                OpenConnection();

                List<Map<String,?>> dataSource = new ArrayList();

                List<Map<String,?>> dataSource2 = new ArrayList();


                System.out.println("We Are In The Finish The Order Function");

                // ToDo Update The Customer Info Including Points And Cash .


                double newPoints ;
                double PreviousPoints ;
                double Consumed ;
                if(PayWithPointsRB.isSelected()){
                    newPoints = 0 ;

                    // loop to get the total
                    double total = 0 ;

                    for (FoodBank item : DeliveryTable.getItems()) {
                        total = total + Double.parseDouble(item.getFoodTotal());
                    }
                    Consumed = total - Double.parseDouble(cashMoneyText.getText().trim()) ;


                }else {
                    newPoints = Double.parseDouble(pointsForThisOrderText.getText().trim());
                    Consumed = 0 ;
                }

                PreviousPoints = Double.parseDouble(yourTotalPointsText.getText().trim()) ;

                double points = newPoints + PreviousPoints ;

                double cash = Double.parseDouble(cashMoneyText.getText().trim());

                if(SaveOrUpdate == 1){

                    Statement UpdateTheUserInfo =  connection.createStatement();
                    String CustomerInfo = "UPDATE customers_info SET customer_name = '"+DeliveryNameText.getText().trim()+
                            "' , address = '"+DeliveryAddressText.getText().trim()+
                            "' , note = '"+DeliveryNoteText.getText().trim()+
                            "' , customer_points = "+points+
                            "  , customer_cash = customer_cash +"+cash+
                            " WHERE phone_number = '"+DeliveryPhoneNumberText.getText().trim()+"'";
                    UpdateTheUserInfo.executeUpdate(CustomerInfo);


                    LoadTheCurrentCustomerTable(0);

                    SaveOrUpdate = 0 ;


                } else if(SaveOrUpdate==2){

                    Statement InsetNewCustomer =  connection.createStatement();
                    String CustomerInfo = "INSERT INTO customers_info (customer_name, phone_number,address,customer_points,customer_cash, note) VALUES ('"+DeliveryNameText.getText().trim()+
                            "','"+DeliveryPhoneNumberText.getText().trim()+
                            "','"+DeliveryAddressText.getText().trim()+
                            "',"+points+
                            " ,"+cash+
                            ",'"+DeliveryNoteText.getText().trim()+"')";
                    InsetNewCustomer.executeUpdate(CustomerInfo);


                    LoadTheCurrentCustomerTable(0);

                    SaveOrUpdate = 0 ;

                }




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
                        "','"+Consumed+
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
                        "','"+Consumed+
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

                    for (FoodBank item : DeliveryTable.getItems()) {

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
                    parameter2.put("CustomerNote", DeliveryNoteText.getText().trim());

                    // ToDo Update Clear The Previous Data And Start Over  .

                    DeliveryPhoneNumberText.setText("");
                    DeliveryNameText.setText("");
                    DeliveryAddressText.setText("");
                    DeliveryNoteText.setText("");
                    EstimatedTimeText.setText("");
                    yourTotalPointsText.setText("");
                    DeliveryManComboBox.getSelectionModel().clearSelection();
                    ClearAllRowsFromFoodTableFun();
                    CurrentDelivery = "";
                    PayWithPointsRB.setSelected(false);
                    TakeAWayTypeRB.setSelected(false);
                    DeliveryTypeRB.setSelected(false);
                    LobbyTypeRB.setSelected(false);

                    LoadTheOrderInfoTable("",0,0);


                    UpDateTheShift();




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


    // ToDo >>> For ( Manage MENU ) Sub Tab


    public void CreateTheSearchFoodTable(){

        MenuFoodTable.getColumns().clear();


        SearchMenuColumnCode.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(0).toString()));

        SearchMenuColumnItemName.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(1).toString()));

        SearchMenuColumnItemUnit.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(2).toString()));

        SearchMenuColumnS.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(3).toString()));

        SearchMenuColumnM.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(4).toString()));

        SearchMenuColumnL.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(5).toString()));

        SearchMenuColumnXL.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(6).toString()));

        SearchMenuColumnXXL.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(7).toString()));

        SearchMenuColumnDiscountS.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(8).toString()));

        SearchMenuColumnDiscountM.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(9).toString()));

        SearchMenuColumnDiscountL.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(10).toString()));

        SearchMenuColumnDiscountXL.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(11).toString()));

        SearchMenuColumnDiscountXXL.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(12).toString()));

        SearchMenuColumnRate.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(13).toString()));

        SearchMenuColumnLastUpdate.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(14).toString()));

        SearchMenuColumnCategory.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(15).toString()));

        SearchMenuColumnType.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(16).toString()));


        MenuFoodTable.getColumns().add(SearchMenuColumnCode);
        MenuFoodTable.getColumns().add(SearchMenuColumnItemName);
        MenuFoodTable.getColumns().add(SearchMenuColumnItemUnit);
        MenuFoodTable.getColumns().add(SearchMenuColumnS);
        MenuFoodTable.getColumns().add(SearchMenuColumnM);
        MenuFoodTable.getColumns().add(SearchMenuColumnL);
        MenuFoodTable.getColumns().add(SearchMenuColumnXL);
        MenuFoodTable.getColumns().add(SearchMenuColumnXXL);
        MenuFoodTable.getColumns().add(SearchMenuColumnDiscountS);
        MenuFoodTable.getColumns().add(SearchMenuColumnDiscountM);
        MenuFoodTable.getColumns().add(SearchMenuColumnDiscountL);
        MenuFoodTable.getColumns().add(SearchMenuColumnDiscountXL);
        MenuFoodTable.getColumns().add(SearchMenuColumnDiscountXXL);
        MenuFoodTable.getColumns().add(SearchMenuColumnRate);
        MenuFoodTable.getColumns().add(SearchMenuColumnLastUpdate);
        MenuFoodTable.getColumns().add(SearchMenuColumnCategory);
        MenuFoodTable.getColumns().add(SearchMenuColumnType);

        LoadTheSearchFoodTableFun(0);

    }

    public void LoadTheSearchFoodTableFun(int mode){


        // mode 0 for the normal arrange
        // mode 1 for arrange Items by rate

        try {
            OpenConnection();

            MenuFoodTable.setItems(null);

            dataForMenuFoodTable = FXCollections.observableArrayList();

            Statement GetAllItems = connection.createStatement();
            String Items ;
            if(mode==0) {

                Items = "SELECT * FROM menu";

            }
            else {

                Items = "SELECT * FROM menu order by rate DESC ";
            }
            ResultSet resultSet = GetAllItems.executeQuery(Items);



            while (resultSet.next()){

                ObservableList<String> row = FXCollections.observableArrayList();

                row.add(resultSet.getString("code"));
                row.add(resultSet.getString("item_name"));
                row.add(resultSet.getString("item_unit"));
                row.add(resultSet.getString("s"));
                row.add(resultSet.getString("m"));
                row.add(resultSet.getString("l"));
                row.add(resultSet.getString("xl"));
                row.add(resultSet.getString("xxl"));
                row.add(resultSet.getString("dis_s"));
                row.add(resultSet.getString("dis_m"));
                row.add(resultSet.getString("dis_l"));
                row.add(resultSet.getString("dis_xl"));
                row.add(resultSet.getString("dis_xxl"));
                row.add(resultSet.getString("rate"));
                row.add(resultSet.getString("last_time_updated"));
                row.add(resultSet.getString("category"));
                row.add(resultSet.getString("type"));

                dataForMenuFoodTable.add(row);


            }

            MenuFoodTable.setItems(dataForMenuFoodTable);


            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }



    }

    class FoodCategoryCBClassForComboBox {
        private String category;

        public FoodCategoryCBClassForComboBox(String category) {
            this.category = category;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }

    public void LoadTheFoodCategoryCBComboBoxFun(){
        try{

            FoodCategoryCB.valueProperty().addListener((obs, oldVal, newVal) -> {
                CurrentCategory = newVal.getCategory() ;

                if(!FoodItemNameForSearch.getText().trim().isEmpty() && !(FoodCategoryCB.getValue()==null)){
                    SearchForFoodItemByNameAndCategoryWhileTypingFun(0,FoodItemNameForSearch.getText().trim());
                }

            });

            FoodCategoryCB.setConverter(new StringConverter<FoodCategoryCBClassForComboBox>() {
                @Override
                public String toString(FoodCategoryCBClassForComboBox object) {
                    return object.getCategory();
                }

                @Override
                public FoodCategoryCBClassForComboBox fromString(String string) {
                    return null;
                }
            });

            DataForFoodCategoryCB.clear();

            // Add Food Categories
            DataForFoodCategoryCB.add(new FoodCategoryCBClassForComboBox("Pizza"));
            DataForFoodCategoryCB.add(new FoodCategoryCBClassForComboBox("pies"));
            DataForFoodCategoryCB.add(new FoodCategoryCBClassForComboBox("pies"));
            DataForFoodCategoryCB.add(new FoodCategoryCBClassForComboBox("pies"));
            DataForFoodCategoryCB.add(new FoodCategoryCBClassForComboBox("pies"));
            DataForFoodCategoryCB.add(new FoodCategoryCBClassForComboBox("pies"));
            DataForFoodCategoryCB.add(new FoodCategoryCBClassForComboBox("pies"));
            DataForFoodCategoryCB.add(new FoodCategoryCBClassForComboBox("pies"));
            DataForFoodCategoryCB.add(new FoodCategoryCBClassForComboBox("pies"));

            FoodCategoryCB.setItems(DataForFoodCategoryCB);
            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }
    }

    public void ShowFoodByRateFun(){

        if(!FoodItemNameForSearch.getText().trim().isEmpty() && !(FoodCategoryCB.getValue()==null)){
            SearchForFoodItemByNameAndCategoryWhileTypingFun(1,FoodItemNameForSearch.getText().trim());
        }else {
            LoadTheSearchFoodTableFun(1);
        }

    }

    public void SearchForFoodWhileTypingFun(){
        SearchForFoodItemByNameAndCategoryWhileTypingFun(0,FoodItemNameForSearch.getText().trim());
    }

    public void SearchForFoodItemByNameAndCategoryWhileTypingFun(int mode , String text){

        if(!FoodItemNameForSearch.getText().trim().isEmpty() && !(FoodCategoryCB.getValue()==null)){


            // mode 0 for the normal arrange
            // mode 1 for arrange food by rate

            try {
                OpenConnection();

                MenuFoodTable.setItems(null);

                dataForMenuFoodTable = FXCollections.observableArrayList();

                System.out.println(text);
                System.out.println(CurrentCategory);

                Statement GetAllItems = connection.createStatement();
                String Items ;
                if(mode==0) {

                    Items = "SELECT * FROM menu WHERE item_name LIKE '%"+text+"%' And category = '"+CurrentCategory+"'";


                }
                else {

                    Items = "SELECT * FROM menu WHERE item_name LIKE '%"+text+"%' And category = '"+CurrentCategory+"' order by rate DESC";


                }
                ResultSet resultSet = GetAllItems.executeQuery(Items);



                while (resultSet.next()){

                    ObservableList<String> row = FXCollections.observableArrayList();

                    row.add(resultSet.getString("code"));
                    row.add(resultSet.getString("item_name"));
                    row.add(resultSet.getString("item_unit"));
                    row.add(resultSet.getString("s"));
                    row.add(resultSet.getString("m"));
                    row.add(resultSet.getString("l"));
                    row.add(resultSet.getString("xl"));
                    row.add(resultSet.getString("xxl"));
                    row.add(resultSet.getString("dis_s"));
                    row.add(resultSet.getString("dis_m"));
                    row.add(resultSet.getString("dis_l"));
                    row.add(resultSet.getString("dis_xl"));
                    row.add(resultSet.getString("dis_xxl"));
                    row.add(resultSet.getString("rate"));
                    row.add(resultSet.getString("last_time_updated"));
                    row.add(resultSet.getString("category"));
                    row.add(resultSet.getString("type"));

                    dataForMenuFoodTable.add(row);


                }

                MenuFoodTable.setItems(dataForMenuFoodTable);


                CloseConnection();
            }catch (Exception e){
                e.printStackTrace();
                CloseConnection();
            }


        }else {
            LoadTheSearchFoodTableFun(0);
        }


    }

    public void ShowAndUpdateFoodItemFun(){


        String SelectedType = MenuFoodTable.getSelectionModel().getSelectedItems().get(0).toString().split(",")[16].substring(1);
        SelectedType = SelectedType.replace(']','*') ;
        System.out.println(SelectedType);

        if(!SelectedType.equals("1*")){
            try {
                SelectedFoodItem = MenuFoodTable.getSelectionModel().getSelectedItems().get(0).toString().split(",")[0].substring(1);

                System.out.println(SelectedFoodItem);


                // Load the popup
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowTheSelectedFoodItem.fxml"));
                loader.load();
                ShowTheSelectedFoodItem controller = loader.getController();
                Parent popup = loader.getRoot();

                // Give popup a callback method
                controller.setup(
                        (value)->{
                            FoodCategoryCB.getSelectionModel().clearSelection();
                            FoodItemNameForSearch.setText("");
                            LoadTheSearchFoodTableFun(0);
                        }
                );

                try {
                    ShowTheFoodInfoStage.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

                ShowTheFoodInfoStage.setTitle("Update A Menu Item  !!!");
                ShowTheFoodInfoStage.setScene(new Scene(popup,800 ,600));
                ShowTheFoodInfoStage.show();


            }catch (Exception e){
                e.printStackTrace();
            }
        }



    }


    // ToDo >>> For ( Special Menu ) Sub TabA


    @FXML
    void ManageTheSpecialButtonsSwitcher(ActionEvent event) {



        try {
            // get the id of the clicked button
            Button btn = (Button) event.getSource();
            String ButtonID = btn.getId();

            LastButtonIDForSpecialMenu = ButtonID ;

            UpdateSpecialMenuPanel.toFront();

            OpenConnection();

            Statement FillSpecialMenuQuest =  connection.createStatement();
            String SpecialMenu = "SELECT * FROM special_menu WHERE code = '"+ButtonID+"'";
            ResultSet resultSet = FillSpecialMenuQuest.executeQuery(SpecialMenu);

            if(resultSet.next()){

                BtnCodeLabel.setText(resultSet.getString("code"));
                SpecialBtnText.setText(resultSet.getString("btn_name"));
                SpecialItemNameText.setText(resultSet.getString("item_name"));
                SpecialItemCategoryText.setText(resultSet.getString("category"));
                SpecialSCostText.setText(resultSet.getString("s"));
                SpecialMCostText.setText(resultSet.getString("m"));
                SpecialLCostText.setText(resultSet.getString("l"));
                SpecialXLCostText.setText(resultSet.getString("xl"));
                SpecialXXLCostText.setText(resultSet.getString("xxl"));
                SpecialSDIsText.setText(resultSet.getString("dis_s"));
                SpecialMDisText.setText(resultSet.getString("dis_m"));
                SpecialLDisText.setText(resultSet.getString("dis_l"));
                SpecialXLDisText.setText(resultSet.getString("dis_xl"));
                SpecialXXLDisText.setText(resultSet.getString("dis_xxl"));

            }

            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }


    }

    public void UpdateASpecialMenuItem() {

        if (!SpecialItemNameText.getText().trim().isEmpty() &&
                !SpecialItemCategoryText.getText().trim().isEmpty() &&
                !SpecialBtnText.getText().trim().isEmpty() &&
                !SpecialSCostText.getText().trim().isEmpty() &&
                !SpecialMCostText.getText().trim().isEmpty() &&
                !SpecialLCostText.getText().trim().isEmpty() &&
                !SpecialXLCostText.getText().trim().isEmpty() &&
                !SpecialXXLCostText.getText().trim().isEmpty() &&
                !SpecialSDIsText.getText().trim().isEmpty() &&
                !SpecialMDisText.getText().trim().isEmpty() &&
                !SpecialLDisText.getText().trim().isEmpty() &&
                !SpecialXLDisText.getText().trim().isEmpty() &&
                !SpecialXXLDisText.getText().trim().isEmpty() &&
                !LastButtonIDForSpecialMenu.equals("")) {

            try {
                OpenConnection();

                Statement UpdateTheFoodItem = connection.createStatement();
                String FoodItem = "UPDATE special_menu SET item_name = '" + SpecialItemNameText.getText().trim() + "' ," +
                        " category = '" + SpecialItemCategoryText.getText().trim() + "' ," +
                        " s = " + Double.parseDouble(SpecialSCostText.getText().trim()) + " ," +
                        " m = " + Double.parseDouble(SpecialMCostText.getText().trim()) + "," +
                        " l = " + Double.parseDouble(SpecialLCostText.getText().trim()) + " ," +
                        " xl = " + Double.parseDouble(SpecialXLCostText.getText().trim()) + "," +
                        " xxl = " + Double.parseDouble(SpecialXXLCostText.getText().trim()) + "," +
                        " dis_s = " + Double.parseDouble(SpecialSDIsText.getText().trim()) + "," +
                        " dis_m = " + Double.parseDouble(SpecialMDisText.getText().trim()) + "," +
                        " dis_l = " + Double.parseDouble(SpecialLDisText.getText().trim()) + "," +
                        " dis_xl = " + Double.parseDouble(SpecialXLDisText.getText().trim()) + "," +
                        " dis_xxl = " + Double.parseDouble(SpecialXXLDisText.getText().trim()) +
                        ", btn_name = '"+SpecialBtnText.getText().trim()+"' " +
                        "WHERE code = '" + LastButtonIDForSpecialMenu + "'";
                UpdateTheFoodItem.executeUpdate(FoodItem);

                Statement UpdateTheSpecialFoodItem = connection.createStatement();
                String SpecialFoodItemInfo = "SELECT * FROM special_menu WHERE code = '"+LastButtonIDForSpecialMenu+"'";
                ResultSet resultSet = UpdateTheSpecialFoodItem.executeQuery(SpecialFoodItemInfo);

                if(resultSet.next()){

                    Statement UpdateTheSpecialFoodItemInMenuTable = connection.createStatement();
                    String SpecialFoodItemInMenuTable = "UPDATE menu SET" +
                            " item_name = '"+resultSet.getString("item_name")+"' " +
                            ", s = "+Double.parseDouble(resultSet.getString("s"))+" " +
                            ",m = "+Double.parseDouble(resultSet.getString("m"))+
                            ",l = "+Double.parseDouble(resultSet.getString("l"))+
                            ",xl = "+Double.parseDouble(resultSet.getString("xl"))+
                            ",xxl = "+Double.parseDouble(resultSet.getString("xxl"))+
                            ",dis_s = "+Double.parseDouble(resultSet.getString("dis_s"))+
                            ",dis_m = "+Double.parseDouble(resultSet.getString("dis_m"))+
                            ",dis_l = "+Double.parseDouble(resultSet.getString("dis_l"))+
                            ",dis_xl = "+Double.parseDouble(resultSet.getString("dis_xl"))+
                            ",dis_xxl = "+Double.parseDouble(resultSet.getString("dis_xxl"))+
                            ",category = '"+resultSet.getString("category")+"'" +
                            "WHERE code = '"+resultSet.getString("connected_code")+"'";
                    UpdateTheSpecialFoodItemInMenuTable.executeUpdate(SpecialFoodItemInMenuTable);

                }

                ClearTheFormAfterUpdateTheSpecialMenu();

                LoadTheSearchFoodTableFun(0);

                UpdateTheButtonsName();

                CloseConnection();
            } catch (Exception e) {
                e.printStackTrace();
                CloseConnection();
            }

        }


    }

    public void ClearTheFormAfterUpdateTheSpecialMenu(){
        SpecialItemNameText.setText("");
        SpecialItemCategoryText.setText("");
        SpecialSCostText.setText("");
        SpecialMCostText.setText("");
        SpecialLCostText.setText("");
        SpecialXLCostText.setText("");
        SpecialXXLCostText.setText("");
        SpecialSDIsText.setText("");
        SpecialMDisText.setText("");
        SpecialLDisText.setText("");
        SpecialXLDisText.setText("");
        SpecialXXLDisText.setText("");
        SpecialBtnText.setText("");


        LastButtonIDForSpecialMenu = "";

        UpdateSpecialMenuCoverPanel.toFront();

    }

    public void UpdateTheButtonsName(){
        try{
            OpenConnection();

            Statement UpdateButtons = connection.createStatement();
            String Buttons = "SELECT * FROM special_menu";
            ResultSet resultSet = UpdateButtons.executeQuery(Buttons);
            int i = 0 ;

            while (resultSet.next()){
                btn_names[i] = resultSet.getString("btn_name");
                i++;
            }

            // set The Buttons Names

            S1.setText(btn_names[0]);
            S10.setText(btn_names[1]);
            S2.setText(btn_names[2]);
            S3.setText(btn_names[3]);
            S4.setText(btn_names[4]);
            S5.setText(btn_names[5]);
            S6.setText(btn_names[6]);
            S7.setText(btn_names[7]);
            S8.setText(btn_names[8]);
            S9.setText(btn_names[9]);

            Specialb1.setText(btn_names[0]);
            Specialb10.setText(btn_names[1]);
            Specialb2.setText(btn_names[2]);
            Specialb3.setText(btn_names[3]);
            Specialb4.setText(btn_names[4]);
            Specialb5.setText(btn_names[5]);
            Specialb6.setText(btn_names[6]);
            Specialb7.setText(btn_names[7]);
            Specialb8.setText(btn_names[8]);
            Specialb9.setText(btn_names[9]);


            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }
    }






    // ToDO End Of The Orders Management Tab *************************


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // ToDO Start Of The Main Tab *************************




    // ToDo >>>>>> Vars


    @FXML
    private TableView<OrderInfo> OrderInfoTable;

    private ObservableList<OrderInfo> dataForOrderInfoTable = FXCollections.observableArrayList();

    @FXML
    private TableColumn<OrderInfo, String > MainGeneralOrderColumnOrderNumber;

    @FXML
    private TableColumn<OrderInfo, String > MainGeneralOrderColumnCustomerPhone;

    @FXML
    private TableColumn<OrderInfo, String > MainGeneralOrderColumnCustomerName;

    @FXML
    private TableColumn<OrderInfo, String > MainGeneralOrderColumnOrderType;

    @FXML
    private TableColumn<OrderInfo, String > MainGeneralOrderColumnEstimatedTime;

    @FXML
    private TableColumn<OrderInfo, String > MainGeneralOrderColumnTotal;

    @FXML
    private TableColumn<OrderInfo, String > MainGeneralOrderColumnOrderTime;


    @FXML
    private JFXTextField SearchForAnOrderByOrderNumberText;

    @FXML
    private JFXTextField SearchForAnOrderByCustomerPhoneText;


    public class OrderInfo {

        private final SimpleStringProperty OrderNumber ;
        private final SimpleStringProperty CustomerPhone ;
        private final SimpleStringProperty CustomerName ;
        private final SimpleStringProperty OrderType ;
        private final SimpleStringProperty EstimatedTime ;
        private final SimpleStringProperty Total ;
        private final SimpleStringProperty OrderTime ;


        public OrderInfo(String orderNumber, String customerPhone, String customerName, String orderType, String estimatedTime, String total, String orderTime) {
            OrderNumber = new SimpleStringProperty(orderNumber);
            CustomerPhone = new SimpleStringProperty(customerPhone);
            CustomerName = new SimpleStringProperty(customerName);
            OrderType = new SimpleStringProperty(orderType);
            EstimatedTime = new SimpleStringProperty(estimatedTime);
            Total = new SimpleStringProperty(total);
            OrderTime = new SimpleStringProperty(orderTime);
        }


        public String getOrderNumber() {
            return OrderNumber.get();
        }

        public SimpleStringProperty orderNumberProperty() {
            return OrderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.OrderNumber.set(orderNumber);
        }

        public String getCustomerPhone() {
            return CustomerPhone.get();
        }

        public SimpleStringProperty customerPhoneProperty() {
            return CustomerPhone;
        }

        public void setCustomerPhone(String customerPhone) {
            this.CustomerPhone.set(customerPhone);
        }

        public String getCustomerName() {
            return CustomerName.get();
        }

        public SimpleStringProperty customerNameProperty() {
            return CustomerName;
        }

        public void setCustomerName(String customerName) {
            this.CustomerName.set(customerName);
        }

        public String getOrderType() {
            return OrderType.get();
        }

        public SimpleStringProperty orderTypeProperty() {
            return OrderType;
        }

        public void setOrderType(String orderType) {
            this.OrderType.set(orderType);
        }

        public String getEstimatedTime() {
            return EstimatedTime.get();
        }

        public SimpleStringProperty estimatedTimeProperty() {
            return EstimatedTime;
        }

        public void setEstimatedTime(String estimatedTime) {
            this.EstimatedTime.set(estimatedTime);
        }

        public String getTotal() {
            return Total.get();
        }

        public SimpleStringProperty totalProperty() {
            return Total;
        }

        public void setTotal(String total) {
            this.Total.set(total);
        }

        public String getOrderTime() {
            return OrderTime.get();
        }

        public SimpleStringProperty orderTimeProperty() {
            return OrderTime;
        }

        public void setOrderTime(String orderTime) {
            this.OrderTime.set(orderTime);
        }
    }

    class EditingCell2 extends TableCell<OrderInfo, String> {

        private TextField textField;

        public EditingCell2() {
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


    // ToDo >>>>>> Functions

    public void LoadTheOrderInfoTable(String Text , int mode , int searchType){


        try {
            OpenConnection();

            dataForOrderInfoTable.clear();

            Statement GetTheInfoRow =  connection.createStatement();

            String Row ;

            if(mode==0){
                 Row = "SELECT * FROM general_order_temp ";
            }else {
                if(searchType==0){
                    Row = "SELECT * FROM general_order_temp WHERE code LIKE '%"+Text+"%'";
                }else {
                    Row = "SELECT * FROM general_order_temp WHERE customer_phone LIKE '%"+Text+"%'";
                }
            }

            ResultSet resultSet = GetTheInfoRow.executeQuery(Row);


            while (resultSet.next()){

                dataForOrderInfoTable.add(new OrderInfo(
                         resultSet.getString("code")
                        ,resultSet.getString("customer_phone")
                        ,resultSet.getString("customer_name")
                        ,resultSet.getString("order_type")
                        ,resultSet.getString("estimated_time")
                        ,resultSet.getString("total")
                        ,resultSet.getString("last_time_updated")));
            }

            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }

    public void DeleteTheOrderFun(){

        try {
            SelectedOrderItem = OrderInfoTable.getSelectionModel().getSelectedItems().get(0).getOrderNumber();
            SelectedOrderItemCash = OrderInfoTable.getSelectionModel().getSelectedItems().get(0).getTotal();
            SelectedOrderItemPhone = OrderInfoTable.getSelectionModel().getSelectedItems().get(0).getCustomerPhone();


            System.out.println(SelectedOrderItem);
            System.out.println(SelectedOrderItemCash);
            System.out.println(SelectedOrderItemPhone);


            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DeleteOrderConfirmation.fxml"));
            loader.load();
            DeleteOrderConfirmation controller = loader.getController();
            Parent popup = loader.getRoot();

            // Give popup a callback method
            controller.setup(
                    (value)->{
                        LoadTheOrderInfoTable("",0,0);
                    }
            );

            try {
                DeleteAnOrderQuestion.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            DeleteAnOrderQuestion.setTitle("Remove An Order  !!!?");
            DeleteAnOrderQuestion.setScene(new Scene(popup,800 ,300));
            DeleteAnOrderQuestion.show();


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void SearchByOrderNumberFun(){
        SearchForAnOrderByCustomerPhoneText.clear();
        LoadTheOrderInfoTable(SearchForAnOrderByOrderNumberText.getText().trim(),1,0);
    }

    public void SearchByCustomerPhoneFun(){
        SearchForAnOrderByOrderNumberText.clear();
        LoadTheOrderInfoTable(SearchForAnOrderByCustomerPhoneText.getText().trim(),1,1);
    }

    public void ShowOrderInformation(){

        try {

            SelectedOrderItem = OrderInfoTable.getSelectionModel().getSelectedItems().get(0).getOrderNumber();

            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowOrderInfo.fxml"));
            loader.load();
            Parent popup = loader.getRoot();


            try {
                ShowTheOrderInfoStage.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            ShowTheOrderInfoStage.setTitle("Order Information !");
            ShowTheOrderInfoStage.setScene(new Scene(popup,800 ,600));
            ShowTheOrderInfoStage.show();

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void TheOrderIsDoneAndReady(){


        try {
            SelectedOrderItem = OrderInfoTable.getSelectionModel().getSelectedItems().get(0).getOrderNumber();


            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("IsTheOrderReady.fxml"));
            loader.load();
            IsTheOrderReady controller = loader.getController();
            Parent popup = loader.getRoot();

            // Give popup a callback method
            controller.setup(
                    (value)->{
                        LoadTheOrderInfoTable("",0,0);
                    }
            );

            try {
                ShowQuestionAboutTheOrder.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            ShowQuestionAboutTheOrder.setTitle("Are You Sure About The Order ???");
            ShowQuestionAboutTheOrder.setScene(new Scene(popup,800 ,300));
            ShowQuestionAboutTheOrder.show();


        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void RemoveAllOrdersFromTheKitchen(){
        try {

            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AreTheOrdersReady.fxml"));
            loader.load();
            AreTheOrdersReady controller = loader.getController();
            Parent popup = loader.getRoot();

            // Give popup a callback method
            controller.setup(
                    (value)->{
                        LoadTheOrderInfoTable("",0,0);
                    }
            );

            try {
                ShowQuestionAboutTheOrdersRemoving.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            ShowQuestionAboutTheOrdersRemoving.setTitle("Are You Sure About Clear The Kitchen List ???");
            ShowQuestionAboutTheOrdersRemoving.setScene(new Scene(popup,800 ,300));
            ShowQuestionAboutTheOrdersRemoving.show();


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void EditThisOrder(){
        try {
            SelectedOrderItem = OrderInfoTable.getSelectionModel().getSelectedItems().get(0).getOrderNumber();
            SelectedOrderItemCustomerPhone = OrderInfoTable.getSelectionModel().getSelectedItems().get(0).getCustomerPhone();
            SelectedOrderItemCash = OrderInfoTable.getSelectionModel().getSelectedItems().get(0).getTotal();
            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditThisOrderNow.fxml"));
            loader.load();
            EditThisOrderNow controller = loader.getController();
            Parent popup = loader.getRoot();

            // Give popup a callback method
            controller.setup(
                    (value)->{
                        LoadTheOrderInfoTable("",0,0);
                        LoadTheCurrentCustomerTable(0);
                    }
            );

            try {
                EditThisOrderStage.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            EditThisOrderStage.setTitle("Editing The Order");
            EditThisOrderStage.setScene(new Scene(popup,1000 ,800));
            EditThisOrderStage.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }










    // ToDO Start Of The Sitting Tab




    // ToDO Vars

    @FXML
    private JFXTextField BillPrinterNameText;

    @FXML
    private JFXTextField BillReportUrlText;

    @FXML
    private JFXTextField BillPrinterForKitchenPrinter;

    @FXML
    private JFXTextField BillUrlForKitchenPath;

    @FXML
    private JFXRadioButton YesWithPointsRB;

    @FXML
    private ToggleGroup YesOrNoGroup;

    @FXML
    private JFXRadioButton NoWithPointsRB;

    // ToDo Functions


    public void SaveBillPathFunAndSaveBillPrinterNameFun(){
        try {
            if(!BillReportUrlText.getText().trim().isEmpty() && !BillPrinterNameText.getText().trim().isEmpty()) {
                OpenConnection();

                Statement SetUrlForBillPath = connection.createStatement();
                String urlPath = "INSERT INTO bill_path (path_url,printer_name) VALUES ('" + BillReportUrlText.getText().trim() +
                        "','"+BillPrinterNameText.getText().trim()+"')";
                SetUrlForBillPath.executeUpdate(urlPath);

                BillReportUrlText.setText("");
                BillPrinterNameText.setText("");

                UpdateTheBillPath();

                CloseConnection();

            }

        }catch (Exception exception){
            exception.printStackTrace();
            CloseConnection();
        }
    }

    public void SaveBillPathFunAndSaveBillPrinterNameForKitchenFun(){
        try {
            if(!BillPrinterForKitchenPrinter.getText().trim().isEmpty() && !BillUrlForKitchenPath.getText().trim().isEmpty()) {
                OpenConnection();

                Statement SetUrlForBillPath = connection.createStatement();
                String urlPath = "INSERT INTO order_bill_path (path_url,printer_name) VALUES ('" + BillUrlForKitchenPath.getText().trim() +
                        "','"+BillPrinterForKitchenPrinter.getText().trim()+"')";
                SetUrlForBillPath.executeUpdate(urlPath);

                BillPrinterForKitchenPrinter.setText("");
                BillUrlForKitchenPath.setText("");

                UpdateTheOrderBillPath();

                CloseConnection();

            }

        }catch (Exception exception){
            exception.printStackTrace();
            CloseConnection();
        }
    }

    public void CheckPayWithPointsPermission(){

        try {
            OpenConnection();


            Statement GetThePermissionMode = connection.createStatement();
            String PermissionMode = "SELECT * FROM pay_with_points";
            ResultSet resultSet = GetThePermissionMode.executeQuery(PermissionMode);

            if(resultSet.next()){

                if(resultSet.getString("permission").equals("No")){
                    PayWithPointsForEdit = "No" ;
                    PayWithPointsRB.setDisable(true);
                }else {
                    PayWithPointsForEdit = "Yes" ;
                }

            }

            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }


    }

    public void UpdateThePayWithPointsPermission(){

        try {
            OpenConnection();

            String Permission ;

            if(YesWithPointsRB.isSelected()){
                Permission = "Yes" ;
            }else {
                Permission = "No" ;
            }

            Statement UpdateThePermissionMode = connection.createStatement();
            String UpdatePermissionMode = "UPDATE pay_with_points SET permission = '"+Permission+"'";
            UpdateThePermissionMode.executeUpdate(UpdatePermissionMode);

            CheckPayWithPointsPermission();

            CloseConnection();
        }catch (Exception e){
            e.printStackTrace();
            CloseConnection();
        }

    }


    // ToDo The end of the sittings tab

    // ToDO Start Of The Sitting Tab




    // ToDo Vars


    @FXML
    private JFXTextField StatisticsCashierNameText;

    @FXML
    private JFXTextField StatisticsDeliveryManText;

    @FXML
    private JFXTextField StatisticsCustomerPhoneText;

    @FXML
    private JFXRadioButton StatisticsLobbyRB;

    @FXML
    private ToggleGroup OrderTypeGroup;

    @FXML
    private JFXRadioButton StatisticsTakeAwayRB;

    @FXML
    private JFXRadioButton StatisticsDeliveryRB;

    @FXML
    private JFXTextField StatisticsShiftNumberText;

    @FXML
    private JFXTextField StatisticsCashierName2Text;

    @FXML
    private JFXTextField StatisticsOrderNumberText;

    @FXML
    private JFXButton StatisticsShowAllOrders;

    @FXML
    private JFXButton StatisticsShowAllOrdersForCashier;

    @FXML
    private JFXButton StatisticsShowAllOrdersForDelivery;

    @FXML
    private JFXButton StatisticsShowAllOrdersForCustomer;

    @FXML
    private JFXButton StatisticsShowAllOrdersForOrderType;

    @FXML
    private JFXButton StatisticsShowAllOrdersInfoForShift;

    @FXML
    private JFXButton StatisticsShowAllShiftsButton;

    @FXML
    private JFXButton StatisticsShowAllShiftsForCashierButton;

    @FXML
    private JFXTextField StatisticsShiftForCashierText;


    // ToDo Functions

    public void ShowCustomersInfoForStatistics(){

        try {


            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerInfoForStatistics.fxml"));
            loader.load();
            Parent popup = loader.getRoot();


            try {
                StatisticsCustomerInfo.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            StatisticsCustomerInfo.setTitle("Customer Information !");
            StatisticsCustomerInfo.setScene(new Scene(popup,800 ,600));
            StatisticsCustomerInfo.show();



        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void ShowWorkersInfoForStatistics(){

        try {


            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("WorkerInfoForStatistics.fxml"));
            loader.load();
            Parent popup = loader.getRoot();


            try {
                StatisticsWorkerInfo.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            StatisticsWorkerInfo.setTitle("Workers Information !");
            StatisticsWorkerInfo.setScene(new Scene(popup,800 ,600));
            StatisticsWorkerInfo.show();



        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void ShowOrdersInfoForStatistics(int mode){

        try {


            StatisticsKeyValue = mode ;

            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OrdersInfoForStatistics.fxml"));
            loader.load();
            Parent popup = loader.getRoot();


            try {
                StatisticsOrdersInfo.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            StatisticsOrdersInfo.setTitle("Orders Information !");
            StatisticsOrdersInfo.setScene(new Scene(popup,800 ,600));
            StatisticsOrdersInfo.show();



        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @FXML
    public void ShowOrdersInfoForStatisticsByFun(ActionEvent event) {


        if(event.getSource() == StatisticsShowAllOrders){

            ShowOrdersInfoForStatistics(0);

        }else if(event.getSource() == StatisticsShowAllOrdersForCashier){


            if(!StatisticsCashierNameText.getText().trim().isEmpty()){

                StatisticsValue1 = StatisticsCashierNameText.getText().trim();

                ShowOrdersInfoForStatistics(1);
            }


        }else if(event.getSource() == StatisticsShowAllOrdersForDelivery){


            if(!StatisticsDeliveryManText.getText().trim().isEmpty()){
                StatisticsValue1 = StatisticsDeliveryManText.getText().trim();
                ShowOrdersInfoForStatistics(2);
            }


        }else if(event.getSource() == StatisticsShowAllOrdersForCustomer){


            if(!StatisticsCustomerPhoneText.getText().trim().isEmpty()){
                StatisticsValue1 = StatisticsCustomerPhoneText.getText().trim();
                ShowOrdersInfoForStatistics(3);
            }


        }else if(event.getSource() == StatisticsShowAllOrdersForOrderType){

            if(StatisticsLobbyRB.isSelected()||StatisticsDeliveryRB.isSelected()||StatisticsTakeAwayRB.isSelected()){

                if(StatisticsLobbyRB.isSelected()){
                    StatisticsValue1 = "Lobby" ;
                }else if(StatisticsDeliveryRB.isSelected()){
                    StatisticsValue1 = "Delivery" ;
                }else {
                    StatisticsValue1 = "Take Away" ;
                }

                ShowOrdersInfoForStatistics(4);

            }




        }else if(event.getSource() == StatisticsShowAllOrdersInfoForShift){

            if(!StatisticsCashierName2Text.getText().trim().isEmpty() && !StatisticsShiftNumberText.getText().trim().isEmpty()){

                StatisticsValue1 = StatisticsCashierName2Text.getText().trim() ;
                StatisticsValue2 = StatisticsShiftNumberText.getText().trim() ;
                ShowOrdersInfoForStatistics(5);

            }



        }

    }


    public void ShowShiftsInfoForStatistics(int mode){

        try {


            StatisticsKeyValue = mode ;

            // Load the popup
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StatisticsShiftsInfo.fxml"));
            loader.load();
            Parent popup = loader.getRoot();


            try {
                StatisticsShiftsInfoDetails.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            StatisticsShiftsInfoDetails.setTitle("Shifts Information !");
            StatisticsShiftsInfoDetails.setScene(new Scene(popup,800 ,600));
            StatisticsShiftsInfoDetails.show();



        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @FXML
    public void ShowShiftsInfoForStatisticsByFun(ActionEvent event) {


        if(event.getSource() == StatisticsShowAllShiftsButton){

            ShowShiftsInfoForStatistics(0);

        }else if(event.getSource() == StatisticsShowAllShiftsForCashierButton){


            if(!StatisticsShiftForCashierText.getText().trim().isEmpty()){

                StatisticsValue1 = StatisticsShiftForCashierText.getText().trim();

                ShowShiftsInfoForStatistics(1);
            }


        }

    }

    public void ShowOrderDetails(){

        if(!StatisticsOrderNumberText.getText().trim().isEmpty()){
            try {


                StatisticsValue3 = StatisticsOrderNumberText.getText().trim();

                // Load the popup
                FXMLLoader loader = new FXMLLoader(getClass().getResource("StatisticsOrderDetails.fxml"));
                loader.load();
                Parent popup = loader.getRoot();


                try {
                    StatisticsOrdersInfoDetails.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

                StatisticsOrdersInfoDetails.setTitle("Order Details !");
                StatisticsOrdersInfoDetails.setScene(new Scene(popup,800 ,600));
                StatisticsOrdersInfoDetails.show();



            }catch (Exception e){
                e.printStackTrace();
            }
        }





    }




    }

    
