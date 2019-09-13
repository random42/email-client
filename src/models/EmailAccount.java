package models;

import java.util.*;

public class EmailAccount extends Observable {
    private String name;
    private LinkedList<Email> inbox;

    public EmailAccount(String name) {
        this.name = name;
        inbox = new LinkedList<>();
    }

    public EmailAccount(String name, LinkedList<Email> inbox) {
        this.name = name;
        this.inbox = inbox;
    }

    public void addEmail(Email email) {
        inbox.add(email);
        notifyObservers(1);
    }

    public boolean deleteEmail(Email email) {
        boolean b = inbox.remove(email);
        if (b)
            notifyObservers();
        return b;
    }

    public void addEmails(List<Email> emails) {
        inbox.addAll(emails);
        notifyObservers(emails.size());
    }

    public LinkedList<Email> getInbox() { return inbox; }

    public void setInbox(LinkedList<Email> inbox) {
        this.inbox = inbox;
        notifyObservers();
    }

    public String getName() {return name;}

    @Override
    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
    }
}
