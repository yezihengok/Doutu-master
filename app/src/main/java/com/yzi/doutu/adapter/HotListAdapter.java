package com.yzi.doutu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pizidea.imagepicker.UilImagePresenter;
import com.yzi.doutu.R;
import com.yzi.doutu.bean.DataBean;
import com.yzi.doutu.interfaces.CommInterface;
import com.yzi.doutu.utils.CommUtil;

import java.util.List;


public class HotListAdapter extends RecyclerView.Adapter<HotListAdapter.ViewHolder> {


    public CommInterface.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(CommInterface.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public Context mContext;
    public List<DataBean> hotList;
    public LayoutInflater mLayoutInflater;
    UilImagePresenter presenter;
    String dataform;

    boolean justShowBitmap=false;
    /**只显示静态图片**/
    public void setJustShowBitmap(boolean justShowBitmap) {
        this.justShowBitmap = justShowBitmap;
    }

    public void setHotList(List<DataBean> hotList) {
        this.hotList = hotList;
    }

    int itemWidth=3;//item宽度 默认为屏幕1/3

    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
    }

    int flag;
    public HotListAdapter(Context mContext, List<DataBean> hotList) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        presenter=new UilImagePresenter();
        this.hotList =hotList;
    }

    /**1 不显示文字的布局**/
    public HotListAdapter(Context mContext, List<DataBean> hotList,int flag,String dataform) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        presenter=new UilImagePresenter();
        this.hotList =hotList;
        this.flag=flag;
        this.dataform=dataform;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_main, viewGroup, false);

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
        lp.height = CommUtil.getScreenWidth()/itemWidth-CommUtil.dip2px(8);
        if(flag!=1){
            holder.mTextView.setText(hotList.get(position).getName());
        }else{
            holder.mTextView.setVisibility(View.GONE);
        }
        holder.img.setLayoutParams(lp);


        String url=hotList.get(position).getGifPath();
        if("showMade".equals(dataform)){
            url=hotList.get(position).getMadeUrl();
        }
        if (CommUtil.isGif(url)&&!justShowBitmap) {
            presenter.displayGif(holder.img,url,CommUtil.getScreenWidth()/3);
        }else{
            presenter.displayImg(holder.img,url,CommUtil.getScreenWidth()/3);
        }

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
        public LinearLayout rootLayouts;
        public ViewHolder(View view){
            super(view);
            mTextView = (TextView) view.findViewById(R.id.name);
            img= (ImageView) itemView.findViewById(R.id.img);
            rootLayouts= (LinearLayout) itemView.findViewById(R.id.rootLayouts);
        }
    }
}
