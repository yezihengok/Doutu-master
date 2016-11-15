

package com.yzi.doutu.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yzi.doutu.R;
import com.yzi.doutu.adapter.RealmanAdapter;
import com.yzi.doutu.adapter.NewListAdapter;
import com.yzi.doutu.bean.DataBean;
import com.yzi.doutu.bean.NewPic;
import com.yzi.doutu.utils.CommInterface;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.PraseUtils;
import com.yzi.doutu.utils.RecycleViews.SpaceItemDecoration;
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
public class NewListFragment extends Fragment
        implements CommInterface.OnItemClickListener{

    private View mView;
    private XRecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private NewListAdapter mStaggeredAdapter;
    private int flag = 0;

    private int listPage = 1;

    private List<DataBean> newList; //最新列表

    private View top;
    public NewListFragment() {}

    @SuppressLint("ValidFragment")
    public NewListFragment(View top) {
        this.top = top;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("", this.getClass().getSimpleName() + "-onCreate");

        mView = inflater.inflate(com.yzi.doutu.R.layout.frag_main, container, false);
        initview();
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("", this.getClass().getSimpleName() + "-onActivityCreated");
    }

    private void initview() {
        mRecyclerView = (XRecyclerView) mView.findViewById(R.id.xrecyclerview);
        flag = (int) getArguments().get("flag");
        newList = new ArrayList<>();

        mStaggeredAdapter = new NewListAdapter(getActivity(), newList);
        mStaggeredAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mStaggeredAdapter);

        //添加上下间距
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        configXRecyclerView();
        mRecyclerView.setRefreshing(true);

    }


    private void configXRecyclerView() {
//        mRecyclerView.setLoadingMoreEnabled(false);
//        mRecyclerView.setPullRefreshEnabled(false);
//        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
//        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);
        mRecyclerView.setRefreshProgressStyle(new Random().nextInt(28));
        mRecyclerView.setLoadingMoreProgressStyle(new Random().nextInt(28));
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                listPage = 0;
                getNewList( false);
            }

            @Override
            public void onLoadMore() {
                getNewList( false);
            }
        });
    }

    int time=0;
    public void getNewList(boolean showDialog) {
        mRecyclerView.setRefreshProgressStyle(new Random().nextInt(28));
        mRecyclerView.setLoadingMoreProgressStyle(new Random().nextInt(28));

        Log.v("", "请求getNewList");
        if (showDialog) {
            CommUtil.showWaitDialog(getActivity(), "加载中...", false);
        }

        OkHttpUtils.get()
                .url(CommUtil.NEW_URL)
                .addParams("pageNum", String.valueOf(listPage))
                .addParams("pageSize", "20")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("", e.toString());
                        CommUtil.closeWaitDialog();
                        refreshComplete(listPage);

                        if(time<2){
                            setData((NewPic) SharedUtils.getObject("newList",getActivity()));
                        }

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("", response);
                        CommUtil.closeWaitDialog();
                        refreshComplete(listPage);

                        NewPic newPic = PraseUtils.parseJsons(response, NewPic.class);
                        setData(newPic);

                    }
                });
    }

    /**
     * 设置数据
     * @param newPic
     */
    private void setData(NewPic newPic) {
        if (newPic != null) {
            if (newPic.getData() != null) {
                if (listPage==0){
                    newList.clear();
//                new TopTips(getActivity(),0,50).show(top,
//                 "已刷新"+newPic.getData().size()+"条数据",2000L);
                }
                listPage++;
                newList.addAll(newPic.getData());
                Log.d("", "newList.size():" + newList.size());
                if(listPage==1){
                    Collections.shuffle(newList);//随机打乱一下list顺序
                    //不要问我为什么这样干，只是不想和别人的列表数据顺序展示的一模一样 - -！
                }

                mStaggeredAdapter.setmDatas(newList);
            } else {
                CommUtil.showToast("没有更多了");
            }

            if(time==0){
                SharedUtils.putObject("newList",newPic,getActivity());
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

    public void refersh(){
        new TopTips(getActivity(),0,40).show(top, "已刷新数据",2000L);
    }

    @Override
    public void onItemClick(View view, int position) {
        CommUtil.getInstance().showSharePop(getActivity(), newList.get(position),null);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        SnackbarUtil.show(mRecyclerView,newList.get(position).getName(), 0);
    }
}
