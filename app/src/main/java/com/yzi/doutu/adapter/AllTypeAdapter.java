package com.yzi.doutu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pizidea.imagepicker.ImagePresenter;
import com.pizidea.imagepicker.UilImagePresenter;
import com.yzi.doutu.R;
import com.yzi.doutu.activity.TypeTemplateListActivity;
import com.yzi.doutu.bean.AllType;
import com.yzi.doutu.utils.CommInterface;
import com.yzi.doutu.utils.CommUtil;

import java.util.List;


public class AllTypeAdapter extends RecyclerView.Adapter<AllTypeAdapter.ViewHolder> {


    public CommInterface.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(CommInterface.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public Context mContext;
    public List<AllType.DataBean.TagListBean> hotList;
    public LayoutInflater mLayoutInflater;
    ImagePresenter presenter;

    public AllTypeAdapter(Context mContext, List<AllType.DataBean.TagListBean> hotList) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        presenter=new UilImagePresenter();
        this.hotList =hotList;
    }
    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.alltype_item, viewGroup, false);

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


        holder.mTextView.setText(hotList.get(position).getName());
        if(!TextUtils.isEmpty(hotList.get(position).getGifPath())){
            holder.img.setVisibility(View.VISIBLE);
            presenter.onPresentImage(holder.img,hotList.get(position).getGifPath(), CommUtil.getScreenWidth()/3);
        }else{
            holder.img.setVisibility(View.GONE);
        }

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, TypeTemplateListActivity.class);
                intent.putExtra("id",hotList.get(position).getId());
                intent.putExtra("name",hotList.get(position).getName());
                mContext.startActivity(intent);
            }
        });

    }
    //获取数据的数量
    @Override
    public int getItemCount() {
        return hotList==null?0:hotList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView img;
        public LinearLayout rootLayout;
        public ViewHolder(View view){
            super(view);
            mTextView = (TextView) view.findViewById(R.id.name);
            img= (ImageView) itemView.findViewById(R.id.img);
            rootLayout= (LinearLayout) itemView.findViewById(R.id.rootLayout);
        }
    }
}
