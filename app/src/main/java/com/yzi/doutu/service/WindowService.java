package com.yzi.doutu.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.pizidea.imagepicker.UilImagePresenter;
import com.yzi.doutu.R;
import com.yzi.doutu.activity.SearchDialogActivity;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.HandlerUtil;
import com.yzi.doutu.utils.SharedUtils;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


/**
 * 检测是否后台运行服务
 * Created by yzh on 2015/12/25.
 */
public class WindowService extends Service {

    //定义浮动窗口布局
    View mFloatLayout;
    LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;

    ImageView mFloatView;

    private static final String TAG = "WindowService";



    float DownX=0;//上次x坐标
    float DownY=0;
    float moveX=0;//移动的x 距离
    float moveY=0;
    long currentMS = 0;


    AlarmManager alarm;
    PendingIntent sender;
    private  int LONG_TAG=0x12;
    private void register() {
//        IntentFilter localIntentFilter = new IntentFilter();
//        localIntentFilter.addAction(TAG);
//        registerReceiver(this.recevier, localIntentFilter);
//        sendHeart();

        //
        Log.d(TAG, "register");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    //Log.d(TAG,"thread--"+DataCatch.isWarn);
                    try {
                        Thread.sleep(2000);
                        HandlerUtil.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                check();
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
        thread.start();
    }


    private void closeHeart() {
        if ((this.alarm != null) && (this.sender != null)) {
            this.alarm.cancel(this.sender);
            this.alarm = null;
        }
    }

    private void sendHeart() {
        if (this.alarm == null) {
            this.sender = PendingIntent.getBroadcast(this, 0, new Intent(TAG), 0);
            long l = SystemClock.elapsedRealtime();
            this.alarm = ((AlarmManager) getSystemService(Context.ALARM_SERVICE));
            //this.alarm.setRepeating(2, l,1000L, this.sender);
            this.alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, l, 2000, this.sender);
            // 间隔低于1分钟  4.4以上会强制 60S 执行一次
        }
    }

    private BroadcastReceiver recevier = new BroadcastReceiver() {
        public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent) {
//            ComponentName localComponentName = (((ActivityManager)WindowService.this.getSystemService("activity"))
//                    .getRunningTasks(2).get(0)).topActivity;
//            Log.e("wu","getClassName:"+ localComponentName.getClassName());
            check();

        }
    };

    public void check() {
       // if (isAppIsInBackground(this)) {
        if (isQQWecat()) {
            createFloatView();
        } else {
            remove();
        }
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.e(TAG, "WindowService-Create()");
        //SharedUtils.putBoolean("", this, "warn", true);//开启创建悬浮窗标记
        register();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }


    private void createFloatView() {

        //没关闭的话 就不创建了
        if (mFloatLayout != null) {
            return;
        }

        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        Log.i(TAG, "mWindowManager--->" + mWindowManager);
        //设置window type

        if(wmParams==null){
            wmParams = new LayoutParams();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                wmParams.type = LayoutParams.TYPE_PHONE;
            } else {
                //貌似4.4以下设置TYPE_TOAST 不需要手动打开就可以显示悬浮窗
                wmParams.type = LayoutParams.TYPE_TOAST;
            }


            //设置图片格式，效果为背景透明
            wmParams.format = PixelFormat.RGBA_8888;
            //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
            wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
            //调整悬浮窗显示的停靠位置为左侧置顶
            wmParams.gravity = Gravity.TOP | Gravity.LEFT;
            // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
            wmParams.x = CommUtil.getScreenWidth(this);
            wmParams.y = CommUtil.getScreenHeight(this) / 2;
            //CommUtil.showToast("wmParams.x="+wmParams.x+"===wmParams.y:"+wmParams.y);
            //设置悬浮窗口长宽数据
            wmParams.width = LayoutParams.MATCH_PARENT;
            wmParams.height = LayoutParams.MATCH_PARENT;
            // 设置悬浮窗口长宽数据
            wmParams.width = CommUtil.dip2px(this, 45);
            wmParams.height = CommUtil.dip2px(this, 45);
        }


        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局

        mFloatLayout = inflater.inflate(R.layout.float_layout, null);

        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮
        mFloatView = (ImageView) mFloatLayout.findViewById(R.id.floatImg);

        new UilImagePresenter().onPresentCircleImage(mFloatView,
                SharedUtils.getString("", DouApplication.getInstance(), "icon_img", CommUtil.ICON), 0);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth() / 2);
        Log.i(TAG, "Height/2--->" + mFloatView.getMeasuredHeight() / 2);
        //设置监听浮动窗口的触摸移动
        mFloatView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                wmParams.x = (int) event.getRawX() - mFloatView.getMeasuredWidth() / 2;
                Log.i(TAG, "RawX" + event.getRawX());
                Log.i(TAG, "X" + event.getX());
                //减25为状态栏的高度
                wmParams.y = (int) event.getRawY() - mFloatView.getMeasuredHeight() / 2 - 25;
                Log.i(TAG, "RawY" + event.getRawY());
                Log.i(TAG, "Y" + event.getY());
                //刷新
                if(mWindowManager!=null&&mFloatLayout!=null){
                    mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                }

                //以下模拟长按
                //按下的时间 大于1.5S 且移动距离过小者视为长按操作。
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        DownX = event.getX();//float DownX
                        DownY = event.getY();//float DownY
                        moveX = 0;
                        moveY = 0;
                        currentMS = System.currentTimeMillis();
                        break;

                    case MotionEvent.ACTION_MOVE:

                        moveX += Math.abs(event.getX() - DownX);//X轴距离
                        moveY += Math.abs(event.getY() - DownY);//y轴距离

                        break;
                    case MotionEvent.ACTION_UP:
                        long moveTime = System.currentTimeMillis() - currentMS;
                        if(moveTime>1300&&(moveX<70||moveY<70)){
                            handler.sendEmptyMessage(LONG_TAG);
                            return true;//消费掉事件不在传递
                        }
                            break;
                }

                return false;  //返回false，否则OnClickListener获取不到监听
            }
        });



        mFloatView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Toast.makeText(WindowService.this, "onClick", Toast.LENGTH_SHORT).show();
                DouApplication.getInstance().removeAllActivity();//保证弹出界面前面没有activity
                startActivity(new Intent(WindowService.this, SearchDialogActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK));

            }
        });


    }


    //模拟长按关闭悬浮窗的handler
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==LONG_TAG){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(WindowService.this);
                alertDialog.setMessage("关闭悬浮窗？");
                alertDialog.setPositiveButton("否",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                alertDialog.setNegativeButton("是",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                remove();
                            }
                        });

                AlertDialog ad = alertDialog.create();

                ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                //ad.setCanceledOnTouchOutside(false);
                ad.show();
            }

        }
    };



    public void remove() {
       // SharedUtils.putBoolean("", this, "warn", true);//每次移除时，标记下一次检查开启创建悬浮窗
        if (mFloatLayout != null) {
            //移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
            mFloatLayout = null;
            Log.e(TAG, "remove()----");
        }

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        remove();
        closeHeart();
        //unregisterReceiver(this.recevier);
    }




    /**
     * QQ或微信是否运行在前台
     * @return
     */
    private boolean isQQWecat() {
        String name=CommUtil.getTopActivty(this);
        if(name.equals(CommUtil.QQ)){
            CommUtil.FLAG=CommUtil.QQ;
        }else if(name.equals(CommUtil.WeChat)){
            CommUtil.FLAG=CommUtil.WeChat;
        }
        return name.equals(CommUtil.QQ)||name.equals(CommUtil.WeChat);
    }


}