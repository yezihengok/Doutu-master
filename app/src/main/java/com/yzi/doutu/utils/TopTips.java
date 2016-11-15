package com.yzi.doutu.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yzi.doutu.R;

/**
 * 顶部提示类
 * Created by yzh-t105 on 2016/8/22.
 */
public class TopTips {
    private View top_pop;
    private TextView arert_info;
    private LinearLayout bg_layout;
    private PopupWindowHelper popHelper;
    private Context context;

    public TopTips(Context context) {
        this.context = context;
        initPop();
    }


    public TopTips(Context context, int bgColor, int height) {
        this.context = context;
        initPop();
        if(bgColor>0)
        bg_layout.setBackgroundColor(bgColor);
        if(height>0)
        arert_info.getLayoutParams().height=dip2px(context,height);
    }

    private void initPop() {
        //if(top_pop==null){
        top_pop= LayoutInflater.from(context).inflate(R.layout.top_pop, null);
        bg_layout= (LinearLayout) top_pop.findViewById(R.id.bg_layout);
        arert_info= (TextView) top_pop.findViewById(R.id.arert_info);
        popHelper = new PopupWindowHelper(top_pop,context,PopupWindowHelper.TYPE_MATCH_PARENT);
        popHelper.setCancelable(false);
        //}
    }

    /**
     * 从顶部提示的PopupWindow
     * @param v 从该View的下方弹出
     * @param info 提示信息
     * @param duration 多久后消失
     */
    public void show(final View v, final String info, final long duration) {
        arert_info.setText(info);
        popHelper.showFromTops(v,duration);
        popHelper.setCompleteListener(new PopupWindowHelper.CompleteListener() {
            @Override
            public void complete() {
                popHelper.dismiss();
            }
        });
    }

    public  int dip2px(Context context, float dpValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
