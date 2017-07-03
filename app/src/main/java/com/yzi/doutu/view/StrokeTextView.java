package com.yzi.doutu.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by yzh-t105 on 2017/6/30.
 */

public class StrokeTextView extends android.support.v7.widget.AppCompatTextView {

    TextPaint m_TextPaint;
    int mInnerColor;
    int mOuterColor;

    public StrokeTextView(Context context, int outerColor, int innnerColor) {
        super(context);
        m_TextPaint = this.getPaint();
        this.mInnerColor = innnerColor;
        this.mOuterColor = outerColor;

        // TODO Auto-generated constructor stub
    }



    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_TextPaint = this.getPaint();
        //获取自定义的XML属性名称
  //      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StrokeTextView);
//        //获取对应的属性值
//        this.mInnerColor = a.getColor(R.styleable.StrokeTextView_innnerColor,0xffffff);
//        this.mOuterColor = a.getColor(R.styleable.StrokeTextView_outerColor,0xffffff);

    }

    public StrokeTextView(Context context, AttributeSet attrs, int defStyle,int outerColor,int innnerColor) {
        super(context, attrs, defStyle);
        m_TextPaint = this.getPaint();
        this.mInnerColor = innnerColor;
        this.mOuterColor = outerColor;
        // TODO Auto-generated constructor stub
    }

    private boolean m_bDrawSideLine = true; // 默认采用描边

    @Override
    protected void onDraw(Canvas canvas) {
        if (m_bDrawSideLine) {
            // 描外层
            // super.setTextColor(Color.BLUE); // 不能直接这么设，setTextColor后刷新执行onDraw 如此会导致递归
            setTextColorUseReflection(mOuterColor);
           // m_TextPaint.setColor(mOuterColor);
            m_TextPaint.setStrokeWidth(7); // 描边宽度
            m_TextPaint.setStyle(Paint.Style.FILL_AND_STROKE); // 描边种类
            m_TextPaint.setFakeBoldText(true); // 外层text采用粗体
            m_TextPaint.setShadowLayer(1, 0, 0, 0); // 字体的阴影效果，可以忽略
            super.onDraw(canvas);

            // 描内层，恢复原先的画笔

            // super.setTextColor(Color.BLUE); // 不能直接这么设，如此会导致递归
            setTextColorUseReflection(mInnerColor);
           // m_TextPaint.setColor(mInnerColor);
           // m_TextPaint.setStrokeWidth(0);
            m_TextPaint.setStyle(Paint.Style.FILL);
            m_TextPaint.setFakeBoldText(false);
            m_TextPaint.setShadowLayer(0, 0, 0, 0);

        }
        super.onDraw(canvas);
    }


    /**
     *  使用反射的方法进行字体颜色的设置
     * @param color
     */
    public void setTextColorUseReflection(int color) {
        Field textColorField;
        try {
            textColorField = TextView.class.getDeclaredField("mCurTextColor");
            textColorField.setAccessible(true);
            textColorField.set(this, color);
            textColorField.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        m_TextPaint.setColor(color);
    }

}

