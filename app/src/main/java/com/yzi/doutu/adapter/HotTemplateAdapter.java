package com.yzi.doutu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pizidea.imagepicker.UilImagePresenter;
import com.yzi.doutu.R;
import com.yzi.doutu.bean.DataBean;
import com.yzi.doutu.interfaces.CommInterface;
import com.yzi.doutu.utils.CommUtil;

import java.util.List;

/**
 *
 */
public class HotTemplateAdapter extends RecyclerView.Adapter<HotTemplateAdapter.ViewHolder> {

    public CommInterface.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(CommInterface.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setHotList(List<DataBean> hotList) {
        this.hotList = hotList;
    }

    public Context mContext;
    public List<DataBean> hotList;
    public LayoutInflater mLayoutInflater;
    UilImagePresenter presenter;
    int itemW;//一行有多少个tiem
    public HotTemplateAdapter(Context mContext, List<DataBean> hotList,int itemW) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        presenter=new UilImagePresenter();
        this.hotList =hotList;
        this.itemW=itemW;
    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.alltype_items, viewGroup, false);

        return new ViewHolder(view);
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {
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

        lp.height = CommUtil.getScreenWidth()/itemW-CommUtil.dip2px(10);
        holder.img.setLayoutParams(lp);
        String url=hotList.get(position).getGifPath();
       // presenter.displayImg(holder.img,url,CommUtil.getScreenWidth()/3);
        presenter.onPresentImage(holder.img,url,CommUtil.getScreenWidth()/3);

    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return hotList==null?0:hotList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public ViewHolder(View view){
            super(view);
            img= (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
