package controllers;

import db.EmailDb;
import models.Email;
import socket.EmailSocket;

public class EmailCtrl {

    private static EmailCtrl instance;

    public static EmailCtrl getInstance() {
        if (instance == null) {
            instance = new EmailCtrl();
        }
        return instance;
    }

    private String user;
    private EmailSocket socket;
    private EmailDb db;

    private EmailCtrl() {
        socket = EmailSocket.getInstance();
        db = EmailDb.getInstance();
    }

    public void setUser(String u) {
        user = u;
    }

    public void send(Email e) {
        socket.send(e);
        db.saveEmail(e);
    }

    public void onEmailReceived(Email e) {
        db.saveEmail(e);
        System.out.println("Received");
        System.out.println(e);
    }
}
