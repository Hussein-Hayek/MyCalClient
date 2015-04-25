package Controllers;


import Models.ClientSocket;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class AddEventController implements Initializable{
    private Task<Void> addTask;
    private Task<Void> GoogleTask;
    private Alert connectionError=new Alert(Alert.AlertType.ERROR);
    private Alert signUpFailed=new Alert(Alert.AlertType.ERROR);

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
    public ComboBox<Integer> hh_b,hh_e,mm_b,mm_e;
    @FXML
    public ComboBox<String> timeZone;

    @FXML
    public void Submit(){
        initializeAddEventTask();
        (new Thread(addTask)).start();

    }

     @Override
    public void initialize(URL location, ResourceBundle resources) {
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
         Integer[] hrs=new Integer[24];
         for(int i=1;i<25;i++)
             hrs[i-1]=i;
         Integer[] mins=new Integer[59];
         for(int i=1;i<60;i++)
             mins[i-1]=i;
         hh_b.setItems(FXCollections.observableArrayList(hrs));
         hh_e.setItems(FXCollections.observableArrayList(hrs));
         mm_b.setItems(FXCollections.observableArrayList(mins));
         mm_e.setItems(FXCollections.observableArrayList(mins));
    }

    private void initializeAddEventTask(){
        addTask=new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ClientSocket.getClientSocket().addEvent(new String[]{
                        Event_name.getText(),Event_location.getText(),"google maps",""+Event_Start.getValue(),""+Event_End.getValue(),
                        hh_b.getValue()+":"+mm_b.getValue()+":00",hh_e.getValue()+":"+mm_e.getValue()+":00",timeZone.getValue()
                });
                return null;
            }
            @Override
            protected void succeeded(){

            }
        };
    }
}
