
package com.yzi.doutu.main;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yzi.doutu.R;
import com.yzi.doutu.activity.RealManInfoListActivity;
import com.yzi.doutu.adapter.RealmanAdapter;
import com.yzi.doutu.bean.DataBean;
import com.yzi.doutu.bean.NewPic;
import com.yzi.doutu.interfaces.CommInterface;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.PraseUtils;
import com.yzi.doutu.utils.SharedUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yzh on 2016/09/26.
 */
public class RealManFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, CommInterface.OnItemClickListener{

    private View mView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RealmanAdapter realmanAdapter;

    private List<DataBean> realList; //真人列表
    int time=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("", this.getClass().getSimpleName() + "-onCreate");

        mView = inflater.inflate(R.layout.realman_main, container, false);
        initview();
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("", this.getClass().getSimpleName() + "-onActivityCreated");
    }

    private void initview() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(com.yzi.doutu.R.id.id_swiperefreshlayout);
        // 刷新时，指示器旋转后变化的颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.main_blue_dark);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) mView.findViewById(com.yzi.doutu.R.id.id_recyclerview);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        realList = new ArrayList<>();
        realmanAdapter = new RealmanAdapter(getActivity(), realList);
        mRecyclerView.setAdapter(realmanAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        realmanAdapter.setOnItemClickListener(this);

        getRealManList(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(realList.size()==0){
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }



    public void getRealManList(boolean showDialog) {
        Log.v("", "请求getrealList");
        if (showDialog) {
            CommUtil.showWaitDialog(getActivity(), "加载中...", true);
        }

        OkHttpUtils
                .get()
                .url(CommUtil.REALMAN_URL)
                .addParams("typeId", "6")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("", e.toString());
                        CommUtil.closeWaitDialog();
                        mSwipeRefreshLayout.setRefreshing(false);
                        if(time<1){
                            setData((NewPic) SharedUtils.getObject("realList",getActivity()));
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("", response);
                        // Toast.makeText(getActivity(),response,1).show();
                        CommUtil.closeWaitDialog();
                        mSwipeRefreshLayout.setRefreshing(false);
                        NewPic newPic = PraseUtils.parseJsons(response, NewPic.class);
                        setData(newPic);
                    }
                });
    }

    private void setData(NewPic newPic) {
        if (newPic != null) {
            if (newPic.getData() != null) {
                realList.clear();
                realList.addAll(newPic.getData());
                Collections.shuffle(realList);//随机打乱一下list顺序
                Log.d("", "realList.size():" + realList.size());
                realmanAdapter.notifyDataSetChanged();
            }
            if(time==0){
                SharedUtils.putObject("realList",newPic,getActivity());
            }
            time++;
        }
    }


    @Override
    public void onRefresh() {

        getRealManList(false);

    }

    @TargetApi(21)
    @Override
    public void onItemClick(View view, int position) {
        //SnackbarUtil.show(mRecyclerView, getString(com.yzi.doutu.R.string.item_clicked), 0);
        Intent intent=new Intent(getActivity(), RealManInfoListActivity.class);
        intent.putExtra("id",realList.get(position).getId());
        intent.putExtra("name",realList.get(position).getName());
        intent.putExtra("url",realList.get(position).getPicPath());
        if(Build.VERSION.SDK_INT>=21){
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()
                    , view, "shareNames").toBundle());
        }else{
            startActivity(intent);
        }


    }

    @Override
    public void onItemLongClick(View view, int position) {
       // SnackbarUtil.show(mRecyclerView,realList.get(position).getName(), 0);
    }
}
