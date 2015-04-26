package Controllers;

import Models.Event;
import Models.MyCalCalendar;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.util.*;

public class HomeController implements Initializable {
    private GregorianCalendar currentDate=(GregorianCalendar)Calendar.getInstance();
    private Task<Void> update;
    @FXML
    public Label month;
    @FXML
    public ComboBox<Integer> year;
    @FXML
    public TableView<MyCalCalendar> monday;
    @FXML
    public TableColumn<MyCalCalendar,String> monday_c;
    @FXML
    public TableView<MyCalCalendar> tuesday;
    @FXML
    public TableColumn<MyCalCalendar,String> tuesday_c;
    @FXML
    public TableView<MyCalCalendar> wednesday;
    @FXML
    public TableColumn<MyCalCalendar,String> wednesday_c;
    @FXML
    public TableView<MyCalCalendar> thursday;
    @FXML
    public TableColumn<MyCalCalendar,String> thursday_c;
    @FXML
    public TableView<MyCalCalendar> friday;
    @FXML
    public TableColumn<MyCalCalendar,String> friday_c;
    @FXML
    public TableView<MyCalCalendar> saturday;
    @FXML
    public TableColumn<MyCalCalendar,String> saturday_c;
    @FXML
    public TableView<MyCalCalendar> sunday;
    @FXML
    public TableColumn<MyCalCalendar,String> sunday_c;
    @FXML
    public ListView<Event> events_list;

    private final String[] months={"January","February","March","April","May","June","July","August","September","October","November","December"};
    private ObservableList<Integer> years=FXCollections.observableArrayList();
    @Override
    public void initialize(URL url,ResourceBundle resourceBundle){
        sunday_c.setCellValueFactory(data -> new SimpleStringProperty("" + (data.getValue().getDate())));
        monday_c.setCellValueFactory(data -> new SimpleStringProperty("" + (data.getValue().getDate())));
        tuesday_c.setCellValueFactory(data->new SimpleStringProperty(""+(data.getValue().getDate())));
        wednesday_c.setCellValueFactory(data->new SimpleStringProperty(""+(data.getValue().getDate())));
        thursday_c.setCellValueFactory(data -> new SimpleStringProperty("" + (data.getValue().getDate())));
        friday_c.setCellValueFactory(data -> new SimpleStringProperty("" + (data.getValue().getDate())));
        saturday_c.setCellValueFactory(data -> new SimpleStringProperty("" + (data.getValue().getDate())));
        year.setItems(years);
        editTable(sunday);
        editTable(monday);
        editTable(tuesday);
        editTable(wednesday);
        editTable(thursday);
        editTable(friday);
        editTable(saturday);
        events_list.setCellFactory(new Callback<ListView<Event>, ListCell<Event>>() {
            @Override
            public ListCell<Event> call(ListView<Event> param) {
                return new ListCell<Event>() {
                    @Override
                    protected void updateItem(Event event, boolean empty) {
                        super.updateItem(event, empty);
                        if (event == null || empty) {
                            setText("");
                            return;
                        }
                        setText(event.getName());
                    }
                };
            }
        });
        updateCalendar();
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
                ArrayList<ObservableList<MyCalCalendar>> mycalcalendar = MyCalCalendar.getMonth(currentDate.get(Calendar.MONTH), currentDate.get(Calendar.YEAR));
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
        currentDate.add(Calendar.MONTH, -1);
        updateCalendar();
    }

    @FXML
    public void selectYear(){
        currentDate.set(Calendar.YEAR, year.getValue());
        updateCalendar();
    }

    @FXML
    public void addEvent(){
        
    }

    private void showEvents(ObservableList<Event> events){
        events_list.setItems(events);
    }

    private void editTable(TableView<MyCalCalendar> t){
        t.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null)
                    showEvents(newValue.getEvents());
            else showEvents(oldValue.getEvents());
        });
    }
}
