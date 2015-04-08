import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public TextField Login_Username;
    @FXML
    public PasswordField Login_Password;
    @FXML
    public Label Login_Status;
    @FXML
    public Button Login_Btn;
    @FXML
    public Button Show_Friends;
    @FXML
    public Button Show_Events;
    @FXML
    public Label List;
    @FXML
    public Label Name;
    @FXML
    public void Login(ActionEvent Login_pressed){
        String out="login\n"+Login_Username.getText()+"\n"+ Login_Password.getText()+"\n";
        Login_Btn.setDisable(true);
        try {
            Main.outToServer.writeBytes(out);
            BufferedReader response=Main.inFromServer;
            String status=response.readLine();
            if(status.equals("Failed")){
                Login_Status.setText(response.readLine());
                Login_Btn.setDisable(false);
            }
            else{
                Main.My_ID=response.readLine();
                Login_Status.setText("Success");
                OpenHome();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void ShowFriends(ActionEvent Show_Friends_Pressed){
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
            String id=response.readLine();
            while(!id.equals("end")){
                N_Friends++;
                friends+=id+'\n';
                id=response.readLine();
            }
            List.setText(friends);
            if(N_Friends==0)
                List.setText("Forever Alone ...\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void ShowEvents(){

    }
    @FXML
    public TextField fname_su;
    @FXML
    public TextField lname_su;
    @FXML
    public TextField uname_su;
    @FXML
    public TextField email_su;
    @FXML
    public PasswordField pass_su;
    @FXML
    public PasswordField cpass_su;
    @FXML
    public DatePicker bday_su;
    @FXML
    public Label signup_status;
    @FXML
    public void signup(){
        bday_su.setConverter(new StringConverter<LocalDate>() {
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
        try {
            Main.outToServer.writeBytes("signup\n"+fname_su.getText()+'\n'+lname_su.getText()+'\n'+
                    uname_su.getText()+'\n'+email_su.getText()+'\n'+pass_su.getText()+'\n'+bday_su.getValue()+'\n');
        } catch (IOException e) {

        }
        finally {
            try {
                signup_status.setText(Main.inFromServer.readLine()+Main.inFromServer.readLine());
            } catch (IOException e) {

            }
        }
    }
    @FXML
    public void signup_btn(){
        try {
            Parent signup_fxml= FXMLLoader.load(getClass().getResource("signup.fxml"));
            Stage signupStage=new Stage();
            signupStage.setScene(new Scene(signup_fxml,400,600));
            signupStage.setTitle("MyCal: SignUP");
            signupStage.show();
        }
        catch (IOException e) {
        }
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
