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
import com.yzi.doutu.bean.Theme;
import com.yzi.doutu.interfaces.CommInterface;
import com.yzi.doutu.utils.CommUtil;

import java.util.List;

/**
 * 收藏的主题列表adapter
 */
public class AllThemeListAdapter extends RecyclerView.Adapter<AllThemeListAdapter.ViewHolder> {


    public CommInterface.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(CommInterface.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public Context mContext;
    public List<Theme> list;
    public LayoutInflater mLayoutInflater;
    UilImagePresenter presenter;


    public void setlist(List<Theme> list) {
        this.list = list;
    }

    int itemWidth = 3;//item宽度 默认为屏幕1/3

    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
    }

    public AllThemeListAdapter(Context mContext, List<Theme> list) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        presenter = new UilImagePresenter();
        this.list = list;
    }


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.alllist_item, viewGroup, false);

        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });
        }

      //  ViewGroup.LayoutParams lp = holder.img1.getLayoutParams();
        int height=CommUtil.getScreenWidth() / itemWidth - CommUtil.dip2px(20);
 //       lp.height = CommUtil.getScreenWidth() / itemWidth - CommUtil.dip2px(30);
//        holder.img1.setLayoutParams(lp);

        holder.mTextView.setText(list.get(position).getFolderName());

        String []thumes=list.get(position).getThumbs().split(",");
        if (thumes!= null && thumes.length>0) {
            for (int i = 0; i <thumes.length ; i++) {
                //holder.imgViews[i].setLayoutParams(lp);
                presenter.displayImg(holder.imgViews[i], thumes[i],height);
            }
        }

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView img1,img2,img3,img4;
        public LinearLayout rootLayouts;
        public ImageView[] imgViews;
        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.name);
            img1 = (ImageView) itemView.findViewById(R.id.img1);
            img2 = (ImageView) itemView.findViewById(R.id.img2);
            img3 = (ImageView) itemView.findViewById(R.id.img3);
            img4 = (ImageView) itemView.findViewById(R.id.img4);
            imgViews=new ImageView[]{img1,img2,img3,img4};
            rootLayouts = (LinearLayout) itemView.findViewById(R.id.rootLayouts);
        }
    }
}
