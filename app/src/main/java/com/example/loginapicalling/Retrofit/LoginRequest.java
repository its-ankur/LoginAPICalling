// LoginRequest.java
package com.example.loginapicalling.Retrofit;

public class LoginRequest {

    // Username of the user trying to log in
    private String username;

    // Password of the user trying to log in
    private String password;

    // Duration in minutes for which the login is valid
    private int expiresInMins;

    /**
     * Constructor to initialize the LoginRequest object.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @param expiresInMins The duration in minutes for which the login is valid.
     */
    public LoginRequest(String username, String password, int expiresInMins) {
        this.username = username;
        this.password = password;
        this.expiresInMins = expiresInMins;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter for expiresInMins
    public int getExpiresInMins() {
        return expiresInMins;
    }

    // Setter for expiresInMins
    public void setExpiresInMins(int expiresInMins) {
        this.expiresInMins = expiresInMins;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", expiresInMins=" + expiresInMins +
                '}';
    }
}
