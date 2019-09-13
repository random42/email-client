package models;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class Email implements Serializable {

    private int id;
    private String sender;
    private Set<String> receivers;
    private String subject;
    private String body;
    private Date date;

    public Email(int id, String sender, Set<String> receivers, String subject, String body, Date date) {
        this.id = id;
        this.sender = sender;
        this.receivers = receivers;
        this.subject = subject;
        this.body = body;
        this.date = date;
    }

//    public String toString() {
//        String a = "'";
//        String s = "','";
//        return a + id + s + sender + s + receivers + s + subject + s + body + s + date.toString() + a;
//    }

    public String toString() {
        return "from: " + getSender() + "\nsubject: " + getSubject();
    }

    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public Set<String> getReceivers() {
        return receivers;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public Date getDate() {
        return date;
    }

    public boolean isReceiver(String user) {
        return receivers.contains(user);
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof  Email))
            return false;
        Email e = (Email)o;
        return
                id == e.getId()
                && sender.equals(e.getSender())
                && receivers.equals(e.getReceivers())
                && subject.equals(e.getSubject())
                && body.equals(e.getBody())
                && date.equals(e.getDate());
    }

}
