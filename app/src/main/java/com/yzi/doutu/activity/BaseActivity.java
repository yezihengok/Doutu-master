package com.yzi.doutu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.yzi.doutu.service.DouApplication;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.SharedUtils;


/**
 * Created by yzh on 2015/11/24.
 */
public abstract class BaseActivity extends AppCompatActivity {
    Toast toast;
    public DouApplication application;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        application=(DouApplication)this.getApplication();
//        FullModes fullModes=new FullModes(this);
//        fullModes.setStateBarColor(Color.parseColor("#ff3d3d"));
//
//        initView();
//        setData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

   // public void initView(){};
    //public  abstract void setData();

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0 || s.equals("null");
    }


    public void toActivity(Class activity) {
        Intent intent = new Intent(BaseActivity.this, activity);
        startActivity(intent);
    }


    /**
     * 获取 string 字符串
     * @param id R.string.app_name
     * @return
     */
    public String getStrings(int id){
        return getResources().getString(id);
    }

    public  void showToast(final String tip)
    {
        if(isEmpty(tip)){
            Log.v("", "toast的字符串为空!");
            // return;
        }
        if (toast == null)
        {
            toast = Toast.makeText(this, tip, Toast.LENGTH_SHORT);
        }
        else
        {
            toast.setText(tip);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    /**
     * 为textview 设值，避免空值情况
     * @param tv
     * @param str
     */
    public void setTextValues(TextView tv,String str){
        if(tv!=null)
            if(!isEmpty(str)){
                tv.setText(str);
            }
    }

    /**
     * 为textview 设值，避免空值情况
     * @param tv
     * @param str 显示内容
     * @param s 追加内容
     */
    public void setTextValues(TextView tv,String str,String s){
        if(tv!=null)
            if(!isEmpty(str)){
                if(!isEmpty(s)){
                    str+=s;
                }
                tv.setText(str);
            }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        switch (keyCode)
        {

            case KeyEvent.KEYCODE_VOLUME_DOWN:

                return true;
            case KeyEvent.KEYCODE_MENU:
                return super.onKeyDown(keyCode, event);
            case KeyEvent.KEYCODE_BACK:

                return super.onKeyDown(keyCode, event);

        }
        return super.onKeyDown(keyCode, event);
    }
}
