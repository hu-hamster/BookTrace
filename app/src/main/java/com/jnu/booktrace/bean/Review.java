package com.jnu.booktrace.bean;

//书评
public class Review {
    private int userId; //用户ID
    private String bookIsbn; //对应书籍
    private String content; //内容
    private int likeCount;//获赞数
    private int mark;//评分(0,1,2,3,4,5)
    private String date; //发表日期(yyyy-MM-dd)

    public Review(int userId, String bookIsbn, String content, int likeCount, int mark) {
        this.userId = userId;
        this.bookIsbn = bookIsbn;
        this.content = content;
        this.likeCount = likeCount;
        this.mark = mark;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
