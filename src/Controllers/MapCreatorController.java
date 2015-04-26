package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MapCreatorController implements Initializable{

    @FXML
    public Button apply,cancel;
    @FXML
    public WebView web;
    @FXML
    public GridPane root;

    public static String url="";
    @FXML
    public void Apply(){
        url=web.getEngine().getLocation();
        ((Stage)root.getScene().getWindow()).close();
    }

    @FXML
    public void Cancel(){
        ((Stage)root.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        web.getEngine().setJavaScriptEnabled(true);
        web.getEngine().load("http://localhost/addpin.html");
    }
}
