package com.yzi.doutu.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yzi.doutu.R;
import com.yzi.doutu.adapter.AllThemeListAdapter;
import com.yzi.doutu.bean.Theme;
import com.yzi.doutu.db.DBHelpers;
import com.yzi.doutu.db.DBTools;
import com.yzi.doutu.interfaces.CommInterface;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.HandlerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.yzi.doutu.utils.CommUtil.DETAIL;

/**我的收藏
 * Created by yzh-t105 on 2017/4/27.
 */
public class MyThemeFavoritesActivity extends BaseActivity implements CommInterface.OnItemClickListener
,View.OnClickListener{

    private XRecyclerView mRecyclerView;
    private AllThemeListAdapter mAdapter;

    private List<Theme> beanList; //
    private TextView tvRight;
    int ITEM=2;//item个数
    int COUNT=8;//分页加载条数
    private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application.addActivity(this);
        context=this;
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
        ((TextView)findViewById(R.id.tvtitle)).setText("收藏的主题");
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
        mAdapter = new AllThemeListAdapter(this,beanList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                HandlerUtil.runOnUiThreadDelay(new Runnable() {
                    @Override
                    public void run() {
                        getFavorites(true);
                    }
                },DETAIL);

            }
            @Override
            public void onLoadMore() {
                HandlerUtil.runOnUiThreadDelay(new Runnable() {
                    @Override
                    public void run() {
                        getFavorites(false);
                    }
                },DETAIL);

            }
        });
        mRecyclerView.setRefreshing(true);
    }

    List<Theme> favorites;
    /**
     * 获取我的收藏数据
     * @param isrefresh 是否为初始刷新，否则视为上拉加载
     */
    public void getFavorites(final boolean isrefresh) {
        mRecyclerView.setRefreshProgressStyle(new Random().nextInt(28));
        mRecyclerView.setLoadingMoreProgressStyle(new Random().nextInt(28));
        favorites=new ArrayList<>();
        if(isrefresh){
            beanList.clear();
            favorites= DBTools.getInstance().getThemes(0,COUNT);
            setData(isrefresh);
        }else{
            favorites= DBTools.getInstance().getThemes(beanList.size(),beanList.size()+COUNT);
            setData(isrefresh);

        }


    }

    private void setData(boolean isrefresh) {
        if(favorites!=null&&!favorites.isEmpty()){
            beanList.addAll(favorites);
        }else{
            CommUtil.showToast("没有更多了");
        }
        mAdapter.setlist(beanList);
        mAdapter.notifyDataSetChanged();
        if(isrefresh){
            mRecyclerView.refreshComplete();
        }else{
            mRecyclerView.loadMoreComplete();
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        Intent intent=new Intent(MyThemeFavoritesActivity.this, AllpicInfoActivity.class);
        intent.putExtra("theme",beanList.get(position));
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view,final int position) {
        CommUtil.showDialog(context, "取消收藏该主题", "确定", "取消"
                , new CommInterface.setClickListener() {
                    @Override
                    public void onResult() {

                        DBTools.getInstance().deleteById(String.valueOf(beanList.get(position).getId()), DBHelpers.TABLE_THEME);
                        getFavorites(true);
                    }
                }, null);
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.tvRight:
                    CommUtil.showDialog(MyThemeFavoritesActivity.this, "你要清空所有收藏的图片吗？", "是的", "不了"
                            , new CommInterface.setClickListener() {
                        @Override
                        public void onResult() {
                            DBTools.getInstance().deleteAll(DBHelpers.TABLE_THEME);
                            mAdapter.setlist(null);
                            mAdapter.notifyDataSetChanged();
                        }
                    },null);
                    break;
            }
    }
}
