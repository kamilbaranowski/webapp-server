package pl.kamilbaranowski.chatappserver.model;

import org.springframework.stereotype.Component;

import java.util.Date;


public class User {

    private String uid;
    private String email;
    private String username;
    private String password;
    private String status;
    private Date registrationDate;

    public User(String uid, String email, String username, String password, String status) {
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.password = password;
        this.status = status;
        this.registrationDate = new Date(System.currentTimeMillis());
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }

    public String getUid() {
        return uid;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }
}
