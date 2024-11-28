package com.example.newsrecommendationsystem;

public class SessionManager {
    private static SessionManager instance;
    private String username;

    // Private constructor to prevent instantiation
    private SessionManager() {}

    // Get the single instance of SessionManager
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Set the username when the user logs in
    public void setUsername(String username) {
        this.username = username;
    }

    // Get the current logged-in username
    public String getUsername() {
        return username;
    }

    // Clear the session (log out)
    public void clearSession() {
        this.username = null;
    }
}
