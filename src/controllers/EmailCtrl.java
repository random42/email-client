package controllers;

import db.EmailDb;
import models.*;
import socket.EmailSocket;
import threads.SocketListener;

import java.util.*;

public class EmailCtrl {

    private static EmailCtrl instance;

    public static EmailCtrl getInstance() {
        if (instance == null) {
            instance = new EmailCtrl();
        }
        return instance;
    }

    private EmailAccount account;
    private EmailSocket socket;
    private EmailDb db;

    private EmailCtrl() {
        socket = EmailSocket.getInstance();
        db = EmailDb.getInstance();
    }

    public void init() {
        db.init();
    }
    public EmailAccount getAccount() {return account;}

    public boolean openSocket() {
        if (!socket.connect())
            return false;
        LinkedList<Email> inbox = account.getInbox();
        Date lastDate = null;
        if (!inbox.isEmpty())
            lastDate = inbox.getLast().getDate();
        SocketListener listener = new SocketListener(socket);
        listener.start();
        return socket.authenticate(account.getName(), lastDate);
    }

    public void setUser(String user) {
        account = new EmailAccount(user);
        loadInbox();
    }

    public void delete(Email e) {
        if (!socket.deleteEmail(e)) {
            error("Socket error deleting email.");
            return;
        }
        account.deleteEmail(e);
        db.writeEmails(account.getName(), account.getInbox(), false);
    }

    public void send(Email e) {
        if (!socket.sendEmail(e)) {
            error("Socket error sending email.");
        }
    }

    public void forward(Email e, Set<String> receivers) {
        Email n = new Email(randomId(), account.getName(), receivers, e.getSubject(), e.getBody(), new Date());
        send(n);
    }

    public synchronized void onEmails(List<Email> emails) {
        account.addEmails(emails);
        db.saveEmails(account.getName(), emails);
        System.out.println("Received");
        System.out.println(emails);
    }

    private void loadInbox() {
        account.setInbox((LinkedList<Email>) db.getEmails(account.getName()));
    }

    public void error(Object msg) {
        System.out.println(msg);
    }

    public int randomId() {
        return (int)(Math.random() * 1000000);
    }

    public void debugUserInbox(String user) {
        List<Email> inbox = db.getEmails(user);
        System.out.println(inbox);
    }


}
