package socket;

import java.io.*;
import java.util.*;
import java.net.*;
import models.Email;

public class EmailSocket {
    private static final String host = "localhost";
    private static final int port = 10001;
    private static EmailSocket instance;

    public static EmailSocket getInstance() {
        if (instance == null)
            instance = new EmailSocket();
        return instance;
    }


    private Socket socket;
    private boolean connected;

    private EmailSocket() { }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            connected = true;
            return true;
        } catch (Exception e) {
            if (!(e instanceof ConnectException))
                e.printStackTrace();
            return false;
        }
    }

    public void disconnect() {
        if (!isConnected())
            return;
        try {
            socket.close();
            connected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObjectOutputStream getOutput() throws IOException {
        return new ObjectOutputStream(socket.getOutputStream());
    }

    public ObjectInputStream getInput() throws IOException {
        return new ObjectInputStream(socket.getInputStream());
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean authenticate(String user, Date last) {
        ClientMessage m = new ClientMessage(user, last);
        return sendClientMessage(m);
    }


    public boolean sendEmail(Email email) {
        ClientMessage m = new ClientMessage(ClientMessage.Type.SEND, email);
        return sendClientMessage(m);
    }

    public boolean deleteEmail(Email email) {
        ClientMessage m = new ClientMessage(ClientMessage.Type.DELETE, email);
        return sendClientMessage(m);
    }

    private boolean sendClientMessage(ClientMessage m) {
        if (!isConnected())
            return false;
        try {
            getOutput().writeObject(m);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ServerMessage listen() {
        try {
            return (ServerMessage)getInput().readObject();
        } catch (IOException e) { // server disconnected
            connected = false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
