package Controllers;

import Models.ClientSocket;
import Models.Event;
import Models.MyCalCalendar;
import Models.MyCalDay;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class HomeController implements Initializable {
    private GregorianCalendar currentDate=(GregorianCalendar)Calendar.getInstance();
    private int id;
    private MyCalCalendar myCalCalendar;
    private boolean isLocal=true;
    private Task<Void> update;
    @FXML
    public Label month;
    @FXML
    public ComboBox<Integer> year;
    @FXML
    public TableView<MyCalDay> monday;
    @FXML
    public TableColumn<MyCalDay,String> monday_c;
    @FXML
    public TableView<MyCalDay> tuesday;
    @FXML
    public TableColumn<MyCalDay,String> tuesday_c;
    @FXML
    public TableView<MyCalDay> wednesday;
    @FXML
    public TableColumn<MyCalDay,String> wednesday_c;
    @FXML
    public TableView<MyCalDay> thursday;
    @FXML
    public TableColumn<MyCalDay,String> thursday_c;
    @FXML
    public TableView<MyCalDay> friday;
    @FXML
    public TableColumn<MyCalDay,String> friday_c;
    @FXML
    public TableView<MyCalDay> saturday;
    @FXML
    public TableColumn<MyCalDay,String> saturday_c;
    @FXML
    public TableView<MyCalDay> sunday;
    @FXML
    public TableColumn<MyCalDay,String> sunday_c;
    @FXML
    public ListView<Event> events_list;
    @FXML
    public Button logout_btn,refresh_btn,addEvent_btn,invitations_btn;
    @FXML
    public VBox root;

    private final String[] months={"January","February","March","April","May","June","July","August","September","October","November","December"};
    private ObservableList<Integer> years=FXCollections.observableArrayList();

    public HomeController(int id,boolean isLocal){
        this.id=id;
        this.isLocal=isLocal;
    }

    @Override
    public void initialize(URL url,ResourceBundle resourceBundle){
        myCalCalendar=new MyCalCalendar(id);
        logout_btn.setVisible(isLocal);
        invitations_btn.setDisable(!isLocal);
        addEvent_btn.setDisable(!isLocal);

        sunday_c.setCellValueFactory(data -> new SimpleStringProperty("" + (data.getValue().getDate())));
        monday_c.setCellValueFactory(data -> new SimpleStringProperty("" + (data.getValue().getDate())));
        tuesday_c.setCellValueFactory(data->new SimpleStringProperty(""+(data.getValue().getDate())));
        wednesday_c.setCellValueFactory(data->new SimpleStringProperty(""+(data.getValue().getDate())));
        thursday_c.setCellValueFactory(data -> new SimpleStringProperty("" + (data.getValue().getDate())));
        friday_c.setCellValueFactory(data -> new SimpleStringProperty("" + (data.getValue().getDate())));
        saturday_c.setCellValueFactory(data -> new SimpleStringProperty("" + (data.getValue(    ).getDate())));
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
                    {
                        super.setCursor(Cursor.HAND);
                        super.setOnMouseClicked(event ->  {
                            try {
                                if(this.getItem()!=null) {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/eventInfo.fxml"));
                                    Stage stage = new Stage();
                                    stage.setTitle("Event Info");
                                    EventInfoController controller = new EventInfoController(this.getItem());
                                    loader.setController(controller);
                                    stage.setScene(new Scene(loader.load()));
                                    stage.show();
                                }
                            }
                            catch (IOException e){
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    protected void updateItem (Event event,boolean empty){
                        super.updateItem(event, empty);
                        if (event == null || empty) {
                            setText("");
                            return;
                        }
                        setText(event.getName());
                    }
                };
                }
            }
        );
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
                myCalCalendar.update();
                for(int i=1980;i<2100;i++)
                    years.add(i);
                return null;
            }
            @Override
            protected void succeeded(){
                ArrayList<ObservableList<MyCalDay>> mycalcalendar = myCalCalendar.getMonth(currentDate.get(Calendar.MONTH), currentDate.get(Calendar.YEAR));
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
        Parent addEvent_fxml= null;
        try {
            addEvent_fxml = FXMLLoader.load(getClass().getResource("../View/eventAdder.fxml"));
            Stage eventAdder=new Stage();
            eventAdder.setScene(new Scene(addEvent_fxml));
            eventAdder.setTitle("MyCal: Create Event");
            eventAdder.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showEvents(ObservableList<Event> events){
        events_list.setItems(events);
    }

    private void editTable(TableView<MyCalDay> t){
        t.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                showEvents(newValue.getEvents());
            else showEvents(oldValue.getEvents());
        });
    }

    @FXML
    public void Refresh(){
        updateCalendar();
    }

    @FXML
    public void showFriends(){
        FXMLLoader userslist_fxml=new FXMLLoader(getClass().getResource("../View/users.fxml"));
        try{
            UsersListController usersListController=new UsersListController(id,0);
            userslist_fxml.setController(usersListController);
            Stage usersList=new Stage();
            usersList.setScene(new Scene(userslist_fxml.load()));
            usersList.setTitle("Friends");
            usersList.show();
        }
        catch (IOException e){
            System.out.println(e.toString());
        }
    }

    @FXML
    public void logout(){
        ClientSocket.getClientSocket().logout();
                ((Stage) root.getScene().getWindow()).close();
        try {
            Parent main=FXMLLoader.load(getClass().getResource("../View/login.fxml"));
            Stage stage=new Stage();
            stage.setScene(new Scene(main));
            stage.setTitle("Main");
            stage.show();
        }
        catch (IOException e){
            System.out.println(e.toString());
        }
    }
}
