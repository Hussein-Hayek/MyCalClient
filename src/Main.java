
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;

public class Main extends Application {
    public static Socket clientSocket=null;
    public static boolean connected=false;
    public static DataOutputStream outToServer=null;
    public static BufferedReader inFromServer=null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("MyCal: Login");
        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.show();
    }


    public static void main(String[] args) throws Exception
    {
        String response="disconnected";
        try {
            clientSocket= new Socket("localhost",2015);
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Buffered reader is saving input in a buffer so that when we invoke .readLine() // it will efficiently return everything before .readLine. sentence = inFromUser.readLine();
            outToServer.writeBytes("check connection\n");
            response=inFromServer.readLine();
            System.out.println(response);
            if (response.equals("Welcome")) connected = true;
        }
        catch (IOException e) {
        }
        launch(args);
    }
}
