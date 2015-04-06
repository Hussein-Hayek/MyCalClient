
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;

public class Main extends Application {
    private static Socket clientSocket=null;
    private static boolean connected=false;
    private static DataOutputStream outToServer=null;
    private static BufferedReader inFromServer=null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("MyCal: Login");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) throws Exception
    {
        String response;
        try {
            clientSocket= new Socket("hostname",2015);
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Buffered reader is saving input in a buffer so that when we invoke .readLine() // it will efficiently return everything before .readLine. sentence = inFromUser.readLine();
            response=inFromServer.readLine();
            if (response.equals("Welcome")) connected = true;
        }
        catch (IOException e) {
        }
        launch(args);
    }
}
