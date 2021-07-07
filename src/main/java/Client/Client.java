package Client;


import Interfaces.TCPConnectionObject;
import TCPConnection.TCPConnection;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class Client implements TCPConnectionObject {
    private final TCPConnection connection;
    private final String login;
    private final ArrayList<String> messages;
    private final String password;
    private  String status;

    public Client(String login,String password) throws IOException {
        Socket socket = new Socket("127.0.0.1",9090);
        this.connection = new TCPConnection(this,socket);
        this.login = login;
        messages = new ArrayList<>();
        this.status = "...";
        this.password = password;
    }

    public  synchronized void SendMessage(String message) {
        connection.SendMessage(message);
    }

    public String GetStatus() {
        return status;
    }

    public ArrayList<String> GetMessages() {
        return messages;
    }

    public void End() {
        Disconnect(connection);
        //System.out.println("I am here");
        connection.EndWork();
    }

    @Override
    public synchronized void Connect(TCPConnection connection) {
        if(!login.isEmpty() & !password.isEmpty()) {
            connection.SendLogin(login);
            connection.SendPassword(password);
        }
    }

    @Override
    public synchronized void ReceiveMessage(TCPConnection connection, String message) {
        if(message.equals("/~status~/-Yes") | message.equals("/~status~/-No")) {
            this.status = message;
        } else if(!message.equals("")) {
            messages.add(message);
        }

    }

    @Override
    public synchronized void Disconnect(TCPConnection connection) {
        connection.SendMessage("/~disconnect~/-" + login);
    }

    @Override
    public synchronized void AddException(TCPConnection connection, Exception e) {
        System.out.println("ClientError:" + e);
    }


}
