package com.yzi.doutu.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pizidea.imagepicker.UilImagePresenter;
import com.yzi.doutu.R;
import com.yzi.doutu.activity.ModifyPicActivity;
import com.yzi.doutu.bean.DataBean;
import com.yzi.doutu.db.DBTools;
import com.yzi.doutu.service.DouApplication;
import com.yzi.doutu.service.WindowService;
import com.yzi.doutu.share.QQShareManager;
import com.yzi.doutu.utils.gifdecoder.GifImageDecoder;
import com.yzi.doutu.view.ColorTagImageView;
import com.yzi.doutu.view.LoadDialog;
import com.yzi.doutu.view.operate.OperateConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static android.R.attr.tag;
import static android.R.attr.typeface;
import static com.yzi.doutu.utils.ContextUtil.getApplicationContext;

/**
 * 公用方法工具类
 * Created by yzh-t105 on 2016/9/20.
 */
public class CommUtil {

    private static LoadDialog frameDialog;
    static Toast toast;
    public static final String qq_key  = "100380359";//
    public static final String WECHAT_APP_ID  = "wxacc4322798406c76";//zhb
    public static final String QQ = "com.tencent.mobileqq";
    public static final String WeChat = "com.tencent.mm";
    public static final String WEIBA="Qweiba";
    public static String FLAG;

    /**默认头像的网络图片地址**/
    public static final String ICON="http://h.hiphotos.baidu.com/image/pic/item/34fae6cd7b899e51601a7b9c40a7d933c9950da5.jpg";
    /**最热表情图片列表**/
   // public static final String HOT_URL="http://api.jiefu.tv/app2/api/dt/item/hotList.html";
    public static final String HOT_URL="http://api.jiefu.tv/app2/api/dt/item/newList.html";
    /**最新表情图片列表**/
    public static final String NEW_URL="http://api.jiefu.tv/app2/api/dt/shareItem/newList.html";
    /**真人表情图片列表**/
    public static final String REALMAN_URL="http://api.jiefu.tv/app2/api/dt/tag/getByType.html";
    /**真人表情图片列表**/
    public static final String REALMANINFO_URL="http://api.jiefu.tv/app2/api/dt/item/getByTag.html";
    /**表情分类列表**/
    public static final String ALLTYPE= "http://api.jiefu.tv/app2/api/dt/tag/allList.html";
    /**表情分类列表详情**/
    //public static final String ALLTYPEBYID="http://api.jiefu.tv/app2/api/dt/shareItem/getByTag.html";
    public static final String ALLTYPEBYID= "http://api.jiefu.tv/app2/api/dt/item/getByTag.html";
    /**关键字搜索表情**/
    public static final String KEYWORD_SEARCH="http://api.jiefu.tv/app2/api/dt/shareItem/search.html";

   private  static CommUtil commUtil;
    public static CommUtil getInstance()
    {
        if(commUtil==null){
            commUtil=new CommUtil();
        }
        return commUtil;
    }


    public static void showWaitDialog(Context context, String msg, boolean cancelAble) {
        if (frameDialog != null && frameDialog.isShowing()) {
            return;
        }
        closeWaitDialog();
        frameDialog = new LoadDialog(context, msg, cancelAble);
        frameDialog.show();
    }

    public static void closeWaitDialog() {
        if (frameDialog != null) {
            frameDialog.dismiss();
            frameDialog = null;

        }
    }

    /**
     *
     * @param tip
     * @param flag 0短 时间  1长时间
     */
    public static void showToast(final String tip,int flag) {
        if (TextUtils.isEmpty(tip)) {
            Log.e("", "toast的字符串为空!");
            return;
        }
        TextView text=null;
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), tip, Toast.LENGTH_SHORT);
        } else {
            // toast.setText(tip);
            if(flag==0){
                toast.setDuration(Toast.LENGTH_SHORT);
            }else{
                toast.setDuration(Toast.LENGTH_LONG);
            }

        }
        View layout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.toast,null);
        text = (TextView) layout.findViewById(R.id.text);
        toast.setView(layout);
        text.setText(tip);
        toast.setGravity(Gravity.BOTTOM, 0, 150);
        toast.show();

    }

    public static void showToast(final String tip) {
        if (TextUtils.isEmpty(tip)) {
            Log.e("", "toast的字符串为空!");
            return;
        }
        TextView text=null;
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), tip, Toast.LENGTH_SHORT);
        } else {
           // toast.setText(tip);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        View layout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.toast,null);
        text = (TextView) layout.findViewById(R.id.text);
        toast.setView(layout);
        text.setText(tip);
        toast.setGravity(Gravity.BOTTOM, 0, 150);
        toast.show();

    }




    /**
     * 利用Glide 把图片下载本地
     * @param dataBean
     * @param context
     */
    public static void onDownLoad(DataBean dataBean,final Context context
            ,final CommInterface.setListener listener) {
        //showWaitDialog(context, "加载中...", false);
        //启动图片下载线程
        DownLoadImageService service = new DownLoadImageService(context,dataBean,
                new CommInterface.ImageDownLoadCallBack() {

                    @Override
                    public void onDownLoadSuccess(final String filePath) {
                        listener.onResult(filePath);
                    }
                    @Override
                    public void onDownLoadFailed() {
                        // 图片保存失败
                        //closeWaitDialog();
                        showToast("获取图片失败");
                    }
                });
        //启动图片下载线程
        new Thread(service).start();
    }

    /**
     * 利用Glide 把图片下载本地
     * @param dataBean
     * @param context
     * @param flag 0调用系统分享  1直接跳转到QQ界面分享 2直接跳转到微信界面分享
     */
    public static void onDownLoad(DataBean dataBean,final Context context,final int flag) {
        //showWaitDialog(context, "加载中...", false);
        //启动图片下载线程
        DownLoadImageService service = new DownLoadImageService(context,dataBean,
                new CommInterface.ImageDownLoadCallBack() {

                    @Override
                    public void onDownLoadSuccess(final String filePath) {
                        if(flag==0){
                            toShare(context,new File(filePath));
                        }else if(flag==1){

                            //是否开启了QQ尾巴分享
                            if(SharedUtils.getBoolean("",context,WEIBA,false)){
                                //使用QQSDK分享
                                QQShareManager.getInstance(context).toQShare(filePath);
                            }else{
                                shareQQ(context,new File(filePath));
                            }

                        }else if(flag==2){

                            shareToWechat(context,new File(filePath));
                        }

                        //closeWaitDialog();
                    }

                    @Override
                    public void onDownLoadFailed() {
                        // 图片保存失败
                        //closeWaitDialog();
                        showToast("获取图片失败");
                    }
                });
        //启动图片下载线程
        new Thread(service).start();
    }


    /**
     * 调用系统分享
     * @param context
     * @param file
     */
    public static synchronized void toShare(Context context,File file){
        if(file!=null){
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            shareIntent.setType("image/*");
            context.startActivity(Intent.createChooser(shareIntent
                    , context.getResources().getText(R.string.app_name)));
        }
    }

    /**
     * 直接跳转到QQ界面分享
     * @param context
     * @param file
     */
    public  static void shareQQ(Context context,File file){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT,"发送纯文本");
//        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        sendIntent.setType("image/*");
        try {
            sendIntent.setClassName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity");
            Intent chooserIntent = Intent.createChooser(sendIntent, "选择分享途径");
            if (chooserIntent == null) {
                return;
            }
            context.startActivity(chooserIntent);
        } catch (Exception e) {
            context.startActivity(sendIntent);
        }
    }

    /**
     * 直接跳转到微信界面分享(但是微信无法接受GIF只显示静图, QQ都能接受GIF好么..)
     * @param context
     * @param file
     */
    public static void shareToWechat(Context context,File file) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT,"发送纯文本");
//        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        sendIntent.setType("image/*");
        try {
            //sendIntent.setClassName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");//微信朋友圈
            sendIntent.setClassName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
            Intent chooserIntent = Intent.createChooser(sendIntent, "选择分享途径");
            if (chooserIntent == null) {
                return;
            }
            context.startActivity(chooserIntent);
        } catch (Exception e) {
            context.startActivity(sendIntent);
        }
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyyMMddHHmmss
     */
    public static String getDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     *
     * @param id R.id.app_name
     * @return
     */
    public static String getString(int id){
        return DouApplication.getInstance().getString(id);
    };

    // 设置屏幕透明度
    public static void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0~1.0
        context.getWindow().setAttributes(lp);
    }


    private View sharePopView;

    /**
     *
     * @param context
     * @param dataBean
     * @param listener 操作后需要刷新的回调
     * @return
     */
    public void showSharePop(final Context context, final DataBean dataBean,final CommInterface.setFinishListener listener){
        TextView share_name;
         ImageView share_img;
         Button share_send,share_save,share_update;
        //if(sharePopView==null){
            sharePopView= LayoutInflater.from(context).inflate(R.layout.share_dialog, null);
            share_img= (ImageView) sharePopView.findViewById(R.id.share_img);
            share_name= (TextView) sharePopView.findViewById(R.id.share_name);
            share_send= (Button) sharePopView.findViewById(R.id.share_send);
            share_save= (Button) sharePopView.findViewById(R.id.share_save);
            share_update= (Button) sharePopView.findViewById(R.id.share_update);
        //}

        Glide.with(context)
                .load(dataBean.getGifPath())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                //.crossFade()
                .thumbnail(0.5f)
                .centerCrop()
                .error(com.example.yzh.mylibrary.R.drawable.default_img)
                .into(share_img);

        share_name.setText(dataBean.getName());
       final PopupWindowHelper pop = new PopupWindowHelper(sharePopView,context,PopupWindowHelper.TYPE_MATCH_PARENT);
        pop.showFromBottom(context);
        final String tag=dataBean.getFormWhere();

        share_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!"DIY".equals(tag)){
                    CommUtil.onDownLoad(dataBean,context,0);
                }else{
                    //从我的制作列表进来的话，直接发送SD卡里制作过的图片，不在下载
                    toShare(context,new File(dataBean.getGifPath()));
                }

            }
        });

        if(!TextUtils.isEmpty(tag)){
            share_save.setText("删除");
            if("DIY".equals(tag)){
                share_name.setVisibility(View.GONE);
                share_update.setVisibility(View.VISIBLE);
                share_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toAddText(dataBean, context,listener);
                        pop.dismiss();
                    }
                });
            }
        }
        share_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(tag)) {
                    DBTools.getInstance(context).addFavorites(dataBean);
                    showToast("已收藏");
                    pop.dismiss();

                }else{

                    if("Favorites".equals(tag)){
                        DBTools.getInstance(context).remove(dataBean.getId());
                    }else if("DIY".equals(tag)){
                        DBTools.getInstance(context).deleteByid_made(String.valueOf(dataBean.getId()));
                    }
                    listener.onFinish();
                    pop.dismiss();
                }
            }
        });

    }

    /**
     * 公用的 弹出GIF添加文字的dialog
     * @param context
     * @param dataBean
     * @param finishListener
     * @return
     */
    public static Dialog showgifMaker(final Context context,final DataBean dataBean
            , final CommInterface.setFinishListener finishListener) {
        int width = getScreenWidth(context);
        final Dialog showDialog = new Dialog(context);
        showDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        showDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        showDialog.getWindow().setGravity(Gravity.CENTER);
        showDialog.setContentView(R.layout.comm_gif_dialog);

        showDialog.setCancelable(true);
        if(!((Activity)context).isFinishing()){
            showDialog.show();
        }

        LinearLayout lyShowFrame = (LinearLayout) showDialog.findViewById(R.id.lyShow_Frame);
        Button btShowsure = (Button) showDialog.findViewById(R.id.btShow_sure);
        ColorTagImageView colortag = (ColorTagImageView) showDialog.findViewById(R.id.color_tag);
        //RelativeLayout modifyLayout = (RelativeLayout) showDialog.findViewById(R.id.modifyLayout);
        ImageView gifDialogImg= (ImageView) showDialog.findViewById(R.id.gifDialogImg);
        final EditText edmsg = (EditText) showDialog.findViewById(R.id.ed_msg);

        String url;
        if(!TextUtils.isEmpty(dataBean.getOldUrl())){//如果是已经制作过了的图片就展示原图
            url=dataBean.getOldUrl();
        }else{
            url=dataBean.getGifPath();
        }
        new UilImagePresenter().onImage(gifDialogImg,url);

        lyShowFrame.getLayoutParams().width = width - 120;
        edmsg.getLayoutParams().width=dip2px(context,200);
        String txt=dataBean.getName();
        if(!TextUtils.isEmpty(txt)){
            edmsg.setTypeface(CommUtil.getTypeface(SharedUtils.getString(null,"typeface",null),Typeface.BOLD));
            edmsg.setText(txt);
            edmsg.setSelection(txt.length());
        }

        colortag.setListener(new ColorTagImageView.OnColorTagChanges() {
            @Override
            public void onColorChange(int color) {
                edmsg.setTextColor(color);
            }
        });

        btShowsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(edmsg.getText().toString())) {
                    dataBean.setName(edmsg.getText().toString());
                }
                decoderGif(dataBean,context,edmsg,showDialog,finishListener);

            }
        });

        return showDialog;
    }

    /**
     * 利用gilde 获得图片文件并拆分每一帧图片bitmap并添加文字保存至本地在合成gif
     * @param dataBean
     * @param context
     * @param editText
     * @param showDialog
     * @param listener 结束监听
     */
    public static void decoderGif(final DataBean dataBean,final Context context,final EditText editText
    ,final Dialog showDialog,final CommInterface.setFinishListener listener){
        showWaitDialog(context,"处理中...",false);
        CommUtil.onDownLoad(dataBean, context, new CommInterface.setListener() {
            @Override
            public void onResult(String picpath) {
                List<String> paths;
                GifImageDecoder decoder;

                try {
                    FileInputStream inputStream   = new FileInputStream(new File(picpath));
                    try {
                        paths=new ArrayList<>();
                        decoder =new GifImageDecoder();
                        decoder.read(inputStream);
                        Log.d("","共有多少帧:" + decoder.getFrameCount());

                        for (int i=0;i<decoder.getFrameCount();i++){

                            String fileName=dataBean.getName()+i+ ".png";
                            Bitmap bitmaps =drawTextToBitmap(decoder.getFrame(i),editText);
                            String filePath = ImageUtils.saveBitmapToFile(bitmaps, fileName);
                            paths.add(filePath);
                            Log.v("","已保存至:" + filePath);

                        }

                        ImageUtils.createGif(dataBean, paths,80, new CommInterface.setListener() {
                            @Override
                            public void onResult(String picpath) {
                                showDialog.dismiss();
                                closeWaitDialog();
                                toShare(context,new File(picpath));
                                //为空的情况下才需要设置原图地址
                                if(TextUtils.isEmpty(dataBean.getOldUrl())){
                                    String oldUrl=dataBean.getGifPath();
                                    dataBean.setOldUrl(oldUrl);
                                }

                                dataBean.setGifPath(picpath);//替换网址路径为SD文件路径
                                //这里dataBean是会影响外面传进来dataBean 所以 从我的制作列表进来时才替换
                                if("DIY".equals(dataBean.getFormWhere())){

                                }

                                DBTools.getInstance(context).addMades(dataBean);
                                SimpleFileUtils.delFile(ImageUtils.FILE_ROOT_PATH,0,null);//清空分解的文件夹
                                if(listener!=null)
                                    listener.onFinish();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }
        });

    }





    float scaleWidth =0;//bitmap被缩放的比例
    float scaleHeight =0;
    int oldwidth=0;//bitmap原始的宽
    int oldHeight=0;//
    public  Bitmap scaleWithWH(Bitmap bitmap, double w, double h) {
        if (w == 0 || h == 0 || bitmap == null) {
            return bitmap;
        } else {
            // 记录bitmap的宽高
            oldwidth = bitmap.getWidth();
            oldHeight = bitmap.getHeight();
            // 创建一个matrix容器
            Matrix matrix = new Matrix();
            // 计算缩放比例
            scaleWidth = (float) (w / oldwidth);
            scaleHeight = (float) (h / oldHeight);
            // 开始缩放
            matrix.postScale(scaleWidth, scaleHeight);
            // 创建缩放后的图片
            return Bitmap.createBitmap(bitmap, 0, 0, oldwidth, oldHeight, matrix, true);
        }
    }

    /**
     *  bitmap上添加文字
     * @param bitmap
     * @param editText
     * @return
     */
    public static Bitmap drawTextToBitmap(Bitmap bitmap,EditText editText) {

        //因为modifyLayout里显示的bitmap 是被缩放了显示的，这里的GIF拆分的bitmap是原尺寸 ，需要也要按照modifyLayout大小缩放。
        //不然 textview在modifyLayout显示的的坐标， 画到到未缩放bitmap 上也按这个xy 画上去。文字的位置肯定是无法对应的.

        // bitmap = scaleWithWH(bitmap,300,300);

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(editText.getCurrentTextColor());
        //Paint.setTextSize()单位为px,TextView.setTextSize默认传入的单位是sp ,这里需要转换一下
        paint.setTextSize(bitmap.getHeight()/10);
        paint.setDither(true); //获取跟清晰的图像采样
        paint.setFilterBitmap(true);//过滤一些
        paint.setAntiAlias(true);// 去掉边缘锯齿
        paint.setTypeface(CommUtil.getTypeface(SharedUtils.getString(null,"typeface",null),Typeface.BOLD));

        Rect bounds = new Rect();
        String  text=editText.getText().toString();
        paint.getTextBounds(text, 0, text.length(), bounds);

        canvas.drawText(text,(bitmap.getWidth()-bounds.width())/2,bitmap.getHeight()-bounds.height(), paint);

        //画到这里bitmap很可能被拉伸放大了。我在还原d到原来的大小
        //  bitmap = scaleWithWH(bitmap,oldwidth,oldHeight);
        return bitmap;
    }




    /**
     * 确认取消弹窗
     * @param msg 弹框信息
     * @param leftName 左边按钮名称
     * @param rightName 右边按钮名称 (传null表示只显示一个按钮)
     * @param leftlistener 左边按钮监听 (无需监听事件可传null)
     * @param rightlistener 右边按钮监听 (无需监听事件可传null)
     * @return
     */
    public static Dialog showDialog(Context context, String msg, String leftName, String rightName
            , final CommInterface.setClickListener leftlistener,
       final CommInterface.setClickListener rightlistener)
    {
        int width = getScreenWidth(context);

        final Dialog showDialog = new Dialog(context);
        showDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        showDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        showDialog.getWindow().setGravity(Gravity.CENTER);
        showDialog.setContentView(R.layout.comm_show_dialog);
        showDialog.setCancelable(false);
        if(!((Activity)context).isFinishing()){
            showDialog.show();
        }

        LinearLayout lyrame;
        Button btSure, btCancel;
        TextView tvContent, tvContentDes;

        lyrame = (LinearLayout) showDialog.findViewById(R.id.lyShow_Frame);
        btSure = (Button) showDialog.findViewById(R.id.btShow_sure);
        btCancel = (Button) showDialog.findViewById(R.id.btShow_cancle);
        tvContent = (TextView) showDialog.findViewById(R.id.tvShow_content);
        //tvContentDes = (TextView) showDialog.findViewById(getById("R.id.tvShow_content_des"));
        lyrame.getLayoutParams().width = width - 100;

        tvContent.setGravity(Gravity.CENTER);
        tvContent.setText(msg);
//        btSure.setTextColor(Color.parseColor("#467CD4"));
//        btCancel.setTextColor(Color.parseColor("#467CD4"));
        btSure.setText(leftName);

        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog.dismiss();
                if(leftlistener!=null)
                    leftlistener.onResult();
            }
        });

        if(TextUtils.isEmpty(rightName)){
            btCancel.setVisibility(View.GONE);
        }else{
            btCancel.setText(rightName);
            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog.dismiss();
                    if(rightlistener!=null)
                        rightlistener.onResult();
                }
            });
        }

        return showDialog;
    }

    public void toAddText(DataBean dataBean,final Context context,final CommInterface.setFinishListener finishListener){
        String URL=dataBean.getGifPath();
        if(URL.endsWith("GIF")||URL.endsWith("gif")){
          CommUtil.showgifMaker(context, dataBean,finishListener);

        }else{
        Intent intent=new Intent(context, ModifyPicActivity.class);
        intent.putExtra("dataBean",dataBean);
         ((Activity)context).startActivityForResult(intent,0x123);
        }
    }



    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    Intent intent;
    public void  startService(){
        intent=new Intent(DouApplication.getInstance(), WindowService.class);
        DouApplication.getInstance().startService(intent);
        Log.d("","startService");
    }

    public void  stopService(){
        Log.d("","stopService");
        if(intent!=null){
            DouApplication.getInstance().stopService(intent);
        }

    }


    /**
     * 屏幕框的
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 屏幕高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 判断 悬浮窗口权限是否打开
     *
     * @param context
     * @return true 允许  false禁止
     */
    public static boolean getAppOps(Context context) {
        try {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = Integer.valueOf(24);
            arrayOfObject1[1] = Integer.valueOf(Binder.getCallingUid());
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1)).intValue();
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {

        }
        return false;
    }


    /**
     * 跳转到系统的应用详情
     * @param context
     */
    public static void getAppDetailSettingIntent(Context context) {

        if(Build.VERSION.SDK_INT >= 23){

        }
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }


    /**
     * 是否存在“有权查看应用使用情况” 模块
     * @return
     */
    public static boolean hasModule() {
        PackageManager packageManager = getApplicationContext().getPackageManager();
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     *  用户是否已经打开开 有权查看应用使用情况
     * @return
     */
    @SuppressLint("NewApi")
    public  static boolean hasEnable(){
        long ts = System.currentTimeMillis();
        UsageStatsManager usageStatsManager=(UsageStatsManager)getApplicationContext()
                .getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,0, ts);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 跳转 有权查看应用使用情况
     */
    public static void openModule(Context context){
        context.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }



//   注意: 5.0上面的获取顶层应用的方式和5.0以下的应用不同，主要使用UsageStatsManager  来获取顶层应用，
//    但是使用UsageStatsManager 之前必须手动的在设置->安全->有权查看应用使用情况 中打开开关
//    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));

    /**
     * 获取到栈顶应用程序的包名
     * @return
     */
    public static String getTopActivty(Context context) {

        //android5.0以上获取方式
        String topPackageName = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();

            //查询时间设置，时间到了后面不在查询。
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,time - 1000 * 1500, time);
            if (stats != null) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                    Log.e("TopPackage Name", topPackageName);
                }
            }
        }
        //android5.0以下获取方式
        else {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
            ActivityManager.RunningTaskInfo taskInfo = tasks.get(0);
            topPackageName = taskInfo.topActivity.getPackageName();
        }
        return topPackageName;
    }


    /**
     * 判断当前程序是否在前台运行 解决 getRunningTasks 在5.0以后被弃用无效问题
     *
     * @param context
     * @return
     */
    private boolean isAppIsInBackground(Context context) {

        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                //前台程序
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            //5.0以下有效
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
                //JLog.d("componentInfo.getPackageName():"+componentInfo.getPackageName());
            }
        }

        return isInBackground;
    }

    /**
     * 设置字体样式，设置属性后变换字体 目前支持本地两种字体 新蒂小丸子体.ttf 、
     * @param typeface 字体文件名称，为空则设置默认字体
     * @param type Typeface.BOLD |Typeface.ITALIC| Typeface.BOLD_ITALIC
     * @return
     */
    public static Typeface getTypeface(String typeface,int type)
    {
        Typeface tmptf = Typeface.DEFAULT;
        if (typeface != null)
        {
            tmptf = Typeface.createFromAsset(DouApplication.getInstance().getAssets(), "fonts/"
                    + typeface + ".ttf");
        }
        tmptf = Typeface.create(tmptf,type);
        return tmptf;
    }
}
