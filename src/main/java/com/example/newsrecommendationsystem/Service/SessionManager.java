package com.example.newsrecommendationsystem.Service;

import java.util.concurrent.atomic.AtomicReference;

public class SessionManager {
    private static SessionManager instance;
    private final AtomicReference<String> username;

    // Private constructor to prevent instantiation
    private SessionManager() {
        username = new AtomicReference<>(null);
    }

    // Get the single instance of SessionManager
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Set the username when the user logs in
    public void setUsername(String username) {
        this.username.set(username);
    }

    // Get the current logged-in username
    public String getUsername() {
        return username.get();
    }

    // Clear the session (log out)
    public void clearSession() {
        this.username.set(null);
    }
}
