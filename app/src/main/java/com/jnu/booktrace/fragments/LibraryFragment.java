package com.jnu.booktrace.fragments;

import static android.app.Activity.RESULT_OK;
import static com.jnu.booktrace.database.DBManager.QueryBook;
import static com.jnu.booktrace.database.DBManager.isBookExist;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.booktrace.R;
import com.jnu.booktrace.activity.LibraryBookDetailActivity;
import com.jnu.booktrace.bean.Book;
import com.jnu.booktrace.bean.TagInfo;
import com.jnu.booktrace.utils.ISBNApiUtil;
import com.jnu.booktrace.utils.ImageUtil;
import com.king.zxing.CameraScan;
import com.king.zxing.CaptureActivity;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.InputDialog;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.interfaces.OnInputDialogButtonClickListener;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LibraryFragment extends Fragment implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener{
    private static final int REQUEST_CODE_SCAN = 100;
    private static final int REQUEST_CODE_PHOTO = 200;
    public List<Book> mBookList;
    private SwipeRecyclerView mRecyclerView;
    private BookAdapter mBookAdapter;
    private Toolbar toolbar;
    private Spinner spinner;
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionContentLabelList rfaContent;
    private RapidFloatingActionButton rfaBtn;
    private RapidFloatingActionHelper rfabHelper;
    HashMap<String, Drawable> imgCache;     // ????????????
    HashMap<Integer, TagInfo> tag_map;      // TagInfo??????
    ImageUtil loader;                // ?????????????????????

    public LibraryFragment() {}
    public static LibraryFragment newInstance() {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DialogX.init(getContext());
        imgCache = new HashMap<String, Drawable>();
        loader = new ImageUtil();
        tag_map = new HashMap<Integer, TagInfo>();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data!=null){
            switch (requestCode){
                case REQUEST_CODE_SCAN:
                    String result = CameraScan.parseScanResult(data);
                    Log.e("???????????????",result);
                    Toast.makeText(getContext(),result,Toast.LENGTH_SHORT);
                    handleScanResult(result);
                    break;
                case REQUEST_CODE_PHOTO:
                    //arsePhoto(data);
                    break;
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_library, container, false);
        initToolbar(rootView);
        initSpinner(rootView);

        initData();
        initRecyclerView(rootView);
        initFabList(rootView);

        return rootView;
    }

    private void initFabList(View v){
        rfaLayout = v.findViewById(R.id.activity_main_rfal);
        rfaBtn = v.findViewById(R.id.activity_main_rfab);
        rfaContent = new RapidFloatingActionContentLabelList(getContext());
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("????????????")
                .setResId(R.mipmap.library_fab_ic_input)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("????????????")
                .setResId(R.mipmap.library_fab_ic_scan)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setWrapper(1)
        );
//        items.add(new RFACLabelItem<Integer>()
//                .setLabel("???????????????")
//                .setResId(R.mipmap.library_fab_ic_scan)
//                .setIconNormalColor(0xffd84315)
//                .setIconPressedColor(0xffbf360c)
//                .setWrapper(2)
//        );
        rfaContent
                .setItems(items)
                .setIconShadowColor(0xff888888);
        rfabHelper = new RapidFloatingActionHelper(
                getContext(),
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();
    }

    /**
     * ??????????????????ISBN??????
     */
    private void handleScanResult(String inputStr){
        Book book = new Book();
        Toast.makeText(getContext(),"?????????????????????",Toast.LENGTH_LONG).show();
        new ISBNApiUtil().getBookFromISBN(book,inputStr);
        if(inputStr == null || book.getTitle() == null){
            Toast.makeText(getContext(),"????????????????????????", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.e("book",book.getTitle()+" "+book.getAuthor()+" "+book.getImage());
            MessageDialog.show("????????????", null, "??????", "??????", "??????")
                    .setCustomView(new OnBindView<MessageDialog>(R.layout.dialog_layout_custom_view) {
                        @Override
                        public void onBind(MessageDialog dialog, View v) {
                            ImageView image = v.findViewById(R.id.iv_item_book_image);
                            TextView title = v.findViewById(R.id.tv_item_book_title);
                            TextView author = v.findViewById(R.id.tv_item_book_author);

                            //??????????????????????????????
                            title.setText(book.getTitle());
                            author.setText(book.getAuthor());

                            String imgurl = book.getImage();   // ????????????????????????url??????
                            Drawable drawable = imgCache.get(imgurl);       // ??????????????????

                            TagInfo tag = new TagInfo();
                            tag.url = book.getImage();

                            if (null != drawable) {                         // ??????????????????????????????
                                image.setImageDrawable(drawable);
                            }
                            else {                                      // ??????????????????????????????
                                drawable = loader.loadDrawableByTag(tag, new ImageUtil.ImageCallBack() {
                                    @Override
                                    public void obtainImage(TagInfo ret_info) {
                                        imgCache.put(ret_info.url, ret_info.drawable);    // ??????????????????????????????????????????
                                        // ???????????????TagInfo???Tag???????????????????????????????????????Tag????????????????????????ImageView
                                        image.setImageDrawable(ret_info.drawable);
                                    }
                                });
                            }
                        }
                    }).setOkButton(new OnDialogButtonClickListener<MessageDialog>() {
                @Override
                public boolean onClick(MessageDialog baseDialog, View v) {
                    //????????????
                    mBookList.add(book);
                    mBookAdapter.notifyItemInserted(mBookList.size());
                    return false;
                }
            }).setOtherButton(new OnDialogButtonClickListener<MessageDialog>() {
                @Override
                public boolean onClick(MessageDialog baseDialog, View v) {
                    //????????????
                    startActivityForResult(new Intent(getContext(), CaptureActivity.class),REQUEST_CODE_SCAN);
                    return false;
                }
            });
        }
    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        //Toast.makeText(getContext(), "clicked label: " + position, Toast.LENGTH_SHORT).show();
        switch (position){
            case 0:
                InputDialog inputDialog = new InputDialog("????????????", "??????????????????ISBN???", "??????", "??????", "")
                            .setCancelable(false)
                            .setOkButton(new OnInputDialogButtonClickListener<InputDialog>() {
                                @Override
                                public boolean onClick(InputDialog baseDialog, View v, String inputStr) {
                                    if(!inputStr.isEmpty()){
                                        Book book = new Book();
                                        Toast.makeText(getContext(),"?????????????????????",Toast.LENGTH_LONG).show();
                                        new ISBNApiUtil().getBookFromISBN(book,inputStr);
                                        if(book.getTitle() == null){
                                            Toast.makeText(getContext(),"??????????????????????????????ISBN???????????????",Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(getContext(),"??????????????????",Toast.LENGTH_SHORT).show();
                                            mBookList.add(book);
                                            mBookAdapter.notifyItemInserted(mBookList.size());
                                        }
                                    }
                                    else{
                                        Toast.makeText(getContext(),"?????????ISBN???",Toast.LENGTH_SHORT).show();
                                    }
                                    return false;
                                }
                            });
                    inputDialog.show();
                break;
            case 1:
                startActivityForResult(new Intent(getContext(), CaptureActivity.class),REQUEST_CODE_SCAN);
                break;
        }
        rfabHelper.toggleContent();
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        //Toast.makeText(getContext(), "clicked icon: " + position, Toast.LENGTH_SHORT).show();
        switch (position){
            case 0:
            InputDialog inputDialog = new InputDialog("????????????", "??????????????????ISBN???", "??????", "??????", "")
                        .setCancelable(false)
                        .setOkButton(new OnInputDialogButtonClickListener<InputDialog>() {
                            @Override
                            public boolean onClick(InputDialog baseDialog, View v, String inputStr) {
                                if(!inputStr.isEmpty()){
                                    Book book = new Book();
                                    Toast.makeText(getContext(),"?????????????????????",Toast.LENGTH_LONG).show();
                                    new ISBNApiUtil().getBookFromISBN(book,inputStr);
                                    if(book.getTitle() == null){
                                        Toast.makeText(getContext(),"??????????????????????????????ISBN???????????????",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        mBookList.add(book);
                                        mBookAdapter.notifyItemInserted(mBookList.size());
                                        Toast.makeText(getContext(),"??????????????????",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(getContext(),"?????????ISBN???",Toast.LENGTH_SHORT).show();
                                }
                                return false;
                            }
                        });
                inputDialog.show();
                break;
            case 1:
                startActivityForResult(new Intent(getContext(), CaptureActivity.class),REQUEST_CODE_SCAN);
                break;
            case 2:
//                //????????????
//                EasyPhotos.createAlbum(getActivity(), true,false, GlideEngine.getInstance())//?????????????????????????????????????????????????????????????????????????????????false??????????????????0???????????????????????????[??????Glide?????????????????????](https://github.com/HuanTanSheng/EasyPhotos/wiki/12-%E9%85%8D%E7%BD%AEImageEngine%EF%BC%8C%E6%94%AF%E6%8C%81%E6%89%80%E6%9C%89%E5%9B%BE%E7%89%87%E5%8A%A0%E8%BD%BD%E5%BA%93)
//                        .setFileProviderAuthority("com.jnu.booktrace")//????????????????????????`FileProvider?????????`
//                        .start(new SelectCallback() {
//                            @Override
//                            public void onResult(ArrayList<Photo> photos, boolean isOriginal) {
//                                //??????file,??????????????????
//                                try {
//                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photos.get(0).uri);
//                                    bitmap = ImageHandle.compressImage(bitmap);
//                                    Bitmap bitmap1 = ImageHandle.toRoundBitmap(bitmap);
//
//                                    //???????????????/?????????
//                                    String result = CodeUtils.parseCode(bitmap);
//                                    Log.e("????????????????????????",result);
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            @Override
//                            public void onCancel() {
//                            }
//                        });
                break;
        }
        rfabHelper.toggleContent();
    }

    private void initToolbar(View v){
        //??????toolbar
        toolbar = v.findViewById(R.id.toolbar);
//        //?????????????????????setSupportActionBar??????????????????????????????????????????????????????????????????setTitle??????
        toolbar.setTitle("");
        //???toolbar??????actionbar
        //setSupportActionBar(toolbar);
    }

    private void initSpinner(View v){
        spinner = v.findViewById(R.id.spinner);
        // ???????????????
        String[] mItems = {"????????????", "????????????", "??????", "??????"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, mItems);
        //spinner_item ?????????????????????
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        //spinner_dropdown_item ???????????????
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //mBookList = QueryBookByBookshelf(person.getName(), mItems[pos]);
                mBookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO
            }
        });
    }

    private void initData() {
        //????????????????????????????????????
        String[] isbn={"9787020024759","9787505352377","9787040195835","9787544210966","9787544211765"};
        mBookList = new ArrayList<>();
        for(int i = 0; i < isbn.length; i++){
            Book book = new Book();
            if(!isBookExist(isbn[i])){ //??????????????????????????????????????????????????????????????????????????????
                new ISBNApiUtil().getBookFromISBN(book, isbn[i]);
            }
            mBookList.add(QueryBook(isbn[i]));
        }
    }

    private void initRecyclerView(View v){
        mRecyclerView = v.findViewById(R.id.rv_book_overview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        // ???????????????
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
                int width = getResources().getDimensionPixelSize(R.dimen.dp_80);
                // 1. MATCH_PARENT ???????????????????????????Item?????????;
                // 2. ???????????????????????????80;
                // 3. WRAP_CONTENT???????????????????????????;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                SwipeMenuItem deleteItem = new SwipeMenuItem(getContext())
                        .setBackgroundColor(getContext().getColor(R.color.text_sticker_red_easy_photos))
                        .setWidth(width)
                        .setHeight(height)
                        .setText("??????")
                        .setTextColor(getContext().getColor(R.color.white));
                SwipeMenuItem moveItem = new SwipeMenuItem(getContext())
                        .setBackgroundColor(getContext().getColor(R.color.text_sticker_orange_easy_photos))
                        .setWidth(width)
                        .setHeight(height)
                        .setText("?????????...")
                        .setTextColor(getContext().getColor(R.color.white));
                rightMenu.addMenuItem(moveItem); //????????????????????????
                rightMenu.addMenuItem(deleteItem); //Item???????????????????????????
            }
        };

        OnItemMenuClickListener mItemMenuClickListener = new OnItemMenuClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge, int position) {
                // ??????????????????????????????????????????????????????Item???????????????????????????
                menuBridge.closeMenu();

                int direction = menuBridge.getDirection(); // ???????????????????????????
                int menuPosition = menuBridge.getPosition(); // ?????????RecyclerView???Item??????Position???

                if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                    //Toast.makeText(getContext(), "list???" + position + "; ???????????????" + menuPosition, Toast.LENGTH_SHORT).show();
                } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
                    //Toast.makeText(getContext(), "list???" + position + "; ???????????????" + menuPosition, Toast.LENGTH_SHORT).show();
                }
            }
        };

        // ?????????????????????
        mRecyclerView.setOnItemMenuClickListener(mItemMenuClickListener);
        // ????????????????????????
        mRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);

        mBookAdapter = new BookAdapter(getContext(), mBookList);
        mRecyclerView.setAdapter(mBookAdapter);
    }

    public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Book> adpList;
        private Context context;

        public BookAdapter(Context context, List<Book> bookList){
            this.context = context;
            this.adpList = bookList;

        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.item_book_overview_layout,parent,false);
            return new BookHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            BookHolder bookHolder = (BookHolder) holder;
            Book book = adpList.get(position);
            bookHolder.tvTitle.setText(book.getTitle());
            bookHolder.tvAuthor.setText(book.getAuthor());
            bookHolder.tvAbstract.setText(book.getSummary());
            bookHolder.tvBookShelf.setText("");//TODO:?????????????????????????????????

            String imgurl = adpList.get(position).getImage();   // ????????????????????????url??????
            Drawable drawable = imgCache.get(imgurl);       // ??????????????????

            TagInfo tag = new TagInfo();
            tag.url = adpList.get(position).getImage();

            if (null != drawable) {                         // ??????????????????????????????
                bookHolder.ivImage.setImageDrawable(drawable);
            }
            else {                                      // ??????????????????????????????
                drawable = loader.loadDrawableByTag(tag, new ImageUtil.ImageCallBack() {
                    @Override
                    public void obtainImage(TagInfo ret_info) {
                        imgCache.put(ret_info.url, ret_info.drawable);    // ??????????????????????????????????????????
                        // ???????????????TagInfo???Tag???????????????????????????????????????Tag????????????????????????ImageView
                        bookHolder.ivImage.setImageDrawable(ret_info.drawable);
                    }
                });
            }

            /**
             * ?????????????????????????????????????????????
             */
            bookHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LibraryBookDetailActivity.class);
                    intent.putExtra("book",(Parcelable)book);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return adpList.size();
        }

        class BookHolder extends RecyclerView.ViewHolder{
            View rootView;
            ImageView ivImage;
            TextView tvTitle,tvAuthor,tvAbstract,tvBookShelf;
            public BookHolder(@NonNull View itemView) {
                super(itemView);
                rootView = itemView.findViewById(R.id.item_book_root_view);
                ivImage = itemView.findViewById(R.id.iv_item_book_image);
                tvTitle = itemView.findViewById(R.id.tv_item_book_title);
                tvAuthor = itemView.findViewById(R.id.tv_item_book_author);
                tvAbstract = itemView.findViewById(R.id.tv_item_book_abstract);
                tvBookShelf = itemView.findViewById(R.id.tv_item_book_belong_bookshelf);
            }
        }
    }
}