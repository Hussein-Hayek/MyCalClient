package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class MapController implements Initializable{
    private boolean withPinAdder=true;
    @FXML
    public Button apply,cancel;
    @FXML
    public WebView web;
    @FXML
    public GridPane root;
    @FXML
    public Label title;
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
        apply.setVisible(withPinAdder);
        cancel.setVisible(withPinAdder);
        title.setVisible(withPinAdder);
        web.getEngine().setJavaScriptEnabled(true);
        web.getEngine().load(url);
    }

    public MapController(boolean withPinAdder,String url){
        this.url=url;
        this.withPinAdder=withPinAdder;
    }
}
