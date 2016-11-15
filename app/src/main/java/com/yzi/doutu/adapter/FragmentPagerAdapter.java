package com.yzi.doutu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 避免viewpager切换时重复加载Fragment 的PagerAdapter
 * Created by yzh-t105 on 2016/9/2.
 */

public class FragmentPagerAdapter extends PagerAdapter {
    private List<Fragment> fragments;
    private FragmentManager manager;
    private String[] mTitles;
    public FragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments,String[] mTitles) {
        super();
        manager = fm;
        this.fragments = fragments;
        this.mTitles=mTitles;
    }


    @Override
    public int getCount() {
        return fragments.size();
    }

    //重写 Fragment进行管理，让FragmentViewPager不执行移除原先的Fragment。
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = fragments.get(position);
        //判断当前的fragment是否已经被添加进入Fragmentanager管理器中
        if (!fragment.isAdded()) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(fragment, fragment.getClass().getSimpleName());
            //不保存系统参数，自己控制加载的参数
            transaction.commitAllowingStateLoss();
            //手动调用,立刻加载Fragment片段
            manager.executePendingTransactions();
        }
        if (fragment.getView().getParent() == null) {
            //添加布局
            container.addView(fragment.getView());
        }
        return fragment.getView();
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //移除布局
        container.removeView(fragments.get(position).getView());
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    //如果 设置了 viewpager  标题  tabLayout 显示的就是 viewpager标题
    @Override
    public CharSequence getPageTitle(int position) {
        // return "title";
        return mTitles[position];
    }

}


