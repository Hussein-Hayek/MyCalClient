package Controllers;

import Models.MyCalCalendar;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    private GregorianCalendar currentDate=(GregorianCalendar)Calendar.getInstance();
    private Task<Void> update;
    @FXML
    public Label month;
    @FXML
    public ComboBox<Integer> year;
    @FXML
    public TableView monday;
    @FXML
    public TableColumn<MyCalCalendar,String> monday_c;
    @FXML
    public TableView tuesday;
    @FXML
    public TableColumn<MyCalCalendar,String> tuesday_c;
    @FXML
    public TableView wednesday;
    @FXML
    public TableColumn<MyCalCalendar,String> wednesday_c;
    @FXML
    public TableView thursday;
    @FXML
    public TableColumn<MyCalCalendar,String> thursday_c;
    @FXML
    public TableView friday;
    @FXML
    public TableColumn<MyCalCalendar,String> friday_c;
    @FXML
    public TableView saturday;
    @FXML
    public TableColumn<MyCalCalendar,String> saturday_c;
    @FXML
    public TableView sunday;
    @FXML
    public TableColumn<MyCalCalendar,String> sunday_c;

    private final String[] months={"January","February","March","April","May","June","July","August","September","October","November","December"};
    private ObservableList<Integer> years=FXCollections.observableArrayList();
    @Override
    public void initialize(URL url,ResourceBundle resourceBundle){
        updateCalendar();
        sunday_c.setCellValueFactory(data -> new SimpleStringProperty("" + (data.getValue().getDate())));
        monday_c.setCellValueFactory(data -> new SimpleStringProperty("" + (data.getValue().getDate())));
        tuesday_c.setCellValueFactory(data->new SimpleStringProperty(""+(data.getValue().getDate())));
        wednesday_c.setCellValueFactory(data->new SimpleStringProperty(""+(data.getValue().getDate())));
        thursday_c.setCellValueFactory(data->new SimpleStringProperty(""+(data.getValue().getDate())));
        friday_c.setCellValueFactory(data -> new SimpleStringProperty("" + (data.getValue().getDate())));
        saturday_c.setCellValueFactory(data -> new SimpleStringProperty("" + (data.getValue().getDate())));
        year.setItems(years);
    }

    private void updateCalendar(){
        initializeUpdateTask();
        (new Thread(update)).start();
        month.setText(months[currentDate.get(Calendar.MONTH)]);
        year.setValue(currentDate.get(Calendar.YEAR));
    }
    private void initializeUpdateTask(){
        update=new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                MyCalCalendar.update();
                for(int i=1980;i<2100;i++)
                    years.add(i);
                return null;
            }
            @Override
            protected void succeeded(){
                ArrayList<ObservableList<MyCalCalendar>> mycalcalendar=MyCalCalendar.getMonth(currentDate.get(Calendar.MONTH),currentDate.get(Calendar.YEAR));
                sunday.setItems(mycalcalendar.get(0));
                monday.setItems(mycalcalendar.get(1));
                tuesday.setItems(mycalcalendar.get(2));
                wednesday.setItems(mycalcalendar.get(3));
                thursday.setItems(mycalcalendar.get(4));
                friday.setItems(mycalcalendar.get(5));
                saturday.setItems(mycalcalendar.get(6));
            }

        };
    }

    @FXML
    public void addMonth(){
        currentDate.add(Calendar.MONTH,1);
        updateCalendar();
    }

    @FXML
    public void subMonth(){
        currentDate.add(Calendar.MONTH,-1);
        updateCalendar();
    }

    @FXML
    public void selectYear(){
        currentDate.set(Calendar.YEAR,year.getValue());
        updateCalendar();
    }

    @FXML
    public void addEvent(){
        
    }

}
