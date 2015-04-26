package Models;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ClientSocket extends Socket {   //this is a singleton class since I want only one instance during application lifecycle that can be of course re-instantiated
    private static ClientSocket clientSocket = null;
    private static boolean ClientConnected=false;
    private static DataOutputStream outToServer=null;
    private static BufferedReader inFromServer=null;
    private static int id;
    public static ClientSocket getClientSocket(){
        if(clientSocket==null)
            connect();
        return clientSocket;
    }

    private ClientSocket(String host,int port) throws IOException {
        super(host,port);
    }

    private ClientSocket(){

    }

    public static void connect(){
        if(clientSocket==null || !ClientConnected){
            try {
                clientSocket= new ClientSocket("localhost",2015);
                ClientConnected=true;
                outToServer = new DataOutputStream(clientSocket.getOutputStream());
                inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));// Buffered reader is saving input in a buffer so that when we invoke .readLine()
                // it will efficiently return everything before .readLine. sentence = inFromUser.readLine();
            }
            catch (IOException e){
                System.out.println(e.toString());
                clientSocket=new ClientSocket();
            }
        }
    }

    public boolean isClientConnected(){ return ClientConnected; }
    public DataOutputStream getOutToServer(){ return outToServer; }
    public BufferedReader getInFromServer(){ return inFromServer; }

    public int login(String username,String password){
        if (!isClientConnected())
            return -1;
        String out = "login\n" + username + "\n" + password + "\n";
        try {
            outToServer.writeBytes(out);
            String status = inFromServer.readLine();
            if (status.equals("Failed: 0"))
                return -1;
            else if (status.equals("Failed: 1"))
                return 0;
            else
                id=Integer.parseInt((status.split(" "))[1]);
        }
        catch(IOException e) {
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

    public ArrayList<Event> getEvents(){
        String out="request events\n"+id+"\n";
        ArrayList<Event> events=new ArrayList<>();
        try {
            outToServer.writeBytes(out);
            String status=inFromServer.readLine();
            if(status.equals("Success")){
                while (!inFromServer.readLine().equals("end")){
                    Event event=new Event();
                    event.setId(Integer.parseInt(inFromServer.readLine()));
                    event.setCreator_id(Integer.parseInt(inFromServer.readLine()));
                    event.setName(inFromServer.readLine());
                    event.setDateBegin(inFromServer.readLine());
                    event.setDateEnd(inFromServer.readLine());
                    event.setTimeBegin(inFromServer.readLine());
                    event.setTimeEnd(inFromServer.readLine());
                    event.setN_going(Integer.parseInt(inFromServer.readLine()));
                    event.setN_invited(Integer.parseInt(inFromServer.readLine()));
                    events.add(event);
                }
            }
            else
                return null;

        } catch (IOException e) {
            System.out.println(e.toString());
            return null;
        }
        return events;
    }

    public void addEvent(String[] args){
        String out="add event\n";
        out+=id+"\n"+args[0]+"\n"+args[1]+"\n"+args[2]+"\n"+args[3]+"\n"+args[4]+"\n"+args[5]+"\n"+args[6]+"\n"+args[7]+"\n";
        try{
            outToServer.writeBytes(out);
            String status=inFromServer.readLine();
        }
        catch (IOException e){
            System.out.println(e.toString());
        }
    }

    public void finish(){
        if(!isClientConnected())
            return;
        try {
            outToServer.writeBytes("exit\n");
        }
        catch (IOException e) {
            System.out.println(e.toString());
        }
    }

}
