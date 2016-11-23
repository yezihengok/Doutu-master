package com.yzi.doutu.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yzi.doutu.R;
import com.yzi.doutu.adapter.HotListAdapter;
import com.yzi.doutu.bean.DataBean;
import com.yzi.doutu.db.DBTools;
import com.yzi.doutu.utils.CommInterface;
import com.yzi.doutu.utils.CommUtil;

import java.util.ArrayList;
import java.util.List;

/**我的收藏
 * Created by yzh-t105 on 2016/9/22.
 */
public class MyFavoritesActivity extends BaseActivity implements CommInterface.OnItemClickListener
,View.OnClickListener{

    private XRecyclerView mRecyclerView;
    private HotListAdapter mAdapter;

    private int hotPage = 0;
    private List<DataBean> beanList; //
    private TextView tvRight;
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
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setPullRefreshEnabled(false);
        beanList=new ArrayList<>();
        mAdapter = new HotListAdapter(this,beanList,1,null);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        getFavorites();
    }


    public void getFavorites() {
        List<DataBean> favorites= DBTools.getInstance().getFavorites(false);
        if(favorites!=null&&!favorites.isEmpty()){
            beanList=new ArrayList<>(favorites);
            mAdapter.setHotList(beanList);
            mAdapter.notifyDataSetChanged();
        }

    }



    @Override
    public void onItemClick(View view, int position) {
        beanList.get(position).setFormWhere("Favorites");
        CommUtil.getInstance().showSharePop(this, beanList.get(position), new CommInterface.setFinishListener() {
            @Override
            public void onFinish() {
                getFavorites();
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
