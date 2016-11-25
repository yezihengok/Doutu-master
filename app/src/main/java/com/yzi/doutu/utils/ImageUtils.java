package com.yzi.doutu.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.gifencoder.AnimatedGifEncoder;
import com.yzi.doutu.R;
import com.yzi.doutu.bean.DataBean;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.R.attr.width;

public class ImageUtils {
    private static final int TYPE_FILE_IMAGE = 1;
    private static final int TYPE_FILE_VEDIO = 2;
    //APP
    public static final String ROOT_PATH =  Environment.getExternalStorageDirectory().getPath() + "/"
            +CommUtil.getString(R.string.app_name)+"/";
    //图片下载的路径
    public static final String DOWN_PATH = ROOT_PATH+"Down/";

    //分解图片的临时图片路径
   public static String FILE_ROOT_PATH = ROOT_PATH+"Temp/";

    //合成的保存路径
    public static String FILE_DIY_PATH = ROOT_PATH+"DIY/";
   private static String TAG="ImageUtils";

    /**
     * 更具图片路径返回压缩Bitmap的大小
     *
     * @param imagePath     图片文件路径
     * @param requestWidth  压缩到想要的宽度
     * @param requestHeight 压缩到想要的高度
     * @return
     */
    public static Bitmap decodeBitmapFromFile(String imagePath, int requestWidth, int requestHeight) {
        if (!TextUtils.isEmpty(imagePath)) {
            Log.i(TAG, "requestWidth: " + requestWidth);
            Log.i(TAG, "requestHeight: " + requestHeight);
            if (requestWidth <= 0 || requestHeight <= 0) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                return bitmap;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;//不加载图片到内存，仅获得图片宽高
            BitmapFactory.decodeFile(imagePath, options);
            Log.i(TAG, "original height: " + options.outHeight);
            Log.i(TAG, "original width: " + options.outWidth);
            if (options.outHeight == -1 || options.outWidth == -1) {
                try {
                    ExifInterface exifInterface = new ExifInterface(imagePath);
                    int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的高度
                    int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);//获取图片的宽度
                    Log.i(TAG, "exif height: " + height);
                    Log.i(TAG, "exif width: " + width);
                    options.outWidth = width;
                    options.outHeight = height;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            options.inSampleSize = calculateInSampleSize(options, requestWidth, requestHeight); //计算获取新的采样率
            Log.i(TAG, "inSampleSize: " + options.inSampleSize);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(imagePath, options);

        } else {
            return null;
        }
    }


    /**
     *
     * @param options
     * @param reqWidth 期望的高
     * * @param reqHeight 期望的宽
     * @return
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
    public static Bitmap createScaleBitmap(Bitmap src, int dstWidth,
                                           int dstHeight) {
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) { // 如果没有缩放，那么不回收
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }

    // 从Resources中加载压缩图片
    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options); // 读取图片长款
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight); // 计算inSampleSize
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeResource(res, resId, options); // 载入一个稍大的缩略图
        return createScaleBitmap(src, reqWidth, reqHeight); // 进一步得到目标大小的缩略图
    }

    // 从sd卡上加载压缩图片
    public static Bitmap decodeSampledBitmapFromFSDCard(String pathName,
                                                        int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
        return createScaleBitmap(src, reqWidth, reqHeight);
    }

    // 从sd卡上加载压缩图片
    public static Bitmap decodeBitmapFromFSDCard(String pathName) {
        return BitmapFactory.decodeFile(pathName);
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /***
     * 将bitmap保存在SD jpeg格式
     *
     * @param bitmap   图片bitmap
     * @param filePath 要保存图片路径
     * @param quality  压缩质量值
     */
    public static void saveImage(Bitmap bitmap, String filePath, int quality) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(filePath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            // 如果图片还没有回收，强制回收
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
                System.gc();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 旋转图片
     *
     * @param angle  旋转角度
     * @param bitmap 要处理的Bitmap
     * @return 处理后的Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (resizedBitmap != bitmap && bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        return resizedBitmap;
    }

    /**
     * 生成输出文件
     *
     * @param fileType
     * @param directoryPath
     * @param format
     * @return
     */
    public static File getOutFile(int fileType, String directoryPath, String format) {
        File mediaStorageDir = null;
        //判断SDCard的状态
        String storageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_REMOVED.equals(storageState)) {
            //SDCard不存在 则使用手机内存进行存储
            mediaStorageDir = new File(Environment.getDataDirectory(), directoryPath);
        } else {
            //SDCard存在 创建目录
            mediaStorageDir = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    , directoryPath);
        }

        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }

        //生成文件
        File file = null;
        if (TextUtils.isEmpty(format) || format.equals("")) {
            file = new File(getFilePath(mediaStorageDir, fileType));
        } else {
            file = new File(getFilePath(mediaStorageDir, fileType, format));
        }
        return file;
    }

    /**
     * 生成输出文件路径
     *
     * @param mediaStorageDir
     * @param fileType
     * @return
     */
    public static String getFilePath(File mediaStorageDir, int fileType) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String filePath = mediaStorageDir.getPath() + File.separator;
        if (fileType == TYPE_FILE_IMAGE) {
            filePath += ("IMG_" + timeStamp + ".jpg");
        } else if (fileType == TYPE_FILE_VEDIO) {
            filePath += ("VIDEO_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        return filePath;
    }

    /**
     * 生成输出文件路径
     *
     * @param mediaStorageDir 存储的文件
     * @param fileType        类型
     * @param format          生成的文件名的格式
     * @return
     */
    public static String getFilePath(File mediaStorageDir, int fileType, String format) {
        String timeStamp = new SimpleDateFormat(format)
                .format(new Date());
        String filePath = mediaStorageDir.getPath() + File.separator;
        if (fileType == TYPE_FILE_IMAGE) {
            filePath += ("IMG_" + timeStamp + ".jpg");
        } else if (fileType == TYPE_FILE_VEDIO) {
            filePath += ("VIDEO_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        return filePath;
    }

    /**
     * 将一个View转化成一个Bitmap
     * @param v
     * @param scale 根據寬高縮放級別
     * @return
     */
    public static Bitmap createViewBitmap(View v,float scale) {
         int width=  (int)(v.getWidth()*scale);
        int height= (int)(v.getHeight()*scale);
        Bitmap bitmap = Bitmap.createBitmap(width,height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.scale(scale,scale);
        v.draw(canvas);
        return bitmap;
    }

    /**
     * 将一个View转化成一个Bitmap
     * @param v
     * @param width 指定宽度
     * @param height 指定高度
     * @return
     */
    public static Bitmap createViewBitmap(View v,int width,int height) {
        float scaleW=(float) width/(float) v.getWidth();
        float scaleH=(float)height/(float) v.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width,height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.scale(scaleW,scaleH);
        v.draw(canvas);
        return bitmap;
    }


    public static Bitmap getimage(String srcPath, float hh, float ww) {
        if (hh <= 0 || ww <= 0) {
            hh = 800;
            ww = 480;
        }
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap, 100);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 返回一张压缩后的图片
     *
     * @param image
     * @param size
     * @return
     */
    public static Bitmap compressImage(Bitmap image, int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > size) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 通过文件名返回一个压缩的图片
     *
     * @param filePath
     * @return
     */
    public static Bitmap compressBitmap(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int height = options.outHeight;
        int width = options.outWidth;

        int inSampleSize = 1;
        int reqHeight = 1280;
        int reqWidth = 960;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 对图片进行缩放
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap resizeImage(Bitmap bitmap, float w, float h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();


        float scaleWidth =w / width;
        float scaleHeight =h / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        return Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
    }

    /**
     * 根据文件名压缩文件再保存
     *
     * @param filePath
     * @param newFilePath
     */
    public static void saveCompressFile(String filePath, String newFilePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int height = options.outHeight;
        int width = options.outWidth;

        int inSampleSize = 1;
        int reqHeight = 1280;
        int reqWidth = 960;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(new File(newFilePath)));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存Bitmap到指定文件
     *
     * @param tempBitmap
     * @param fileName
     */
    public static String saveBitmapToFile(Bitmap tempBitmap, String fileName) {
        if (TextUtils.isEmpty(getFilesPath(FILE_ROOT_PATH))) {
            return null;
        }
        String fileNames = getFilesPath(FILE_ROOT_PATH) + fileName;
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(new File(fileNames)));
           // compressBitmap(tempBitmap).compress(Bitmap.CompressFormat.JPEG, 100, bos);
            tempBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileNames;
    }

    /**
     * 保存Bitmap到指定路径
     *
     * @param tempBitmap
     * @param filePath
     */
    public static void saveBitmapToPath(Bitmap tempBitmap, String filePath) {
        if (TextUtils.isEmpty(getFilesPath(filePath))) {
            return ;
        }
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
//            compressBitmap(tempBitmap).compress(Bitmap.CompressFormat.JPEG, 100, bos);
            tempBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String saveBitmapToFiles(Bitmap tempBitmap,final DataBean dataBean,int height,int width) {
        if (TextUtils.isEmpty(getFilesPath(FILE_DIY_PATH))) {
            return null;
        }

        String now=CommUtil.getDate();
        final String fileName =now+ ".jpg";
        String fileNames = FILE_DIY_PATH + fileName;
        BufferedOutputStream bos;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(new File(fileNames)));
            compressBitmap(tempBitmap,height,width).compress(Bitmap.CompressFormat.JPEG, 100, bos);
            //tempBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();

            //保证缓存图片的唯一性删掉旧图,替换新的文件名
            if(!TextUtils.isEmpty(dataBean.getFileName())){
                SimpleFileUtils.deleteFile(new File(FILE_DIY_PATH+dataBean.getFileName()),null);
            }
            dataBean.setFileName(fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileNames;
    }

    public static String getFilesPath(String path) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return "";
        }
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();// 创建文件夹
        return path;
    }


    /**
     * 对Bitmap进行压缩
     *
     * @param image
     * @return
     */
    public static Bitmap compressBitmap(Bitmap image,int height,int width) {
        //防止对本数据进行就该
        Bitmap tempBitmap = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        tempBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(isBm, null, options);

        int size=1;
        //重新设置压缩比
        size = calculateInSampleSize(options, height, width);// TODO:  根据自己的需求 动态设置
        options.inSampleSize =size;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;//降低图片从ARGB888到RGB565
        isBm = new ByteArrayInputStream(baos.toByteArray());
        return BitmapFactory.decodeStream(isBm, null, options);
    }




    /**
     * 对Bitmap宽高缩放
     * @param src
     * @param w
     * @param h
     * @return
     */
    public static Bitmap scaleWithWH(Bitmap src, double w, double h) {
        if (w == 0 || h == 0 || src == null) {
            return src;
        } else {
            // 记录src的宽高
            int width = src.getWidth();
            int height = src.getHeight();
            // 创建一个matrix容器
            Matrix matrix = new Matrix();
            // 计算缩放比例
            float scaleWidth = (float) (w / width);
            float scaleHeight = (float) (h / height);
            // 开始缩放
            matrix.postScale(scaleWidth, scaleHeight);
            // 创建缩放后的图片
            return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
        }
    }

    public Bitmap drawTextToBitmap(Context gContext,
                                   int gResId,
                                   String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap =
                BitmapFactory.decodeResource(resources, gResId);

        bitmap = scaleWithWH(bitmap, 300*scale, 300*scale);

        android.graphics.Bitmap.Config bitmapConfig =
                bitmap.getConfig();



        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.RED);
        paint.setTextSize((int) (18 * scale));
        paint.setDither(true); //获取跟清晰的图像采样
        paint.setFilterBitmap(true);//过滤一些
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = 30;
        int y = 30;
        canvas.drawText(gText, x * scale, y * scale, paint);
        return bitmap;
    }



    /**
     * 多张图片合成GIF
     * @param dataBean
     * @param paths
     * @param fps
     * @return
     * @throws IOException
     */
    public static void createGif(final DataBean dataBean,final  List<String> paths
            ,final  int fps,final CommInterface.setListener setListener) {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                AnimatedGifEncoder localAnimatedGifEncoder = new AnimatedGifEncoder();
                localAnimatedGifEncoder.start(baos);//start
                localAnimatedGifEncoder.setRepeat(0);//设置生成gif的开始播放时间。0为立即开始播放
                if(paths.size()>12){
                    localAnimatedGifEncoder.setDelay(fps-5);
                }else{
                    localAnimatedGifEncoder.setDelay(fps);
                }

                if (paths.size() > 0) {
                    for (int i = 0; i < paths.size(); i++) {
                        Bitmap bitmap = BitmapFactory.decodeFile(paths.get(i));
                        //可以缩放
//                Bitmap resizeBm = ImageUtils.resizeImage(bitmap, 150, 150);
//                localAnimatedGifEncoder.addFrame(resizeBm);
                        localAnimatedGifEncoder.addFrame(bitmap);
                    }
                }
                localAnimatedGifEncoder.finish();//finish

                File file = new File(FILE_DIY_PATH);
                if (!file.exists()) file.mkdir();

                String now=CommUtil.getDate();
                String fileName=now + ".gif";
                final String path =FILE_DIY_PATH + fileName;

                //保证缓存图片的唯一性删掉旧图
                if(!TextUtils.isEmpty(dataBean.getFileName())){
                    SimpleFileUtils.deleteFile(new File(getFilesPath(FILE_DIY_PATH)+dataBean.getFileName()),null);
                }
                dataBean.setFileName(fileName);//替换新的文件名
                try {
                    FileOutputStream fos = new FileOutputStream(path);
                    baos.writeTo(fos);
                    baos.flush();
                    fos.flush();
                    baos.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                HandlerUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setListener.onResult(path);
                    }
                });

            }
        };
        new Thread(runnable).start();

    }

}
