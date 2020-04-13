package com.healthy.healthyaweaness.Model;

public class Users {
    String Username;
    String age;
    private String Password;
    String Mobile;
    String Email;

    public Users() {
    }

    public Users(String username, String age, String password, String mobile, String email) {
        Username = username;
        this.age = age;
        Password = password;
        Mobile = mobile;
        Email = email;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}

