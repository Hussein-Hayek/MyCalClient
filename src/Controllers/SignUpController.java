package Controllers;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;


import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    private Task<Integer> signUpTask;
    private Alert connectionError=new Alert(Alert.AlertType.ERROR);
    private Alert signUpFailed=new Alert(Alert.AlertType.ERROR);
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
        initializeSignUpTask();
        (new Thread(signUpTask)).start();
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
        connectionError.setTitle("Connection Error");
        connectionError.setContentText("Cannot connect to server.");
        connectionError.getButtonTypes().clear();
        connectionError.getButtonTypes().add(new ButtonType("Reconnect", ButtonBar.ButtonData.OK_DONE));
        connectionError.getButtonTypes().add(new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE));
        signUpFailed.setTitle("Error");
        signUpFailed.setContentText("User Already Exists");
    }

    private void initializeSignUpTask(){
        signUpTask=new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                String[] attributes={fname_su.getText(),lname_su.getText(),uname_su.getText(),email_su.getText(),pass_su.getText(),
                        ""+bday_su.getValue()};
                if(bday_su.getValue()==null)
                    attributes[5]="0000-00-00";
                int status=Models.ClientSocket.getClientSocket().signup(attributes);
                updateValue(status);
                return status;
            }
            @Override
            protected void succeeded(){
                if(getValue()<0)
                    connectionError.showAndWait();
                else if(getValue()>0)
                    signup_status.setText("Success");
                else
                    signUpFailed.showAndWait();
            }
        };
    }
}
