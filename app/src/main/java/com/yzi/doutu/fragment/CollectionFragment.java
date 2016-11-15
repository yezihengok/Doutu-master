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
import com.yzi.doutu.utils.CommInterface;
import com.yzi.doutu.utils.CommUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzh-t105 on 2016/10/8.
 */

public class CollectionFragment extends Fragment implements CommInterface.OnItemClickListener{

    private XRecyclerView mRecyclerView;
    private LinearLayout noDataLayout;
    private HotListAdapter mAdapter;

    private List<DataBean> beanList;
    private int ITEM=4;
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
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setPullRefreshEnabled(false);
        beanList=new ArrayList<>();
        mAdapter = new HotListAdapter(getActivity(),beanList,1);
        mAdapter.setItemWidth(ITEM);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        getFavorites();
    }

    public void getFavorites() {
        List<DataBean> favorites= DBTools.getInstance(getActivity()).getFavorites(false);
        if(favorites!=null&&!favorites.isEmpty()){
            beanList=new ArrayList<>(favorites);
            mAdapter.setHotList(beanList);
            mAdapter.notifyDataSetChanged();
            noDataLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(View view, int position) {

        if(CommUtil.QQ.equals(CommUtil.FLAG)){
            CommUtil.onDownLoad(beanList.get(position),getActivity(),1);
            //分享后，关闭当前dialog
            DouApplication.getInstance().removeAllActivity();
        }else if(CommUtil.WeChat.equals(CommUtil.FLAG)){
            CommUtil.onDownLoad(beanList.get(position),getActivity(),2);
            //分享后，关闭当前dialog
            DouApplication.getInstance().removeAllActivity();
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
