package com.yzi.doutu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yzi.doutu.R;
import com.yzi.doutu.adapter.HotListAdapter;
import com.yzi.doutu.bean.DataBean;
import com.yzi.doutu.db.DBHelpers;
import com.yzi.doutu.db.DBTools;
import com.yzi.doutu.utils.CommInterface;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.HandlerUtil;
import com.yzi.doutu.utils.ImageUtils;
import com.yzi.doutu.utils.SimpleFileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.yzi.doutu.utils.CommUtil.DETAIL;

/**我的制作
 * Created by yzh-t105 on 2016/11/01.
 */
public class MyDIYPicActivity extends BaseActivity implements CommInterface.OnItemClickListener
,View.OnClickListener{

    private XRecyclerView mRecyclerView;
    private HotListAdapter mAdapter;

    private List<DataBean> beanList; //
    private TextView tvRight;

    int ITEM=3;//item个数
    int COUNT=20;//分页加载条数
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
        ((TextView)findViewById(R.id.tvtitle)).setText("我的制作图片");
        tvRight= (TextView) findViewById(R.id.tvRight);
        tvRight.setText("清空");

        mRecyclerView = (XRecyclerView)this.findViewById(R.id.xrecyclerview);

        tvRight.setOnClickListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this,ITEM);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setPullRefreshEnabled(true);
        beanList=new ArrayList<>();
        mAdapter = new HotListAdapter(this,beanList,1,"showMade");
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

    List<DataBean> favorites;
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
            favorites= DBTools.getInstance().getMades(0,COUNT);
            setData(isrefresh);
        }else{
            favorites= DBTools.getInstance().getMades(beanList.size(),beanList.size()+COUNT);
            setData(isrefresh);

        }


    }

    private void setData(boolean isrefresh) {
        if(favorites!=null&&!favorites.isEmpty()){
            beanList.addAll(favorites);
        }else{
            CommUtil.showToast("没有更多了");
        }
        mAdapter.setHotList(beanList);
        mAdapter.notifyDataSetChanged();
        if(isrefresh){
            mRecyclerView.refreshComplete();
        }else{
            mRecyclerView.loadMoreComplete();
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        beanList.get(position).setFormWhere("DIY");
        CommUtil.getInstance().showSharePop(this, beanList.get(position),new CommInterface.setFinishListener() {
            @Override
            public void onFinish() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
                        getFavorites(true);
//                    }
//                });
            }
        });
    }

    @Override
    public void onItemLongClick(View view, int position) {}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x123&&resultCode==RESULT_OK){
            getFavorites(true);
        }
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.tvRight:
                    CommUtil.showDialog(MyDIYPicActivity.this, "你要清空所有制作的图片吗？", "是的", "不了"
                            , new CommInterface.setClickListener() {
                        @Override
                        public void onResult() {
                            DBTools.getInstance().deleteAll(DBHelpers.TABLE_MADE);
                            mAdapter.setHotList(null);
                            mAdapter.notifyDataSetChanged();
                            SimpleFileUtils.delFile(ImageUtils.FILE_DIY_PATH,0,null);
                        }
                    },null);
                    break;
            }
    }
}
