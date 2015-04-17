package Controllers;

import Models.ClientSocket;
import javafx.beans.binding.BooleanBinding;
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

import java.io.IOException;
import java.net.URL;
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
        /*int status= ClientSocket.getClientSocket().login(Login_Username.getText(),Login_Password.getText());
        if(status>0)
            OpenHome();
        else if(status<0)
            showErrorMessage("Connection Error","Cannot connect to server.");
        else
            showErrorMessage("Login Failed","Invalid Username/Password.");*/
    }

    @FXML
    public void signup_btn(){
        try {
            Parent signup_fxml= FXMLLoader.load(getClass().getResource("../View/signup.fxml"));
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
        Login_Btn.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(Login_Username.textProperty(),Login_Password.textProperty());
            }
            @Override
            protected boolean computeValue() {
                return Login_Btn.getText().isEmpty() || Login_Password.getText().isEmpty();
            }
        });
    }
    public void OpenHome(){
        try {
            Stage stage=new Stage();
            Parent root = null;
            root = FXMLLoader.load(getClass().getResource("../View/home.fxml"));
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
