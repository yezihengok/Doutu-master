package com.yzi.doutu.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.yzi.doutu.R;
import com.yzi.doutu.adapter.FragmentPagerAdapter;
import com.yzi.doutu.fragment.CollectionFragment;
import com.yzi.doutu.fragment.DIYFragment;
import com.yzi.doutu.fragment.SearchFragment;
import com.yzi.doutu.fragment.SearchListFragment;
import com.yzi.doutu.interfaces.CommInterface;
import com.yzi.doutu.main.MainActivity;
import com.yzi.doutu.service.DouApplication;
import com.yzi.doutu.utils.HandlerUtil;
import com.yzi.doutu.utils.SharedUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yzh-t105 on 2016/10/8.
 */

public class SearchDialogActivity extends FragmentActivity implements ViewPager.OnPageChangeListener
,View.OnClickListener{
    private android.support.v4.view.ViewPager mViewPager;
    //private android.support.design.widget.TabLayout tabLayout;
    private android.widget.LinearLayout dialoglayout;
    private android.widget.LinearLayout noneLayout;

    SearchFragment searchFragment;
    SearchListFragment searchListFragment;
    CollectionFragment collectionFragment;
    DIYFragment diyFragment;
    //TabLayout.Tab tabA,tabB;

    // TabLayout中的tab标题
    private String[] mTitles={"查找","收藏"};
    // 填充到ViewPager中的Fragment
    private List<Fragment> mFragments;
    // ViewPager的数据适配器
    private FragmentPagerAdapter mViewPagerAdapter;

    private LinearLayout searchLayout,tLayout,collectionLayout,backLayout,diyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DouApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_searchdialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        initView();

        HandlerUtil.runOnUiThreadDelay(new Runnable() {
            @Override
            public void run() {
                noneLayout.setAlpha(1f);
            }
        },200);
    }

    private void initView() {
        searchLayout= (LinearLayout) findViewById(R.id.searchLayout);
        tLayout= (LinearLayout) findViewById(R.id.tLayout);
        collectionLayout= (LinearLayout) findViewById(R.id.collectionLayout);
        backLayout= (LinearLayout) findViewById(R.id.backLayout);

        this.noneLayout = (LinearLayout) findViewById(R.id.noneLayout);
        this.dialoglayout = (LinearLayout) findViewById(R.id.dialog_layout);
       // this.tabLayout = (TabLayout) findViewById(tabLayout);
        this.mViewPager = (ViewPager) findViewById(R.id.vPager);
        diyLayout= (LinearLayout) findViewById(R.id.diyLayout);
        searchLayout.setOnClickListener(this);
        tLayout.setOnClickListener(this);
        collectionLayout.setOnClickListener(this);
        backLayout.setOnClickListener(this);
        diyLayout.setOnClickListener(this);
        searchFragment=new SearchFragment();
        searchListFragment=new SearchListFragment();
        collectionFragment=new CollectionFragment();
        diyFragment=new DIYFragment();
        mFragments=new ArrayList<>();
        mFragments.add(searchFragment);
        mFragments.add(searchListFragment);
        mFragments.add(collectionFragment);
        mFragments.add(diyFragment);
        searchFragment.setSearchListener(new CommInterface.SearchListener() {
            @Override
            public void onClick(String keyWord) {
                mViewPager.setCurrentItem(1);
                searchListFragment.setKeyWord(keyWord);
            }
        });
        // 初始化ViewPager的适配器，并设置给它
        mViewPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), mFragments,mTitles);
        mViewPager.setAdapter(mViewPagerAdapter);
        // 设置ViewPager最大缓存的页面个数
        mViewPager.setOffscreenPageLimit(4);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mViewPager.addOnPageChangeListener(this);
       // tabLayout.setupWithViewPager(mViewPager);
       // tabA = tabLayout.getTabAt(0);
       // tabB = tabLayout.getTabAt(1);
        //设置Tab的图标
       // tabA.setIcon(R.mipmap.taba);
       // tabB.setIcon(R.mipmap.tabb);

        noneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int index=SharedUtils.getInt(null,"TabPosition");
        mViewPager.setCurrentItem(index);
        setTabBg(index);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTabBg(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void finish(){
        //结束前占位部分设置透明
        noneLayout.setAlpha(0f);
       // SharedUtils.putBoolean("", "warn", true);//标记开启
        super.finish();
    }


    public void setTabBg(int position){
        SharedUtils.putInt(null,"TabPosition",position);
        switch (position){
            case 0:
                searchLayout.setBackgroundResource(R.color.aplsh);
                tLayout.setBackgroundResource(R.color.white);
                collectionLayout.setBackgroundResource(R.color.white);
                diyLayout.setBackgroundResource(R.color.white);
                break;
            case 1:
                tLayout.setBackgroundResource(R.color.aplsh);
                searchLayout.setBackgroundResource(R.color.white);
                collectionLayout.setBackgroundResource(R.color.white);
                diyLayout.setBackgroundResource(R.color.white);
                break;
            case 2:
                collectionLayout.setBackgroundResource(R.color.aplsh);
                searchLayout.setBackgroundResource(R.color.white);
                tLayout.setBackgroundResource(R.color.white);
                diyLayout.setBackgroundResource(R.color.white);
                break;
            case 3:
                diyLayout.setBackgroundResource(R.color.aplsh);
                collectionLayout.setBackgroundResource(R.color.white);
                searchLayout.setBackgroundResource(R.color.white);
                tLayout.setBackgroundResource(R.color.white);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.searchLayout:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.tLayout:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.collectionLayout:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.diyLayout:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.backLayout:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }
}
