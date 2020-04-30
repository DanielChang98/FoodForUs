package com.example.myfoodappv2.ui.forum;

public class ForumRowModel {

    private String title;
    private String userName;
    private String comments;
    private String time;
    private String imageUri;


    public ForumRowModel(String title, String userID, String comments, String time, String imageUri) {
        this.title = title;
        this.userName = userID;
        this.comments = comments;
        this.time = time;
        this.imageUri = imageUri;
    }

    public ForumRowModel(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
