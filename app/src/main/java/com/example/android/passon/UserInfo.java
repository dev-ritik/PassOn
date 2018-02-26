package com.example.android.passon;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by ritik on 26-01-2018.
 */

/*
object to store all user related information
 */

public class UserInfo {

    private String userName;
    private String userId;
    private String dpUrl;
    private String emailId;
    private String rating;
    //total marks+"+"+total people rated
    private String address;
    private int phoneNo;
    private int profileNo;
    private ArrayList<String> connectedUsers;
    private ArrayList<ChatHead> connectionRequestUsers;
    private ArrayList<String> notifications;

    public UserInfo() {
    }

    public UserInfo(int profileNo, String username, String userid,String photoUrl, String emailId, String rating, String address, int phoneNo, ArrayList<String> connectedUsers, ArrayList<ChatHead> requestededUsers, ArrayList<String> notifications) {
        this.profileNo = profileNo;
        this.userName = username;
        this.userId = userid;
        this.dpUrl=photoUrl;
        this.emailId = emailId;
        this.rating = rating;
        this.address = address;
        this.phoneNo = phoneNo;
        this.connectedUsers = connectedUsers;
        this.connectionRequestUsers = requestededUsers;
        this.notifications = notifications;

    }


    public ArrayList<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<String> notifications) {
        this.notifications = notifications;
    }

    public ArrayList<ChatHead> getConnectionRequestUsers() {
        return connectionRequestUsers;
    }

    public void setConnectionRequestUsers(ArrayList<ChatHead> connectionRequestUsers) {
        this.connectionRequestUsers = connectionRequestUsers;
    }

    public String getAddress() {
        return address;
    }


    public ArrayList<String> getConnectedUsers() {
        return connectedUsers;
    }

    public void setConnectedUsers(ArrayList<String> connectedUsers) {
        this.connectedUsers = connectedUsers;
    }

    public int getProfileNo() {
        return profileNo;
    }

    public void setProfileNo(int profileNo) {
        this.profileNo = profileNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getdpUrl() {
        return dpUrl;
    }

    public void setdpUrl(String dpUrl) {
        this.dpUrl = dpUrl;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(int phoneNo) {
        this.phoneNo = phoneNo;
    }

}
