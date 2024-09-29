package com.autohard.api.security;

public class LoginRequest {
    String username;
    String password;

    public LoginRequest(){
        super();
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
