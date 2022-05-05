package com.jnu.booktrace.bean;

//主题贴
public class Post {
    private String username;
    private String content;
    private String date;
    private int userAvatar;
    private String relativeTopic;
    private int replyCount;
    private int likeCount;

    public Post(int userAvatar, String username, String content, String relativeTopic){
        this.userAvatar = userAvatar;
        this.username = username;
        this.content = content;
        this.relativeTopic = relativeTopic;
        this.replyCount = 0;
        this.likeCount = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(int userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getRelativeTopic() {
        return relativeTopic;
    }

    public void setRelativeTopic(String relativeTopic) {
        this.relativeTopic = relativeTopic;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
