package com.yzi.doutu.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yzi.doutu.R;
import com.yzi.doutu.bean.DataBean;
import com.yzi.doutu.service.DouApplication;
import com.yzi.doutu.utils.CommInterface;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.DownLoadImageService;

import static com.yzi.doutu.utils.CommUtil.closeWaitDialog;
import static com.yzi.doutu.utils.CommUtil.showToast;
import static com.yzi.doutu.utils.CommUtil.showWaitDialog;

/**
 * 实现QQSDK分享功能的核心类 2016/10/11.
 */

public class QQShareManager {

    static QQShareManager mInstance;
    private Context context;


    Tencent tencent;
    private IUiListener qShareListener;

    public QQShareManager(Context context) {
        this.context = context;
       // this.qShareListener=qShareListener;
        initListener();
    }

    public static QQShareManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new QQShareManager(context);
        }
        return mInstance;
    }



    /**
     * 初始化QQ分享
     */
    private void initListener(){

        tencent= Tencent.createInstance(CommUtil.qq_key,context);
        //分享返回后，要关闭dialog
        qShareListener = new IUiListener() {
            @Override
            public void onCancel() {
//                DouApplication.getInstance().removeAllActivity();
//                CommUtil.getInstance().startService();
            }
            @Override
            public void onComplete(Object response) {

            }
            @Override
            public void onError(UiError e) {

            }
        };
    }

//
//     acitivy 里 设置onActivityResult qShareListener 监听才会接受
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Tencent.onActivityResultData(requestCode,resultCode,data,qShareListener);
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (tencent != null) {
//            tencent.releaseResource();
//        }
//    }

    /**
     * 利用Glide 把图片下载本地 分享图片文件
     */
    public  void onDownLoad(DataBean dataBean) {
        showWaitDialog(context, "加载中...", false);
        //启动图片下载线程
        DownLoadImageService service = new DownLoadImageService(context,dataBean,
                new CommInterface.ImageDownLoadCallBack() {
                    @Override
                    public void onDownLoadSuccess(final String filePath) {
                        // toShare(context,new File(filePath));
                        toQShare(filePath);
                        closeWaitDialog();
                    }
                    @Override
                    public void onDownLoadFailed() {
                        // 图片保存失败
                        closeWaitDialog();
                        showToast("获取图片失败");
                    }
                });
        //启动图片下载线程
        new Thread(service).start();
    }

    public void toQShare(String url) {
        Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,url);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,CommUtil.getInstance().getString( R.string.app_name));
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        // params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);//分享时打开分享到QZone的对话框
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        tencent.shareToQQ((Activity) context, params,qShareListener);
    }

}
