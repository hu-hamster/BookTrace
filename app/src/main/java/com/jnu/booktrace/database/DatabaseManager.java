package com.jnu.booktrace.database;

import android.content.SyncRequest;
import android.database.Cursor;
import android.os.Message;
import android.util.Log;

import com.jnu.booktrace.R;
import com.jnu.booktrace.bean.Book;
import com.jnu.booktrace.bean.Drift;
import com.jnu.booktrace.bean.Person;
import com.jnu.booktrace.filehandle.FileHandle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String TAG = "DatabaseManager";
    public static Connection connection;
    public static Statement statement;
    private static PreparedStatement preparedStatement;
    public static ResultSet result;
    public static String sql;

    public static String test() throws SQLException {
        String name = "";
        try {
            connection = DBUtil.getConnection();
            statement = connection.createStatement();
            sql = "select * from persontb";
            result = statement.executeQuery(sql);
            while (result.next()){
                name = result.getString("name");
                Log.i(TAG, "test: "+name);;
            }
            connection.close();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return name;
    }

    /**
     * 用于注册用户的时候向数据库添加用户数据
     * @param person
     */
    public static void insertPersontb(Person person){
        try {
            connection = DBUtil.getConnection();   //进行数据库连接
            statement = connection.createStatement();
            sql = "insert into persontb (name,password, nickname,gender,birth,description,avatar) values (?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,person.getName());
            preparedStatement.setString(2,person.getPassword());
            preparedStatement.setString(3,person.getNickName());
            preparedStatement.setString(4,person.getGender());
            preparedStatement.setString(5,person.getBirth());
            preparedStatement.setString(6,person.getDescription());
            preparedStatement.setString(7,person.getAvatar());
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 用于在注册时根据用户输入的用户名判断用户是否在数据库中存在
     * @param name-用户名
     * @return ture-存在 false-不存在
     */
    public static Boolean judgePersonExist(String name) {
        int count=0;
        try {
            connection = DBUtil.getConnection();    //进行数据库连接
            sql = "select count(*) from persontb where name = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,name);
            result=preparedStatement.executeQuery();
            while (result.next()){
                count  = result.getInt("count(*)");
            }
            connection.close();
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        if (count==1) return true;
        else return false;
    }

    /**
     * 用于在登录时根据用户输入的用户名和密码判断用户是否存在
     * @param name-用户名
     * @param password-密码
     * @return ture-存在 false-不存在
     */
    public static Boolean judgeLogin(String name, String password){
        int count = 0;
        try {
            connection = DBUtil.getConnection();    //进行数据库连接
            sql = "select count(*) from persontb where name = ? and password = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,password);
            result=preparedStatement.executeQuery();
            while (result.next()){
                count  = result.getInt("count(*)");
            }
            connection.close();
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        if (count==1) return true;
        else return false;
    }

    /**
     * 根据用户名获取用户类
     * @param name-用户名
     * @return 用户类
     */
    public static Person getPersonFromName(String name){
        Person person = new Person();
        try {
            connection = DBUtil.getConnection();
            sql = "select * from persontb where name = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,name);
            result = preparedStatement.executeQuery();
            while(result.next()){
                person.setId(result.getInt("id"));
                person.setName(name);
                person.setPassword(result.getString("password"));
                person.setNickName(result.getString("nickname"));
                person.setGender(result.getString("gender"));
                person.setBirth(result.getString("birth"));
                person.setDescription(result.getString("description"));
                person.setAvatar(FileHandle.ReadFromFile("avatar.txt"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return person;
    }

    public static void updatePersontb(Person person){
        try {
            connection = DBUtil.getConnection();   //进行数据库连接
            sql = "update persontb set password = ? , nickname = ?, gender = ? ,birth = ?, description = ?, avatar = ? where id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,person.getPassword());
            preparedStatement.setString(2, person.getNickName());
            preparedStatement.setString(3,person.getGender());
            preparedStatement.setString(4,person.getBirth());
            preparedStatement.setString(5,person.getDescription());
            preparedStatement.setString(6, FileHandle.ReadFromFile("avatar.txt"));
            preparedStatement.setInt(7,person.getId());
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * 将书本传入数据库
     * @param book-书本
     */
    public static void insertBooktb(Book book){
        try {
            connection = DBUtil.getConnection();   //进行数据库连接
            sql = "insert into bootb (isbn,title, image,author,translator,publisher,pubdate,tags,binding,price,pages,author_intro,summary) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,book.getIsbn10());
            preparedStatement.setString(2,book.getTitle());
            preparedStatement.setString(3,book.getImage());
            preparedStatement.setString(4,book.getAuthor());
            preparedStatement.setString(5,book.getTranslator());
            preparedStatement.setString(6,book.getPublisher());
            preparedStatement.setString(7,book.getPubdate());
            preparedStatement.setString(8,book.getTags());
            preparedStatement.setString(9,book.getBinding());
            preparedStatement.setString(10,book.getPrice());
            preparedStatement.setString(11,book.getPages()+"");
            preparedStatement.setString(12,book.getAuthor_intro());
            preparedStatement.setString(13,book.getSummary());
            preparedStatement.executeUpdate();
            connection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 根据isbn号查看书籍是否存在远程数据库中
     * @param isbn-书的isbn号
     * @return true-存在 false-不存在
     */
    public static Boolean isBookExist(String isbn) {
        int count = 0;
        try {
            connection = DBUtil.getConnection();
            sql = "select count(*) from booktb where isbn = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,isbn);
            result=preparedStatement.executeQuery();
            while (result.next()){
                count  = result.getInt("count(*)");
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (count==1) return true;
        else return false;
    }

    /**
     * 判断书籍是否在远程数据库中，若存在，根据isbn号查询书籍信息
     * @param isbn-书籍ISBN号
     * @return 返回Book对象（已new）;若数据不存在，Book对象为null
     */
    public static Book QueryBook(String isbn){
        Book book = new Book();
        if(isBookExist(isbn)) {
            try {
                connection = DBUtil.getConnection();
                sql = "select * from booktb where isbn = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, isbn);
                result = preparedStatement.executeQuery();
                while (result.next()) {
                    book.setId(result.getString("isbn"));
                    book.setIsbn10(result.getString("isbn"));
                    book.setTitle(result.getString("title"));
                    book.setImage(result.getString("image"));
                    book.setAuthor(result.getString("author"));
                    book.setTranslator(result.getString("translator"));
                    book.setPublisher(result.getString("publisher"));
                    book.setPubdate(result.getString("pubdate"));
                    book.setTags(result.getString("tags"));
                    book.setBinding(result.getString("binging"));
                    book.setPrice(result.getString("price"));
                    book.setPages(result.getInt("pages") + "");
                    book.setAuthor_intro(result.getString("author_intro"));
                    book.setSummary(result.getString("summary"));
                }
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return book;
    }

    /**
     * 判断远程服务器中是否有该用户的user-book信息
     * @param username 用户名
     * @return true-至少有一条；false-一条都没有
     */
    public static boolean isUserBookExist(String username){
        int count = 0;
        try {
            connection = DBUtil.getConnection();
            sql = "select count(*) from userbooktb where username = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            result = preparedStatement.executeQuery();
            while (result.next()){
                count = result.getInt("count(*)");
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count >= 0;
    }

    /**
     * 根据用户id找到他的漂流瓶
     * @return 漂流瓶列表
     */

    public static List<Drift> GetDrift(){
        List<Drift> drifts = new ArrayList<>();
        try {
            connection = DBUtil.getConnection();
            sql = "select * from drifttb";
            preparedStatement = connection.prepareStatement(sql);

            result = preparedStatement.executeQuery();
            while(result.next()){
                int id = result.getInt("id");
                String author_name = result.getString("author_name");
                String time = result.getString("time");
                String title = result.getString("title");
                String book_author =result.getString("book_author");
                String recommend = result.getString("recommend");
                Drift drift = new Drift(id,author_name,time,title,book_author,recommend);
                drifts.add(drift);
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return drifts;
    }

    /**
     * 获取漂流瓶个数
     * @return 漂流瓶个数
     */
    public static int GetSizeOfDrifttb(){
        int count = 0;
        try {
            connection = DBUtil.getConnection();
            sql = "select count(*) from drifttb";
            preparedStatement = connection.prepareStatement(sql);
            result = preparedStatement.executeQuery();
            while(result.next()){
                count = result.getInt("count(*)");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }

    /**
     *
     * @return
     */
//    public static Drift GetOneDriftByRandom(){
//        Drift drift = new Drift();
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                int count = GetSizeOfDrifttb();
//
//            }
//        });
//        thread.start();
//        while (true){
//            try {
//                thread.join();
//                break;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//    }

}
