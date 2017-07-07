package com.yzi.doutu.interfaces;

import android.view.View;

/**
 * Created by yzh-t105 on 2016/9/21.
 */
public class CommInterface {



    public  interface setFinishListener{
        void onFinish();
    }
    public  interface setClickListener{
        void onResult();
    }

    public  interface setListener{
        void onResult(String picpath);
    }

    public interface ImageDownLoadCallBack {
        void onDownLoadSuccess(String filePath);
        //void onDownLoadSuccess(Bitmap bitmap);

        void onDownLoadFailed();
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public interface SearchListener {
        void onClick(String keyWord);

    }

    public interface DoListener {
        void finish(boolean isOk);

    }
}
