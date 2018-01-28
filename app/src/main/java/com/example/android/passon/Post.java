package com.example.android.passon;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ritik on 25-01-2018.
 */

public class Post {

//    private String text;
    private int index;
    private String bookPhotoUrl;
    private String bookName;
    private String posterId;
    private String posterName;
    private String filter1;
    private String filter2;
//    private ArrayList<String> likedUsers;
//    private ArrayList<String> unlikedUsers;
    private String time;
    private ArrayList<String> favourite;
    private boolean post;

//
    public Post(){
    }

    public Post(int index,String photoUrl, String currentTime, String bookname, String posterId,String posterName, String filter1,String filter2,boolean post, ArrayList saveIt) {
        this.index=index;
        this.bookPhotoUrl = photoUrl;
        this.time = currentTime;
        this.bookName = bookname;
        this.filter1 = filter1;
        this.filter2 = filter2;
        this.posterId = posterId;
        this.posterName=posterName;
        this.post=post;
        this.favourite = saveIt;

//        Log.i(posterId, "standpoint post43");
////        Log.i(text, "standpoint post44");
//        Log.i(photoUrl, "standpoint post45");
//        Log.i(time, "standpoint post46");
//        Log.i(saveIt.toString(), "standpoint post47");
////        Log.i(likedUsers.toString(), "standpoint post48");
////        Log.i(unlikedUsers.toString(), "standpoint post49");
//        Log.i(bookName, "standpoint post50");

    }
    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public boolean isPost() {
        return post;
    }

    public void setPost(boolean post) {
        this.post = post;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

//


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getPosterId() {
        return posterId;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

//
    public String getBookPhotoUrl() {
        return bookPhotoUrl;
    }

    public void setBookPhotoUrl(String bookPhotoUrl) {
        this.bookPhotoUrl = bookPhotoUrl;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String user) {
        this.bookName = user;
    }

    public ArrayList<String> getFavourite() {

        return favourite;
    }
    public void setFilter1(String filter1){
        this.filter1=filter1;
    }

    public String getFilter1(){
        return filter1;
    }

    public void setFilter2(String filter2){
        this.filter2=filter2;
    }

    public String getFilter2(){
        return filter2;
    }

    public void setFavourite(ArrayList<String> favourite) {
//        System.out.println("standpoint 84");
        this.favourite = favourite;

    }

}
