package Interfaces;

import TCPConnection.TCPConnection;

public interface TCPConnectionObject {
    void Connect(TCPConnection connection);
    void ReceiveMessage(TCPConnection connection,String message);
    void Disconnect(TCPConnection connection);
    void AddException(TCPConnection connection,Exception e);
}

