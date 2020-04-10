package com.healthy.healthyaweaness.Model;

public class Users {
    String Username;
    String Email;
    String Passward;
    String Mobile;

    public Users() {
    }

    public String getUsername() {

        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassward() {
        return Passward;
    }

    public void setPassward(String passward) {
        Passward = passward;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public Users(String username, String email, String passward, String mobile) {
        Username = username;
        Email = email;
        Passward = passward;
        Mobile = mobile;
    }
}

