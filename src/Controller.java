import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField Login_U;
    @FXML
    private PasswordField Login_P;
    @FXML
    private void Login() {
        String out="login\n"+Login_U+"\n"+Login_P+"\n";
        
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
