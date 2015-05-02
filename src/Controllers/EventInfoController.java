package Controllers;

import Models.ClientSocket;
import Models.Event;
import Models.MyCalCalendar;
import javafx.beans.binding.BooleanBinding;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EventInfoController implements Initializable{

    private Task<Integer> invitationTask;
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
    public Button viewmap_btn;
    @FXML
    public Button invite_btn;
    @FXML
    public TextField invited_username;
    @FXML
    public Button invited;
    @FXML
    public Button going;

    @FXML
    public void Invite(){
        initializeInvitationTask();
        (new Thread(invitationTask)).start();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        title.setText(event.getName());
        time.setText(event.getTimeBegin()+" to "+event.getTimeEnd());
        date.setText(event.getDateBegin()+" to "+event.getDateEnd());
        this.location.setText(event.getLocation());
        N_going.setText(""+event.getN_going());
        N_invited.setText(""+event.getN_invited());
        viewmap_btn.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(event.mapUrlProperty());
            }

            @Override
            protected boolean computeValue() {
                return event.mapUrlProperty().get().equals("");
            }
        });
        invite_btn.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(invited_username.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return invited_username.getText().equals("");
            }
        });

    }

    public EventInfoController(Event event){
        this.event=event;
    }

    @FXML
    public void viewMap(){
        FXMLLoader MapView_FXML= null;
        try {
            MapView_FXML = new FXMLLoader(getClass().getResource("../View/MapsView.fxml"));
            Stage MapViewStage=new Stage();
            MapController mapController=new MapController(false,event.getMapUrl());
            MapView_FXML.setController(mapController);
            MapViewStage.setScene(new Scene(MapView_FXML.load()));
            MapViewStage.setTitle("MyCal: Google Maps Location");
            MapViewStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeInvitationTask(){
        invitationTask=new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return ClientSocket.getClientSocket().invite(event.getId(),invited_username.getText(),ClientSocket.getLocalUser().getId());
            }
            @Override
            protected void succeeded(){
                int status=getValue();
                if(status==0)
                    showSuccessMessage(Alert.AlertType.CONFIRMATION,"Success",invited_username.getText()+" is invited");
                else if(status==1)
                    showSuccessMessage(Alert.AlertType.ERROR,"Error","User does not exist.");
                else if(status==2)
                    showSuccessMessage(Alert.AlertType.ERROR,"Error","User is already going.");
                else if(status==3)
                    showSuccessMessage(Alert.AlertType.ERROR,"Error","User is already invited.");
            }
        };
    }

    public void showSuccessMessage(Alert.AlertType type,String title,String content){
        Alert message=new Alert(type);
        message.setTitle(title);
        message.setContentText(content);
        message.showAndWait();
    }

    @FXML
    public void showGoing(){
        FXMLLoader going_fxml=new FXMLLoader(getClass().getResource("../View/users.fxml"));
        try{
            UsersListController usersListController=new UsersListController(event.getId(),1);
            going_fxml.setController(usersListController);
            Stage goingStage=new Stage();
            goingStage.setScene(new Scene(going_fxml.load()));
            goingStage.setTitle("Who's Going to " + event.getName());
            goingStage.show();
        }
        catch (IOException e){
            System.out.println(e.toString());
        }
    }

    @FXML
    public void showInvited(){
        FXMLLoader invited_fxml=new FXMLLoader(getClass().getResource("../View/users.fxml"));
        try{
            UsersListController usersListController=new UsersListController(event.getId(),2);
            invited_fxml.setController(usersListController);
            Stage invitedStage=new Stage();
            invitedStage.setTitle("Invited People.");
            invitedStage.setScene(new Scene(invited_fxml.load()));
            invitedStage.show();
        }
        catch (IOException e){
            System.out.println(e.toString());
        }
    }

}
