package com.example.grocery.Config;

public class DatabaseConfig {
    // Configuration for the database connection
    // This class can be used to set up the database connection properties
    // such as URL, username, password, and other settings.
    
    // Example properties (these should be replaced with actual values)
    private String url = "jdbc:mssql://localhost:1433/grocery_db";
    private String username = "sa";
    private String password = "123";

    // Getters and Setters for the properties
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
