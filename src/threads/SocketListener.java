package threads;

import models.Email;
import socket.EmailSocket;
import db.EmailDb;
import controllers.EmailCtrl;
import socket.ServerMessage;

public class SocketListener extends Thread {

    private String user;
    private EmailSocket socket;
    private EmailCtrl ctrl;

    public SocketListener(EmailSocket socket) {
        this.socket = socket;
        ctrl = EmailCtrl.getInstance();
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            ServerMessage msg = socket.listen();
            if (msg != null)
                ctrl.onEmails(msg.getEmails());
        }
    }
}
