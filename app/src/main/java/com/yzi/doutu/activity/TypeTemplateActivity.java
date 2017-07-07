package com.yzi.doutu.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzi.doutu.R;
import com.yzi.doutu.adapter.AllTypeAdapter;
import com.yzi.doutu.bean.AllType;
import com.yzi.doutu.interfaces.CommInterface;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.PraseUtils;
import com.yzi.doutu.utils.RecycleViews.FullyGridLayoutManager;
import com.yzi.doutu.utils.RecycleViews.SpaceItemDecorations;
import com.yzi.doutu.utils.SharedUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**表情分类
 * Created by yzh-t105 on 2016/9/22.
 */
public class TypeTemplateActivity extends BaseActivity implements CommInterface.OnItemClickListener{

    private android.widget.ImageView back;
    private TextView tvtitle;
    private android.widget.RelativeLayout titlet;
    private android.widget.LinearLayout maincontent;
    LayoutInflater inflater;
    private RecyclerView.LayoutManager mLayoutManager;
    List<AllType.DataBean> beanList;
    private Context context;
    int time=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moretype);
        context=this;
        application.addActivity(this);
        this.maincontent = (LinearLayout) findViewById(R.id.main_content);
        this.titlet = (RelativeLayout) findViewById(R.id.titlet);
        this.tvtitle = (TextView) findViewById(R.id.tvtitle);
        this.back = (ImageView) findViewById(R.id.back);

        this.typeName = (TextView) findViewById(R.id.typeName);
        inflater=LayoutInflater.from(TypeTemplateActivity.this);
        beanList=new ArrayList<>();
        ((TextView)findViewById(R.id.tvtitle)).setText("模板分类");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(beanList.size()==0){
            getList(true);
        }
    }

    public void getList(boolean showDialog) {
        Log.v("", "getList");
        if (showDialog) {
            CommUtil.showWaitDialog(this, "加载中...", true);
        }

        OkHttpUtils
                .get()
                .url(CommUtil.ALLTYPE)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("", e.toString());
                        CommUtil.closeWaitDialog();
                        if(time<1){
                            setData((AllType) SharedUtils.getObject("allType",context));
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("", response);
                        CommUtil.closeWaitDialog();
                        AllType allType = PraseUtils.parseJsons(response, AllType.class);
                        if (allType != null) {
                            setData(allType);
                        }

                    }
                });
    }

    private void setData(AllType allType) {
        if(allType==null||allType.getData()==null){
            return;
        }
        beanList=allType.getData();

        if (beanList != null&&beanList.size()>0) {

            //分类还到第一位置
            AllType.DataBean dataBean=beanList.get(beanList.size()-1);
            beanList.remove(beanList.get(beanList.size()-1));
            beanList.add(0,dataBean);
            //Collections.shuffle(beanList);//随机打乱一下list顺序
            for (int i = 0; i <beanList.size() ; i++) {
               View v=inflater.inflate(R.layout.activity_moretype_item,null);
                addtype(beanList.get(i).getDtTypeModel(), v,i);
                maincontent.addView(v);
            }
        }

        if(time==0){
            SharedUtils.putObject("allType",allType,context);
        }
        time++;
    }

    private RecyclerView mRecyclerView;
    private TextView typeName;
    private void addtype(AllType.DataBean.DtTypeModelBean typeBean, View v,int position) {

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        typeName = (TextView) v.findViewById(R.id.typeName);
        if("其它".equals(typeBean.getName())){
            setTextValues(typeName,"分类");
        }else{
            setTextValues(typeName,typeBean.getName());
        }


        mLayoutManager = new FullyGridLayoutManager(this,3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);// 设置是否允许嵌套滑动，嵌套设置false，不然卡顿
        //注意 使用 recyclerview-v7:23.2.1' 23版本的 ，直接设置true 使用自带的LinearLayoutManager 就可以自适应高度了
        //然而我使用 24版本编译的时候，却无效了，需要setAutoMeasureEnabled 设置为false+ 自定义FullyLinearLayoutManager
        //才能正常的嵌套
        mLayoutManager.setAutoMeasureEnabled(false);

        mRecyclerView.addItemDecoration(new SpaceItemDecorations(2));
        List<AllType.DataBean.TagListBean> tagLis=beanList.get(position).getTagList();
        if(tagLis!=null&&tagLis.size()>0){
            AllTypeAdapter realListAdapter = new AllTypeAdapter(this,tagLis);
            realListAdapter.setOnItemClickListener(this);
            mRecyclerView.setAdapter(realListAdapter);
        }

    }


    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
