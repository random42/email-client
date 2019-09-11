package socket;

import models.Email;

import java.io.Serializable;
import java.util.Date;

public class ClientMessage implements Serializable {

    public enum Type {
        SEND,
        AUTH,
        DELETE
    }

    private Type type;
    private String user;
    private Date lastDate; // date of last email that the client received or null if client has no emails
    private Email email;

    public ClientMessage(String user, Date last) { // auth
        this.type = Type.AUTH;
        this.lastDate = last;
        this.user = user;
    }

    public ClientMessage(Type type, Email email) { // send/delete
        this.type = type;
        this.email = email;
    }

    public Type getType() {return type;}

    public String getUser() {return user;}

    public Email getEmail() {return email;}

    public Date getLastDate() {return lastDate;}

    public String toString() {
        String a = ", ";
        return type + a + user + a + email + a + lastDate;
    }
}