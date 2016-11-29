package com.yzi.doutu.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yzi.doutu.R;
import com.yzi.doutu.bean.DataBean;
import com.yzi.doutu.db.DBTools;
import com.yzi.doutu.operate.OperateUtils;
import com.yzi.doutu.operate.OperateView;
import com.yzi.doutu.operate.TextObject;
import com.yzi.doutu.share.QQShareManager;
import com.yzi.doutu.utils.CommInterface;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.ImageUtils;
import com.yzi.doutu.utils.SharedUtils;
import com.yzi.doutu.view.ColorTagImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static com.yzi.doutu.R.id.modifyLayout;
import static com.yzi.doutu.utils.CommUtil.closeWaitDialog;
import static com.yzi.doutu.utils.CommUtil.dip2px;
import static com.yzi.doutu.utils.CommUtil.showWaitDialog;
import static com.yzi.doutu.utils.CommUtil.toShare;


/**
 * 图片添加文字activity
 * Created by yzh-t105 on 2016/10/25.
 */

public class ModifyPicActivity extends BaseActivity implements  View.OnClickListener{

    private TextView tvRight;
    private LinearLayout mainLayout;
    private com.yzi.doutu.view.ColorTagImageView colortag;
    private android.widget.EditText edWords;
    private TextView tvcc;
    private ImageView addTextImg;
    private TextView tvMul;
    DataBean dataBean;


    Bitmap bitmap;
    Bitmap showBitmap = null;
    Bitmap resizeBmp;
    private Context context;
    private TextView typeface_a,typeface_b,typeface_c;

    Bundle bundle;
    Uri fileUri;
    String formWhere;

    /**mainLayout里当前显示图片的地址**/
    String showPath;
    //保存图片的宽高
    private int HEIGHT=200;
    private int WIDTH=200;

    String words="新增文字";
    int wordsColor=Color.BLACK;//文字颜色
    Typeface typeface;
    TextObject textObj;
    OperateUtils operateUtils;
    OperateView operateView;
    /**是否可添加多个**/
    boolean ismulTxt=true;
    Handler myHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == 1)
            {
                if (mainLayout.getWidth() != 0)
                {
                    Log.i("LinearLayoutW", mainLayout.getWidth() + "");
                    Log.i("LinearLayoutH", mainLayout.getHeight() + "");
                    // 取消定时器
                    if(!TextUtils.isEmpty(showPath)){
                        closeWaitDialog();
                        timer.cancel();
                        fillContent();

                    }

                }
            }
        }
    };
    Timer timer = new Timer();
    TimerTask task = new TimerTask()
    {
        public void run()
        {
            Message message = new Message();
            message.what = 1;
            myHandler.sendMessage(message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        application.addActivity(this);
        setContentView(R.layout.activity_modify_pic);
        bundle=getIntent().getExtras();
        formWhere=bundle.getString("formWhere");

        initView();
        initData();


    }

    /**
     * 更新文字对象
     */
    public void updateTextObj(){
        if(textObj != null) {
            textObj.setText(words);
            textObj.setColor(wordsColor);
            textObj.setTypeface(typeface);
            textObj.commit();
            operateView.addItem(textObj);
        }
    }
    private void initView() {
        operateUtils = new OperateUtils(this);
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
        addTextImg= (ImageView) findViewById(R.id.addTextImg);
        this.edWords = (EditText) findViewById(R.id.edWords);
        tvcc= (TextView) findViewById(R.id.tvcc);
        tvMul= (TextView) findViewById(R.id.tvMul);
        this.colortag = (ColorTagImageView) findViewById(R.id.color_tag);
        this.mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        tvRight= (TextView) findViewById(R.id.tvRight);
        tvRight.setOnClickListener(this);
        typeface_a.setOnClickListener(this);
        typeface_b.setOnClickListener(this);
        typeface_c.setOnClickListener(this);
        addTextImg.setOnClickListener(this);
        tvMul.setOnClickListener(this);
        int width= CommUtil.getScreenWidth();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,-1);
        mainLayout.setLayoutParams(lp);
        colortag.setListener(new ColorTagImageView.OnColorTagChanges() {
            @Override
            public void onColorChange(int colors) {

            tvcc.setTextColor(colors);
            wordsColor=colors;
            updateTextObj();
            }
        });

        edWords.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    words=edWords.getText().toString();
                    updateTextObj();
                }
            }
        });

        typeface_b.setTypeface(CommUtil.getTypeface("新蒂小丸子体",Typeface.NORMAL));
        typeface_c.setTypeface(CommUtil.getTypeface("华康少女",Typeface.NORMAL));
        setTypeface(SharedUtils.getInt(null,"typeface_"));
    }

    void initData(){

        //是否从相册选图过来的
        if(formWhere!=null){

            fileUri=bundle.getParcelable("fileUri");
            try {
                showBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),fileUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            words="来自相册的图片~";

            //先保存一下未添加文字之前的图片作为原图
            showPath = ImageUtils.saveBitmapToFiles(showBitmap, dataBean,showBitmap.getHeight(),showBitmap.getWidth());
            Log.v("","相册选来的原图已保存至:" + showPath);

            //从相册选图过来的 需要new一个DataBean
            if(formWhere!=null){
                dataBean=new DataBean();
                dataBean.setGifPath(showPath);// GifPath该字段只有在从相册选图时，该地址会保存截图的本地文件路径
                dataBean.setName(words);
                dataBean.setId((int)System.currentTimeMillis());
                WIDTH=HEIGHT=showBitmap.getWidth();//相册选来的图片保存我稍微设置大一点
            }

        }else{

            showWaitDialog(context,null,true);

            dataBean= (DataBean) getIntent().getSerializableExtra("dataBean");
            //如果不是从我的制作界面进来的,dataBean改为从数据库读取
            if(!"DIY".equals(dataBean.getFormWhere())){
                DataBean bean=DBTools.getInstance().madeById(String.valueOf(dataBean.getId()));
                if(bean!=null){
                    dataBean=bean;
                }
            }

            //showPath=dataBean.getGifPath();//这个地址是网络图片地址是不行滴(需要先保存本地)
            showPath=null;//没下载好之前先置空
            dataBean.setFormWhere("DIY");//为了保存到临时temp文件下随时清空，才改为的DIY
            CommUtil.onDownLoad(dataBean, context, new CommInterface.setListener() {
                @Override
                public void onResult(String picpath) {
                    showPath=picpath;
                    dataBean.setFormWhere(null);
                }
            });


            if(!TextUtils.isEmpty(dataBean.getName())){
                words=dataBean.getName();
            }
        }

        edWords.setText(words);
        edWords.setSelection(words.length());

        // 延迟每次延迟10 毫秒 隔1秒执行一次
        timer.schedule(task, 10, 1000);

    }

    public void setTypeface(int type){
        String  name=null;
        if(type==0){
            edWords.setTypeface(CommUtil.getTypeface(name,Typeface.BOLD));
            typeface_a.setTextColor(Color.parseColor("#00afec"));
            typeface_b.setTextColor(Color.parseColor("#6D6D6D"));
            typeface_c.setTextColor(Color.parseColor("#6D6D6D"));
        }else if(type==1){
            name="新蒂小丸子体";
            edWords.setTypeface(CommUtil.getTypeface(name,Typeface.BOLD));
            typeface_a.setTextColor(Color.parseColor("#6D6D6D"));
            typeface_b.setTextColor(Color.parseColor("#00afec"));
            typeface_c.setTextColor(Color.parseColor("#6D6D6D"));
        }else if(type==2){
            name="华康少女";
            edWords.setTypeface(CommUtil.getTypeface(name,Typeface.BOLD));
            typeface_a.setTextColor(Color.parseColor("#6D6D6D"));
            typeface_b.setTextColor(Color.parseColor("#6D6D6D"));
            typeface_c.setTextColor(Color.parseColor("#00afec"));
        }

        typeface=CommUtil.getTypeface(name,Typeface.NORMAL);
        updateTextObj();
        SharedUtils.putString(null,"typeface",name);
        SharedUtils.putInt(null,"typeface_",type);
    }

    private void fillContent()
    {
        //showPath 是有可能被 用户跑到文件夹里自己删掉滴。
        resizeBmp = BitmapFactory.decodeFile(showPath);
        if(resizeBmp!=null){
            //缩放一下bitmap保证bitmap宽高适应view
            resizeBmp = ImageUtils.scaleWithWH(resizeBmp,mainLayout.getWidth(),mainLayout.getHeight());
            operateView = new OperateView(context, resizeBmp);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    resizeBmp.getWidth(), resizeBmp.getHeight());
            operateView.setLayoutParams(lp);
            Log.v("",mainLayout.getWidth()+"--"+resizeBmp.getWidth());
            mainLayout.addView(operateView);
            operateView.setMultiAdd(false); //true可以添加多个文字

            textObj = operateUtils.getTextObject(words, operateView,6, 0,CommUtil.dip2px(50));
            updateTextObj();
        }else{

            CommUtil.showDialog(context,showPath+"\n图片不存在或者无效!", "知道了"
                    , null, new CommInterface.setClickListener() {
                @Override
                public void onResult() {

                }
            },null);
        }
        setMulTxt();
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
            case R.id.addTextImg:
                textObj = operateUtils.getTextObject(words, operateView,6, 0,CommUtil.dip2px(50));
                updateTextObj();
                break;
            case R.id.tvMul:
                setMulTxt();
                break;

        }
    }

    private void setMulTxt() {
        String open;
        if(ismulTxt){
            ismulTxt=false;
            addTextImg.setVisibility(View.GONE);
            operateView.setMultiAdd(false);
            open=String.format(getStrings(R.string.tv_Mul),"关闭");
        }else{
            ismulTxt=true;
            addTextImg.setVisibility(View.VISIBLE);
            operateView.setMultiAdd(true);
            open=String.format(getStrings(R.string.tv_Mul),"开启");
        }

        SpannableString sp=new SpannableString(open);
        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#00afec"))
                ,7,11, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tvMul.setText(sp);
    }


    private void save() {
        operateView.save();

        bitmap = ImageUtils.createViewBitmap(operateView,1);
       // bitmap=ImageUtils.scaleWithWH(bitmap,HEIGHT,WIDTH);
     //   bitmap = ImageUtils.createViewBitmap(operateView,HEIGHT,WIDTH);

        String filePath = ImageUtils.saveBitmapToFiles(bitmap, dataBean,HEIGHT,WIDTH);
        Log.v("","已保存至:" + filePath);

        dataBean.setName(words);
        dataBean.setMadeUrl(filePath);

        if (CommUtil.isWeiBaopen()) {
            CommUtil.getInstance().showSharePop(context,dataBean,null);
        } else {
            CommUtil.onDownLoad(dataBean,context,0);
        }

        DBTools.getInstance().addMades(dataBean);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bitmap!=null){
            bitmap.recycle();
        }
        if(showBitmap!=null){
            showBitmap.recycle();
        }
        if(resizeBmp!=null){
            resizeBmp.recycle();
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
//                                String fileName=dataBean.getName()+i+ ".jpg";
//                                String filePath = ImageUtils.saveBitmapToFile(bitmap, fileName);
//                                Log.v("","已保存至:" + filePath);
//                            }
//                        }
//                    }
//                });
//
//    }


}
