package com.example.android.passon;

/**
 * Created by ritik on 29/1/18.
 */

/*
object for chat selection menu
 */
public class ChatHead {
    private String userId;
    private String username;

    public String getUserId() {
        return userId;
    }

    public ChatHead(){

    }
    public ChatHead(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
