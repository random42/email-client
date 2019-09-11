package socket;

import models.Email;

import java.io.Serializable;
import java.util.*;

public class ServerMessage implements Serializable {

    enum Type {
        EMAIL
    }

    private Type type;
    private List<Email> emails;

    public ServerMessage(Email email) {
        type = Type.EMAIL;
        emails = new LinkedList<>();
        emails.add(email);
    }

    public ServerMessage(List<Email> emails) {
        this.type = Type.EMAIL;
        this.emails = emails;
    }

    public Type getType() {return type;}

    public List<Email> getEmails() {return emails;}


}
