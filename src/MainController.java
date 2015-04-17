import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MainController implements Initializable{

    @FXML
    public TextField Login_Username;
    @FXML
    public PasswordField Login_Password;
    @FXML
    public Label Login_Status;
    @FXML
    public Button Login_Btn;

    @FXML
    public void Login(){
        OpenHome();
        int status=ClientSocket.getClientSocket().login(Login_Username.getText(),Login_Password.getText());
        if(status>0)
            OpenHome();
        else if(status<0)
            showErrorMessage("Connection Error","Cannot connect to server.");
        else
            showErrorMessage("Login Failed","Invalid Username/Password.");
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
        String[] attributes={fname_su.getText(),lname_su.getText(),uname_su.getText(),email_su.getText(),pass_su.getText(),cpass_su.getText(),
            ""+bday_su.getValue()};
        Boolean
        //int status=ClientSocket.getClientSocket().signup(attributes);
    }

    @FXML
    public void signup_btn(){
        try {
            Parent signup_fxml= FXMLLoader.load(getClass().getResource("layouts/signup.fxml"));
            Stage signupStage=new Stage();
            signupStage.setScene(new Scene(signup_fxml,400,600));
            signupStage.setTitle("MyCal: SignUp");
            signupStage.show();
        }
        catch (IOException e) {
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {   // initialize function is run whenever the window is started
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
    }

    public void OpenHome(){
        try {
            Stage stage=new Stage();
            Parent root = null;
            root = FXMLLoader.load(getClass().getResource("layouts/home.fxml"));
            stage.setTitle("Home");
            stage.setScene(new Scene(root, 600,400));
            stage.show();
        } catch (IOException e) {

        }

    }

    public void showErrorMessage(String title,String content){
        Alert connectionError=new Alert(Alert.AlertType.ERROR);
        connectionError.setTitle(title);
        connectionError.setContentText(content);
        connectionError.showAndWait();
    }

}
