package com.yzi.doutu.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzi.doutu.R;
import com.yzi.doutu.utils.CommUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * 这是个自定义布局View
 * Created by cretin on 15/12/17.
 */
public class MyRelativeLayout extends RelativeLayout {
    private Context context;
    private StrokeTextView textView;
    private TextViewParams tvParams;

    public StrokeTextView getTextView() {
        return textView;
    }


    private boolean mflag = false;
    private boolean onefinger;
    private boolean tvOneFinger;

    //记录是否为TextView上的单击事件
    private boolean isClick = true;

    public   int DEFAULT_TEXTSIZE = 30;
    Typeface typeface;
    //左边点的偏移量
    float tv_width;
    float tv_height;
    float mTv_width;
    float mTv_height;
    float tv_widths;
    float tv_heights;
    float mTv_widths;
    float mTv_heights;

    //用于保存创建的TextView
    private List<StrokeTextView> list;
    private List<TextViewParams> listTvParams;
    private List<Double> listDistance;

    private float oldDist = 0;
    private float textSize = 0;
    private int num = 0;

    private int width;
    private int height;
    private float startX;
    private float startY;

    private static final int INVALID_POINTER_ID = -1;
    private float fX, fY, sX, sY;
    private float mfX, mfY, msX, msY;
    private int ptrID1, ptrID2;
    private int mptrID1, mptrID2;
    private float mAngle;
    private float scale;
    private MotionEvent mEvent;

    //记录第一个手指下落时的位置
    private float firstX;
    private float firstY;

    private float defaultAngle;

    //记录当前点击坐标
    private float currentX;
    private float currentY;

    //记录当前设备的缩放倍数
    private double scaleTimes = 1;

    private int color= Color.WHITE;//文字颜色
    private String message="新增文字";//文字

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setTouchCallBack(MyRelativeTouchCallBack touchCallBack) {
        this.touchCallBack = touchCallBack;
    }


    //接口
    private MyRelativeTouchCallBack  touchCallBack;

    /**
     * 处理View上的单击事件 用以添加TextView
     */

    public MyRelativeLayout(Context context) {
        this(context, null, 0);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRelativeLayout(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        testScaleTimes();

        init();
    }

    //计算缩放倍数
    private void testScaleTimes() {
        TextView tv = new TextView(context);
        tv.setTextSize(1);
        scaleTimes = tv.getTextSize();
    }

    @TargetApi( Build.VERSION_CODES.LOLLIPOP )
    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mEvent = event;
        if ( textSize == 0 && textView != null ) {
            textSize = textView.getTextSize();
        }
        switch ( event.getAction() & MotionEvent.ACTION_MASK ) {
            case MotionEvent.ACTION_DOWN:
                Log.d("HHHH", "ACTION_DOWN");
                //此时有一个手指头落点
                onefinger = true;

                //给第一个手指落点记录落点的位置
                firstX = event.getX();
                firstY = event.getY();

                currentX = event.getX();
                currentY = event.getY();

                ptrID1 = event.getPointerId(event.getActionIndex());
                if ( textView != null ) {
                    //计算当前textView的位置和大小
                    width = textView.getWidth();
                    height = textView.getHeight();
                    startX = textView.getX();
                    startY = textView.getY();

                    if ( event.getX() <= (startX + width) && event.getX() >= startX && event.getY() <= (startY + height) && event.getY() >= startY ) {
                        //计算手势在控件上的偏移量
                        tv_width = event.getX() - startX;
                        tv_height = event.getY() - startY;
                    }
                }

                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //第二个手指头落点 早已经不是点击事件
                onefinger = false;

                Log.d("HHHH", "ACTION_DOWN_POINTER");

                try {
                    ptrID2 = event.getPointerId(event.getActionIndex());
                    sX = event.getX(event.findPointerIndex(ptrID1));
                    sY = event.getY(event.findPointerIndex(ptrID1));
                    fX = event.getX(event.findPointerIndex(ptrID2));
                    fY = event.getY(event.findPointerIndex(ptrID2));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if ( listTvParams != null && !listTvParams.isEmpty() ) {
                    //当第二个手指落指的时候 开始计算寻找最近的点
                    listDistance.clear();
                    for ( int i = 0; i < listTvParams.size(); i++ ) {
                        listDistance.add(spacing(getMidPiont(( int ) fX, ( int ) fY, ( int ) sX, ( int ) sY), listTvParams.get(i).getMidPoint()));
                    }
                    //寻找最近的点
                    if ( list != null && !list.isEmpty() ) {
                        double min = listDistance.get(0);
                        num = 0;
                        for ( int i = 1; i < listDistance.size(); i++ ) {
                            if ( min > listDistance.get(i) ) {
                                min = listDistance.get(i);
                                num = i;
                            }
                        }
                        if(num<list.size()){
                            textView = null;
                            textView = list.get(num);
                            tv_widths = getMidPiont(( int ) fX, ( int ) fY, ( int ) sX, ( int ) sY).x - textView.getX();
                            tv_heights = getMidPiont(( int ) fX, ( int ) fY, ( int ) sX, ( int ) sY).y - textView.getY();

                            oldDist = spacing(event, ptrID1, ptrID2);
                            setTextViewParams(getTextViewParams(textView));
                        }

                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("HHHH", "ACTION_MOVE");

                if ( textView != null ) {
                    //平移操作已经交给自己控件自己处理
                    //旋转和缩放操作
                    if ( ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID ) {
                        float nfX, nfY, nsX, nsY;
                        nsX = event.getX(event.findPointerIndex(ptrID1));
                        nsY = event.getY(event.findPointerIndex(ptrID1));
                        nfX = event.getX(event.findPointerIndex(ptrID2));
                        nfY = event.getY(event.findPointerIndex(ptrID2));

//                    //如果两点中点在该View上,则考虑两个点也可以拖动该View操作
//                    if (ifIsOnView(textView, new Point(getMidPiont((int) nfX, (int) nfY, (int) nsX, (int) nsY)))) {
//                    textView.setX(getMidPiont((int) nfX, (int) nfY, (int) nsX, (int) nsY).x - tv_widths);
//                    textView.setY(getMidPiont((int) nfX, (int) nfY, (int) nsX, (int) nsY).y - tv_heights);
//                    }
                        //处理旋转模块
                        mAngle = angleBetweenLines(fX, fY, sX, sY, nfX, nfY, nsX, nsY) + defaultAngle;
                        textView.setRotation(mAngle);

                        //处理缩放模块
                        float newDist = spacing(event, ptrID1, ptrID2);
                        scale = newDist / oldDist;
                        if ( newDist > oldDist + 1 ) {
                            zoom(scale);
                            oldDist = newDist;
                        }
                        if ( newDist < oldDist - 1 ) {
                            zoom(scale);
                            oldDist = newDist;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d("HHHH", "ACTION_UP");
                if ( onefinger ) {
                    if ( spacing(firstX, firstY, event.getX(), event.getY()) < 10 ) {
                        addTv(true);

                       // Toast.makeText(context, "这个是自定义View上的单击事件！", Toast.LENGTH_SHORT).show();

                    } else {
                        if ( touchCallBack != null ) {
                            if ( Math.abs(firstX - event.getX()) > Math.abs(firstY - event.getY()) ) {
                                if ( firstX < event.getX() ) {
                                    Log.d("HHH", "你应该是在右滑吧");
                                    touchCallBack.touchMoveCallBack(1);
                                } else {
                                    Log.d("HHH", "你应该是在左滑吧");
                                    touchCallBack.touchMoveCallBack(0);
                                }
                            }
                        }
                    }
                }

                ptrID1 = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.d("HHHH", "ACTION_UP_POINTER");
                ptrID2 = INVALID_POINTER_ID;
                if ( list != null && !list.isEmpty() ) {
                    if(num<list.size())
                    updateTextViewParams(list.get(num), mAngle, scale);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("HHHH", "ACTION_CANCEL");
                ptrID1 = INVALID_POINTER_ID;
                ptrID2 = INVALID_POINTER_ID;
                break;
        }
        return true;
    }


    /**
     * 移除一下在添加文字
     */
    public void updateTv(){
        //移除前记录位置大小等参数
        float x=textView.getX();
        float  y=textView.getY();
        float size= textView.getTextSize();
        float rotation= textView.getRotation();
        removeView(textView);

        addTextView(null,x,y, message,color, CommUtil.px2sp(context,size),rotation);
    }

    public void removeView(StrokeTextView v){
       // num--;
        list.remove(v);
        listTvParams.remove(tvParams);
        super.removeView(v);
    }

    /**
     * 添加&更新文字
     * @param isNew 是否是新增
     */
    public void addTv( final boolean isNew){
        if ( isNew ) {
            addTextView(null, currentX, currentY, message,color, 0, 0);
        } else {
            if(textView!=null)
            addTextView(textView, textView.getX(), textView.getY(), message,color, textView.getTextSize(), textView.getRotation());
        }
        if(textView!=null&&typeface!=null){
            textView.setTypeface(typeface);
        }
    }

    /**
     * 清空文字
     */
    public void removeAllThings() {
        this.removeAllViews();
//        for (int i = 0; i <list.size() ; i++) {
//            removeView(list.get(i));
//        }
        listDistance.clear();
        list.clear();
        listTvParams.clear();
    }

    /**
     * 删除最近添加的一条文字
     */
    public void removeTextView() {
        if(list!=null&&!list.isEmpty()){
            removeView(list.get(list.size()-1));
        }

    }

    /**
     * 为自定义View设置背景图片 顺便隐藏VerticalSeekBar
     *
     * @param bitmap
     */
    @TargetApi( Build.VERSION_CODES.JELLY_BEAN )
    public void setBackGroundBitmap(Bitmap bitmap) {
        setBackground(new BitmapDrawable(bitmap));
    }


    LayoutInflater inflater;

    /**
     * 添加一个TextView到界面上
     */
    public void addTextView(StrokeTextView tv, float x, float y, String content, int color, float mtextSize, float rotate) {
        if ( tv == null ) {

            if(list.size()>=20){
                if(list.size()>=40){
                    CommUtil.showToast("请控制下您的麒麟臂，在点击图片都看不清啦");
                    return;
                }
                CommUtil.showToast("骚年，您点击的手速过猛哟~");
            }

            if ( mtextSize == 0 ) {
                mtextSize = DEFAULT_TEXTSIZE;
            }
            if(inflater==null){
                inflater= LayoutInflater.from(context);
            }
            if(color==Color.WHITE){
                textView = new StrokeTextView(context, Color.parseColor("#000000"),color);
            }else{
                textView = new StrokeTextView(context, Color.parseColor("#ffffff"),color);
            }


//            textView.setEms(1);
            textView.setTag(System.currentTimeMillis());
            textView.setText(content);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(params);
            textView.setTextSize(mtextSize);
            textView.setTextColor(color);
            textView.setRotation(rotate);
            textView.setX(x - textView.getWidth());
            textView.setY(y - textView.getHeight());
            textView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    textView = ( StrokeTextView ) v;
                    switch ( event.getAction() & MotionEvent.ACTION_MASK ) {
                        case MotionEvent.ACTION_DOWN:

                            tvOneFinger = true;
                            isClick = true;

                            firstX = event.getX();
                            firstY = event.getY();

                            mptrID1 = event.getPointerId(event.getActionIndex());
                            //计算当前textView的位置和大小
                            width = textView.getWidth();
                            height = textView.getHeight();
                            if ( mEvent != null ) {
                                mTv_width = mEvent.getX() - textView.getX();
                                mTv_height = mEvent.getY() - textView.getY();
                            }
                            mflag = true;



                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            tvOneFinger = false;
                            isClick = false;

                            mptrID2 = event.getPointerId(event.getActionIndex());
                            try {
                                msX = mEvent.getX(event.findPointerIndex(mptrID1));
                                msY = mEvent.getY(event.findPointerIndex(mptrID1));
                                mfX = mEvent.getX(event.findPointerIndex(mptrID2));
                                mfY = mEvent.getY(event.findPointerIndex(mptrID2));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            mflag = false;

                            mTv_widths = getMidPiont(( int ) mfX, ( int ) mfY, ( int ) msX, ( int ) msY).x - textView.getX();
                            mTv_heights = getMidPiont(( int ) mfX, ( int ) mfY, ( int ) msX, ( int ) msY).y - textView.getY();

                            oldDist = spacing(event, mptrID1, mptrID2);

                            break;
                        case MotionEvent.ACTION_MOVE:
                            //平移操作
                            if ( mflag && mEvent != null ) {
                                textView.setX(mEvent.getX() - mTv_width);
                                textView.setY(mEvent.getY() - mTv_height);
                                //通知调用者我在平移
                                if ( touchCallBack != null )
                                    touchCallBack.onTextViewMoving(textView);
                            }

                            if ( spacing(firstX, firstY, event.getX(), event.getY()) > 2 ) {
                                isClick = false;
                            }

                            //旋转和缩放操作
                            if ( mptrID1 != INVALID_POINTER_ID && mptrID2 != INVALID_POINTER_ID ) {

                                try {
                                    float nfX, nfY, nsX, nsY;
                                    nsX = mEvent.getX(event.findPointerIndex(mptrID1));
                                    nsY = mEvent.getY(event.findPointerIndex(mptrID1));
                                    nfX = mEvent.getX(event.findPointerIndex(mptrID2));
                                    nfY = mEvent.getY(event.findPointerIndex(mptrID2));

                                    //如果两点中点在该View上,则考虑两个点也可以拖动该View操作
                                    textView.setX(getMidPiont(( int ) nfX, ( int ) nfY, ( int ) nsX, ( int ) nsY).x - mTv_widths);
                                    textView.setY(getMidPiont(( int ) nfX, ( int ) nfY, ( int ) nsX, ( int ) nsY).y - mTv_heights);
                                    //处理旋转模块
                                    mAngle = angleBetweenLines(mfX, mfY, msX, msY, nfX, nfY, nsX, nsY);
                                    textView.setRotation(mAngle);
                                    //处理缩放模块
                                    float newDist = spacing(event, mptrID1, mptrID2);
                                    scale = newDist / oldDist;

                                    if ( newDist > oldDist + 1 ) {
                                        zoom(scale);
                                        oldDist = newDist;
                                    }
                                    if ( newDist < oldDist - 1 ) {
                                        zoom(scale);
                                        oldDist = newDist;
                                    }


                                    //通知调用者我在旋转或者缩放
                                    if ( touchCallBack != null )
                                        touchCallBack.onTextViewMoving(textView);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            break;

                        case MotionEvent.ACTION_UP:
                            //通知调用者我滑动结束了
                            if ( touchCallBack != null )
                                touchCallBack.onTextViewMovingDone();
                            mptrID1 = INVALID_POINTER_ID;
                            updateTextViewParams((TextView) v, mAngle, scale);

                            if ( tvOneFinger && isClick ) {
                                if ( touchCallBack != null )
                                    touchCallBack.onTextViewClick(textView);
                               // Toast.makeText(context, "textView上的单击事件！", Toast.LENGTH_SHORT).show();
                            }

                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            mptrID2 = INVALID_POINTER_ID;
                            updateTextViewParams((TextView) v, mAngle, scale);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            mptrID1 = INVALID_POINTER_ID;
                            mptrID2 = INVALID_POINTER_ID;
                            break;
                    }
                    return true;
                }
            });

            list.add(textView);
            //保存并添加到list中
            saveTextViewparams(textView);
            addView(textView);
        } else {
            textView = tv;
            textView.setText(content);
            textView.setTextColor(color);

        }

    }

    /**
     * 初始化操作
     */
    private void init() {
        DEFAULT_TEXTSIZE=getResources().getDimensionPixelOffset(R.dimen.mindp);
        ptrID1 = INVALID_POINTER_ID;
        ptrID2 = INVALID_POINTER_ID;
        mptrID1 = INVALID_POINTER_ID;
        mptrID2 = INVALID_POINTER_ID;

        list = new ArrayList<>();
        listTvParams = new ArrayList<>();
        listDistance = new ArrayList<>();


    }


    /**
     * 对控件进行参数的更新操作
     *
     * @param tv
     */
    private void updateTextViewParams(TextView tv, float rotation, float scale) {
        for ( int i = 0; i < listTvParams.size(); i++ ) {
            TextViewParams param = new TextViewParams();
            if ( tv.getTag().toString().equals(listTvParams.get(i).getTag()) ) {
                param.setRotation(rotation);
                param.setTextSize(( float ) (tv.getTextSize() / scaleTimes));
                param.setMidPoint(getViewMidPoint(tv));
                param.setScale(scale);
                textSize = tv.getTextSize() / 2;
                param.setWidth(tv.getWidth());
                param.setHeight(tv.getHeight());
                param.setX(tv.getX());
                param.setY(tv.getY());
                param.setTag(listTvParams.get(i).getTag());
                param.setContent(tv.getText().toString());
                param.setTextColor(tv.getCurrentTextColor());
                listTvParams.set(i, param);
                return;
            }
        }
    }

    /**
     * //对状态进行保存操作
     *
     * @param textView
     * @return
     */
    private void saveTextViewparams(TextView textView) {
        if ( textView != null ) {
            tvParams = new TextViewParams();
            tvParams.setRotation(0);
            tvParams.setTextSize(( float ) (textView.getTextSize() / scaleTimes));
            tvParams.setX(textView.getX());
            tvParams.setY(textView.getY());
            tvParams.setWidth(textView.getWidth());
            tvParams.setHeight(textView.getHeight());
            tvParams.setContent(textView.getText().toString());
            tvParams.setMidPoint(getViewMidPoint(textView));
            tvParams.setScale(1);
            tvParams.setTag(String.valueOf(( long ) textView.getTag()));
            tvParams.setRotation(mAngle);
            tvParams.setTextColor(textView.getCurrentTextColor());
            listTvParams.add(tvParams);
        }
    }

    /**
     * 根据TextView获取到该TextView的配置文件
     *
     * @param tv
     * @return
     */
    private TextViewParams getTextViewParams(TextView tv) {
        for ( int i = 0; i < listTvParams.size(); i++ ) {
            if ( listTvParams.get(i).getTag().equals(String.valueOf(( long ) tv.getTag())) ) {
                return listTvParams.get(i);
            }
        }
        return null;
    }

    //返回所有的TextView的参数
    public List<TextViewParams> getListTvParams() {
        List<TextViewParams> newImageList = new ArrayList<>();
        newImageList.addAll(listTvParams);
        return newImageList;
    }

    public void setListTvParams(List<TextViewParams> listTvParams) {
        List<TextViewParams> tempList = new ArrayList<>();
        tempList.addAll(listTvParams);
        this.listTvParams = tempList;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    /**
     * 对控件重新赋值 防止出现错乱
     *
     * @param para
     */
    private void setTextViewParams(TextViewParams para) {
        if(para!=null){
            scale = para.getScale();
            textSize = para.getTextSize();
            mAngle = para.getRotation();
            defaultAngle = mAngle;
            Log.d("HHH", "defaultAngle " + defaultAngle);
        }

    }

    /**
     * 获取中间点
     *
     * @param p1
     * @param p2
     * @return 返回两个点连线的中点
     */
    private Point getMidPiont(Point p1, Point p2) {
        return new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
    }

    /**
     * 获取中间点
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private Point getMidPiont(int x1, int y1, int x2, int y2) {
        return new Point((x1 + x2) / 2, (y1 + y2) / 2);
    }

    /**
     * 该方法用于回一个View的终点坐标
     * 如果该View不存在则返回(0,0)
     *
     * @param view
     * @return
     */
    private Point getViewMidPoint(View view) {
        Point point = new Point();
        if ( view != null ) {
            float xx = view.getX();
            float yy = view.getY();
            int center_x = ( int ) (xx + view.getWidth() / 2);
            int center_y = ( int ) (yy + view.getHeight() / 2);
            point.set(center_x, center_y);
        } else {
            point.set(0, 0);
        }
        return point;
    }

    /**
     * 该方法用于判断某一个点是否某一个范围中
     *
     * @param width
     * @param height
     * @param startX
     * @param startY
     * @param point
     * @return
     */
    private boolean ifIsOnView(int width, int height, int startX, int startY, Point point) {
        return (point.x < (width + startX) && point.x > startX && point.y < (startY + height) && point.y > startY) ? true : false;
    }

    /**
     * 该方法用于判断某一个点是否在View上
     *
     * @param view
     * @param point
     * @return
     */
    private boolean ifIsOnView(View view, Point point) {
        int w = view.getWidth();
        int h = view.getHeight();
        float x = view.getX();
        float y = view.getY();
        return (point.x < (w + x) && point.x > x && point.y < (y + h) && point.y > y) ? true : false;
    }

    /**
     * 计算刚开始触摸的两个点构成的直线和滑动过程中两个点构成直线的角度
     *
     * @param fX  初始点一号x坐标
     * @param fY  初始点一号y坐标
     * @param sX  初始点二号x坐标
     * @param sY  初始点二号y坐标
     * @param nfX 终点一号x坐标
     * @param nfY 终点一号y坐标
     * @param nsX 终点二号x坐标
     * @param nsY 终点二号y坐标
     * @return 构成的角度值
     */
    private float angleBetweenLines(float fX, float fY, float sX, float sY, float nfX, float nfY, float nsX, float nsY) {
        float angle1 = ( float ) Math.atan2((fY - sY), (fX - sX));
        float angle2 = ( float ) Math.atan2((nfY - nsY), (nfX - nsX));

        float angle = (( float ) Math.toDegrees(angle1 - angle2)) % 360;
        if ( angle < -180.f ) angle += 360.0f;
        if ( angle > 180.f ) angle -= 360.0f;
        return -angle;
    }


    //缩放实现
    private void zoom(float f) {
        int maxSize=getResources().getDimensionPixelSize(R.dimen.maxdp);
        if(textSize>=maxSize){
            textView.setTextSize(maxSize);
            if(textSize>maxSize){
                textView.setTextSize(maxSize-1);
                return;
            }
        }
        textView.setTextSize(textSize *= f);
    }

    /**
     * 计算两点之间的距离
     *
     * @param event
     * @return 两点之间的距离
     */
    private float spacing(MotionEvent event, int ID1, int ID2) {
        float x = 0;
        float y = 0;
        try {
            x = event.getX(ID1) - event.getX(ID2);
            y = event.getY(ID1) - event.getY(ID2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 求两个一直点的距离
     *
     * @param p1
     * @param p2
     * @return
     */
    private double spacing(Point p1, Point p2) {
        double x = 0;
        double y = 0;
        try {
            x = p1.x - p2.x;
            y = p1.y - p2.y;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Math.sqrt((x * x + y * y));
    }

    /**
     * 返回两个点之间的距离
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private double spacing(float x1, float y1, float x2, float y2) {
        double x = 0;
        double y = 0;
        try {
            x = x1 - x2;
            y = y1 - y2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Math.sqrt((x * x + y * y));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * Created by cretin on 15/12/21
     * 用于记录每个TextView的状态
     */
    public class TextViewParams {
        private String tag;
        private float textSize;
        private Point midPoint;
        private float rotation;
        private float scale;
        private String content;
        private int width;
        private int height;
        private float x;
        private float y;
        private int textColor;

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        @Override
        public String toString() {
            return "TextViewParams{" +
                    "tag='" + tag + '\'' +
                    ", textSize=" + textSize +
                    ", midPoint=" + midPoint +
                    ", rotation=" + rotation +
                    ", scale=" + scale +
                    ", content='" + content + '\'' +
                    ", width=" + width +
                    ", height=" + height +
                    ", x=" + x +
                    ", y=" + y +
                    ", textColor=" + textColor +
                    '}';
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public float getTextSize() {
            return textSize;
        }

        public void setTextSize(float textSize) {
            this.textSize = textSize;
        }

        public Point getMidPoint() {
            return midPoint;
        }

        public void setMidPoint(Point midPoint) {
            this.midPoint = midPoint;
        }

        public float getRotation() {
            return rotation;
        }

        public void setRotation(float rotation) {
            this.rotation = rotation;
        }

        public float getScale() {
            return scale;
        }

        public void setScale(float scale) {
            this.scale = scale;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }

    public interface MyRelativeTouchCallBack {
        void touchMoveCallBack(int direction);

        void onTextViewMoving(TextView textView);

        void onTextViewMovingDone();

        void onTextViewClick(TextView textView);

    }

}
