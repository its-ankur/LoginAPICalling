package com.example.loginapicalling;

public class LoginRequest {
    private String username;
    private String password;
    private int expiresInMins;
    public LoginRequest(String username,String password,int expiresInMins){
        this.username=username;
        this.password=password;
        this.expiresInMins=expiresInMins;
    }

}
