package org.example.testfx2.bean;

import lombok.Data;

@Data
public class SessionData {

    private static final SessionData instance = new SessionData();

    private int selectedQuartalId;

    private String currentUser;
    public static SessionData getInstance() {
        return instance;
    }

}
