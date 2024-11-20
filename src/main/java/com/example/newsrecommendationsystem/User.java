package com.example.newsrecommendationsystem;

public class User {

    // Instance variables for name, email, password, and confirm password
    private String name;
    private String email;
    private String password;
    private String confirmPassword;

    // Constructor to initialize a user with name, email, and password
    public User(String name, String email, String password, String confirmPassword) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    // Getter and setter methods for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter methods for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and setter methods for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and setter methods for confirmPassword
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    // Method to check if password and confirm password match
    public boolean isPasswordMatch() {
        return password != null && password.equals(confirmPassword);
    }

    @Override
    public String toString() {
        return "User{name='" + name + "', email='" + email + "', password='" + password + "'}";
    }
}
