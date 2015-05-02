package Models;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientSocket extends Socket {   //this is a singleton class since I want only one instance during application lifecycle that can be of course re-instantiated
    private static ClientSocket clientSocket = null;
    private static boolean ClientConnected=false;
    private static DataOutputStream outToServer=null;
    private static BufferedReader inFromServer=null;
    private static LocalUser localUser=new LocalUser();
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
            else if(status.equals("Success"))
                localUser=getUser(inFromServer);
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

    public ArrayList<Event> getEvents(int id){
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
                    event.setMapUrl(inFromServer.readLine());
                    event.setLocation(inFromServer.readLine());
                    event.setDateBegin(inFromServer.readLine());
                    event.setDateEnd(inFromServer.readLine());
                    event.setTimeBegin(inFromServer.readLine());
                    event.setTimeEnd(inFromServer.readLine());
                    event.setTimeZone(inFromServer.readLine());
                    event.setN_going(Integer.parseInt(inFromServer.readLine()));
                    event.setN_invited(Integer.parseInt(inFromServer.readLine()));
                    events.add(event);
                }
            }
            else
                return null;

        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
        return events;
    }

    public boolean addEvent(String[] args,int id){
        String out="add event\n";
        out+=id+"\n"+args[0]+"\n"+args[1]+"\n"+args[2]+"\n"+args[3]+"\n"+args[4]+"\n"+args[5]+"\n"+args[6]+"\n"+args[7]+"\n";
        try{
            outToServer.writeBytes(out);
            String status=inFromServer.readLine();
        }
        catch (IOException e){
            System.out.println(e.toString());
            return false;
        }
        return true;
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

    public void InviteToEvent(String[] args){
        String out="invite to event\n";
        out+=args[0]+"\n"+args[1]+"\n";
        try {
            outToServer.writeBytes(out);
            String status=inFromServer.readLine();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void GoToEvent(String[] args){
        String out="go to event+\n"+args[0]+"\n"+args[1]+"\n";
        try{
            outToServer.writeBytes(out);
            String status=inFromServer.readLine();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void SendFriendRequest(String[] args){
        String out="send friend request\n"+args[0]+"\n"+args[1]+"\n";
        try {
            outToServer.writeBytes(out);
            String status=inFromServer.readLine();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void ConfirmFriendRequest(System[] args){
        String out="confirm request\n"+args[0]+"\n"+args[1]+"\n";
        try {
            outToServer.writeBytes(out);
            String status=inFromServer.readLine();
        } catch (IOException e) {
           System.out.println(e.toString());
        }
    }

    public void RemoveRequest(String[] args){
        String out="remove request\n"+args[0]+"\n"+args[1]+"\n";
        try {
            outToServer.writeBytes(out);
            String status=inFromServer.readLine();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private LocalUser getUser(BufferedReader reader) throws IOException{
        LocalUser user=new LocalUser();
        user.setId(Integer.parseInt(reader.readLine()));
        user.setFirstName(reader.readLine());
        user.setLastName(reader.readLine());
        user.setEmail(reader.readLine());
        user.setBirthday(reader.readLine());
        user.setPhoto(reader.readLine());
        user.setN_friends(Integer.parseInt(reader.readLine()));
        user.setN_requests(Integer.parseInt(reader.readLine()));
        user.setN_invited(Integer.parseInt(reader.readLine()));
        user.setN_going(Integer.parseInt(reader.readLine()));
        return user;
    }

    public static LocalUser getLocalUser(){
        return localUser;
    }

    public ArrayList<User> requestFriends(int id){
        ArrayList<User> users=null;
        String out="request friends\n"+id+"\n";
        try{
            outToServer.writeBytes(out);
            String status=inFromServer.readLine();
            if(status.equals("Failed"))
                return null;
            else if(status.equals("Success")){
                users=new ArrayList<>();
                while (!inFromServer.readLine().equals("end")){
                    User user=new User();
                    user.setId(Integer.parseInt(inFromServer.readLine()));
                    user.setFirstName(inFromServer.readLine());
                    user.setLastName(inFromServer.readLine());
                    users.add(user);
                }
            }

        }
        catch (IOException e){
            System.out.println(e.toString());
            return null;
        }
        return users;
    }

    public User requestUser(int id){
        String out="request user\n"+id+"\n";
        User user=new User();
        try{
            outToServer.writeBytes(out);
            String status=inFromServer.readLine();
            if(status.equals("Failed"))
                return null;
            else if(status.equals("Success")){
                user=getUser(inFromServer);
                inFromServer.readLine();
            }

        }catch (IOException e){
            System.out.println(e.toString());
        }
        return user;
    }

    public int invite(int eventid,String username,int inviterID){
        String out="invite to event\n"+eventid+"\n"+username+"\n"+inviterID+"\n";
        try{
            outToServer.writeBytes(out);
            String status=inFromServer.readLine();
            if(status.equals("Success"))
                return 0;
            else if(status.equals("Success: 0"))
                return 1;
            else if(status.equals("Success: 1"))
                return 2;
            else if(status.equals("Success: 2"))
                return 3;
        }
        catch (IOException e){
            System.out.println(e.toString());
        }
        return -1;
    }

    public ArrayList<User> requestGoing(int eventID){
        String out=" request going\n"+eventID+"\n";
        ArrayList<User> users=new ArrayList<>();
        try{
            outToServer.writeBytes(out);
            String status=inFromServer.readLine();
            if(status.equals("Success")){
                while (!inFromServer.readLine().equals("end")){
                    User user=new User();
                    user.setId(Integer.parseInt(inFromServer.readLine()));
                    user.setFirstName(inFromServer.readLine());
                    user.setLastName(inFromServer.readLine());
                    users.add(user);
                }
            }
            else return null;
        }
        catch (IOException e){
            System.out.println(e.toString());
        }
        return users;
    }

    public ArrayList<User> requestInvited(int eventID){
        String out="request invited\n"+eventID+"\n";
        ArrayList<User> users=new ArrayList<>();
        try{
            outToServer.writeBytes(out);
            String status=inFromServer.readLine();
            if(status.equals("Success")){
                while (!inFromServer.readLine().equals("end")){
                    User user =new User();
                    user.setId(Integer.parseInt(inFromServer.readLine()));
                    user.setFirstName(inFromServer.readLine());
                    user.setLastName(inFromServer.readLine());
                    users.add(user);
                }
            }
            else return null;
        }
        catch (IOException e){
            System.out.println(e.toString());
        }
        return users;
    }

    public ArrayList<Event> requestInvitations(int id){
        String out="request invitations\n"+id+"\n";
        ArrayList<Event> events=new ArrayList<>();
        try{
            outToServer.writeBytes(out);
            String status=inFromServer.readLine();
            if(status.equals("Success")){
                while (!inFromServer.readLine().equals("end")){
                    Event event=new Event();
                    event.setId(Integer.parseInt(inFromServer.readLine()));
                    event.setName(inFromServer.readLine());
                    event.setName(inFromServer.readLine());
                    events.add(event);
                }
            }
            else return null;
        }
        catch (IOException e){
            System.out.println(e.toString());
        }
        return events;
    }

    public void logout(){
        try {
            ClientSocket.getClientSocket().getOutToServer().writeBytes("finish\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
