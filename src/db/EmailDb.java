package db;

import models.Email;

import java.io.*;
import java.util.*;

public class EmailDb {

    private static class AppendObjectOutputStream extends ObjectOutputStream {

        public AppendObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        protected AppendObjectOutputStream() throws IOException, SecurityException {
        }

        @Override
        protected void writeStreamHeader() throws IOException {
        }
    }

    private static final String root = "data/";
    private static EmailDb instance;

    private String user;

    public static EmailDb getInstance() {
        if (instance == null)
            instance = new EmailDb();
        return instance;
    }

    private EmailDb() {

    }

    private File createFileIfNotExists(String user) throws IOException {
        File f = new File(root + user);
        if (!f.exists()) {
            f.createNewFile();
        }
        return f;
    }

    private ObjectOutputStream getOutputStream(String user) throws IOException {
        return new ObjectOutputStream(new FileOutputStream(createFileIfNotExists(user)));
    }

    private ObjectInputStream getInputStream(String user) throws IOException {
        return new ObjectInputStream(new FileInputStream(createFileIfNotExists(user)));
    }

    public void init() {
        File dir = new File(root);
        if (!dir.exists())
            dir.mkdir();
        else if (!dir.isDirectory()) {
            dir.delete();
            dir.mkdir();
        }
    }

    public void writeEmails(String user, List<Email> emails, boolean append) {
        try {
            ObjectOutputStream out = getOutputStream(user);
            List<Email> toWrite;
            if (append) {
                toWrite = getEmails(user);
                toWrite.addAll(emails);
            }  else
                toWrite = emails;
            out.writeObject(toWrite);
            out.flush();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public List<Email> getEmails(String user) {
        List<Email> inbox = new LinkedList<>();
        try {
            ObjectInputStream in = getInputStream(user);
            inbox = (List<Email>)in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            if (!(e instanceof EOFException))
                e.printStackTrace();
        }
        return inbox;
    }

    public void saveEmails(String user, List<Email> emails) {
        writeEmails(user, emails, true);
    }

    public void clear() {
        File dir = new File(root);
        if (dir.listFiles() == null)
            return;
        for (File f : dir.listFiles()) {
            f.delete();
        }
    }
}
