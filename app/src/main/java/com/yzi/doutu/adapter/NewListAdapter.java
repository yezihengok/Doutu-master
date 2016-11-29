

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
import com.yzi.doutu.utils.CommUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzi on 2016/9/19.
 */
public class NewListAdapter extends RecyclerView.Adapter<NewListAdapter.NewListHolder> {


  public CommInterface.OnItemClickListener mOnItemClickListener;

  public void setOnItemClickListener(CommInterface.OnItemClickListener listener) {
    this.mOnItemClickListener = listener;
  }

  public Context mContext;
  public List<DataBean> mDatas=new ArrayList<>();
  public List<Integer> mHeights=new ArrayList<>();
  public LayoutInflater mLayoutInflater;

  ImagePresenter presenter;
  public NewListAdapter(Context mContext, List<DataBean> mDatas) {
    this.mContext = mContext;
    mLayoutInflater = LayoutInflater.from(mContext);
    presenter=new UilImagePresenter();
    this.mDatas =mDatas;

  }

  public void setmDatas(List<DataBean> mDatas) {
    this.mDatas = mDatas;
    if(mDatas!=null&&mDatas.size()>0){
      // 随机高度, 模拟瀑布效果.
      for (int i = 0; i < mDatas.size(); i++) {
        mHeights.add((int) (Math.random() * CommUtil.dip2px(30)) + CommUtil.dip2px(70));
      }
    }

    notifyDataSetChanged();
  }
  /**
   * 创建ViewHolder
   */
  @Override public NewListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View mView = mLayoutInflater.inflate(R.layout.newlist_item, parent, false);
    NewListHolder mViewHolder = new NewListHolder(mView);
    return mViewHolder;
  }

  /**
   * 绑定ViewHoler，给item中的控件设置数据
   */
  @Override public void onBindViewHolder(final NewListHolder holder, final int position) {
    //CommUtil.showToast("onBindViewHolder");
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

    ViewGroup.LayoutParams lp = holder.img.getLayoutParams();
    lp.height = mHeights.get(position);
    holder.img.setLayoutParams(lp);

    holder.mTextView.setText(mDatas.get(position).getName());



    if (mDatas.get(position).getGifPath().endsWith("gif") ||
            mDatas.get(position).getGifPath().endsWith("GIF")) {
      ((UilImagePresenter)presenter).displayGif(holder.img,mDatas.get(position).getGifPath(),CommUtil.getScreenWidth()/2);
    }else{
      presenter.onPresentImage(holder.img,mDatas.get(position).getPicPath(),CommUtil.getScreenWidth()/2);
    }

    //列表加载太多的GIF会oom ,这里还是显示jpg,分享发送时在显示gif

  }


  public class NewListHolder extends RecyclerView.ViewHolder {

    public TextView mTextView,name;
    public ImageView img;
    public NewListHolder(View itemView) {
      super(itemView);
      mTextView = (TextView) itemView.findViewById(R.id.name);
      img= (ImageView) itemView.findViewById(R.id.img);
    }


  }
  
  @Override public int getItemCount() {
    return mDatas==null?0:mDatas.size();
  }
}
