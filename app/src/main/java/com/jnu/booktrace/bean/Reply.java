package com.jnu.booktrace.bean;

public class Reply {
    private int avatar;
    private String username;
    private String content;
    private String date;
    private int likeCount;

    public Reply(int avatar,String username,String content,String date){
        this.avatar = avatar;
        this.username = username;
        this.content = content;
        this.date = date;
        this.likeCount = 0;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
