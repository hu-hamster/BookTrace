package com.jnu.booktrace.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.booktrace.R;
import com.jnu.booktrace.bean.Book;
import com.jnu.booktrace.bean.TagInfo;
import com.jnu.booktrace.utils.ImageUtil;

import java.util.HashMap;
import java.util.List;
//
//public abstract class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    private List<Book> adpList;
//    private Context context;
//    HashMap<String, Drawable> imgCache;     // 图片缓存
//    HashMap<Integer, TagInfo> tag_map;      // TagInfo缓存
//    ImageUtil loader;                // 异步加载图片类
//
//
//    public BookAdapter(Context context, List<Book> bookList){
//        this.context = context;
//        this.adpList = bookList;
//        imgCache = new HashMap<String, Drawable>();
//        loader = new ImageUtil();
//        tag_map = new HashMap<Integer, TagInfo>();
//
//    }
////    @NonNull
////    @Override
////    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////        View itemView = LayoutInflater.from(context)
////                .inflate(R.layout.item_book_overview_layout,parent,false);
////        return new BookHolder(itemView);
////    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        BookHolder bookHolder = (BookHolder) holder;
//        Book book = adpList.get(position);
//        bookHolder.ivImage.setImageBitmap(book.getImage_bitmap());
//        bookHolder.tvTitle.setText(book.getTitle());
//        bookHolder.tvAuthor.setText(book.getAuthor());
//        bookHolder.tvAbstract.setText(book.getSummary());
//        bookHolder.tvBookShelf.setText("");//TODO:确定书籍所属书架的名字
//    }
//
//    @Override
//    public int getItemCount() {
//        return adpList.size();
//    }
//
//    abstract class BookHolder extends RecyclerView.ViewHolder{
//        ImageView ivImage;
//        TextView tvTitle,tvAuthor,tvAbstract,tvBookShelf;
//        public BookHolder(@NonNull View itemView) {
//            super(itemView);
//            ivImage = itemView.findViewById(R.id.iv_item_book_image);
//            tvTitle = itemView.findViewById(R.id.tv_item_book_title);
//            tvAuthor = itemView.findViewById(R.id.tv_item_book_author);
//            tvAbstract = itemView.findViewById(R.id.tv_item_book_abstract);
//            tvBookShelf = itemView.findViewById(R.id.tv_item_book_belong_bookshelf);
//        }
//    }
//}
