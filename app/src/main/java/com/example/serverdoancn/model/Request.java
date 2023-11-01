package com.example.serverdoancn.model;

import java.util.List;

public class Request {
    private String name;
    private String phone;
    private String address;
    private String total;
    private String status;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    public Request(String name, String phone, String address, String total) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.total = total;
        this.key = key;
        this.status = "0";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Request() {
    }
}
