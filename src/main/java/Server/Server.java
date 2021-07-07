package Server;

import DatabaseHandler.DatabaseHandler;
import Interfaces.TCPConnectionObject;
import TCPConnection.TCPConnection;

/*
System:
/~login~/
/~password~/
/~status~/
 */
import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
public class Server implements TCPConnectionObject {
    private ServerSocket socket;
    private final DatabaseHandler databaseHandler;
    private final ArrayList<TCPConnection> connectionList = new ArrayList<>();

    private Server() throws IOException {
        this.socket = new ServerSocket(9090);
        this.databaseHandler = new DatabaseHandler();
        while (true) {
            new TCPConnection(this,socket.accept());

        }
    }

    private void SendToAll(ArrayList<TCPConnection> List, String message) {
        final int size = List.size();
        for(int i = 0; i < size;i++) {
            connectionList.get(i).SendMessage(message);
        }
    }

    private String GetLogin(String login) {
        return login.substring(10);
    }

    private String GetPassword(String password) {
        return password.substring(13);
    }

    public static void main(String[] argc) {
        try {
            new Server();
        } catch (Exception e) {
            System.out.println("ServerError:" + e);
        }
    }

    @Override
    public synchronized void Connect(TCPConnection connection) {
        try {
            String login =GetLogin(connection.GetIn().readLine());
            String password = GetPassword(connection.GetIn().readLine());
            if(databaseHandler.GetUser(login,password)) {
                connection.SendAnswer("Yes");
                String connect ="/~connect~/-" + login;
                System.out.println(connect);
                SendToAll(connectionList,connect);
                connectionList.add(connection);
            } else {
                connection.SendAnswer("No");
            }
        } catch (IOException e) {
            AddException(connection, e);
        }
    }


    @Override
    public synchronized void AddException(TCPConnection connection, Exception e) {
        System.out.println("ServerError:" + e);
    }


    @Override
    public synchronized void Disconnect(TCPConnection connection) {
        connectionList.remove(connection);
    }


    @Override
    public synchronized void ReceiveMessage(TCPConnection connection, String message) {
        try {
            if (!message.equals("")) {
                System.out.println(message);
                int beginIndex = message.indexOf("-[");
                int endIndex = message.indexOf("]-");
                if(beginIndex != -1 & endIndex != -1) {
                    databaseHandler.AddMessageInDatabase(message);
                }
                int index = message.indexOf("/~disconnect~/-");
                if(index != -1) {
                    this.Disconnect(connection);
                }
                SendToAll(connectionList, message);
            }
        } catch (Exception e) {
            //AddException(connection,e);
        }
    }
}
