package Controllers;

import Models.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class EventInfoController implements Initializable{

    private Event event;
    @FXML
    public Label title;
    @FXML
    public Label date;
    @FXML
    public Label location;
    @FXML
    public Label N_going;
    @FXML
    public Label N_invited;
    @FXML
    public Label time;

    @FXML
    public void Invite(){

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        title.setText(event.getName());
        time.setText(event.getTimeBegin()+" to "+event.getTimeEnd());
        date.setText(event.getDateBegin()+" to "+event.getDateEnd());
        this.location.setText(event.getLocation());
        N_going.setText(""+event.getN_going());
        N_invited.setText(""+event.getN_invited());
    }

    public EventInfoController(Event event){
       this.event=event;
    }

}
