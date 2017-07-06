

package com.yzi.doutu.main;

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

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yzi.doutu.R;
import com.yzi.doutu.activity.AllpicInfoActivity;
import com.yzi.doutu.adapter.AllListAdapter;
import com.yzi.doutu.bean.AllPic;
import com.yzi.doutu.bean.Theme;
import com.yzi.doutu.db.DBTools;
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
import java.util.Random;

import okhttp3.Call;

/**主题表情包
 * Created by yzh on 2016/11/24.
 */
public class AllListFragment extends Fragment
        implements CommInterface.OnItemClickListener{

    private View mView;
    private XRecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private AllListAdapter listBeenAdapter;

    private int hotPage =0;
    private List<AllPic.ListBean> listBeen;

    private int lastId;//最后一个id
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

        listBeen=new ArrayList<>();
        listBeenAdapter = new AllListAdapter(getActivity(),listBeen);
        listBeenAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(listBeenAdapter);
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
                getlistBeen(hotPage);
            }
            @Override
            public void onLoadMore() {
                getlistBeen(hotPage);
            }
        });
        mRecyclerView.setRefreshing(true);
    }


    public void getlistBeen(final int page) {
        mRecyclerView.setRefreshProgressStyle(new Random().nextInt(28));
        mRecyclerView.setLoadingMoreProgressStyle(new Random().nextInt(28));
        Log.v("", "getlistBeen");
        Map<String, String> params =new LinkedHashMap<>();
        if(hotPage>0){
            params.put("last_id",String.valueOf(lastId));
        }
        OkHttpUtils
                .get()
                .headers(((MainActivity)getActivity()).getHeaders())
                .url(CommUtil.ALLPIC)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("", e.toString());
                        CommUtil.closeWaitDialog();
                        refreshComplete(page);
                        if(hotPage<1){
                            setData((AllPic) SharedUtils.getObject("allPic",getActivity()),page);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("", response);
                        refreshComplete(page);
                        CommUtil.closeWaitDialog();
                        AllPic allPic = PraseUtils.parseJsons(response, AllPic.class);
                        setData(allPic, page);
                    }
                });
    }

    private void setData(AllPic allPic, int page) {
        if (allPic != null) {
            if (allPic.getList() != null) {
                if (page==0){
                    listBeen.clear();
                }

                if(allPic.getLast_id()!=0){
                    lastId=allPic.getLast_id();
                }

                listBeen.addAll(allPic.getList());

                Log.d("", "listBeen.size():" + listBeen.size());
                listBeenAdapter.notifyDataSetChanged();
            }else{
                CommUtil.showToast("没有更多了");
            }
            if(hotPage==0){
                SharedUtils.putObject("allPic",allPic,getActivity());
            }
            hotPage++;
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



        Intent intent=new Intent(getActivity(), AllpicInfoActivity.class);
        intent.putExtra("listBean",listBeen.get(position));
        startActivity(intent);
       // CommUtil.getInstance().toAddText( listBeen.get(position),getActivity(),null);
        //CommUtil.getInstance().showSharePop(getActivity(), listBeen.get(position));
    }

    @Override
    public void onItemLongClick(View view, final int position) {
        CommUtil.showDialog(getContext(), "收藏该主题", "取消", "确定"
                , null, new CommInterface.setClickListener() {
            @Override
            public void onResult() {
                AllPic.ListBean bean=listBeen.get(position);
                Theme theme=new Theme();
                theme.setUserId(String.valueOf(bean.getUser().getId()));
                theme.setFolderId(String.valueOf(bean.getFolder().getId()));
                theme.setFolderName(bean.getFolder().getName());
                List<String> thumbs=bean.getThumbs();
                if(thumbs!=null){
                    StringBuffer buffer=new StringBuffer("");
                    for (int i=0;i<thumbs.size();i++){
                        buffer.append(thumbs.get(i));
                        buffer.append(",");
                    }
                    theme.setThumbs(buffer.toString());
                }
                DBTools.getInstance().addThemes(theme);
                CommUtil.showToast("已收藏");

            }
        });
    }
}
