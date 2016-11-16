

package com.yzi.doutu.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yzi.doutu.R;
import com.yzi.doutu.activity.ModifyPicActivity;
import com.yzi.doutu.adapter.HotListAdapter;

import com.yzi.doutu.bean.DataBean;
import com.yzi.doutu.bean.NewPic;
import com.yzi.doutu.utils.CommInterface;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.PraseUtils;
import com.yzi.doutu.utils.SharedUtils;
import com.yzi.doutu.utils.SnackbarUtil;
import com.yzi.doutu.utils.TopTips;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import okhttp3.Call;

/**
 * Created by Monkey on 2015/6/29.
 */
public class HotListFragment extends Fragment
        implements CommInterface.OnItemClickListener{

    private View mView;
    private XRecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private HotListAdapter hotListAdapter;

    private int hotPage =1;
    private List<DataBean> hotList; //最热列表

    private View top;
    public HotListFragment() {}
    int time=0;
    @SuppressLint("ValidFragment")
    public HotListFragment(View top) {
        this.top = top;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("", this.getClass().getSimpleName() + "-onCreate");

        mView = inflater.inflate(R.layout.hot_main, container, false);
        initview();
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("", this.getClass().getSimpleName() + "-onActivityCreated");
    }

    private void initview() {
        mRecyclerView = (XRecyclerView) mView.findViewById(R.id.hotrecyclerview);
        // mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        hotList=new ArrayList<>();
        hotListAdapter = new HotListAdapter(getActivity(),hotList);
        hotListAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(hotListAdapter);
        configRecyclerView();
    }

    private void configRecyclerView() {
        mRecyclerView.setRefreshProgressStyle(new Random().nextInt(28));
        mRecyclerView.setLoadingMoreProgressStyle(new Random().nextInt(28));
//        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
//        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                hotPage = 0;
                getHotList(hotPage, false);
            }
            @Override
            public void onLoadMore() {
                getHotList(hotPage, false);
            }
        });
        mRecyclerView.setRefreshing(true);
    }


    public void getHotList(final int page, boolean showDialog) {
        mRecyclerView.setRefreshProgressStyle(new Random().nextInt(28));
        mRecyclerView.setLoadingMoreProgressStyle(new Random().nextInt(28));
        Log.v("", "getHotList");
        if (showDialog) {
           // CommUtil.showWaitDialog(getActivity(), "加载中...", true);
        }

        OkHttpUtils
                .get()
                .url(CommUtil.HOT_URL)
                .addParams("pageNum", String.valueOf(page))
                .addParams("pageSize", "20")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("", e.toString());
                        CommUtil.closeWaitDialog();
                        refreshComplete(page);
                        if(time<2){
                            setData((NewPic) SharedUtils.getObject("hotList",getActivity()),page);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("", response);
                        refreshComplete(page);
                        CommUtil.closeWaitDialog();
                        NewPic newPic = PraseUtils.parseJsons(response, NewPic.class);
                        setData(newPic, page);
                    }
                });
    }

    private void setData(NewPic newPic, int page) {
        if (newPic != null) {
            if (newPic.getData() != null) {
                if (page==0){
                    hotList.clear();
                }
                hotPage++;
                hotList.addAll(newPic.getData());
                if(hotPage==1){
                    Collections.shuffle(hotList);//随机打乱一下list顺序
                }

                Log.d("", "hotList.size():" + hotList.size());
                hotListAdapter.notifyDataSetChanged();
            }
            if(time==0){
                SharedUtils.putObject("hotList",newPic,getActivity());
            }
            time++;
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

        CommUtil.getInstance().toAddText( hotList.get(position),getActivity(),null);
        //CommUtil.getInstance().showSharePop(getActivity(), hotList.get(position));
    }

    @Override
    public void onItemLongClick(View view, int position) {
        SnackbarUtil.show(mRecyclerView,hotList.get(position).getName(), 0);
    }
}
