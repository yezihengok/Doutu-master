package com.yzi.doutu.share;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yzi.doutu.R;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.SharedUtils;

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
     * 分享纯图片
     * @param url 需要分享的本地图片路径。
     */
    public void toQShare(String url) {
        Bundle params = new Bundle();
        //params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  "http://a.app.qq.com/o/simple.jsp?pkgname=com.yzi.doutu");//这条分享消息被好友点击后的跳转URL
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,url);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,CommUtil.getInstance().getString( R.string.app_name));
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        // params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);//分享时打开分享到QZone的对话框
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        tencent.shareToQQ((Activity) context, params,qShareListener);
    }

    /**
     * 分享纯图片
     * @param url 需要分享的本地图片路径。
     */
    public void toQZoneShare(String url) {
        Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,url);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,CommUtil.getInstance().getString( R.string.app_name));
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
         params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);//分享时打开分享到QZone的对话框
        //params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        tencent.shareToQQ((Activity) context, params,qShareListener);
    }

    /**
     * 分享图文信息
     */
    public void onClickShare(String content) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_APP);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "来自"+android.os.Build.MODEL);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,content);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "https://user.qzone.qq.com/295658413/infocenter?ptsig=33nGSwkUxHH61fY2TCSfiO3*zvp3oIJ5zz4PBHLKx4s_");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, SharedUtils.getString("",context,"icon_img", CommUtil.ICON));
        //params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        //params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "来自未来的时光机");
        //params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        tencent.shareToQQ((Activity) context, params,qShareListener);
    }
}
