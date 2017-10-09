package com.yzi.doutu.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yzi.doutu.R;
import com.yzi.doutu.adapter.HotTemplateAdapter;
import com.yzi.doutu.bean.DataBean;
import com.yzi.doutu.bean.HotTemplate;
import com.yzi.doutu.interfaces.CommInterface;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.PraseUtils;
import com.yzi.doutu.utils.SharedUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 热门表情模板
 * Created by yzh-t105 on 2016/11/22.
 */
public class HotTemplateActivity extends BaseActivity implements CommInterface.OnItemClickListener{

    private XRecyclerView mRecyclerView;
    private HotTemplateAdapter mAdapter;

    private int hotPage = 0;
    private List<DataBean> hotList;
    private int lastId;//最后一个id
    int ITEM=3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application.addActivity(this);
        setContentView(R.layout.activity_moretype_list);
        initView();

    }

    private void initView() {

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.tvtitle)).setText("热门模板");
        ((TextView)findViewById(R.id.tvRight)).setVisibility(View.GONE);
        mRecyclerView = (XRecyclerView)this.findViewById(R.id.xrecyclerview);

        GridLayoutManager layoutManager = new GridLayoutManager(this,ITEM);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        hotList=new ArrayList<>();
        mAdapter = new HotTemplateAdapter(this,hotList,ITEM);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                hotPage = 0;
                getHotList();
            }
            @Override
            public void onLoadMore() {
                getHotList();
            }
        });
        mRecyclerView.setRefreshing(true);
    }


    public void getHotList() {
        Log.v("", "getHotList");
        Map<String, String> params =new LinkedHashMap<>();
        params.put("cls","0");
        if(lastId>0){
            params.put("last_id",String.valueOf(lastId));
        }
        OkHttpUtils.get().url(CommUtil.TEMP_HOT)
                .headers(getHeaders())
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("", e.toString());
                        CommUtil.closeWaitDialog();
                        refreshComplete(hotPage);
                        if(hotPage<1){
                            setData((HotTemplate)SharedUtils.getObject("HotTemplateActivity",HotTemplateActivity.this));
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("", response);
                        refreshComplete(hotPage);
                        CommUtil.closeWaitDialog();

                        HotTemplate template = PraseUtils.parseJsons(response, HotTemplate.class);
                        if (template != null) {
                            setData(template);

                        }
                    }
                });
    }

    private void setData(HotTemplate template) {
        if (template.getTemplates() != null) {
            if (hotPage==0){
                hotList.clear();
            }

            //因为之前写的 数据操作都是以dataBean为基准的,所以把数据塞过去
            for (HotTemplate.TemplatesBean templatesBean:template.getTemplates()){
                 String ids =String.valueOf(templatesBean.getId());
                if(!"6022".equals(ids)&& !"6023".equals(ids)&&!"13225".equals(ids)){//这几个是辣鸡数据不显示
                    DataBean dataBean=new DataBean();
                    dataBean.setId(templatesBean.getId());
                    dataBean.setGifPath(templatesBean.getFpic());
                    dataBean.setPicPath(templatesBean.getFthumb());
                    dataBean.setName(templatesBean.getText());
                    dataBean.setIs_gif(templatesBean.isIs_gif());

                    hotList.add(dataBean);
                }

            }
            lastId=template.getLast_id();
            if(hotPage==0){
                SharedUtils.putObject("HotTemplateActivity",template,HotTemplateActivity.this);
            }
            hotPage++;

            Log.d("", "hotList.size():" + hotList.size());
            mAdapter.notifyDataSetChanged();
        }
    }


    private void refreshComplete(int page) {
        if(page==0){
            mRecyclerView.refreshComplete();
        }else{
            mRecyclerView.loadMoreComplete();
        }
    }

    @Override
    public void onItemClick(View view, int position) {

        CommUtil.getInstance().toAddText( hotList.get(position),this,null);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
