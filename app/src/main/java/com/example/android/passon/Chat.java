package com.example.android.passon;

/**
 * Created by ritik on 26-01-2018.
 */

/*
This class is the chat message object
 */

public class Chat {
    private String chatMessage;
    private String time;



    private String senderId;
    public Chat(){

    }
    public Chat(String message,String time,String senderId){
        this.chatMessage=message;
        this.time=time;
        this.senderId=senderId;
    }
    public Chat(String message,String time){
        this.chatMessage=message;
        this.time=time;
    }
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
