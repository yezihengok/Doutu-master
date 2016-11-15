package com.yzi.doutu.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.PopupWindow;

import com.yzi.doutu.R;

/**
 * Created by yzh on 2016/8/3.
 */
public class PopupWindowHelper {

    private View popupView;
    private Context context;
    private PopupWindow mPopupWindow;

    public PopupWindow getmPopupWindow() {
        return mPopupWindow;
    }

    public static final int TYPE_WRAP_CONTENT = 0, TYPE_MATCH_PARENT = 1;

    CompleteListener completeListener;



    public void setCompleteListener(CompleteListener completeListener) {
        this.completeListener = completeListener;
    }

    public PopupWindowHelper(View view, Context context, int type) {
        popupView = view;
        this.context=context;
        initPopupWindow(type);
    }
    /**
     * view的下方弹出
     * @param anchor
     */
    public void showAsDropDown(View anchor) {
        mPopupWindow.showAsDropDown(anchor);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff) {
        mPopupWindow.showAsDropDown(anchor, xoff, yoff);
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        mPopupWindow.showAtLocation(parent, gravity, x, y);
    }

    public void dismiss() {
        if (mPopupWindow!=null &&mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }
    }

    public boolean isShowing(){
        return mPopupWindow.isShowing();
    }
    /**
     * view的上方弹出
     * @param anchor
     */
    public void showAsPopUp(View anchor) {
        showAsPopUp(anchor, 0, 0);
    }

    public void showAsPopUp(View anchor, int xoff, int yoff) {
        mPopupWindow.setAnimationStyle(R.style.popup_animation);
        popupView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int height = popupView.getMeasuredHeight();
        int[] location = new int[2];
        anchor.getLocationInWindow(location);
        mPopupWindow.showAtLocation(anchor, Gravity.LEFT | Gravity.TOP, location[0] + xoff, location[1] - height + yoff);
    }

    /**
     * 底部弹出
     */
    public void showFromBottom(final Context context) {
        CommUtil.backgroundAlpha(((Activity) context),0.4f);
        mPopupWindow.setAnimationStyle(R.style.popup_animation);
        mPopupWindow.showAtLocation(popupView, Gravity.LEFT | Gravity.BOTTOM, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                CommUtil.backgroundAlpha(((Activity) context),1f);
            }
        });
    }

    public void showFromTop( ) {
        mPopupWindow.setAnimationStyle(R.style.popup_animation);
        mPopupWindow.showAtLocation(popupView, Gravity.LEFT | Gravity.TOP, 0,0);
        //mPopupWindow.showAtLocation(popupView, Gravity.LEFT | Gravity.TOP, 0, getStatusBarHeight());
    }



    /**
     * 弹出顶部通知 pop
     * @param blewView 从该View的下方弹出(可以为null)
     */
    public void showFromTops(final View blewView, final long duration ) {
        // mPopupWindow.setAnimationStyle(R.style.AnimationFromTop);
        if (mPopupWindow!=null &&mPopupWindow.isShowing()){
            animator1.cancel();
            animator2.cancel();
            mPopupWindow.dismiss();
        }

        if(popupView.getHeight()==0){
            popupView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    Log.v("", "view高度="+popupView.getHeight());
                    popupView.getViewTreeObserver().removeOnPreDrawListener(this);
                    // mPopupWindow.showAtLocation(v, Gravity.LEFT | Gravity.TOP, 0, getStatusBarHeight());
                    setY(mPopupWindow.getContentView(),popupView.getHeight(), duration);
                    return false;
                }
            });
        }else{
            setY(mPopupWindow.getContentView(),popupView.getHeight(), duration);
        }

        if(blewView!=null){
            mPopupWindow.showAsDropDown(blewView);
        }else{
            mPopupWindow.showAtLocation(popupView, Gravity.LEFT | Gravity.TOP, 0,getStatusBarHeight());
        }


    }

    /**
     *
     * @param v
     */
    public void showLeft(final View v){
        final int[] location = new int[2];
        v.getLocationInWindow(location);
        Log.d("","location[0]--"+location[0]);

        showLeftOrRightAnim(popupView,0);
        mPopupWindow.showAtLocation(popupView, Gravity.NO_GRAVITY,location[0]-popupView.getWidth(),location[1]);

    }

    public void showRight(final View v){

        final int[] location = new int[2];
        v.getLocationInWindow(location);
        Log.d("","location[0]--"+location[0]);
        int width=v.getWidth();//View的宽度
        Log.d("","width--"+width);
        mPopupWindow.showAtLocation(popupView, Gravity.NO_GRAVITY,location[0]+width,location[1]);
        showLeftOrRightAnim(popupView,1);
    }

    /**
     * touch outside dismiss the popupwindow, default is ture
     * @param isCancelable
     */
    public void setCancelable(boolean isCancelable) {
        if (isCancelable) {
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);
        }else {
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setFocusable(false);
        }
    }

    public void initPopupWindow(int type) {
        if (type == TYPE_WRAP_CONTENT) {
            mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }else if (type == TYPE_MATCH_PARENT) {
            mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(true);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId =   context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    ObjectAnimator animator2;
    ObjectAnimator animator1;
    /**
     * 通知动画
     * @param view
     * @param f  需要移动的距离
     * @param lon  多少毫秒后执行第二个动画
     */
    public  void setY(final View view, final float fo, long lon){

        //boolean isRunning=false;//控制动画是否运行中
        Log.e("viewHeight==", fo+"");
        final long duration=600;
        animator1= ObjectAnimator.ofFloat(view, "ko",-fo,0f);
        animator1.setDuration(duration).start();
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                float f=(Float) arg0.getAnimatedValue();
                //Log.d("","~~~~~~~~~"+f);
                view.setY(f);	//通过设置view的Y轴 实现移动
            }
        });


        animator2= ObjectAnimator.ofFloat(view, "ko", 0f,-fo);
        animator2.setDuration(duration);
        animator2.setStartDelay(lon);
        animator2.start();
        animator2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) {completeListener.complete();}
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                float f=(Float) arg0.getAnimatedValue();
                view.setY(f);
               // Log.d("","="+f);
            }
        });

    }


    /**
     *
     * @param v
     * @param type 0左边 1右边
     */
    public void showLeftOrRightAnim(View v, int type){
        if(type==0){
            v.setPivotX(v.getMeasuredWidth());
        }else{
            v.setPivotX(0);
        }
        v.setPivotY(v.getMeasuredHeight()/2);
        PropertyValuesHolder propertyValues1= PropertyValuesHolder.ofFloat("scaleX", 0f,1f);
        PropertyValuesHolder propertyValues2= PropertyValuesHolder.ofFloat("scaleY", 0f,1f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(v, propertyValues1
                ,propertyValues2);
        animator.setDuration(500);
        animator.start();
    }

    interface CompleteListener{
        void complete();
    }
}
