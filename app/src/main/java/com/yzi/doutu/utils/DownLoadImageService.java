package com.yzi.doutu.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.yzi.doutu.R;
import com.yzi.doutu.bean.DataBean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.yzi.doutu.utils.ImageUtils.DOWN_PATH;
import static com.yzi.doutu.utils.ImageUtils.FILE_ROOT_PATH;
import static com.yzi.doutu.utils.ImageUtils.getFilesPath;

/**
 * 图片下载
 */
public class DownLoadImageService implements Runnable {
    private String url;
    private Context context;
    private CommInterface.ImageDownLoadCallBack callBack;
    private DataBean dataBean;
    public DownLoadImageService(Context context,DataBean dataBean, CommInterface.ImageDownLoadCallBack callBack) {
        this.callBack = callBack;
        this.context = context;
        this.dataBean=dataBean;
        url=dataBean.getGifPath();
    }

    @Override
    public void run() {

        File file;
        // Bitmap bitmap = null;
        try {
            //直接读取Glide缓存，得到File对象
            file = Glide.with(context)
                    .load(url)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
//            bitmap = Glide.with(context)
//                    .load(url)
//                    .asBitmap()
//                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .get();
            if (file != null) {
                String pathStr;
                if("DIY".equals(dataBean.getFormWhere())){
                    pathStr=ImageUtils.getFilesPath(FILE_ROOT_PATH);
                }else{
                    pathStr=ImageUtils.getFilesPath(DOWN_PATH);
                }
                String name;
                if (url.endsWith("gif") || url.endsWith("GIF")) {
                    name =dataBean.getId()+ ".gif";
                } else {
                    name =dataBean.getId()+ ".jpg";
                }
                cppyFile(file, pathStr, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }


    }


    /**
     * cppyFile
     *
     * @param fileOld     原文件File
     * @param pathNew     新文件路径  /storage/emulated/0/
     * @param newFileName 新文件名称 /a.jpg
     */
//    public void cppyFile(File fileOld,String pathNew,String newFileName){
//        //File fileOld = new File(fileOld);
//        File fileNew = new File(pathNew);
//        if(!fileNew.exists()){
//            fileNew.mkdir();
//        }
//        if(fileOld.exists()) {
//            try {
//                FileInputStream fis = new FileInputStream(fileOld);
//                FileOutputStream fos = new FileOutputStream(fileNew+newFileName);
//                int read = 0;
//                while ((read = fis.read()) != -1) {
//                    fos.write(read);
//                    fos.flush();
//                }
//                fos.close();
//                fis.close();
//                callBack.onDownLoadSuccess(fileNew+newFileName);
//                return;
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        callBack.onDownLoadFailed();
//    }

    /**
     * cppyFile 将图片复制保存到指定文件夹
     *
     * @param fileOld     原文件File
     * @param pathNew     新文件路径  /storage/emulated/0/
     * @param newFileName 新文件名称 a.jpg
     */
    public void cppyFile(File fileOld, final String pathNew,final String newFileName) {
        //File fileOld = new File(fileOld);
        File files = new File(pathNew);
        if (!files.exists()) {
            files.mkdir();
        }
        if (fileOld.exists()) {
            try {
                FileInputStream in = new FileInputStream(fileOld);
                FileOutputStream out = new FileOutputStream(pathNew+newFileName);
                //使用BufferedInputStream读资源比FileInputStream读取资源的效率高
                BufferedInputStream bufferedIn = new BufferedInputStream(in);
                BufferedOutputStream bufferedOut = new BufferedOutputStream(out);
                byte[] data = new byte[1];
                while (bufferedIn.read(data) != -1) {
                    bufferedOut.write(data);
                }
                bufferedOut.flush();
                bufferedIn.close();
                bufferedOut.close();

                HandlerUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onDownLoadSuccess(pathNew+newFileName);
                    }
                });

                return;
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                error();
            } catch (IOException e) {
                e.printStackTrace();
                error();
            }
        }else{
            error();
        }

    }


    public void error(){
        HandlerUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callBack.onDownLoadFailed();
            }
        });
    }
}

