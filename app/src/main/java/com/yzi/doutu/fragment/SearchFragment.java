package com.yzi.doutu.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yzi.doutu.R;
import com.yzi.doutu.utils.CommInterface;
import com.yzi.doutu.utils.CommUtil;

import static com.yzi.doutu.R.id.imageView;

/**
 * Created by yzh-t105 on 2016/10/8.
 */

public class SearchFragment extends Fragment{

    private android.widget.ImageView searchImg;
    private LinearLayout mainfram;
    private android.widget.EditText searchEd;
    private String keyword;
    CommInterface.SearchListener searchListener;

    public void setSearchListener(CommInterface.SearchListener searchListener) {
        this.searchListener = searchListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seatch, container, false);
        initView(view);
        return  view;
    }

    private void initView(View view) {
        this.searchEd = (EditText) view.findViewById(R.id.searchEd);
        this.mainfram = (LinearLayout) view.findViewById(R.id.main_fram);
        this.searchImg = (ImageView) view.findViewById(R.id.searchImg);
        searchImg.setImageResource(R.drawable.search_anim);
        AnimationDrawable animationDrawable = (AnimationDrawable) searchImg.getDrawable();
        animationDrawable.start();

        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword=searchEd.getText().toString();
                if(TextUtils.isEmpty(keyword)){
                    CommUtil.showToast("骚年,没输入文字就不要点我了");
                }else{
                    if(searchListener!=null)
                    searchListener.onClick(keyword);
                }

            }
        });
    }


}
