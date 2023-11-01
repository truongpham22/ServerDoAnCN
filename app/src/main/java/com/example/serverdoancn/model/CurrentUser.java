package com.example.serverdoancn.model;

public class CurrentUser {
    public static User currentUser;
    public static String UPDATE = "UPDATE";
    public static String DELETE = "DELETE";
    public static final int PICK_IMAGE_REQUEST = 71;
    public static String convertStatus(String status){
        if (status.equals("0"))
            return "Đã nhận";
        else if (status.equals("1")) {
            return "Đang giao";
        }
        else {
            return "Đã giao";
        }
    }

}
