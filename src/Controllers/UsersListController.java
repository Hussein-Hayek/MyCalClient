package Controllers;

import Models.ClientSocket;
import Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UsersListController implements Initializable{
    private int requested_id;
    private Task<ObservableList<User>> usersRequestTask;
    private UserRequestTask openUserCalendar;
     private int choice;
    @FXML
    public ListView<User> users_list;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        users_list.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
            @Override
            public ListCell<User> call(ListView<User> param) {
                return new ListCell<User>(){
                    {
                        super.setCursor(Cursor.HAND);
                        super.setOnMouseClicked(event -> {
                            if(getItem()!=null) {
                                initializeSingleUserRequestTask();
                                openUserCalendar.setId(getItem().getId());
                                (new Thread(openUserCalendar)).start();
                            }
                        });
                    }
                    @Override
                    protected void updateItem(User item,boolean empty){
                        super.updateItem(item, empty);
                        if(item==null || empty)
                            setText("");
                        else setText(item.getFirstName()+" "+item.getLastName());
                    }
                };
            }
        });
        initializeUsersRequestTask();
        (new Thread(usersRequestTask)).start();
    }

    public UsersListController(int id,int choice){
        requested_id=id;
        this.choice=choice;
    }

    public void initializeUsersRequestTask(){
        usersRequestTask=new Task<ObservableList<User>>() {
            @Override
            protected ObservableList<User> call() throws Exception {
                ObservableList<User> users=null;
                if(choice==0){
                    users= FXCollections.observableArrayList(ClientSocket.getClientSocket().requestFriends(requested_id));
                }
                else if(choice==1){
                    users=FXCollections.observableArrayList(ClientSocket.getClientSocket().requestGoing(requested_id));
                }
                else if(choice==2){
                    users=FXCollections.observableArrayList(ClientSocket.getClientSocket().requestInvited(requested_id));
                }
                return users;
            }
            @Override
            protected void succeeded(){
                users_list.setItems(getValue());
            }
        };
    }

    public void initializeSingleUserRequestTask(){
        openUserCalendar =new UserRequestTask();
    }
    public class UserRequestTask extends Task<User>{
        private int id;
        public void setId(int id) {
            this.id = id;
        }
        @Override
        protected User call() throws Exception {
            return ClientSocket.getClientSocket().requestUser(id);
        }

        @Override
        protected void succeeded(){
            FXMLLoader home_fxml=new FXMLLoader(getClass().getResource("../View/home.fxml"));
            try{
                HomeController homeController=new HomeController(id,false);
                home_fxml.setController(homeController);
                Stage userStage=new Stage();
                userStage.setScene(new Scene(home_fxml.load()));
                userStage.setTitle(getValue().getFirstName()+" "+getValue().getLastName());
                userStage.show();
            }
            catch (IOException e){
                System.out.println(e.toString());
            }
        }
    }
}
