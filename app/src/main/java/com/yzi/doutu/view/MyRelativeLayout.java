package com.yzi.doutu.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yzi.doutu.R;
import com.yzi.doutu.utils.CommInterface;
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
    private TextView textView;
    private TextViewParams tvParams;

    private EditText editText;
    private boolean flag = false;
    private boolean mflag = false;
    private boolean onefinger;
    private boolean tvOneFinger;

    //记录是否为TextView上的单击事件
    private boolean isClick = true;

    public static final int DEFAULT_TEXTSIZE = 22;

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
    private List<TextView> list;
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
    /**
     * 指定在MyRelativeLayout中滑动的方向
     */
    public static final int MOVE_LEFT = 6;
    public static final int MOVE_RIGHT = 7;

    public int color=Color.BLACK;
    ImageView bgImg;//背景图片
    private String name="新增文字";

    public void setName(String name) {

        if(!TextUtils.isEmpty(name)){
            this.name = name;
        }
    }

    public ImageView getBgImg() {
        return bgImg;
    }

    public void setColortag(int color) {
        this.color = color;
        editText.setTextColor(color);
    }

    public MyRelativeTouchCallBack getMyRelativeTouchCallBack() {
        return myRelativeTouchCallBack;
    }

    public void setMyRelativeTouchCallBack(MyRelativeTouchCallBack myRelativeTouchCallBack) {
        this.myRelativeTouchCallBack = myRelativeTouchCallBack;
    }


    //接口
    private MyRelativeTouchCallBack myRelativeTouchCallBack;

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

        init();

        //添加背景图片
        bgImg=new ImageView(context);
        bgImg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT));
        bgImg.setImageResource(R.mipmap.doutuu);
        bgImg.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(bgImg);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mEvent = event;
        if (textSize == 0 && textView != null) {
            textSize = textView.getTextSize();
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
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
                if (textView != null) {
                    //计算当前textView的位置和大小
                    width = textView.getWidth();
                    height = textView.getHeight();
                    startX = textView.getX();
                    startY = textView.getY();

                    if (event.getX() <= (startX + width) && event.getX() >= startX && event.getY() <= (startY + height) && event.getY() >= startY) {
                        //计算手势在控件上的偏移量
                        tv_width = event.getX() - startX;
                        tv_height = event.getY() - startY;
                        flag = true;
                    } else {
                        flag = false;
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //第二个手指头落点 早已经不是点击事件
                onefinger = false;

                Log.d("HHHH", "ACTION_DOWN_POINTER");

                ptrID2 = event.getPointerId(event.getActionIndex());
                sX = event.getX(event.findPointerIndex(ptrID1));
                sY = event.getY(event.findPointerIndex(ptrID1));
                fX = event.getX(event.findPointerIndex(ptrID2));
                fY = event.getY(event.findPointerIndex(ptrID2));

                flag = false;

                if (listTvParams != null && !listTvParams.isEmpty()) {
                    //当第二个手指落指的时候 开始计算寻找最近的点
                    listDistance.clear();
                    for (int i = 0; i < listTvParams.size(); i++) {
                        listDistance.add(spacing(getMidPiont((int) fX, (int) fY, (int) sX, (int) sY), listTvParams.get(i).getMidPoint()));
                    }
//寻找最近的点
                    if (list != null && !list.isEmpty()) {
                        double min = listDistance.get(0);
                        num = 0;
                        for (int i = 1; i < listDistance.size(); i++) {
                            if (min > listDistance.get(i)) {
                                min = listDistance.get(i);
                                num = i;
                            }
                        }
                        textView = null;
                        textView = list.get(num);
                        tv_widths = getMidPiont((int) fX, (int) fY, (int) sX, (int) sY).x - textView.getX();
                        tv_heights = getMidPiont((int) fX, (int) fY, (int) sX, (int) sY).y - textView.getY();

                        oldDist = spacing(event, ptrID1, ptrID2);
                        setTextViewParams(getTextViewParams(textView));
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("HHHH", "ACTION_MOVE");

                if (textView != null) {
                    //平移操作已经交给自己控件自己处理
                    //旋转和缩放操作
                    if (ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID) {
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
                        if (newDist > oldDist + 1) {
                            zoom(scale);
                            oldDist = newDist;
                        }
                        if (newDist < oldDist - 1) {
                            zoom(scale);
                            oldDist = newDist;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d("HHHH", "ACTION_UP");
                if (onefinger) {
                    if (spacing(firstX, firstY, event.getX(), event.getY()) < 10) {
                        setTextAdd(name, true);
                       // Toast.makeText(context, "这个是自定义View上的单击事件！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (myRelativeTouchCallBack != null) {
                            if (Math.abs(firstX - event.getX()) > Math.abs(firstY - event.getY())) {
                                if (firstX < event.getX()) {
                                    Log.d("HHH", "你应该是在右滑吧");
                                    myRelativeTouchCallBack.touchMoveCallBack(MOVE_RIGHT);
                                } else {
                                    Log.d("HHH", "你应该是在左滑吧");
                                    myRelativeTouchCallBack.touchMoveCallBack(MOVE_LEFT);
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
                if (list != null && !list.isEmpty()) {
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

    public void removeAllThings() {
        this.removeAllViews();
        listDistance.clear();
        list.clear();
        listTvParams.clear();
    }

    /**
     * 为自定义View设置背景图片 顺便隐藏VerticalSeekBar
     *
     * @param bitmap
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setBackGroundBitmap(Bitmap bitmap) {
        setBackground(new BitmapDrawable(bitmap));
    }

    /**
     * 添加一个TextView到界面上
     */
    public void addTextView(TextView tv, float x, float y, String content, int color, float mtextSize, float rotate) {
        if (tv == null) {
            if (mtextSize == 0) {
                mtextSize = DEFAULT_TEXTSIZE;
            }
            textView = new TextView(context);
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
                    textView = (TextView) v;
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            tvOneFinger = true;
                            isClick = true;

                            firstX = event.getX();
                            firstY = event.getY();

                            mptrID1 = event.getPointerId(event.getActionIndex());
                            //计算当前textView的位置和大小
                            width = textView.getWidth();
                            height = textView.getHeight();
                            if (mEvent != null) {
                                mTv_width = mEvent.getX() - textView.getX();
                                mTv_height = mEvent.getY() - textView.getY();
                            }
                            mflag = true;


                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            tvOneFinger = false;
                            isClick = false;

                            mptrID2 = event.getPointerId(event.getActionIndex());
                            msX = mEvent.getX(event.findPointerIndex(mptrID1));
                            msY = mEvent.getY(event.findPointerIndex(mptrID1));
                            mfX = mEvent.getX(event.findPointerIndex(mptrID2));
                            mfY = mEvent.getY(event.findPointerIndex(mptrID2));

                            mflag = false;
//
                            mTv_widths = getMidPiont((int) mfX, (int) mfY, (int) msX, (int) msY).x - textView.getX();
                            mTv_heights = getMidPiont((int) mfX, (int) mfY, (int) msX, (int) msY).y - textView.getY();
//
                            oldDist = spacing(event, mptrID1, mptrID2);

                            break;
                        case MotionEvent.ACTION_MOVE:
                            //平移操作
                            if (mflag && mEvent != null) {
                                textView.setX(mEvent.getX() - mTv_width);
                                textView.setY(mEvent.getY() - mTv_height);
                                //通知调用者我在平移
                                if (myRelativeTouchCallBack != null)
                                    myRelativeTouchCallBack.onTextViewMoving();
                            }

                            if (spacing(firstX, firstY, event.getX(), event.getY()) > 2) {
                                isClick = false;
                            }

                            //旋转和缩放操作
                            if (mptrID1 != INVALID_POINTER_ID && mptrID2 != INVALID_POINTER_ID) {
                                float nfX, nfY, nsX, nsY;
                                nsX = mEvent.getX(event.findPointerIndex(mptrID1));
                                nsY = mEvent.getY(event.findPointerIndex(mptrID1));
                                nfX = mEvent.getX(event.findPointerIndex(mptrID2));
                                nfY = mEvent.getY(event.findPointerIndex(mptrID2));

                                //如果两点中点在该View上,则考虑两个点也可以拖动该View操作
                                textView.setX(getMidPiont((int) nfX, (int) nfY, (int) nsX, (int) nsY).x - mTv_widths);
                                textView.setY(getMidPiont((int) nfX, (int) nfY, (int) nsX, (int) nsY).y - mTv_heights);
                                //处理旋转模块
                                mAngle = angleBetweenLines(mfX, mfY, msX, msY, nfX, nfY, nsX, nsY);
                                textView.setRotation(mAngle);
                                //处理缩放模块
                                float newDist = spacing(event, mptrID1, mptrID2);
                                scale = newDist / oldDist;

                                float sizess=textSize *= scale;
                                if(sizess<=70){
                                    if (newDist > oldDist + 1) {
                                        textView.setTextSize(sizess);
                                        oldDist = newDist;
                                    }
                                    if (newDist < oldDist - 1) {
                                        textView.setTextSize(sizess);
                                        oldDist = newDist;
                                    }
                                }

                                //通知调用者我在旋转或者缩放
//                                if (myRelativeTouchCallBack != null)
//                                    myRelativeTouchCallBack.onTextViewMoving(textView);
                            }
                            break;
                        case MotionEvent.ACTION_UP:

                            mptrID1 = INVALID_POINTER_ID;
                            updateTextViewParams((TextView) v, mAngle, scale);

                            if (tvOneFinger && isClick) {
                                setTextAdd(textView.getText().toString(), false);
                            }

                            if (myRelativeTouchCallBack != null)
                                    myRelativeTouchCallBack.onTextViewMovingDone(textView);
                            //如果文字滑出当前relativelayout范围
//                            Rect rect=new Rect(getLeft(),getTop(),getRight(),getBottom());
//                            if(!rect.contains((int)event.getX(),(int)event.getY())){
//                                CommUtil.showDialog(context, "-该条文字？", "取消", "Ok", null
//                                        , new CommInterface.setClickListener() {
//                                            @Override
//                                            public void onResult() {
//                                                removeView(textView);
//                                            }
//                                        });
//                            }

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
        ptrID1 = INVALID_POINTER_ID;
        ptrID2 = INVALID_POINTER_ID;
        mptrID1 = INVALID_POINTER_ID;
        mptrID2 = INVALID_POINTER_ID;

        list = new ArrayList<>();
        listTvParams = new ArrayList<>();
        listDistance = new ArrayList<>();

        editText = new EditText(context);
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setVisibility(GONE);
        addView(editText);
    }


    /**
     * 为textview 设值或添加
     * @param content
     * @param isNew 是否为添加textview
     */
    public void setTextAdd(String content,final boolean isNew){

        if (isNew) {
            addTextView(null, currentX,currentY, content,color, 0, 0);
            //新增的时候通知滑动 让acitivty获得这个textview一遍操作
            if (myRelativeTouchCallBack != null)
                myRelativeTouchCallBack.onTextViewMovingDone(textView);
        } else {
            addTextView(textView, textView.getX(), textView.getY(), content,color, textView.getTextSize(), textView.getRotation());
        }

    }

    /**
     * 删除一个textview
     */
    public void  setTextSub(){
        if(list!=null&&list.isEmpty())
            removeView(list.get(list.size()-1));
    }

    /**
     * 对控件进行参数的更新操作
     *
     * @param tv
     */
    private void updateTextViewParams(TextView tv, float rotation, float scale) {
        for (int i = 0; i < listTvParams.size(); i++) {
            TextViewParams param = new TextViewParams();
            if (tv.getTag().toString().equals(listTvParams.get(i).getTag())) {
                param.setRotation(rotation);
                param.setTextSize(tv.getTextSize() / 2);
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
        if (textView != null) {
            tvParams = new TextViewParams();
            tvParams.setRotation(0);
            tvParams.setTextSize(textView.getTextSize() / 2);
            tvParams.setX(textView.getX());
            tvParams.setY(textView.getY());
            tvParams.setWidth(textView.getWidth());
            tvParams.setHeight(textView.getHeight());
            tvParams.setContent(textView.getText().toString());
            tvParams.setMidPoint(getViewMidPoint(textView));
            tvParams.setScale(1);
            tvParams.setTag(String.valueOf((long) textView.getTag()));
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
        for (int i = 0; i < listTvParams.size(); i++) {
            if (listTvParams.get(i).getTag().equals(String.valueOf((long) tv.getTag()))) {
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
        scale = para.getScale();
        textSize = para.getTextSize();
        mAngle = para.getRotation();
        defaultAngle = mAngle;
        Log.d("HHH", "defaultAngle " + defaultAngle);
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
        if (view != null) {
            float xx = view.getX();
            float yy = view.getY();
            int center_x = (int) (xx + view.getWidth() / 2);
            int center_y = (int) (yy + view.getHeight() / 2);
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
        float angle1 = (float) Math.atan2((fY - sY), (fX - sX));
        float angle2 = (float) Math.atan2((nfY - nsY), (nfX - nsX));

        float angle = ((float) Math.toDegrees(angle1 - angle2)) % 360;
        if (angle < -180.f) angle += 360.0f;
        if (angle > 180.f) angle -= 360.0f;
        return -angle;
    }

    //缩放实现
    private void zoom(float f) {
        textView.setTextSize(textSize *= f);
    }

    /**
     * 计算两点之间的距离
     *
     * @param event
     * @return 两点之间的距离
     */
    private float spacing(MotionEvent event, int ID1, int ID2) {
        float x = event.getX(ID1) - event.getX(ID2);
        float y = event.getY(ID1) - event.getY(ID2);
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
        double x = p1.x - p2.x;
        double y = p1.y - p2.y;
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
        double x = x1 - x2;
        double y = y1 - y2;
        return Math.sqrt((x * x + y * y));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Log.d("HHHH", "onMeasure-->我被调用了哦" + widthMeasureSpec + " " + heightMeasureSpec);
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

        void onTextViewMoving();

        void onTextViewMovingDone(TextView textView);
    }

}
