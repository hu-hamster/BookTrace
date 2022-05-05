package com.jnu.booktrace.person;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.jnu.booktrace.R;


public class DescriptionDialog extends Dialog implements View.OnClickListener {
    private EditText editText;
    private Button ensurebutton, cancelbutton;
    OnEnsureListener onEnsureListener;
    //设置回调接口
    public void setOnEnsureListener(OnEnsureListener onEnsureListener){
        this.onEnsureListener = onEnsureListener;
    }

    public DescriptionDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discription_dialog);   //设置对话框显示布局
        editText = findViewById(R.id.dialog_description_et);
        ensurebutton = findViewById(R.id.dialog_description_btn_ensure);
        cancelbutton = findViewById(R.id.dialog_description_btn_cancel);
        ensurebutton.setOnClickListener(this);
        cancelbutton.setOnClickListener(this);
    }
    public interface OnEnsureListener{
        public void onEnsure();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_description_btn_ensure:
                if(onEnsureListener!=null){
                    onEnsureListener.onEnsure();
                }
                break;
            case R.id.dialog_description_btn_cancel:
                cancel();
                break;
        }
    }

    //获取输入数据的方法
    public String getEditText(){
        return editText.getText().toString().trim();
    }
    public void setEditText(String text) {
        editText.setText(text);
        editText.setSelection(editText.getText().length());  //将光标设置到最后
    }
    //设置Dialog的尺寸和屏幕的尺寸一致
    public void setDialogSize(){
        //获取当前窗口对象
        Window window = getWindow();
        //获取窗口对象的参数
        WindowManager.LayoutParams wlp = window.getAttributes();
        //获取屏幕的宽度
        Display display = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int) (display.getWidth());
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
        handler.sendEmptyMessageDelayed(1,100);
    }
    Handler handler = new Handler(){
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            //自动弹出软键盘的方法
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}
