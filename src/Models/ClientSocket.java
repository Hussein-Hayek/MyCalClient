package Models;

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
                clientSocket= new ClientSocket();
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

    private ClientSocket() throws IOException {
        super("localhost",2015);
    }

    public static void connect(){
        clientSocket=getClientSocket();
    }

    public boolean isClientConnected(){ return ClientConnected; }
    public DataOutputStream getOutToServer(){ return outToServer; }
    public BufferedReader getInFromServer(){ return inFromServer; }

    public int login(String username,String password){  // return -1: connection error
        if(!isClientConnected())                        // return 0: invalid username
            return -1;                                  // return 1: success
        String out="login\n"+username+"\n"+ password +"\n";
        try {
            outToServer.writeBytes(out);
            String status=inFromServer.readLine();
            if(status.equals("Failed: 0"))
                return -1;
            else if(status.equals("Failed: 1"))
                return 0;
        } catch (IOException e) {
            System.out.println(e.toString());
            return -1;
        }
        return 1;
    }

    public int signup(String[] attributes){ //return -1: connection error
        if(!isClientConnected())            // return 0: username exists
            return -1;                      // return 1: success
        String out = "signup\n"+attributes[0]+'\n'+attributes[1]+'\n'+
                attributes[2]+'\n'+attributes[3]+'\n'+attributes[4]+'\n'+attributes[5]+'\n';
        try{
            outToServer.writeBytes(out);
            String status=inFromServer.readLine();
            if(status.equals("Failed: 0"))
                return -1;
            else if(status.equals("Failed: 2"))
                return 0;
        }
        catch (IOException e){
            System.out.println(e.toString());
            return -1;
        }
        return 1;
    }


}
