package com.example.serverdoancn.model;

public class User {
    private String Name;
    private String Phone;
    private String Password;
    private String role;

    public User() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User(String name, String phone, String password, String role) {
        Name = name;
        Phone = phone;
        Password = password;
        this.role = role;
    }
}
