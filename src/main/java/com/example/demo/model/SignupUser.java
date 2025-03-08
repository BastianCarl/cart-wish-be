package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupUser {
    String userName;
    String password;

    public SignupUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
