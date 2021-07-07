package TCPConnection;

import Interfaces.TCPConnectionObject;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TCPConnection {
    private final Socket socket;
    private final TCPConnectionObject object;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final  Thread ReceiveThread;

    public TCPConnection(TCPConnectionObject object, Socket socket) throws IOException {
        this.object = object;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8));
        ReceiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    object.Connect(TCPConnection.this);
                    while(!ReceiveThread.isInterrupted()) {
                            object.ReceiveMessage(TCPConnection.this, in.readLine());
                    }

                } catch (IOException e) {
                    object.AddException(TCPConnection.this, e);
                }  finally {
                    object.Disconnect(TCPConnection.this);
                }
            }
        });
        ReceiveThread.start();
    }

    public void EndWork() {
        try {
            ReceiveThread.interrupt();
            socket.close();
            in.close();
            out.close();
        } catch (Exception e) {
            object.AddException(this,e);
        }
    }
    //get
    public BufferedReader GetIn() {
        return in;
    }

    // send to connect
    public synchronized void SendPassword(String password) {
        try {
            out.write("/~password~/-" + password + "\n");
            out.flush();
        } catch (IOException e) {
            object.AddException(this,e);
        }
    }


    public synchronized void SendLogin(String login) {
        try {
            out.write("/~login~/-" +login + "\n");
            out.flush();
        } catch (IOException e) {
            object.AddException(this,e);
        }
    }


    public synchronized  void SendAnswer(String answer) {
        try {
            out.write("/~status~/-" + answer +"\n");
            out.flush();
        } catch (IOException e) {
           object.AddException(this,e);
        }
    }

    //send message
    public synchronized void SendMessage(String message) {
        try {
            out.write(message +"\n");
            out.flush();
        } catch (IOException e) {
            object.AddException(this,e);
        }
    }
}
