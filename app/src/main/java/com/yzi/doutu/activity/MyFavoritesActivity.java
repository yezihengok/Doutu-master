package com.yzi.doutu.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yzi.doutu.R;
import com.yzi.doutu.adapter.HotListAdapter;
import com.yzi.doutu.adapter.HotTemplateAdapter;
import com.yzi.doutu.bean.DataBean;
import com.yzi.doutu.db.DBTools;
import com.yzi.doutu.utils.CommInterface;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.HandlerUtil;

import java.util.ArrayList;
import java.util.List;

/**我的收藏
 * Created by yzh-t105 on 2016/9/22.
 */
public class MyFavoritesActivity extends BaseActivity implements CommInterface.OnItemClickListener
,View.OnClickListener{

    private XRecyclerView mRecyclerView;
    private HotTemplateAdapter mAdapter;

    private List<DataBean> beanList; //
    private TextView tvRight;
    int ITEM=4;//item个数
    int COUNT=30;//分页加载条数
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application.addActivity(this);
        setContentView(R.layout.activity_diy);
        initView();
    }

    private void initView() {

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.tvtitle)).setText("我的收藏");
        tvRight= (TextView) findViewById(R.id.tvRight);
        tvRight.setText("清空");

        mRecyclerView = (XRecyclerView)this.findViewById(R.id.xrecyclerview);

        tvRight.setOnClickListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this,ITEM);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setPullRefreshEnabled(true);
        beanList=new ArrayList<>();
       // mAdapter = new HotListAdapter(this,beanList,1,null);
        mAdapter = new HotTemplateAdapter(this,beanList,ITEM);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

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
            favorites= DBTools.getInstance().getFavorites(0,COUNT);
            setData(isrefresh);
        }else{
            HandlerUtil.runOnUiThreadDelay(new Runnable() {
                @Override
                public void run() {
                    favorites= DBTools.getInstance().getFavorites(beanList.size(),beanList.size()+COUNT);
                    setData(isrefresh);
                }
            },300);
        }


    }

    private void setData(boolean isrefresh) {
        if(favorites!=null&&!favorites.isEmpty()){
            beanList.addAll(favorites);
            mAdapter.setHotList(beanList);

        }else{
            mAdapter.setHotList(null);
            CommUtil.showToast("没有更多了");
        }
        mAdapter.notifyDataSetChanged();
        if(isrefresh){
            mRecyclerView.refreshComplete();
        }else{
            mRecyclerView.loadMoreComplete();
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        beanList.get(position).setFormWhere("Favorites");
        CommUtil.getInstance().showSharePop(this, beanList.get(position), new CommInterface.setFinishListener() {
            @Override
            public void onFinish() {
                getFavorites(true);
            }
        });
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.tvRight:
                    CommUtil.showDialog(MyFavoritesActivity.this, "你要清空所有收藏的图片吗？", "是的", "不了"
                            , new CommInterface.setClickListener() {
                        @Override
                        public void onResult() {
                            DBTools.getInstance().removeAll();
                            mAdapter.setHotList(null);
                            mAdapter.notifyDataSetChanged();
                        }
                    },null);
                    break;
            }
    }
}
