package com.mycompany.oceanview;

public class User {
    private int user_id;
    private String username;
    private String email;
    private String password;
    private String role;
    private boolean isFirstLogin;

    public User() {}

    public User(int id, String username, String email, String role) {
        this.user_id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    // Getters & Setters
    public int getId() { return user_id; }
    public void setId(int user_id) { this.user_id = user_id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public boolean isFirstLogin() { return isFirstLogin; }
    public void setFirstLogin(boolean isFirstLogin) { this.isFirstLogin = isFirstLogin; }
}