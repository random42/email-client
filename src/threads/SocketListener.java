package threads;

import models.Email;
import socket.EmailSocket;
import db.EmailDb;
import controllers.EmailCtrl;

public class SocketListener extends Thread {

    private String user;
    private EmailSocket socket;
    private EmailCtrl ctrl;

    public SocketListener() {
        socket = EmailSocket.getInstance();
        ctrl = EmailCtrl.getInstance();
    }

    @Override
    public void run() {
        while (true) {
            Email e = socket.listen();
            ctrl.onEmailReceived(e);
        }
    }
}
