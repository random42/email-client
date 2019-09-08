import controllers.EmailCtrl;
import db.EmailDb;
import models.Email;
import socket.EmailSocket;

import java.util.Date;

public class Test {

    public static void init() {
        EmailDb.init();
        EmailSocket.getInstance().connect();
    }

    public static void main(String[] args) {
        init();
        EmailCtrl ctrl = EmailCtrl.getInstance();
        String user = "Giancarlo";
        Email test = new Email(0,user, "Mario", "oggetto", "ciao", new Date());
        ctrl.send(test);
    }

}
