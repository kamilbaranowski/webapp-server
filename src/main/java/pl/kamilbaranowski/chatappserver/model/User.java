package pl.kamilbaranowski.chatappserver.model;

import org.springframework.stereotype.Component;

import java.util.Date;


public class User {

    private String email;
    private String username;
    private String password;
    private boolean active;
    private Date registrationDate;

    public User(String email, String username, String password, boolean active) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.active = active;
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

    public boolean isActive() {
        return active;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }
}
