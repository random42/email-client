import controllers.EmailCtrl;
import db.EmailDb;
import models.Email;
import socket.EmailSocket;

import java.util.*;

public class Test {

    public static void main(String[] args) {
        EmailCtrl ctrl = EmailCtrl.getInstance();
        ctrl.init();
        String user = "asd@asd.com";
        ctrl.setUser(user);
        ctrl.debugUserInbox(user);
        if (!ctrl.openSocket()) {
            System.out.println("Could not establish a connection");
            return;
        }
        System.out.println("authenticated");
        Set<String> receivers = new HashSet<>();
        receivers.add("asd@asd.com");
        receivers.add("miao@asd.com");
        Email test = new Email(0, user, receivers, "oggetto", "ciao", new Date());
        ctrl.send(test);
    }
}
