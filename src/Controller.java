import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField Login_Username;
    @FXML
    private PasswordField Login_Password;
    @FXML
    private Label Login_Status;
    @FXML
    private Button Login_Btn;
    @FXML
    private void Login(ActionEvent Login_pressed){
        String out="login\n"+Login_Username.getText()+"\n"+ Login_Password.getText()+"\n";
        Login_Btn.setDisable(true);
        try {
            Main.outToServer.writeBytes(out);
            BufferedReader response=Main.inFromServer;
            String status=response.readLine();
            if(status.equals("Failed")){
                Login_Status.setText("Invalid Username or Password!");
                Login_Btn.setDisable(false);
            }
            else{
                Login_Status.setText("Success");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
