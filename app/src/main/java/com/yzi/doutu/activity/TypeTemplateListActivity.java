package com.yzi.doutu.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yzi.doutu.R;
import com.yzi.doutu.adapter.HotListAdapter;
import com.yzi.doutu.bean.DataBean;
import com.yzi.doutu.bean.NewPic;
import com.yzi.doutu.interfaces.CommInterface;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.PraseUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzh-t105 on 2016/9/22.
 */
public class TypeTemplateListActivity extends BaseActivity implements CommInterface.OnItemClickListener{

    private XRecyclerView mRecyclerView;
    private HotListAdapter mAdapter;

    private int hotPage = 0;
    private List<DataBean> hotList; //

    private int id;
    private String name;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application.addActivity(this);
        setContentView(R.layout.activity_moretype_list);
        initView();

    }

    private void initView() {
        id=getIntent().getExtras().getInt("id");
        name=getIntent().getExtras().getString("name");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.tvtitle)).setText(name);
        ((TextView)findViewById(R.id.tvRight)).setText("");
        mRecyclerView = (XRecyclerView)this.findViewById(R.id.xrecyclerview);

        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        hotList=new ArrayList<>();
        mAdapter = new HotListAdapter(this,hotList);
        mAdapter.setJustShowBitmap(true);
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

        OkHttpUtils.get().url(CommUtil.ALLTYPEBYID)
                .addParams("pageNum", String.valueOf(hotPage))
                .addParams("pageSize", "20")
                .addParams("tagId",String.valueOf(id))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("", e.toString());
                        CommUtil.closeWaitDialog();
                        refreshComplete(hotPage);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("", response);
                        refreshComplete(hotPage);
                        CommUtil.closeWaitDialog();
                        NewPic newPic = PraseUtils.parseJsons(response, NewPic.class);
                        if (newPic != null) {
                            if (newPic.getData() != null) {
                                if (hotPage==0){
                                    hotList.clear();
                                }
                                hotPage++;
                                hotList.addAll(newPic.getData());
                                Log.d("", "hotList.size():" + hotList.size());
                                mAdapter.notifyDataSetChanged();
                            }

                        }
                    }
                });
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

        //CommUtil.getInstance().showSharePop(this, hotList.get(position));
        CommUtil.getInstance().toAddText( hotList.get(position),this,null);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
