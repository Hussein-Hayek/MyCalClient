package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    private GregorianCalendar currentDate=(GregorianCalendar)Calendar.getInstance();
    @FXML
    public Label month;
    @FXML
    public ComboBox<Integer> year;
    @FXML
    public TableView monday;
    @FXML
    public TableView tuesday;
    @FXML
    public TableView wednesday;
    @FXML
    public TableView thursday;
    @FXML
    public TableView friday;
    @FXML
    public TableView saturday;
    @FXML
    public TableView sunday;
    final String[] months={"January","February","March","April","May","June","July","August","September","October","November","December"};
    @Override
    public void initialize(URL url,ResourceBundle resourceBundle){
        initializeCalendar();
    }

    private void initializeCalendar(){
        month.setText(months[currentDate.get(Calendar.MONTH)]);
        year.setValue(currentDate.get(Calendar.YEAR));

    }
}
