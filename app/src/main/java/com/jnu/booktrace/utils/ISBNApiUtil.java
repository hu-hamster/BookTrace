package com.jnu.booktrace.utils;

import static com.jnu.booktrace.database.DBManager.insertBooktb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.jnu.booktrace.bean.Book;
import com.jnu.booktrace.database.DBManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ISBNApiUtil {
    public static final String apikey = "11528.203958641994657fa3c05b913943515e.e5acbaf7197841cfa45c121acd0350d5";
    private static final int WHAT_ISBN_RESULT_OK = 200;

    public Book getBookFromISBN(Book book, String ISBN){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Message message = new Message();
                    String ISBNRequest = "https://api.jike.xyz/situ/book/isbn/"+ISBN+"?apikey="+apikey;

//                    Log.e("MYTAG", ISBNRequest);
                    String resultJson = download(ISBNRequest);
//                    Log.e("MYTAG", resultJson);

                    //不要发送Message，一收到响应立即解析并赋值，否则来不及赋值，主线程那边发现NULL会报错！
                    if(parsonJson(resultJson, book)){
//                        Log.e("MYTAG",book.getTitle());
//                        Log.e("MYTAG",book.getImage());
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);//线程启动读取网络数据
        thread.start();
        while(true) {
            try {
                thread.join(); //必须等待这个线程结束
                break;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        insertBooktb(book);
        return book;
    }

    private static String download(String strUrl) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            //返回Json字符串
            URL url = new URL(strUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            //判断HTTP报文的状态码
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                //输入流
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                //加一层缓冲，就可以一行一行读
                String line = "";
                while (null != (line = bufferedReader.readLine())) {
                    stringBuffer.append(line);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return new String(stringBuffer);
    }

    private boolean parsonJson(String strJson, Book book){
        //解析Json
        //ArrayList<ShopLocation> arrayList = new ArrayList<>();
        try {
            JSONObject result = new JSONObject(strJson);
            String status = result.getString("msg");
            if(status.equals("请求成功")){
                JSONObject bookInfo = new JSONObject(result.getString("data"));
                book.setId(bookInfo.getString("id"));
                book.setIsbn10(bookInfo.getString("id"));
                book.setTitle(bookInfo.getString("name"));
                book.setImage(bookInfo.getString("photoUrl"));
                book.setAuthor(bookInfo.getString("author"));
                if(isValid(bookInfo.getString("translator"))) {
                    book.setTranslator(bookInfo.getString("translator"));
                }
                if(isValid(bookInfo.getString("publishing"))){
                    book.setPublisher(bookInfo.getString("publishing"));
                }
                if(isValid(bookInfo.getString("published"))) {
                    book.setPubdate(bookInfo.getString("published"));
                }
                if(isValid(bookInfo.getString("price"))) {
                    book.setPrice(bookInfo.getString("price"));
                }
                if(isValid(bookInfo.getString("pages"))) {
                    book.setPages(bookInfo.getString("pages"));
                }
                if(isValid(bookInfo.getString("authorIntro"))) {
                    book.setAuthor_intro(bookInfo.getString("authorIntro"));
                }
                if(isValid(bookInfo.getString("description"))) {
                    book.setSummary(bookInfo.getString("description"));
                }
                if(isValid(bookInfo.getString("designed"))) {
                    book.setBinding(bookInfo.getString("designed"));
                }

                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断JSON字符串中的值是否正常
     * @param string 解析后的某一项的值
     * @return
     */
    private boolean isValid(String string){
        if(string.equals("")){
            return false;
        }
        else return true;
    }
}
