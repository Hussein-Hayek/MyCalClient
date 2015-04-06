import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField Login_U;
    @FXML
    private PasswordField Login_P;
    @FXML
    private Label Wrong;
    @FXML
    private void Login(ActionEvent Login_pressed){
        String out="login\n"+Login_U.getText()+"\n"+Login_P.getText()+"\n";
        try {
            Main.outToServer.writeBytes(out);
            BufferedReader response=Main.inFromServer;
            String in=response.readLine();
            Wrong.setText(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
