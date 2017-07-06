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
import com.yzi.doutu.bean.AllPic;
import com.yzi.doutu.bean.AllPicInfo;
import com.yzi.doutu.bean.DataBean;
import com.yzi.doutu.bean.Theme;
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

import static com.yzi.doutu.utils.CommUtil.DDSQ;

/**
 *全部表情详情界面
 * Created by yzh-t105 on 2016/11/24.
 */
public class AllpicInfoActivity extends BaseActivity implements CommInterface.OnItemClickListener{

    private XRecyclerView mRecyclerView;
    private HotTemplateAdapter mAdapter;

    private int hotPage = 0;
    private List<DataBean> hotList;
    private int lastId;//最后一个id
    AllPic.ListBean listBean;
    TextView tvRight;
    int ITEM=4;
    int total =0;

    Theme theme;
    private String folderId,userId,folderName="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application.addActivity(this);
        setContentView(R.layout.activity_moretype_list);
        initView();

    }

    private void initView() {

        listBean= (AllPic.ListBean) getIntent().getSerializableExtra("listBean");
        theme= (Theme) getIntent().getSerializableExtra("theme");
        if(listBean!=null){
            if(listBean.getFolder()!=null) {
                folderName = listBean.getFolder().getName();
                folderId= String.valueOf(listBean.getFolder().getId());
            }
            if(listBean.getUser()!=null) {
                userId = String.valueOf(listBean.getUser().getId());
            }
        }

        if(theme!=null){
            folderName=theme.getFolderName();
            folderId=theme.getFolderId();
            userId=theme.getUserId();
        }


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

            if(folderName.length()>=9){
                ((TextView)findViewById(R.id.tvtitle)).setText(folderName.substring(0,9)+"...");
            }else{
                ((TextView)findViewById(R.id.tvtitle)).setText(folderName);
            }

        tvRight= (TextView) findViewById(R.id.tvRight);
        setTextValues(tvRight,"");
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
        if(total>0&hotPage>0){
            if(hotList.size()>=total){
                CommUtil.showToast("没有更多了");
                mRecyclerView.loadMoreComplete();
                return;
            }
        }
         //"http://mobile.bugua.com/user/social/5507583/folder/17";

        //StringBuffer buffer=new StringBuffer("http://mobile.bugua.com/user/social/");
        StringBuffer buffer=new StringBuffer(DDSQ+"/user/social/");
        buffer.append(userId);
        buffer.append("/folder/");
        buffer.append(folderId);

        Map<String, String> params =new LinkedHashMap<>();
        params.put("page_size","30");
        if(hotPage>0){
            params.put("last_id",String.valueOf(lastId));
        }
        OkHttpUtils.get().url(buffer.toString())
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
                            setData((AllPicInfo)SharedUtils.getObject("AllPicInfo",AllpicInfoActivity.this));
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("", response);
                        refreshComplete(hotPage);
                        CommUtil.closeWaitDialog();

                        AllPicInfo template = PraseUtils.parseJsons(response, AllPicInfo.class);
                        if (template != null) {
                            setData(template);

                        }
                    }
                });
    }

    private void setData(AllPicInfo template) {
        if (template!=null&&template.getEmotions() != null) {
            if (hotPage==0){
                hotList.clear();
            }
            total=template.getTotal();

            //因为之前写的 数据操作都是以dataBean为基准的,所以把数据塞过去
            for (AllPicInfo.EmotionsBean bean :template.getEmotions()){
                DataBean dataBean=new DataBean();
                dataBean.setId(bean.getOnline_id());
                dataBean.setGifPath(bean.getUrl());
                dataBean.setPicPath(bean.getThumb());
                dataBean.setName(folderName);
                hotList.add(dataBean);

            }
            setTextValues(tvRight,"（共"+hotList.size()+"/"+total+"个）");
            lastId=template.getLast_id();
            if(hotPage==0){
                SharedUtils.putObject("HotTemplate",template,AllpicInfoActivity.this);
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

        //CommUtil.getInstance().toAddText( hotList.get(position),this,null);
        DataBean dataBean=hotList.get(position);
        dataBean.setFormWhere("newlist");
        CommUtil.getInstance().showSharePop(this,dataBean,null);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
