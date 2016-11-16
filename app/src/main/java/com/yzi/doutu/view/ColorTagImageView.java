package com.yzi.doutu.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;


public class ColorTagImageView extends ImageView {
    private float mEachItemHeght;

    private int[] mColors = new int[]{
            Color.parseColor("#eb6564"),
            Color.parseColor("#eb65c8"),
            Color.parseColor("#b264eb"),
            Color.parseColor("#47abff"),
            Color.parseColor("#64ebda"),
            Color.parseColor("#ecd865"),
            Color.parseColor("#eaae65"),
            Color.parseColor("#ec7e65"),
            Color.parseColor("#ffffff"),
            Color.parseColor("#000000"),};

    public OnColorTagChanges getListener() {
        return listener;
    }

    public void setListener(OnColorTagChanges listener) {
        this.listener = listener;
    }

    private OnColorTagChanges listener;

    public ColorTagImageView(Context context) {
        super(context);
    }

    public ColorTagImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorTagImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorTagImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mEachItemHeght = getWidth() / 10;
        if (event.getX() >= 0 && event.getX() <= getWidth()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (listener != null) {
                        listener.onColorChange(mColors[(int) (event.getX() / mEachItemHeght)]);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (listener != null) {
                        if(mColors.length>(int) (event.getX() / mEachItemHeght)){
                            listener.onColorChange(mColors[(int) (event.getX() / mEachItemHeght)]);
                        }

                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
        }
        return true;
    }

    public interface OnColorTagChanges {
        void onColorChange(int color);
    }
}
