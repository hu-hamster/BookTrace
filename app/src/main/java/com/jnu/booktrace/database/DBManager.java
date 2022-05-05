package com.jnu.booktrace.database;


import android.annotation.SuppressLint;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import com.jnu.booktrace.bean.Book;
import com.jnu.booktrace.bean.Drift;
import com.jnu.booktrace.bean.Person;
import com.jnu.booktrace.bean.Review;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/*
* 负责管理数据库对表中的数据进行增删改查
**/
public class DBManager {
    private static SQLiteDatabase db;

    public static void initDB(Context context){
        DatabaseHelper databaseHelper = new DatabaseHelper(context,"BookTrace.db",null,1);
        db = databaseHelper.getWritableDatabase();
    }
    /*
    * 判断本地数据库是否存在用户信息
     */
    public static Boolean JudgePersonIsNotNull(){
        String sql = "select count(*) from persontb";
        Cursor cursor = db.rawQuery(sql,null);
        int result = 0 ;
        if(cursor.moveToFirst()){
            @SuppressLint("Range") int count = cursor.getInt(cursor.getColumnIndex("count(*)"));
            result = count;
        }
        if(result==0) return false;
        else return true;
    }

    /*
    * 从用户表中获取姓名信息
     */
    @SuppressLint("Range")
    public static String GetNameFromPersontb(){
        String sql = "select * from persontb";
        String name ="";
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            name = cursor.getString(cursor.getColumnIndex("name"));
        }
        return name;
    }
    /*
    * 根据用户姓名获取用户
     */
    @SuppressLint("Range")
    public static Person getPersonFromName(String name){
        Person person = new Person();
        String sql = "select * from persontb where name = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{name});
        cursor.moveToFirst();
        person.setId(cursor.getInt(cursor.getColumnIndex("id")));
        person.setName(name);
        person.setPassword(cursor.getString(cursor.getColumnIndex("password")));
        person.setNickName(cursor.getString(cursor.getColumnIndex("nickname")));
        person.setGender(cursor.getString(cursor.getColumnIndex("gender")));
        person.setBirth(cursor.getString(cursor.getColumnIndex("birth")));
        person.setDescription(cursor.getString(cursor.getColumnIndex("description")));

        return person;
    }
    /*
    * 向用户表中插入数据
     */
    public static void insertPersontb(Person person){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",person.getId());
        contentValues.put("name",person.getName());
        contentValues.put("password",person.getPassword());
        contentValues.put("nickname",person.getNickName());
        contentValues.put("gender",person.getGender());
        contentValues.put("birth",person.getBirth());
        contentValues.put("description",person.getDescription());
        db.insert("persontb",null,contentValues);
    }
    /*
    * 判断输入的用户在数据库中是否存在
     */
    public static Boolean JudgePersonExist(String name,String password){
        String sql = "select count(*) from persontb where name=? and password =?";
        Cursor cursor = db.rawQuery(sql,new String[]{name,password});
        int result=0;
        //判断
        if(cursor.moveToFirst()){
            @SuppressLint("Range") int count = cursor.getInt(cursor.getColumnIndex("count(*)"));
            result = count;
        }
        if(result==1) return true;
        else return false;
    }
    /*
    * 更新本地用户表
     */
    public static void updatePersontb(Person person){
        ContentValues values = new ContentValues();
        values.put("password",person.getPassword());
        values.put("nickname",person.getNickName());
        values.put("gender",person.getGender());
        values.put("birth",person.getBirth());
        values.put("description",person.getDescription());
        db.update("persontb",values,"name=?",new String[]{person.getName()});

    }


    /*
    * 根据用户名删除用户表中的用户
     */
    public static void deletePersontb(){
        db.execSQL("delete from persontb");
    }


    public static void insertBooktb(Book book){
        ContentValues contentValues = new ContentValues();
        contentValues.put("isbn",book.getIsbn10());
        contentValues.put("title",book.getTitle());
        contentValues.put("image",book.getImage());
        contentValues.put("author",book.getAuthor());
        contentValues.put("translator",book.getTranslator());
        contentValues.put("publisher",book.getPublisher());
        contentValues.put("pubdate",book.getPubdate());
        contentValues.put("tags",book.getTags());
        contentValues.put("binding",book.getBinding());
        contentValues.put("price",book.getPrice());
        contentValues.put("pages",book.getPages());
        contentValues.put("author_intro",book.getAuthor_intro());
        contentValues.put("summary",book.getSummary());
        db.insert("booktb",null,contentValues);
    }

    /**
     * 根据isbn号查看书籍是否存在本地数据库中
     * @param isbn-书的isbn号
     * @return true-存在 false-不存在
     */
    public static Boolean isBookExist(String isbn){
        String sql = "select * from booktb where isbn = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{isbn});
        cursor.moveToFirst();
        return !cursor.isAfterLast();
    }

    /**
     * 判断书籍是否在本地数据库中，若存在，根据isbn号查询书籍信息
     * @param isbn-书籍ISBN号
     * @return 返回Book对象（已new）;若数据不存在，Book对象为null
     */
    public static Book QueryBook(String isbn){
        Book book = new Book();
        if(isBookExist(isbn)) {
            String sql = "select * from booktb where isbn = ?";
            Cursor cursor = db.rawQuery(sql, new String[]{isbn});
            cursor.moveToFirst();

            book.setId(cursor.getString(0));
            book.setIsbn10(cursor.getString(0));
            book.setTitle(cursor.getString(1));
            book.setImage(cursor.getString(2));
            book.setAuthor(cursor.getString(3));
            book.setTranslator(cursor.getString(4));
            book.setPublisher(cursor.getString(5));
            book.setPubdate(cursor.getString(6));
            book.setTags(cursor.getString(7));
            book.setBinding(cursor.getString(8));
            book.setPrice(cursor.getString(9));
            book.setPages(cursor.getString(10));
            book.setAuthor_intro(cursor.getString(11));
            book.setSummary(cursor.getString(12));
            cursor.close();
        }
        return book;
    }

    /**
     * 查询某个用户某个指定书架的所有书籍
     * @param username 用户名
     * @param bookshelf 书架名
     * @return 属于该书架的书籍的ArrayList
     */
    public static ArrayList<Book> QueryBookByBookshelf(String username, String bookshelf){
        ArrayList<Book> arrayList = new ArrayList<>();
        String sql = "select * from userbooktb where username = ? and bookshelf = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{username,bookshelf});
        cursor.moveToFirst();
        while(cursor.moveToNext()){
            arrayList.add(QueryBook(cursor.getString(1)));
        }
        cursor.close();
        return arrayList;
    }

    //更新漂流瓶数据表
    public static void UpdateDrift(List<Drift> drifts){
        db.execSQL("delete from drifttb");  //清空漂流瓶表中数据
        for(Drift drift:drifts){
            ContentValues contentValues = new ContentValues();
            contentValues.put("id",drift.getId());
            contentValues.put("author_name",drift.getAuthor_name());
            contentValues.put("time",drift.getTime());
            contentValues.put("title",drift.getTitle());
            contentValues.put("book_author",drift.getBook_Author());
            contentValues.put("book_image",drift.getBook_image());

            contentValues.put("recommend",drift.getRecommend());
            db.insert("drifttb",null,contentValues);
        }
    }
    //获取根据用户名获取漂流瓶
    public static List<Drift> GetOwnDriftById(String author_name){
        List<Drift> drifts = new ArrayList<>();
        String sql = "select * from drifttb where author_name = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{author_name+""});
        while(cursor.moveToNext()){
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
            @SuppressLint("Range") String book_author = cursor.getString(cursor.getColumnIndex("book_author"));
            @SuppressLint("Range") String recommend = cursor.getString(cursor.getColumnIndex("recommend"));
            Drift drift = new Drift(id, author_name, time, title,book_author,recommend);
            drifts.add(drift);
        }
        return drifts;
    }

    //获取得到的其他人的漂流瓶
    @SuppressLint("Range")
    public static List<Drift> GetOtherDrift(String author_name){
        List<Drift> drifts = new ArrayList<>();
        String sql = "select * from drifttb where author_name != ?";
        Cursor cursor = db.rawQuery(sql, new String[]{author_name+""});
        while(cursor.moveToNext()){
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            author_name  = cursor.getString(cursor.getColumnIndex("author_name"));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
            @SuppressLint("Range") String book_author = cursor.getString(cursor.getColumnIndex("book_author"));
            @SuppressLint("Range") String recommend = cursor.getString(cursor.getColumnIndex("recommend"));
            Drift drift = new Drift(id, author_name, time, title,book_author,recommend);
            drifts.add(drift);
        }
        return drifts;
    }

    //插入一条漂流瓶到数据表
    public static void insertOneDriftToDrifttb(Drift drift){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",drift.getId());
        contentValues.put("author_id",drift.getAuthor_name());
        contentValues.put("book_image",drift.getBook_image());
        contentValues.put("time",drift.getTime());
        contentValues.put("title",drift.getTitle());
        contentValues.put("book_author",drift.getBook_Author());
        contentValues.put("recommend",drift.getRecommend());
        db.insert("drifttb",null,contentValues);
    }

//    public static void


    /**
     * 判断本地数据库中的user-book表是否有指定用户的书籍数据
     * @param username 用户名
     * @return true-至少有一条；false-一条都没有
     */
    public static boolean isUserBookExist(String username){
        String sql = "select * from userbooktb where username =?";
        Cursor cursor = db.rawQuery(sql, new String[]{username});
        cursor.moveToFirst();
        return !cursor.isAfterLast();
    }

    /**
     * 先判断本地数据库是否有用户的书籍数据，再查找书籍
     * @param username 用户名
     * @return HashMap（已new），key为书架名，value为该书架下的书籍列表（仅ISBN号）
     */
    public static HashMap<String, ArrayList<String>> QueryUserBooks(String username){
        HashMap<String, ArrayList<String>> hashMap = new HashMap();
        if(isUserBookExist(username)){
            ArrayList<String> bookshelfs = new ArrayList<>(); //书架名列表
            String sql = "select bookshelf from userbooktb where username = ?";
            Cursor bookshelfCursor = db.rawQuery(sql, new String[]{username});
            bookshelfCursor.moveToFirst();
            while(bookshelfCursor.moveToNext()){
                bookshelfs.add(bookshelfCursor.getString(0)); //TODO：是这样吗？
            }
            bookshelfCursor.close();

            for(int i = 0; i<bookshelfs.size(); i++){
                String bookshelf = bookshelfs.get(i);
                ArrayList<String> arrayList = new ArrayList<>();
                sql = "select book from userbooktb where username = ? and bookshelf = ?";
                Cursor cursor = db.rawQuery(sql, new String[]{username,bookshelf});
                cursor.moveToFirst();
                while(cursor.moveToNext()){
                    arrayList.add(cursor.getString(0));
                }
                cursor.close();
                hashMap.put(bookshelf,arrayList);
            }
        }
        return hashMap;
    }

    /**
     * 将一条 用户名-书籍ISBN-书架名 数据插入到本地数据库中，该数据可来自远程服务器，也可来自Android客户端
     * 注意：判重应在客户端调用本方法前预先完成，本方法不保证数据唯一
     * @param username-用户名
     * @param isbn-书籍ISBN号
     * @param bookshelf-书架名
     */
    public static void insertUserBook(String username, String isbn, String bookshelf){
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("bookisbn",isbn);
        contentValues.put("bookshelf",bookshelf);
        db.insert("userbooktb",null,contentValues);
    }

    /**
     * 删除一条 用户名-书籍ISBN-书架名 数据
     * @param username
     * @param isbn
     * @param bookshelf
     */
    public static void deleteUserBook(String username, String isbn, String bookshelf){
        db.delete("userbooktb","username = ? and bookisbn = ? and bookshelf = ?",
                new String[]{username,isbn,bookshelf});
    }

    /**
     * 更改一条 用户名-书籍ISBN-书架名 数据，通常是所属书架的更改
     * @param username
     * @param isbn
     * @param bookshelf 新的书架名
     */
    public static void editUserBook(String username, String isbn, String bookshelf){
        ContentValues values = new ContentValues();
        values.put("username",username);
        values.put("bookisbn",isbn);
        values.put("bookshelf",bookshelf);
        db.update("userbooktb",values,"username = ? and bookisbn = ?",
                new String[]{username,isbn});
    }

    //根据isbn号获取书籍的书评
    public static Review QueryReview(String isbn){
        String sql = "select * from reviewtb where isbn = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{isbn});
        cursor.moveToFirst();
        Review review = new Review(
            cursor.getInt(1),
            cursor.getString(2),
            cursor.getString(3),
            cursor.getInt(4),
            cursor.getInt(5)
        );
        cursor.close();
        return review;
    }

    //将一条review对象插入表
    public static void insertReviewtb(Review review){
        ContentValues contentValues = new ContentValues();
        contentValues.put("userid",review.getUserId());
        contentValues.put("bookisbn",review.getBookIsbn());
        contentValues.put("content",review.getContent());
        contentValues.put("likecount",review.getLikeCount());
        contentValues.put("mark",review.getMark());
        db.insert("reviewtb",null,contentValues);
    }
}
