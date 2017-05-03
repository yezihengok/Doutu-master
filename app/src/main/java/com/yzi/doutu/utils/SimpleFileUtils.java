package com.yzi.doutu.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;

/**
 * @author yezi
 *         2014-12-18 17:42:15
 */
public class SimpleFileUtils {

    /**
     * 写数据到SD中的文件
     *
     * @param filePath  /my/
     * @param fileName  a.txt
     * @param write_str 要输出的 字符串
     * @throws IOException
     */
    public static void writeFile(String filePath, String fileName, String write_str) throws IOException {
        try {

            String pathStr = Environment.getExternalStorageDirectory().getPath() + filePath;
            Log.e("输出的文件路径----===", pathStr);
            File file = new File(pathStr);
            if (!file.exists()) {
                //检查存放的文件夹是否存在
                file.mkdir();
                //不存在的话 创建文件夹
            }
            //File file=new File(Environment.getExternalStorageDirectory(),"MyFiles/XMLL.txt");
            Log.e("", pathStr + fileName);
            FileOutputStream fout = new FileOutputStream(pathStr + fileName);
            byte[] bytes = write_str.getBytes("UTF-8");
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 追加文件：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true
     *
     * @param content
     */
    public static void writeAppend(String content) {
        String filePath = "/Dinpay/";
        String fileName = "Log.bin";
        String pathStr = Environment.getExternalStorageDirectory().getPath() + filePath;
        Log.e("writeAppend", pathStr + fileName);
        File file1 = new File(pathStr);
        if (!file1.exists()) {
            //检查存放的文件夹是否存在
            file1.mkdir();
            //不存在的话 创建文件夹
        }
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(pathStr + fileName, true), "UTF-8"));


            out.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 追加文件：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true
     *
     * @param filePath /new/
     * @param fileName my.txt
     * @param content
     */
    public static void writeAppend(String filePath, String fileName, String content) {
        String pathStr = Environment.getExternalStorageDirectory().getPath() + filePath;
        Log.e("writeAppend", pathStr + fileName);
        File file1 = new File(pathStr);
        if (!file1.exists()) {
            //检查存放的文件夹是否存在
            file1.mkdir();
            //不存在的话 创建文件夹
        }
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(pathStr + fileName, true), "UTF-8"));


            out.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * @param fileName /my/ac.txt
     * @return
     */
    public static String read(String fileName) {
        String pathStr = Environment.getExternalStorageDirectory().getPath();
        Log.e("pathStr", pathStr + fileName);
        try {
            // 打开文件输入流
            //FileInputStream fis = openFileInput(pathStr+"/Hotel_json.txt");
            File file = new File(pathStr + fileName);
            FileInputStream fis = new FileInputStream(file);
            byte[] buff = new byte[1024];
            int hasRead = 0;
            StringBuilder sb = new StringBuilder("");
            // 读取文件内容
            while ((hasRead = fis.read(buff)) > 0) {
                sb.append(new String(buff, 0, hasRead));
            }
            // 关闭文件输入流
            fis.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 文件超過指定的 大小（MB）則刪除該文件
     *
     * @param filePath /my/aaa.txt
     * @param size     MB
     */
    public static void delFile(final String filePath, int size, final CommInterface.DoListener listener) {
        //String pathStr = Environment.getExternalStorageDirectory().getPath();
        if (getFileOrFilesSize(filePath) >= 1048576 * size) {
            //String path = context.getFilesDir().getPath().toString() +filePath;
            Log.d("", filePath);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
                    deleteFile(new File(filePath), listener);
//                }
//            }).start();

        }
    }


    public static void deleteFile(File file, CommInterface.DoListener listener) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); //删除
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i], listener); // 把每个文件 用这个方法进行迭代
                }
            }
            if (listener != null) {
                listener.finish(true);
            }

            Log.e("", "已刪除");
        } else {
            Log.e("", "文件不存在！" + "\n");
        }
    }


    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return
     */
    public static long getFileOrFilesSize(String filePath) {
        File file;
        long blockSize = 0;
        try {
            file = new File(filePath);
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return blockSize;
    }


    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }


}
