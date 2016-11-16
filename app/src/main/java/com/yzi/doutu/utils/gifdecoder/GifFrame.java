package com.yzi.doutu.utils.gifdecoder;

import android.graphics.Bitmap;

/**
 * Created by yzh-t105 on 2016/11/16.
 */

public class GifFrame {
    public Bitmap image;
    public int delay;
    public GifFrame nextFrame = null;

    public GifFrame(Bitmap im, int del) {
        image = im;
        delay = del;
    }

}
