package com.yzi.doutu.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pizidea.imagepicker.ImagePresenter;
import com.pizidea.imagepicker.UilImagePresenter;
import com.yzi.doutu.R;
import com.yzi.doutu.adapter.HotListAdapter;
import com.yzi.doutu.bean.DataBean;
import com.yzi.doutu.bean.RealMan;
import com.yzi.doutu.utils.CommInterface;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.PraseUtils;
import com.yzi.doutu.utils.RecycleViews.FullyGridLayoutManager;
import com.yzi.doutu.view.GradationScrollView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.yzi.doutu.R.id.listview;

/**
 * Created by yzh-t105 on 2016/9/27.
 */

public class RealManInfoListActivity extends BaseActivity implements GradationScrollView.ScrollViewListener
,CommInterface.OnItemClickListener{
    private android.widget.ImageView ivbanner;
    private RecyclerView mRecyclerView;
    private com.yzi.doutu.view.GradationScrollView scrollview;
    private android.widget.ImageView back;
    private android.widget.TextView tvtitle;
    private android.widget.ImageView share;
    private android.widget.RelativeLayout titlet;
    private int height;

    private RecyclerView.LayoutManager mLayoutManager;
    private int page = 0;
    private int id;
    private String url,name;
    private List<DataBean> realList; //真人详情列表
    ImagePresenter presenter;
    HotListAdapter realListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realman_infolist);
        application.addActivity(this);
        id=getIntent().getExtras().getInt("id");
        url=getIntent().getExtras().getString("url");
        name=getIntent().getExtras().getString("name");
        initView();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        presenter=new UilImagePresenter();
        this.titlet = (RelativeLayout) findViewById(R.id.titlet);
        this.share = (ImageView) findViewById(R.id.share);
        this.tvtitle = (TextView) findViewById(R.id.tvtitle);
        this.back = (ImageView) findViewById(R.id.back);
        this.scrollview = (GradationScrollView) findViewById(R.id.scrollview);
        this.mRecyclerView = (RecyclerView) findViewById(listview);
        this.ivbanner = (ImageView) findViewById(R.id.iv_banner);
        ivbanner.setFocusable(true);
        ivbanner.setFocusableInTouchMode(true);
        ivbanner.requestFocus();

        presenter.onPresentImage(ivbanner,url);
        mLayoutManager = new FullyGridLayoutManager(this,3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);// 设置是否允许嵌套滑动，嵌套设置false，不然卡顿
        //注意 使用 recyclerview-v7:23.2.1' 23版本的 ，直接设置true 使用自带的LinearLayoutManager 就可以自适应高度了
        //然而我使用 24版本编译的时候，却无效了，需要setAutoMeasureEnabled 设置为false+ 自定义FullyLinearLayoutManager
        //才能正常的嵌套
        mLayoutManager.setAutoMeasureEnabled(false);

        realList=new ArrayList<>();
        realListAdapter = new HotListAdapter(this,realList);
        realListAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(realListAdapter);

        getRealManList();
        setAlpha(0);
        initListeners();

        setTextValues(tvtitle,name);
    }




    /**
     * 获取顶部图片高度后，设置滚动监听
     */
    private void initListeners() {

        ViewTreeObserver vto = ivbanner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                titlet.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = ivbanner.getHeight()-50;

                scrollview.setScrollViewListener(RealManInfoListActivity.this);
            }
        });
    }




    /**
     * 滑动监听
     * @param scrollView
     * @param x 当前横向滑动距离
     * @param y 当前纵向滑动距离
     * @param oldx 之前横向滑动距离
     * @param oldy 之前纵向滑动距离
     */
    @Override
    public void onScrollChanged(GradationScrollView scrollView, int x, int y,
                                int oldx, int oldy) {
        if (y <= 0) {
            //设置默认
            setAlpha(0);
        } else if (y > 0 && y <=height) {

            //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / height;
            float alpha = (255 * scale);
            setAlpha((int) alpha);
            //showToast("alpha:" +alpha);

        } else {
            //滑动到banner下面设置普通颜色
            setAlpha(255);
        }
    }

    /**
     * 设置title元素view的透明度
     * @param alpha
     */
    public void setAlpha(int alpha){
        //shop.setAlpha(alpha);
        share.setAlpha(alpha/255f);
        // back.setAlpha(alpha);
        //Color.argb 对应的是十六进制颜色代码 转换的10进制 数字
        //如 255, 255,61,61   对应 颜色代码 是  #ff3d3d  第一个255 是透明度值

        titlet.setBackgroundColor(Color.argb(alpha,00,175,236));
        tvtitle.setTextColor(Color.argb(alpha,255,255,255));
    }



    public void getRealManList() {
        Log.v("", "请求getrealList");
        OkHttpUtils
            .get()
            .url(CommUtil.REALMANINFO_URL)
            .addParams("tagId",String.valueOf(id))
            .addParams("pageNum",String.valueOf(page))
            .addParams("pageSize",String.valueOf(40))
            .build()
            .execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Log.e("", e.toString());
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.d("", response);

                    RealMan newPic = PraseUtils.parseJsons(response, RealMan.class);
                    if (newPic != null) {
                        if (newPic.getData() != null) {
                            if (page==0){
                                realList.clear();
//                                    new TopTips(getActivity(),0,50).show(top,
//                                            "已刷新"+newPic.getData().size()+"条数据",2000L);
                            }
                            page++;
                            realList.addAll(newPic.getData());
                            Log.d("", "realList.size():" + realList.size());
                            realListAdapter.notifyDataSetChanged();
                        }

                    }
                }
            });
    }



    @Override
    public void onItemClick(View view, int position) {
        CommUtil.getInstance().toAddText( realList.get(position),this,null);
        //CommUtil.getInstance().showSharePop(this, realList.get(position));

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
