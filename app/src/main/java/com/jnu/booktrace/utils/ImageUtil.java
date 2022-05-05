package com.jnu.booktrace.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jnu.booktrace.bean.TagInfo;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

//图片处理相关，包含网络请求图片
public class ImageUtil {
    /**
     * 通过url请求网络图片并转换成bitmap
     */
    /**
     * 使用软引用SoftReference，可以由系统在恰当的时候更容易的回收
     */
    private HashMap<String, SoftReference<Drawable>> imageCache;
    public ImageUtil(){
        imageCache = new HashMap<String, SoftReference<Drawable>>();
    }
    /**
     * 通过传入的TagInfo来获取一个网络上的图片
     * @param tag TagInfo对象，保存了position、url和一个待获取的Drawable对象
     * @param callback ImageCallBack对象，用于在获取到图片后供调用侧进行下一步的处理
     * @return drawable 从网络或缓存中得到的Drawable对象，可为null，调用侧需判断
     */
    public Drawable loadDrawableByTag(final TagInfo tag, final ImageCallBack callback){
        Drawable drawable;
        /**
         * 先在缓存中找，如果通过URL地址可以找到，则直接返回该对象
         */
        if(imageCache.containsKey(tag.url)){
            drawable = imageCache.get(tag.url).get();
            if(null!=drawable){
                return drawable;
            }
        }
        /**
         * 用于在获取到网络图片后，保存图片到缓存，并触发调用侧的处理
         */
        final Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                TagInfo info = (TagInfo)msg.obj;
                imageCache.put(info.url, new SoftReference<Drawable>(info.drawable));
                callback.obtainImage(info);
                super.handleMessage(msg);
            }
        };
        /**
         * 如果在缓存中没有找到，则开启一个线程来进行网络请求
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                TagInfo info = getDrawableIntoTag(tag);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = info;
                handler.sendMessage(msg);
            }
        }).start();

        return null;
    }
    /**
     * 通过传入的TagInfo对象，利用其URL属性，到网络请求图片，获取到图片后保存在TagInfo的Drawable属性中，并返回该TagInfo
     * @param info TagInfo对象，需要利用里面的url属性
     * @return TagInfo 传入的TagInfo对象，增加了Drawable属性后返回
     */
    public TagInfo getDrawableIntoTag(TagInfo info){
        URL request;
        InputStream input;
        Drawable drawable = null;

        try{
            request = new URL(info.url);
            input = (InputStream)request.getContent();
            drawable = Drawable.createFromStream(input, "src"); // 第二个属性可为空，为DEBUG下使用，网上的说明
        }
        catch(Exception e){
            e.printStackTrace();
        }
        info.drawable = drawable;
        return info;
    }
    /**
     * 获取图片的回调接口，里面的obtainImage方法在获取到图片后进行调用
     */
    public interface ImageCallBack{
        /**
         * 获取到图片后在调用侧执行具体的细节
         * @param info TagInfo对象，传入的info经过处理，增加Drawable属性，并返回给传入者
         */
        public void obtainImage(TagInfo info);
    }

//
//    public interface bitmapLoadListener{
//        public void getBitmapCallback(Bitmap bitmap);
//    }
//    private bitmapLoadListener listener;
//    public void registerListener(bitmapLoadListener listener){
//        this.listener = listener;
//    }
//
//    public void getBitmapFromUrl(Context context, String url, final bitmapLoadListener callback) {
//        new Thread() {
//            public void run() {
//                Glide.with(context)
//                        .asBitmap()
//                        .load(url)
//                        .into(new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                                callback.getBitmapCallback(resource);
//                            }
//                        });
//            }
//        }.start();
//    }
//
//    private static long times = 0;
//    public static void glideBitmap(Context context, String url){
//        new Thread() {
//            public void run() {
//                final long l1 = System.currentTimeMillis();
//                Glide.with(context).asBitmap().load(url)
//                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
//                        .into(new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(@NonNull Bitmap resource,
//                                                        @Nullable Transition<? super Bitmap> transition) {
//                                //不知道怎么获得，在这里将需要设置的控件设置为resource
//                                long l2 = System.currentTimeMillis();
//                                times = (l2 - l1) + times;
//                                Log.e("毫秒值", times + "");
//                                //请求8张图片，耗时毫秒值98
//                            }
//                        });
//            }
//        }.start();
//    }
}
