package Controllers;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Created by Abdallah on 17-Apr-15.
 */
public class SignUpController implements Initializable {
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
    public Button signUp_btn;
    @FXML
    public Label psswd_error;
    @FXML
    public void signup(){
        String[] attributes={fname_su.getText(),lname_su.getText(),uname_su.getText(),email_su.getText(),pass_su.getText(),
                ""+bday_su.getValue()};
        if(bday_su.getValue()==null)
            attributes[5]="0000-00-00";
        int status=Models.ClientSocket.getClientSocket().signup(attributes);
        if(status<0)
            signup_status.setText("Connection error");
        else if(status>0)
            signup_status.setText("Success");
        else
            signup_status.setText("Username already exists.");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        signUp_btn.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(fname_su.textProperty(), lname_su.textProperty(), uname_su.textProperty(), email_su.textProperty(),
                        pass_su.textProperty(), email_su.textProperty(), pass_su.textProperty(), cpass_su.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return fname_su.getText().isEmpty() || lname_su.getText().isEmpty() || uname_su.getText().isEmpty()
                        || email_su.getText().isEmpty() || pass_su.getText().isEmpty() || cpass_su.getText().isEmpty()
                 || !cpass_su.getText().equals(pass_su.getText());
            }
        });
        psswd_error.textProperty().bind(new StringBinding() {
            {
                super.bind(cpass_su.textProperty(), pass_su.textProperty());
            }
            @Override
            protected String computeValue() {
                if(!(cpass_su.getText().equals(pass_su.getText())))
                    return "Check your Password.";
                else
                    return "";
            }
        });

    }
}
