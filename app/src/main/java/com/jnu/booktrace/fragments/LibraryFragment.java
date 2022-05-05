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
    HashMap<String, Drawable> imgCache;     // 图片缓存
    HashMap<Integer, TagInfo> tag_map;      // TagInfo缓存
    ImageUtil loader;                // 异步加载图片类

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
                    Log.e("扫描结果：",result);
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
                .setLabel("手动添加")
                .setResId(R.mipmap.library_fab_ic_input)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("扫描添加")
                .setResId(R.mipmap.library_fab_ic_scan)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setWrapper(1)
        );
//        items.add(new RFACLabelItem<Integer>()
//                .setLabel("从相册添加")
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
     * 扫描条形码的ISBN处理
     */
    private void handleScanResult(String inputStr){
        Book book = new Book();
        Toast.makeText(getContext(),"查询书籍中……",Toast.LENGTH_LONG).show();
        new ISBNApiUtil().getBookFromISBN(book,inputStr);
        if(inputStr == null || book.getTitle() == null){
            Toast.makeText(getContext(),"查询失败！请重试", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.e("book",book.getTitle()+" "+book.getAuthor()+" "+book.getImage());
            MessageDialog.show("确认书籍", null, "确定", "取消", "重扫")
                    .setCustomView(new OnBindView<MessageDialog>(R.layout.dialog_layout_custom_view) {
                        @Override
                        public void onBind(MessageDialog dialog, View v) {
                            ImageView image = v.findViewById(R.id.iv_item_book_image);
                            TextView title = v.findViewById(R.id.tv_item_book_title);
                            TextView author = v.findViewById(R.id.tv_item_book_author);

                            //显示标题、作者、封面
                            title.setText(book.getTitle());
                            author.setText(book.getAuthor());

                            String imgurl = book.getImage();   // 得到该项所代表的url地址
                            Drawable drawable = imgCache.get(imgurl);       // 先去缓存中找

                            TagInfo tag = new TagInfo();
                            tag.url = book.getImage();

                            if (null != drawable) {                         // 找到了直接设置为图像
                                image.setImageDrawable(drawable);
                            }
                            else {                                      // 没找到则开启异步线程
                                drawable = loader.loadDrawableByTag(tag, new ImageUtil.ImageCallBack() {
                                    @Override
                                    public void obtainImage(TagInfo ret_info) {
                                        imgCache.put(ret_info.url, ret_info.drawable);    // 首先把获取的图片放入到缓存中
                                        // 通过返回的TagInfo去Tag缓存中找，然后再通过找到的Tag来获取到所对应的ImageView
                                        image.setImageDrawable(ret_info.drawable);
                                    }
                                });
                            }
                        }
                    }).setOkButton(new OnDialogButtonClickListener<MessageDialog>() {
                @Override
                public boolean onClick(MessageDialog baseDialog, View v) {
                    //确定按钮
                    mBookList.add(book);
                    mBookAdapter.notifyItemInserted(mBookList.size());
                    return false;
                }
            }).setOtherButton(new OnDialogButtonClickListener<MessageDialog>() {
                @Override
                public boolean onClick(MessageDialog baseDialog, View v) {
                    //重扫按钮
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
                InputDialog inputDialog = new InputDialog("添加书籍", "请输入书籍的ISBN号", "添加", "取消", "")
                            .setCancelable(false)
                            .setOkButton(new OnInputDialogButtonClickListener<InputDialog>() {
                                @Override
                                public boolean onClick(InputDialog baseDialog, View v, String inputStr) {
                                    if(!inputStr.isEmpty()){
                                        Book book = new Book();
                                        Toast.makeText(getContext(),"查询书籍中……",Toast.LENGTH_LONG).show();
                                        new ISBNApiUtil().getBookFromISBN(book,inputStr);
                                        if(book.getTitle() == null){
                                            Toast.makeText(getContext(),"书籍添加失败！请确认ISBN号是否正确",Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(getContext(),"书籍添加成功",Toast.LENGTH_SHORT).show();
                                            mBookList.add(book);
                                            mBookAdapter.notifyItemInserted(mBookList.size());
                                        }
                                    }
                                    else{
                                        Toast.makeText(getContext(),"请输入ISBN号",Toast.LENGTH_SHORT).show();
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
            InputDialog inputDialog = new InputDialog("添加书籍", "请输入书籍的ISBN号", "添加", "取消", "")
                        .setCancelable(false)
                        .setOkButton(new OnInputDialogButtonClickListener<InputDialog>() {
                            @Override
                            public boolean onClick(InputDialog baseDialog, View v, String inputStr) {
                                if(!inputStr.isEmpty()){
                                    Book book = new Book();
                                    Toast.makeText(getContext(),"查询书籍中……",Toast.LENGTH_LONG).show();
                                    new ISBNApiUtil().getBookFromISBN(book,inputStr);
                                    if(book.getTitle() == null){
                                        Toast.makeText(getContext(),"书籍添加失败！请确认ISBN号是否正确",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        mBookList.add(book);
                                        mBookAdapter.notifyItemInserted(mBookList.size());
                                        Toast.makeText(getContext(),"书籍添加成功",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(getContext(),"请输入ISBN号",Toast.LENGTH_SHORT).show();
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
//                //打开相册
//                EasyPhotos.createAlbum(getActivity(), true,false, GlideEngine.getInstance())//参数说明：上下文，是否显示相机按钮，是否使用宽高数据（false时宽高数据为0，扫描速度更快），[配置Glide为图片加载引擎](https://github.com/HuanTanSheng/EasyPhotos/wiki/12-%E9%85%8D%E7%BD%AEImageEngine%EF%BC%8C%E6%94%AF%E6%8C%81%E6%89%80%E6%9C%89%E5%9B%BE%E7%89%87%E5%8A%A0%E8%BD%BD%E5%BA%93)
//                        .setFileProviderAuthority("com.jnu.booktrace")//参数说明：见下方`FileProvider的配置`
//                        .start(new SelectCallback() {
//                            @Override
//                            public void onResult(ArrayList<Photo> photos, boolean isOriginal) {
//                                //获取file,进行对应操作
//                                try {
//                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photos.get(0).uri);
//                                    bitmap = ImageHandle.compressImage(bitmap);
//                                    Bitmap bitmap1 = ImageHandle.toRoundBitmap(bitmap);
//
//                                    //解析条形码/二维码
//                                    String result = CodeUtils.parseCode(bitmap);
//                                    Log.e("从相册获取条形码",result);
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
        //获取toolbar
        toolbar = v.findViewById(R.id.toolbar);
//        //主标题，必须在setSupportActionBar之前设置，否则无效，如果放在其他位置，则直接setTitle即可
        toolbar.setTitle("");
        //用toolbar替换actionbar
        //setSupportActionBar(toolbar);
    }

    private void initSpinner(View v){
        spinner = v.findViewById(R.id.spinner);
        // 建立数据源
        String[] mItems = {"默认书架", "名家名著", "历史", "经济"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, mItems);
        //spinner_item 直接显示的样式
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        //spinner_dropdown_item 下拉的样式
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
        //预先加载三本书，方便调试
        String[] isbn={"9787020024759","9787505352377","9787040195835","9787544210966","9787544211765"};
        mBookList = new ArrayList<>();
        for(int i = 0; i < isbn.length; i++){
            Book book = new Book();
            if(!isBookExist(isbn[i])){ //如果数据库中没有则请求，请求完了加入到（本地）数据库
                new ISBNApiUtil().getBookFromISBN(book, isbn[i]);
            }
            mBookList.add(QueryBook(isbn[i]));
        }
    }

    private void initRecyclerView(View v){
        mRecyclerView = v.findViewById(R.id.rv_book_overview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        // 创建菜单：
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
                int width = getResources().getDimensionPixelSize(R.dimen.dp_80);
                // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
                // 2. 指定具体的高，比如80;
                // 3. WRAP_CONTENT，自身高度，不推荐;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                SwipeMenuItem deleteItem = new SwipeMenuItem(getContext())
                        .setBackgroundColor(getContext().getColor(R.color.text_sticker_red_easy_photos))
                        .setWidth(width)
                        .setHeight(height)
                        .setText("删除")
                        .setTextColor(getContext().getColor(R.color.white));
                SwipeMenuItem moveItem = new SwipeMenuItem(getContext())
                        .setBackgroundColor(getContext().getColor(R.color.text_sticker_orange_easy_photos))
                        .setWidth(width)
                        .setHeight(height)
                        .setText("移动至...")
                        .setTextColor(getContext().getColor(R.color.white));
                rightMenu.addMenuItem(moveItem); //添加书架更换按钮
                rightMenu.addMenuItem(deleteItem); //Item右侧添加删除按钮。
            }
        };

        OnItemMenuClickListener mItemMenuClickListener = new OnItemMenuClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge, int position) {
                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                menuBridge.closeMenu();

                int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

                if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                    //Toast.makeText(getContext(), "list第" + position + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
                } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
                    //Toast.makeText(getContext(), "list第" + position + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
                }
            }
        };

        // 菜单点击监听。
        mRecyclerView.setOnItemMenuClickListener(mItemMenuClickListener);
        // 滑动菜单监听器。
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
            bookHolder.tvBookShelf.setText("");//TODO:确定书籍所属书架的名字

            String imgurl = adpList.get(position).getImage();   // 得到该项所代表的url地址
            Drawable drawable = imgCache.get(imgurl);       // 先去缓存中找

            TagInfo tag = new TagInfo();
            tag.url = adpList.get(position).getImage();

            if (null != drawable) {                         // 找到了直接设置为图像
                bookHolder.ivImage.setImageDrawable(drawable);
            }
            else {                                      // 没找到则开启异步线程
                drawable = loader.loadDrawableByTag(tag, new ImageUtil.ImageCallBack() {
                    @Override
                    public void obtainImage(TagInfo ret_info) {
                        imgCache.put(ret_info.url, ret_info.drawable);    // 首先把获取的图片放入到缓存中
                        // 通过返回的TagInfo去Tag缓存中找，然后再通过找到的Tag来获取到所对应的ImageView
                        bookHolder.ivImage.setImageDrawable(ret_info.drawable);
                    }
                });
            }

            /**
             * 书籍项的点击事件，跳转至详情页
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