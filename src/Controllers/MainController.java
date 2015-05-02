package Controllers;

import Models.ClientSocket;
import javafx.beans.binding.BooleanBinding;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable{
    private Alert connectionError=new Alert(Alert.AlertType.ERROR);
    private Alert loginFailed=new Alert(Alert.AlertType.ERROR);
    private Task<Integer> loginTask;
    private Stage currentStage;

    @FXML
    public TextField Login_Username;
    @FXML
    public PasswordField Login_Password;
    @FXML
    public Label Login_Status;
    @FXML
    public Button Login_Btn;
    @FXML
    public Parent root;
    @FXML
    public void Login(){
        initializeLoginTask();
        (new Thread(loginTask)).start();
    }

    @FXML
    public void signup_btn(){
        try {
            Parent signup_fxml= FXMLLoader.load(getClass().getResource("../View/signup.fxml"));
            Stage signupStage=new Stage();
            signupStage.setScene(new Scene(signup_fxml));
            signupStage.setTitle("MyCal: SignUp");
            signupStage.show();
        }
        catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {   // initialize function is run whenever the window is started
        Login_Btn.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(Login_Username.textProperty(), Login_Password.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return Login_Btn.getText().isEmpty() || Login_Password.getText().isEmpty();
            }
        });

        connectionError.setTitle("Connection Error");
        connectionError.setContentText("Cannot connect to server.");
        connectionError.getButtonTypes().clear();
        connectionError.getButtonTypes().add(new ButtonType("Reconnect", ButtonData.OK_DONE));
        connectionError.getButtonTypes().add(new ButtonType("Cancel", ButtonData.CANCEL_CLOSE));
        loginFailed.setTitle("Login Failed.");
        loginFailed.setContentText("Invalid Username/Password.");
    }

    private void initializeLoginTask(){
        loginTask=new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                Integer result=ClientSocket.getClientSocket().login(Login_Username.getText(), Login_Password.getText());
                updateValue(result);
                return result;
            }

            @Override
            protected void succeeded(){
                super.succeeded();
                if(getValue()<0) {
                    connectionError.showAndWait().ifPresent(response -> {
                        if (response.getButtonData() == ButtonData.OK_DONE)
                            ClientSocket.connect();
                    });
                }
                else if(getValue()>0) {
                    FXMLLoader home_fxml=new FXMLLoader(getClass().getResource("../View/home.fxml"));
                    try {
                        HomeController homeController=new HomeController(ClientSocket.getLocalUser().getId(),true);
                        home_fxml.setController(homeController);
                        Stage homeStage=new Stage();
                        homeStage.setScene(new Scene(home_fxml.load()));
                        currentStage=(Stage)root.getScene().getWindow();
                        currentStage.close();
                        homeStage.show();

                    } catch (IOException e) {
                        System.out.println(e.toString());
                    }

                }
                else
                    loginFailed.showAndWait();
            }
        };
    }
}
