import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientSocket extends Socket {   //this is a singleton class since I want only one instance during application lifecycle that can be of course re-instantiated
    private static ClientSocket clientSocket = null;
    private static boolean ClientConnected=false;
    private static DataOutputStream outToServer=null;
    private static BufferedReader inFromServer=null;

    public static ClientSocket getClientSocket(){
        if(clientSocket==null){
            try {
                clientSocket= (ClientSocket)new Socket("localhost",2015);
                ClientConnected=true;
                outToServer = new DataOutputStream(clientSocket.getOutputStream());
                inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));// Buffered reader is saving input in a buffer so that when we invoke .readLine()
                                                                                                        // it will efficiently return everything before .readLine. sentence = inFromUser.readLine();
            }
            catch (IOException e){
                System.out.println(e.toString());
            }
        }
        return clientSocket;
    }

    private ClientSocket() {
    }

    public static void connect(){
        clientSocket=getClientSocket();
    }

    public boolean isClientConnected(){ return ClientConnected; }
    public DataOutputStream getOutToServer(){ return outToServer; }
    public BufferedReader getInFromServer(){ return inFromServer; }

    public int login(String username,String password){
        if(isClientConnected())
            return -1;
        String out="login\n"+username+"\n"+ password +"\n";
        try {
            outToServer.writeBytes(out);
            String status=inFromServer.readLine();
            if(status.equals("Failed")){
                return 0;
            }
        } catch (IOException e) {
            System.out.println(e.toString());
            return 0;
        }
        return 1;
    }

    public int signup(String[] attributes){

    }


}
