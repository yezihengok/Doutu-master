package com.yzi.doutu.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXEmojiObject;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXVideoObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;
import com.yzi.doutu.R;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.ImageUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 实现微信SDK分享功能的核心类
 * @author 
 *
 */
public class WechatShareManager {

    private static final int THUMB_SIZE = 150;

    public static final int WECHAT_SHARE_WAY_TEXT = 1;   //文字
    public static final int WECHAT_SHARE_WAY_PICTURE = 2; //图片
    public static final int WECHAT_SHARE_WAY_WEBPAGE = 3;  //链接
    public static final int WECHAT_SHARE_WAY_VIDEO = 4; //视频
    public static final int WECHAT_SHARE_TYPE_TALK = SendMessageToWX.Req.WXSceneSession;  //会话
    public static final int WECHAT_SHARE_TYPE_FRENDS = SendMessageToWX.Req.WXSceneTimeline; //朋友圈

    private static WechatShareManager mInstance;
    private ShareContent mShareContentText, mShareContentPicture, mShareContentWebpag, mShareContentVideo;
    ShareContent mSharePicPath;//本地图片路径分享对象
    private IWXAPI mWXApi;
    private Context mContext;

    private WechatShareManager(Context context){
        this.mContext = context;
        //初始化数据
        //初始化微信分享代码
        initWechatShare(context);
    }

    /**
     * 获取WeixinShareManager实例
     * 非线程安全，请在UI线程中操作
     * @return
     */
    public static WechatShareManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new WechatShareManager(context);
        }
        return mInstance;
    }

    private void initWechatShare(Context context){
        if (mWXApi == null) {
            mWXApi = WXAPIFactory.createWXAPI(context, CommUtil.WECHAT_APP_ID, true);
        }
        mWXApi.registerApp(CommUtil.WECHAT_APP_ID);
    }

    /**
     * 通过微信分享
     * @param shareContent 分享的方式（文本、图片、链接）
     * @param shareType 分享的类型（朋友圈，会话）
     */
    public void shareByWebchat(ShareContent shareContent, int shareType){
        if (!mWXApi.isWXAppInstalled()) {
            Toast.makeText(mContext, "请先安装微信", Toast.LENGTH_LONG).show();
            return;
        }
        switch (shareContent.getShareWay()) {
            case WECHAT_SHARE_WAY_TEXT:
                shareText(shareContent.getContent(), shareType);
                break;
            case WECHAT_SHARE_WAY_PICTURE:
                if(TextUtils.isEmpty(shareContent.getPicPath())){
                    sharePicture(shareContent, shareType);
                }else {
                    //使用图片路径方式分享图片
                    if(shareContent.getPicPath().endsWith("gif")||shareContent.getPicPath().endsWith("GIF")){
                        shareGifFile(shareContent, shareType);
                    }else{
                        sharePicFile(shareContent, shareType);
                    }

                }

                break;
            case WECHAT_SHARE_WAY_WEBPAGE:
                shareWebPage(shareContent, shareType);
                break;
            case WECHAT_SHARE_WAY_VIDEO:
                shareVideo(shareContent, shareType);
                break;
        }
    }

    private abstract class ShareContent {
        protected abstract int getShareWay();
        protected abstract String getContent();
        protected abstract String getTitle();
        protected abstract String getURL();
        protected abstract String getPicPath();//本地图片路径 yzh 2016-11-18 add
        protected abstract int getPictureResource();
    }

    /**
     * 设置分享文字的内容
     *
     */
    public class ShareContentText extends ShareContent {
        private String content;

        /**
         * 构造分享文字类
         * @param content 分享的文字内容
         */
        public ShareContentText(String content){
            this.content = content;
        }

        @Override
        protected int getShareWay() {
            return WECHAT_SHARE_WAY_TEXT;
        }

        @Override
        protected String getContent() {
            return content;
        }

        @Override
        protected String getTitle() {
            return null;
        }

        @Override
        protected String getURL() {
            return null;
        }

        @Override
        protected String getPicPath() {
            return null;
        }

        @Override
        protected int getPictureResource() {
            return -1;
        }
    }

    /*
     * 获取文本分享对象
     */
    public  ShareContent getShareContentText(String content) {
        if (mShareContentText == null) {
            mShareContentText = new ShareContentText(content);
        }
        return mShareContentText;
    }

    /**
     * 设置分享图片的内容
     * @author 
     *
     */
    public class ShareContentPicture extends ShareContent {
        private int pictureResource;//id drawable图片
        private String picPath;//本地图片路径
        public ShareContentPicture(int pictureResource){
            this.pictureResource = pictureResource;
        }
        public ShareContentPicture(String picPath){
            this.picPath = picPath;
        }

        @Override
        protected int getShareWay() {
            return WECHAT_SHARE_WAY_PICTURE;
        }

        @Override
        protected int getPictureResource() {
            return pictureResource;
        }

        @Override
        protected String getContent() {
            return null;
        }

        @Override
        protected String getTitle() {
            return null;
        }

        @Override
        protected String getURL() {
            return null;
        }

        @Override
        protected String getPicPath() {
            return picPath;
        }
    }

    /*
     * 获取图片分享对象(id方式)
     */
    public ShareContent getShareContentPicture(int pictureResource) {
        if (mShareContentPicture == null) {
            mShareContentPicture = new ShareContentPicture(pictureResource);
        }
        return mShareContentPicture;
    }

    /*
    * 获取图片分享对象（本地文件路径方式）
    */
    public ShareContent getShareContentPicture(String picpath) {
        return new ShareContentPicture(picpath);
    }


    /**
     * 设置分享链接的内容
     * @author 
     *
     */
    public class ShareContentWebpage extends ShareContent {
        private String title;
        private String content;
        private String url;
        private int pictureResource;
        public ShareContentWebpage(String title, String content, String url, int pictureResource){
            this.title = title;
            this.content = content;
            this.url = url;
            this.pictureResource = pictureResource;
        }

        @Override
        protected int getShareWay() {
            return WECHAT_SHARE_WAY_WEBPAGE;
        }

        @Override
        protected String getContent() {
            return content;
        }

        @Override
        protected String getTitle() {
            return title;
        }

        @Override
        protected String getURL() {
            return url;
        }

        @Override
        protected String getPicPath() {
            return null;
        }

        @Override
        protected int getPictureResource() {
            return pictureResource;
        }
    }

    /*
     * 获取网页分享对象
     */
    public ShareContent getShareContentWebpag(String title, String content, String url, int pictureResource) {
        if (mShareContentWebpag == null) {
            mShareContentWebpag = new ShareContentWebpage(title, content, url, pictureResource);
        }
        return (ShareContentWebpage) mShareContentWebpag;
    }

    /**
     * 设置分享视频的内容
     *
     */
    public class ShareContentVideo extends ShareContent {
        private String url;
        public ShareContentVideo(String url) {
            this.url = url;
        }

        @Override
        protected int getShareWay() {
            return WECHAT_SHARE_WAY_VIDEO;
        }

        @Override
        protected String getContent() {
            return null;
        }

        @Override
        protected String getTitle() {
            return null;
        }

        @Override
        protected String getURL() {
            return url;
        }

        @Override
        protected String getPicPath() {
            return null;
        }

        @Override
        protected int getPictureResource() {
            return -1;
        }
    }

    /*
     * 获取视频分享内容
     */
    public ShareContent getShareContentVideo(String url) {
        if (mShareContentVideo == null) {
            mShareContentVideo = new ShareContentVideo(url);
        }
        return (ShareContentVideo) mShareContentVideo;
    }

    /*
     * 分享文字
     */
    public void shareText(String text, int shareType) {
        //初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;
        //用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        //transaction字段用于唯一标识一个请求
        req.transaction = buildTransaction("textshare");
        req.message = msg;
        //发送的目标场景， 可以选择发送到会话 WXSceneSession 或者朋友圈 WXSceneTimeline。 默认发送到会话。
        req.scene = shareType;
        mWXApi.sendReq(req);
    }

    /*
     * 分享图片 图片路径方式（分享gif静态图片适用）--分享到微信直接显示动图无需点开
     */
    private void shareGifFile(ShareContent shareContent, int shareType) {
        String path=shareContent.getPicPath();
        File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(mContext, "图片不存在" + " path= " + path, Toast.LENGTH_LONG).show();
            return;
        }

        WXEmojiObject emoji = new WXEmojiObject();
        emoji.emojiPath = path;
        WXMediaMessage msg = new WXMediaMessage(emoji);

        msg.title = "Emoji Title";
        msg.description = "Emoji Description";

        //动图的静态略缩图
//        String pa= ImageUtils.FILE_DIY_PATH+"111.png";
//        msg.thumbData = WXUtil.readFromFile(pa, 0, (int) new File(pa).length());
        Bitmap bmp = BitmapFactory.decodeFile(path);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("emoji");
        req.message = msg;
        req.scene = shareType;
        mWXApi.sendReq(req);
    }
    /*
         * 分享图片 分享jpp\png静态图片适用）--分享默认显示缩略图点开显示原图
         */
    private void sharePicFile(ShareContent shareContent, int shareType) {
        String path=shareContent.getPicPath();
        File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(mContext, "图片不存在" + " path= " + path, Toast.LENGTH_LONG).show();
            return;
        }

        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap bmp = BitmapFactory.decodeFile(path);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = shareType;
        mWXApi.sendReq(req);

    }


    /*
     * 分享图片
     */
    private void sharePicture(ShareContent shareContent, int shareType) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), shareContent.getPictureResource());
        WXImageObject imgObj = new WXImageObject(bitmap);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBitmap =  Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        bitmap.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBitmap, true);  //设置缩略图

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("imgshareappdata");
        req.message = msg;
        req.scene = shareType;
        mWXApi.sendReq(req);
    }

    /**
     * 直接分享图片
     * @param picPath 本地图片文件路径
     * @param flag 0 微信好友 1微信朋友圈
     */
    public void sharePic(String picPath,int flag){
        ShareContentPicture scp = (ShareContentPicture)getShareContentPicture(picPath);
        if(flag==0){
            shareByWebchat(scp, WechatShareManager.WECHAT_SHARE_TYPE_TALK);
        }else if(flag==1){
            shareByWebchat(scp, WechatShareManager.WECHAT_SHARE_TYPE_FRENDS);
        }

    }

    /*
     * 分享链接
     */
    private void shareWebPage(ShareContent shareContent, int shareType) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareContent.getURL();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = shareContent.getTitle();
        msg.description = shareContent.getContent();

        Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), shareContent.getPictureResource());
        if(thumb == null) {
            Toast.makeText(mContext, "图片不能为空", Toast.LENGTH_SHORT).show();
        } else {
            msg.thumbData = Util.bmpToByteArray(thumb, true);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = shareType;
        mWXApi.sendReq(req);
    }

    /*
     * 分享视频
     */
    private void shareVideo(ShareContent shareContent, int shareType) {
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = shareContent.getURL();

        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = shareContent.getTitle();
        msg.description = shareContent.getContent();
        Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.collection_null);
//		BitmapFactory.decodeStream(new URL(video.videoUrl).openStream());
        /**
         * 测试过程中会出现这种情况，会有个别手机会出现调不起微信客户端的情况。造成这种情况的原因是微信对缩略图的大小、
         * title、description等参数的大小做了限制，所以有可能是大小超过了默认的范围。
         * 一般情况下缩略图超出比较常见。Title、description都是文本，一般不会超过。
         */
        Bitmap thumbBitmap =  Bitmap.createScaledBitmap(thumb, THUMB_SIZE, THUMB_SIZE, true);
        thumb.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBitmap, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("video");
        req.message = msg;
        req.scene =  shareType;
        mWXApi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
