package com.jnu.booktrace.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * 本地sqlite数据库
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    //数据库版本号
    private static Integer Version = 1;

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        //context:上下文 name：数据库名称 factory:可选的游标工厂（通常是NULL） version：数据库版本，值必须为整数且递增
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //个人中心：用户信息表
        String sqlCreatePersonTable = "create table persontb (id integer primary key, name varchar(25) not null unique, " +
                "password varchar(50) not null, nickname varchar(20),gender varchar(10),birth varchar(20), description text)";

        //个人图书馆：书籍基本信息总表，ISBN号作为主键
        //0.ISBN号，1.标题，2.封面url，3.作者，4.译者，5.出版社，6.出版时间，7.分类/标签，8.精装/平装，9.定价（xx.xx元），10.页数，11.作者简介，12.书籍简介
        String sqlCreateBookTable = "create table booktb (isbn varchar(15) not null unique, " +
                "title text, image text, author varchar(100), translator varchar(100), " +
                "publisher varchar(100), pubdate varchar(25), tags varchar(100), binding varchar(20), price varchar(25), "+
                "pages verchar(25), author_intro text, summary text)";
        //个人图书馆，书评信息总表：
        //0.书评ID，1.发表用户ID，2.关联书籍ISBN，3.书评内容，4.书评点赞数，5.评分(5分满分)
        String sqlCreateReviewTable = "create table reviewtb (id integer primary key autoincrement, userId integer, " +
                "bookIsbn varchar(15), content text, likeCount integer, mark integer, date varchar(20))";
        //个人图书馆，用户-藏书-书架 关系表，存储一个用户的藏书
        //0.用户名，1.书籍ISBN，2.所属书架名（为null则在默认书架）
        String sqlCreateUserBookTable = "create table userbooktb (username varchar(25) not null, " +
                "bookisbn varchar(15) not null, bookshelf varchar(35))";

        //自由谈：话题（后台发布），ID，话题名称，话题描述，参与用户（字符串格式维护一个用户ID列表），相关主题数量
        String sqlCreateTopicTable = "create table topictb(id integer primary key autoincrement, title varchar(200) not null, "+
                "description text, subscribers text, postCount integer)";
        //自由谈：主题帖（用户在某一话题下发布）,ID，内容，日期（yyyy-MM-dd格式字符串），作者用户ID（一个整数），所属话题ID，赞同数，回复数
        String sqlCreatePostTable = "create table posttb(id integer primary key autoincrement, "+
                "content text not null, date text, author integer, relativeTopic integer, likeCount integer, replyCount integer)";
        //自由谈：回复（在主题贴下由用户发布），ID，内容，日期（yyyy-MM-dd格式字符串），作者用户ID，所属主题帖ID，赞同数
        String sqlCreateReplyTable = "create table replytb(id integer primary key autoincrement, content text not null, date text, "+
                "author integer, relativePost integer, likeCount integer)";

        //漂流瓶：ID，用户ID，日期，书籍标题，书籍作者，书籍读后感
        String sqlCreateDriftTable = "create table drifttb(id integer primary key autoincrement, author_name varchar(50) not null, " +
                "time varchar(20) not null, title varchar(100) not null, book_author varchar(100),book_image text, recommend text)";

        db.execSQL(sqlCreatePersonTable);
        db.execSQL(sqlCreateBookTable);
        db.execSQL(sqlCreateUserBookTable);
        db.execSQL(sqlCreateReviewTable);
        db.execSQL(sqlCreateTopicTable);
        db.execSQL(sqlCreatePostTable);
        db.execSQL(sqlCreateReplyTable);
        db.execSQL(sqlCreateDriftTable);
    }

    //数据库版本发生改变时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
