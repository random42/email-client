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

    public String getUser() {
        return account.getName();
    }

    public boolean connect() {
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

    public void disconnect() {
        socket.disconnect();
    }

    public void setUser(String user) {
        account = new EmailAccount(user);
        loadInbox();
        db.debugDb();
    }

    public boolean delete(Email e) {
        if (socket.deleteEmail(e)) {
            account.deleteEmail(e);
            db.writeEmails(account.getName(), account.getInbox(), false);
            db.debugDb();
            return true;
        } else
            return false;
    }

    public boolean send(Email e) {
        if (socket.sendEmail(e)) {
            db.debugDb();
            return true;
        } else
            return false;
    }

    public Email createEmail(Set<String> receivers, String subject, String body) {
        return new Email(randomId(), getUser(), receivers, subject, body, new Date());
    }

    public boolean forward(Email e, Set<String> receivers) {
        Email n = createEmail(receivers, e.getSubject(), e.getBody());
        return send(n);
    }

    public synchronized void onEmails(List<Email> emails) {
        account.addEmails(emails);
        db.saveEmails(getUser(), emails);
        db.debugDb();
    }

    private void loadInbox() {
        account.setInbox((LinkedList<Email>) db.getEmails(account.getName()));
    }

    public int randomId() {
        return (int)(Math.random() * 1000000);
    }

    public boolean isSocketConnected() {
        return socket.isConnected();
    }

    public void clearDb() {
        account.setInbox(new LinkedList<>());
        db.clear();
        db.debugDb();
    }
}
