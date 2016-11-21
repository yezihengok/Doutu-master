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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pizidea.imagepicker.ImagePresenter;
import com.pizidea.imagepicker.UilImagePresenter;
import com.yzi.doutu.R;
import com.yzi.doutu.bean.DataBean;
import com.yzi.doutu.utils.CommInterface;

import java.util.List;

/**
 * Created by yzh on 2015/6/29.
 */
public class RealmanAdapter extends RecyclerView.Adapter<RealmanAdapter.ViewHolder> {


  public CommInterface.OnItemClickListener mOnItemClickListener;

  public void setOnItemClickListener(CommInterface.OnItemClickListener listener) {
    this.mOnItemClickListener = listener;
  }

  public Context mContext;
  public List<DataBean> mDatas;
  public LayoutInflater mLayoutInflater;
  UilImagePresenter presenter;
  public void setmDatas(List<DataBean> mDatas) {
    this.mDatas = mDatas;
  }

  public RealmanAdapter(Context mContext, List<DataBean> mDatas) {
    this.mContext = mContext;
    mLayoutInflater = LayoutInflater.from(mContext);
    presenter=new UilImagePresenter();
    this.mDatas =mDatas;
  }

  /**
   * 创建ViewHolder
   */
  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View mView = mLayoutInflater.inflate(R.layout.realman_item, parent, false);
    ViewHolder mViewHolder = new ViewHolder(mView);
    return mViewHolder;
  }

  /**
   * 绑定ViewHoler，给item中的控件设置数据
   */
  @Override public void onBindViewHolder(final ViewHolder holder, final int position) {
    if (mOnItemClickListener != null) {
      holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          mOnItemClickListener.onItemClick(holder.itemView, position);
        }
      });

      holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override public boolean onLongClick(View v) {
          mOnItemClickListener.onItemLongClick(holder.itemView, position);
          return true;
        }
      });
    }


    holder.mTextView.setText(mDatas.get(position).getName());
    presenter.onPresentImage(holder.img,mDatas.get(position).getPicPath());

  }

  @Override public int getItemCount() {
    return mDatas==null?0:mDatas.size();
  }

  //自定义的ViewHolder，持有每个Item的的所有界面元素
  public static class ViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextView;
    public ImageView img;
    public ViewHolder(View view){
      super(view);
      mTextView = (TextView) view.findViewById(R.id.name);
      img= (ImageView) itemView.findViewById(R.id.img);
    }
  }
}
