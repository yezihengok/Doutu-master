package com.yzi.doutu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yzi.doutu.R;
import com.yzi.doutu.adapter.HotListAdapter;
import com.yzi.doutu.bean.DataBean;
import com.yzi.doutu.db.DBTools;
import com.yzi.doutu.service.DouApplication;
import com.yzi.doutu.interfaces.CommInterface;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.HandlerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.yzi.doutu.utils.CommUtil.isWeiBaopen;

/**
 * Created by yzh-t105 on 2016/10/9.
 */

public class DIYFragment extends Fragment implements CommInterface.OnItemClickListener{

    private XRecyclerView mRecyclerView;
    private LinearLayout noDataLayout;
    private HotListAdapter mAdapter;

    private List<DataBean> beanList;
    int ITEM=4;//item个数
    int COUNT=20;//分页加载条数
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        initView(view);
        return  view;
    }

    private void initView(View view) {
        mRecyclerView= (XRecyclerView) view.findViewById(R.id.xrecyclerview);
        noDataLayout= (LinearLayout) view.findViewById(R.id.noDataLayout);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),ITEM);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setPullRefreshEnabled(true);
        beanList=new ArrayList<>();
        mAdapter = new HotListAdapter(getActivity(),beanList,1,"showMade");
        mAdapter.setItemWidth(ITEM);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLoadingMoreProgressStyle(new Random().nextInt(28));
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getFavorites(true);
            }
            @Override
            public void onLoadMore() {
                getFavorites(false);
            }
        });
        mRecyclerView.setRefreshing(true);
    }

    List<DataBean> favorites;
    /**
     * 获取我的收藏数据
     * @param isrefresh 是否为初始刷新，否则视为上拉加载
     */
    public void getFavorites(final boolean isrefresh) {
        favorites=new ArrayList<>();
        if(isrefresh){
            beanList.clear();
            favorites= DBTools.getInstance().getMades(0,COUNT);
            setData(isrefresh);
        }else{
            HandlerUtil.runOnUiThreadDelay(new Runnable() {
                @Override
                public void run() {
                    favorites= DBTools.getInstance().getMades(beanList.size(),beanList.size()+COUNT);
                    setData(isrefresh);
                }
            },300);
        }

    }

    private void setData(boolean isrefresh) {
        if(favorites!=null&&!favorites.isEmpty()){
            beanList.addAll(favorites);
            mAdapter.setHotList(beanList);
            mAdapter.notifyDataSetChanged();
            noDataLayout.setVisibility(View.GONE);
        }else{
            //CommUtil.showToast("没有更多了");
        }

        if(isrefresh){
            mRecyclerView.refreshComplete();
        }else{
            mRecyclerView.loadMoreComplete();
        }
    }

    @Override
    public void onItemClick(View view, int position) {

        DataBean dataBean=beanList.get(position);
        dataBean.setFormWhere("DIYFragment");

        if(CommUtil.QQ.equals(CommUtil.FLAG)){
            //是否开启了尾巴分享
            if (isWeiBaopen()) {
                //使用QQ SDK分享
                CommUtil.onDownLoad(dataBean,getActivity(),3);
            } else {
                CommUtil.onDownLoad(dataBean,getActivity(),1);
            }

        }else if(CommUtil.WeChat.equals(CommUtil.FLAG)){
            if (isWeiBaopen()) {
                CommUtil.onDownLoad(dataBean,getActivity(),5);
            } else {
                CommUtil.onDownLoad(dataBean,getActivity(),2);
            }
        }
        //分享后，关闭当前dialog
        DouApplication.getInstance().removeAllActivity();
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
