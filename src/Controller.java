import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    private TextField Login_Username;
    @FXML
    private PasswordField Login_Password;
    @FXML
    private Label Login_Status;
    @FXML
    private Button Login_Btn;
    @FXML
    private Button Show_Friends;
    @FXML
    private Button Show_Events;
    @FXML
    private Text List;
    @FXML
    private Text Name;
    @FXML
    private void Login(ActionEvent Login_pressed){
        String out="login\n"+Login_Username.getText()+"\n"+ Login_Password.getText()+"\n";
        Login_Btn.setDisable(true);
        try {
            Main.outToServer.writeBytes(out);
            BufferedReader response=Main.inFromServer;
            String status=response.readLine();
            Main.My_ID=response.readLine();
            if(status.equals("Failed")){
                Login_Status.setText("Invalid Username or Password!");
                Login_Btn.setDisable(false);
            }
            else{
                Login_Status.setText("Success");
                OpenHome();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowFriends(ActionEvent Show_Friends_Pressed){
        String out="request friends\n"+Main.My_ID+"\n";
        try {
            Main.outToServer.writeBytes(out);
            BufferedReader response=Main.inFromServer;
            String status=response.readLine();
            if(status.equals("Failed")){
                List.setText("Failed..\n");
                return;
            }
            List.setText(status);
            String friends="";
            int N_Friends=0;
            String id;
            while((id=response.readLine())!=null){
                N_Friends++;
                friends+=id+'\n';
            }
            List.setText(friends);
            if(N_Friends==0)
                List.setText("Forever Alone ...\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowEvents(){

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void OpenHome()throws Exception{
        Stage stage=new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        stage.setTitle("Home");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }
}
