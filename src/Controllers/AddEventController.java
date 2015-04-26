package Controllers;


import Models.ClientSocket;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class AddEventController implements Initializable{
    private Task<Boolean> addTask;
    private Task<Void> GoogleTask;
    private Alert connectionError=new Alert(Alert.AlertType.ERROR);
    private Alert Success=new Alert(Alert.AlertType.CONFIRMATION);

    @FXML
    public DatePicker Event_Start;
    @FXML
    public DatePicker Event_End;
    @FXML
    public TextField Event_name,Event_location;
    @FXML
    public ImageView maps;
    @FXML
    public Label Error;
    @FXML
    public ComboBox<String> hh_b,hh_e,mm_b,mm_e;
    @FXML
    public ComboBox<String> timeZone;
    @FXML
    public Button Sbmt, Cncl;
    @FXML
    public GridPane root;

    @FXML
    public void Submit(){
        initializeAddEventTask();
        (new Thread(addTask)).start();

    }

     @Override
    public void initialize(URL location, ResourceBundle resources) {
         Sbmt.disableProperty().bind(new BooleanBinding() {
             {
                 super.bind(timeZone.valueProperty(), hh_e.valueProperty(), hh_b.valueProperty(), mm_e.valueProperty(), mm_b.valueProperty(), Event_name.textProperty(), Event_location.textProperty(), Event_Start.valueProperty(), Event_End.valueProperty());
             }

             @Override
             protected boolean computeValue() {
                 return timeZone.getValue() == null || timeZone.getValue().isEmpty() || Event_name.getText().isEmpty() || Event_location.getText().isEmpty() || Event_End.getValue() == null || Event_End.getValue().toString().isEmpty();
             }

         });

         connectionError.setTitle("Connection Error");
         connectionError.setContentText("Cannot connect to server.");
         connectionError.getButtonTypes().clear();
         connectionError.getButtonTypes().add(new ButtonType("Reconnect", ButtonBar.ButtonData.OK_DONE));
         connectionError.getButtonTypes().add(new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE));
         Success.setTitle("Sucess.");
         Success.setContentText("Your event was added successfuly!");

         String[] timezones=TimeZone.getAvailableIDs();
         timeZone.setItems(FXCollections.observableArrayList(timezones));
         Event_Start.setConverter(new StringConverter<LocalDate>() {
             DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
             @Override
             public String toString(LocalDate date) {
                 if (date != null) {
                     return dateFormatter.format(date);
                 } else {
                     return "";
                 }
             }
             @Override
             public LocalDate fromString(String string) {
                 if (string != null && !string.isEmpty()) {
                     return LocalDate.parse(string, dateFormatter);
                 } else {
                     return null;
                 }
             }
         });
         Event_End.setConverter(new StringConverter<LocalDate>() {
             DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

             @Override
             public String toString(LocalDate date) {
                 if (date != null) {
                     return dateFormatter.format(date);
                 } else {
                     return "";
                 }
             }

             @Override
             public LocalDate fromString(String string) {
                 if (string != null && !string.isEmpty()) {
                     return LocalDate.parse(string, dateFormatter);
                 } else {
                     return null;
                 }
             }
         });
         String[] hrs=new String[24];
         for(int i=0;i<10;i++)
             hrs[i]="0"+i;
         for(int i=10;i<24;i++)
             hrs[i]=""+i;
         String mins[]={"00","10","20","30","40","50"};
         hh_b.setItems(FXCollections.observableArrayList(hrs));
         hh_e.setItems(FXCollections.observableArrayList(hrs));
         mm_b.setItems(FXCollections.observableArrayList(mins));
         mm_e.setItems(FXCollections.observableArrayList(mins));
    }

    private void initializeAddEventTask(){
        addTask=new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                String[] args=new String[]{
                        Event_name.getText(),Event_location.getText(),"",""+Event_Start.getValue(),""+Event_End.getValue(),
                        hh_b.getValue()+":"+mm_b.getValue()+":00",hh_e.getValue()+":"+mm_e.getValue()+":00",timeZone.getValue()
                };
                args[2]= !MapCreatorController.url.equals("") ? MapCreatorController.url:"";
                return ClientSocket.getClientSocket().addEvent(args);
            }
            @Override
            protected void succeeded(){
                super.succeeded();
                if(getValue()){
                    Success.showAndWait();
                    ((Stage)root.getScene().getWindow()).close();
                }
                else
                    connectionError.showAndWait().ifPresent(response -> {
                        if (response.getButtonData() == ButtonBar.ButtonData.OK_DONE)
                            ClientSocket.connect();
                    });
            }
        };
    }

    @FXML
    public void GoogleMaps(){
        Parent GoogleMapsWebView_fxml= null;
        try {
            GoogleMapsWebView_fxml = FXMLLoader.load(getClass().getResource("../View/MapsView.fxml"));
            Stage GoogleMapsWebView=new Stage();
            GoogleMapsWebView.setScene(new Scene(GoogleMapsWebView_fxml));
            GoogleMapsWebView.setTitle("MyCal: Google Maps Location");
            GoogleMapsWebView.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void Cancel(){
        ((Stage)root.getScene().getWindow()).close();
    }
}
