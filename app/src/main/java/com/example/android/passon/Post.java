package com.example.android.passon;

import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ritik on 25-01-2018.
 */
/*
object for post and request handling
 */
public class Post {

    //    private String text;
    private int index;
    private Uri bookPhotoUrl;
    private String bookName;
    private String posterId;
    private String posterName;
    private String filter1;
    private String filter2;
    private ArrayList<String> bookRequestUsers;
    private String time;
    private boolean post;

    public Post() {
    }

    public Post(int index, Uri photoUrl, String currentTime, String bookname, String posterId, String posterName, String filter1, String filter2,ArrayList<String> bookRequestUsers, boolean post, String phonenumber, String institute) {
        this.index = index;
        this.bookPhotoUrl = photoUrl;
        this.time = currentTime;
        this.bookName = bookname;
        this.filter1 = filter1;
        this.filter2 = filter2;
        this.posterId = posterId;
        this.posterName = posterName;
        this.post = post;
        this.institute = institute;
        this.phonenumber = phonenumber;
        this.bookRequestUsers=bookRequestUsers;

    }
    //    private ArrayList<String> unlikedUsers;

    public ArrayList<String> getBookRequestUsers() {
        return bookRequestUsers;
    }

    public void setBookRequestUsers(ArrayList<String> bookRequestUsers) {
        this.bookRequestUsers = bookRequestUsers;
    }


    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    private String phonenumber;
    private String institute;

//

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
    public Uri getBookPhotoUrl() {
        return bookPhotoUrl;
    }

    public void setBookPhotoUrl(Uri bookPhotoUrl) {
        this.bookPhotoUrl = bookPhotoUrl;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String user) {
        this.bookName = user;
    }

    public void setFilter1(String filter1) {
        this.filter1 = filter1;
    }

    public String getFilter1() {
        return filter1;
    }

    public void setFilter2(String filter2) {
        this.filter2 = filter2;
    }

    public String getFilter2() {
        return filter2;
    }

}
