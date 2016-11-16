package com.yzi.doutu.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.pizidea.imagepicker.ImagePresenter;
import com.pizidea.imagepicker.UilImagePresenter;
import com.yzi.doutu.R;
import com.yzi.doutu.bean.DataBean;
import com.yzi.doutu.db.DBTools;
import com.yzi.doutu.share.QQShareManager;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.ImageUtils;
import com.yzi.doutu.utils.SharedUtils;
import com.yzi.doutu.view.ColorTagImageView;
import com.yzi.doutu.view.MyRelativeLayout;

import java.io.File;

import static com.yzi.doutu.utils.CommUtil.isQQopen;
import static com.yzi.doutu.utils.CommUtil.toShare;


/**
 * 图片添加文字activity
 * Created by yzh-t105 on 2016/10/25.
 */

public class ModifyPicActivity extends BaseActivity implements MyRelativeLayout.MyRelativeTouchCallBack
        , View.OnClickListener{

    private TextView tvRight;
    private com.yzi.doutu.view.MyRelativeLayout modifyLayout;
    private com.yzi.doutu.view.ColorTagImageView colortag;
    private android.widget.EditText edWords;
    private TextView tvcc;

    DataBean dataBean;
    ImagePresenter presenter;
    int num=0;
    Bitmap bitmap;
    private Context context;
    private TextView typeface_a,typeface_b,typeface_c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        application.addActivity(this);
        setContentView(R.layout.activity_modify_pic);

        dataBean= (DataBean) getIntent().getSerializableExtra("dataBean");

        //如果不是从我的制作界面进来的,dataBean改为从数据库读取
        if(!"DIY".equals(dataBean.getFormWhere())){
            DataBean bean=DBTools.getInstance(context).madeById(String.valueOf(dataBean.getId()));
            if(bean!=null){
                dataBean=bean;
            }

        }

        initView();

    }

    private void initView() {
        ((TextView)findViewById(R.id.tvtitle)).setText("制作表情");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        typeface_a= (TextView) findViewById(R.id.typeface_a);
        typeface_b= (TextView) findViewById(R.id.typeface_b);
        typeface_c= (TextView) findViewById(R.id.typeface_c);

        this.edWords = (EditText) findViewById(R.id.edWords);
        tvcc= (TextView) findViewById(R.id.tvcc);
        this.colortag = (ColorTagImageView) findViewById(R.id.color_tag);
        this.modifyLayout = (MyRelativeLayout) findViewById(R.id.modifyLayout);
        tvRight= (TextView) findViewById(R.id.tvRight);
        tvRight.setOnClickListener(this);
        typeface_a.setOnClickListener(this);
        typeface_b.setOnClickListener(this);
        typeface_c.setOnClickListener(this);

        presenter=new UilImagePresenter();
        modifyLayout.setMyRelativeTouchCallBack(this);

        if(!TextUtils.isEmpty(dataBean.getName())){
            modifyLayout.setName(dataBean.getName());
        }

        int width=CommUtil.getScreenWidth(context)-CommUtil.dip2px(context,50);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,width);
        //lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        modifyLayout.setLayoutParams(lp);

        //是否为初次改图
        if(TextUtils.isEmpty(dataBean.getOldUrl())){
            presenter.onImage(modifyLayout.getBgImg(),dataBean.getGifPath());
        }else{
            presenter.onImage(modifyLayout.getBgImg(),dataBean.getOldUrl());
        }

        colortag.setListener(new ColorTagImageView.OnColorTagChanges() {
            @Override
            public void onColorChange(int colors) {
                modifyLayout.setColortag(colors);
                tvcc.setTextColor(colors);
                if(textView!=null) {
                    textView.setTextColor(colors);

                }
            }
        });

        edWords.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                setTextValues(textView,edWords.getText().toString());
                if(s.length()>0){
                    modifyLayout.setName(edWords.getText().toString());
                }
            }
        });

        typeface_b.setTypeface(CommUtil.getTypeface("新蒂小丸子体",Typeface.NORMAL));
        typeface_c.setTypeface(CommUtil.getTypeface("华康少女",Typeface.NORMAL));
        setTypeface(SharedUtils.getInt(null,"typeface_"));
    }

    public void setTypeface(int  type){
        String  name=null;
        if(type==0){
            if(textView!=null){
                textView.setTypeface(CommUtil.getTypeface(name,Typeface.NORMAL));
            }
            edWords.setTypeface(CommUtil.getTypeface(name,Typeface.NORMAL));
            typeface_a.setTextColor(Color.parseColor("#00afec"));
            typeface_b.setTextColor(Color.parseColor("#6D6D6D"));
            typeface_c.setTextColor(Color.parseColor("#6D6D6D"));
        }else if(type==1){
            name="新蒂小丸子体";
            if(textView!=null) {
                textView.setTypeface(CommUtil.getTypeface(name, Typeface.NORMAL));
            }
            edWords.setTypeface(CommUtil.getTypeface(name,Typeface.NORMAL));
            typeface_a.setTextColor(Color.parseColor("#6D6D6D"));
            typeface_b.setTextColor(Color.parseColor("#00afec"));
            typeface_c.setTextColor(Color.parseColor("#6D6D6D"));
        }else if(type==2){
            name="华康少女";
            if(textView!=null) {
                textView.setTypeface(CommUtil.getTypeface(name, Typeface.NORMAL));
            }
            edWords.setTypeface(CommUtil.getTypeface(name,Typeface.NORMAL));
            typeface_a.setTextColor(Color.parseColor("#6D6D6D"));
            typeface_b.setTextColor(Color.parseColor("#6D6D6D"));
            typeface_c.setTextColor(Color.parseColor("#00afec"));
        }
        SharedUtils.putString(null,"typeface",name);
        SharedUtils.putInt(null,"typeface_",type);
    }

    /**
     * 模拟让 modifyLayout 自动点击一下
     */
    private void autoTouch() {
        // Obtain MotionEvent object
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        float x = 200f;
        float y =400f;
        // List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
        int metaState = 0;
        MotionEvent motionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_UP,
                x,
                y,
                metaState
        );
        // Dispatch touch event to view
        modifyLayout.dispatchTouchEvent(motionEvent);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tvRight:
                save();
                break;
            case R.id.typeface_a:
                setTypeface(0);
                break;
            case R.id.typeface_b:
                setTypeface(1);
                break;
            case R.id.typeface_c:
                setTypeface(2);
                break;

        }
    }

    private void save() {

        // bitmap = ImageUtils.createViewBitmap(modifyLayout,0.3f);
        bitmap = ImageUtils.createViewBitmap(modifyLayout,250,250);
        String filePath = ImageUtils.saveBitmapToFiles(bitmap, dataBean);
        Log.v("","已保存至:" + filePath);
        if (isQQopen) {
            QQShareManager.getInstance(context).toQShare(filePath);
        } else {
            toShare(context, new File(filePath));
        }

        //为空的情况下才需要设置原图地址
        if(TextUtils.isEmpty(dataBean.getOldUrl())){
            String oldUrl=dataBean.getGifPath();
            dataBean.setOldUrl(oldUrl);
        }

        dataBean.setGifPath(filePath);//替换网址路径为SD文件路径

        if (!TextUtils.isEmpty(edWords.getText().toString())) {
            dataBean.setName(edWords.getText().toString());
        }
        DBTools.getInstance(context).addMades(dataBean);
    }




    @Override
    public void touchMoveCallBack(int direction) {
        if (direction == MyRelativeLayout.MOVE_LEFT) {
            Log.d("", "手指向左滑动");
        } else {
            Log.d("", "手指向右滑动");
        }
    }
    /**
     * 这个方法可以用来实现滑到某一个地方删除该TextView的实现
     */

    TextView textView;
    @Override
    public void onTextViewMoving() {
        Log.i("", "TextView正在滑动");
    }

    @Override
    public void onTextViewMovingDone(TextView textView) {
        Log.d("", "标签TextView滑动完毕！");

        this.textView=textView;

        String s=textView.getText().toString();
        edWords.setText(textView.getText().toString());
        if (!TextUtils.isEmpty(s)) {
            edWords.setSelection(s.length());
            textView.setTypeface(CommUtil.getTypeface(SharedUtils.getString(null,"typeface",null),Typeface.NORMAL));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bitmap!=null){
            bitmap.recycle();
        }

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_MENU:
                return super.onKeyDown(keyCode, event);
            case KeyEvent.KEYCODE_BACK:
                setResult(RESULT_OK);
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    //打算Glide分解gif每一帧保存至本地，然而好像用Gilde拿不到每一帧的bitmap？
//    public void display(final ImageView imageView, String imageUri) {
//
//        Glide.with(imageView.getContext())
//                .load(imageUri)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                //.into(imageView);
//                .into(new SimpleTarget<GlideDrawable>() {
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                        imageView.setImageDrawable(resource);
//                        if (resource instanceof GifDrawable){
//                            int duration = 0;
//                            // 计算动画时长
//                            GifDrawable drawable = (GifDrawable) resource;
//                            GifDecoder decoder = drawable.getDecoder();
//                            Log.v("","图片帧数:" + drawable.getFrameCount());
//                            for (int i = 0; i < drawable.getFrameCount(); i++) {
//                                duration += decoder.getDelay(i);
//                                Bitmap bitmap=decoder.getNextFrame();
//                                String fileName=dataBean.getName()+i+ ".png";
//                                String filePath = ImageUtils.saveBitmapToFile(bitmap, fileName);
//                                Log.v("","已保存至:" + filePath);
//                            }
//                        }
//                    }
//                });
//
//    }


}
