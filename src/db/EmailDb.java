package db;

import models.Email;

public class EmailDb {
    public static final String root = "data";
    private static EmailDb instance;

    public static EmailDb getInstance() {
        if (instance == null)
            throw new Error("Db not initialized.");
        return instance;
    }

    public static void init() {
        instance = new EmailDb();
    }

    private EmailDb() {

    }

    public boolean saveEmail(Email e) {
        return true;
    }
}
