package com.example.android.passon;

import android.net.Uri;
import java.util.ArrayList;

/**
 * Created by ritik on 26-01-2018.
 */

public class UserInfo {

    private String userName;
    private String userId;
    private Uri dp;
    private String emailId;
    private int rating;
    private String address;
    private int phoneNo;
    private int profileNo;
    private ArrayList<String> connectedUsers;
    private ArrayList<String> connectionRequestUsers;

    public UserInfo() {
    }

    public UserInfo(int profileNo, String username, String userid, Uri dp, String emailId, int rating, String address, int phoneNo, ArrayList connectedUsers,ArrayList requestededUsers) {
        this.profileNo = profileNo;
        this.userName = username;
        this.userId = userid;
        this.dp = dp;
        this.emailId = emailId;
        this.rating = rating;
        this.address = address;
        this.phoneNo = phoneNo;
        this.connectedUsers = connectedUsers;
        this.connectionRequestUsers=requestededUsers;
    }
    public ArrayList<String> getConnectionRequestUsers() {
        return connectionRequestUsers;
    }

    public void setConnectionRequestUsers(ArrayList<String> connectionRequestUsers) {
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

    public Uri getDp() {
        return dp;
    }

    public void setDp(Uri dp) {
        this.dp = dp;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
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
