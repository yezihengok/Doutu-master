package com.yzi.doutu.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yzi.doutu.R;
import com.yzi.doutu.adapter.AllTypeAdapter;
import com.yzi.doutu.adapter.HotListAdapter;
import com.yzi.doutu.adapter.KeySeacrhAdapter;
import com.yzi.doutu.bean.DataBean;
import com.yzi.doutu.bean.NewPic;
import com.yzi.doutu.service.DouApplication;
import com.yzi.doutu.utils.CommInterface;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.PraseUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.yzi.doutu.R.id.imageView;
import static com.yzi.doutu.utils.CommUtil.closeWaitDialog;

/**
 * Created by yzh-t105 on 2016/10/8.
 */

public class SearchListFragment extends Fragment implements CommInterface.OnItemClickListener{
    private android.widget.ImageView noDataImg;
    private com.jcodecraeer.xrecyclerview.XRecyclerView mRecyclerView;
    private List<DataBean> searchList;
    private HotListAdapter searchListAdapter;
    int page=0;
    private String keyWord;

    private android.widget.LinearLayout noDataLayout;

   // private WechatShareManager mShareManager;
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
        searchList.clear();
        getsearchList(keyWord);
    }

    int ITEM=4;//每行个数
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        this.noDataLayout = (LinearLayout) view.findViewById(R.id.noDataLayout);

        initView(view);
        return  view;
    }


    private void initView(View view) {
        this.mRecyclerView = (XRecyclerView) view.findViewById(R.id.xrecyclerview);
        this.noDataImg = (ImageView) view.findViewById(imageView);
        noDataImg.setImageResource(R.drawable.show_anim_list);
        AnimationDrawable animationDrawable = (AnimationDrawable) noDataImg.getDrawable();
        animationDrawable.start();

        mRecyclerView = (XRecyclerView) view.findViewById(R.id.xrecyclerview);
        // mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity()
                ,ITEM, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        searchList=new ArrayList<>();
        searchListAdapter = new HotListAdapter(getActivity(),searchList,1);
        searchListAdapter.setItemWidth(ITEM);
        searchListAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(searchListAdapter);
        configRecyclerView();

    }

    private void configRecyclerView() {
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {}
            @Override
            public void onLoadMore() {
                getsearchList(keyWord);
            }
        });
        mRecyclerView.setRefreshing(true);
    }

    public void getsearchList(String key) {
        Log.v("", "getsearchList");

        OkHttpUtils
                .get()
                .url(CommUtil.KEYWORD_SEARCH)
                .addParams("pageNum", String.valueOf(page))
                .addParams("pageSize", "20")
                .addParams("keyWord", key)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("", e.toString());
                        closeWaitDialog();
                        refersh();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("", response);

                        closeWaitDialog();
                        NewPic newPic = PraseUtils.parseJsons(response, NewPic.class);
                        if (newPic != null) {
                            if (newPic.getData() != null) {
                                if (page==0){
                                    searchList.clear();
                                }
                                page++;
                                searchList.addAll(newPic.getData());
                                Log.d("", "searchList.size():" + searchList.size());
                                searchListAdapter.notifyDataSetChanged();
                            }else{
                                mRecyclerView.setLoadingMoreEnabled(false);
                            }

                        }
                        refersh();
                    }
                });
    }


    public void refersh(){
        mRecyclerView.loadMoreComplete();
        if(searchList.size()==0){
            noDataLayout.setVisibility(View.VISIBLE);
        }else{
            noDataLayout.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onItemClick(View view, int position) {


        if(CommUtil.QQ.equals(CommUtil.FLAG)){
            CommUtil.onDownLoad(searchList.get(position),getActivity(),1);
            //分享后，关闭当前dialog
            DouApplication.getInstance().removeAllActivity();
        }else if(CommUtil.WeChat.equals(CommUtil.FLAG)){
            CommUtil.onDownLoad(searchList.get(position),getActivity(),2);
            //分享后，关闭当前dialog
            DouApplication.getInstance().removeAllActivity();
        }



       // onDownLoad(url,getActivity());  //这种是带应用小尾巴的分享

//        CommUtil.getInstance().showSharePop(getActivity(),
//                searchList.get(position).getGifPath(),searchList.get(position).getName());
//        CommUtil.backgroundAlpha( getActivity(),1f);



        //使用微信SDK分享需要微信开发平台申请APPID（注册APP需要填写APP官网地址，臣妾办不到阿..）
//        mShareManager = WechatShareManager.getInstance(getActivity());
//        WechatShareManager.ShareContentPicture scp = (WechatShareManager.ShareContentPicture)
//                mShareManager.getShareContentPicture(R.mipmap.collection_null);
//        mShareManager.shareByWebchat(scp, WechatShareManager.WECHAT_SHARE_TYPE_FRENDS);
    }


    @Override
    public void onItemLongClick(View view, int position) {

    }

}
