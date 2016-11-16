/*
 *
 *  *
 *  *  *
 *  *  *  * ===================================
 *  *  *  * Copyright (c) 2016.
 *  *  *  * 作者：安卓猴
 *  *  *  * 微博：@安卓猴
 *  *  *  * 博客：http://sunjiajia.com
 *  *  *  * Github：https://github.com/opengit
 *  *  *  *
 *  *  *  * 注意**：如果您使用或者修改该代码，请务必保留此版权信息。
 *  *  *  * ===================================
 *  *  *
 *  *  *
 *  *
 *
 */

package com.yzi.doutu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.yzi.doutu.R;

import java.util.List;

/**
 * Created by Monkey on 2015/6/29.
 */
public class RealAdapter extends RecyclerView.Adapter<RealAdapter.ViewHolder> {


  public Context mContext;
  public List<String> mDatas;
  public LayoutInflater mLayoutInflater;

  public RealAdapter(Context mContext, List<String> mDatas) {
    this.mContext = mContext;
    mLayoutInflater = LayoutInflater.from(mContext);
    this.mDatas =mDatas;
  }


  /**
   * 创建ViewHolder
   */
  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View mView = mLayoutInflater.inflate(R.layout.test, parent, false);
    ViewHolder mViewHolder = new ViewHolder(mView);
    return mViewHolder;
  }

  /**
   * 绑定ViewHoler，给item中的控件设置数据
   */
  @Override public void onBindViewHolder(final ViewHolder holder, final int position) {


    Log.e("",mDatas.get(position));
    holder.mTextView.setText(mDatas.get(position));

  }

  @Override public int getItemCount() {
    return mDatas==null?0:mDatas.size();
  }

  //自定义的ViewHolder，持有每个Item的的所有界面元素
  public static class ViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextView;
    public ViewHolder(View view){
      super(view);
      mTextView = (TextView) view.findViewById(R.id.text1);
    }
  }
}
