package com.yzi.doutu.fragment;

import android.graphics.Color;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yzi.doutu.R;
import com.yzi.doutu.share.QQShareManager;
import com.yzi.doutu.share.WechatShareManager;
import com.yzi.doutu.interfaces.CommInterface;
import com.yzi.doutu.utils.CommUtil;
import com.yzi.doutu.utils.SharedUtils;

/**
 * Created by yzh-t105 on 2016/10/8.
 */

public class SearchFragment extends Fragment{

    private android.widget.ImageView searchImg;

    private android.widget.EditText searchEd;

    CommInterface.SearchListener searchListener;
    private LinearLayout ciadang;
    private android.widget.RadioGroup radioGroup;
    private TextView tv_tips,cd_tv;
    RadioButton radioButton1;
    /**0搜索图片  1发送文字只微信好友 2发送文字至朋友圈 **/
    int type=0;
    public void setSearchListener(CommInterface.SearchListener searchListener) {
        this.searchListener = searchListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seatch, container, false);
        this.radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        this.ciadang = (LinearLayout) view.findViewById(R.id.ciadang);
        this.searchImg = (ImageView) view.findViewById(R.id.searchImg);
        this.searchEd = (EditText) view.findViewById(R.id.searchEd);
        tv_tips= (TextView) view.findViewById(R.id.tv_tips);
        cd_tv= (TextView) view.findViewById(R.id.cd_tv);
        radioButton1= (RadioButton) view.findViewById(R.id.radioButton1);
        initView(view);
        return  view;
    }

    private void initView(View view) {
        this.searchEd = (EditText) view.findViewById(R.id.searchEd);
        this.searchImg = (ImageView) view.findViewById(R.id.searchImg);
        searchImg.setImageResource(R.drawable.search_anim);
        AnimationDrawable animationDrawable = (AnimationDrawable) searchImg.getDrawable();
        animationDrawable.start();
        String str=SharedUtils.getString(null,"keyWord","");
        searchEd.setText(str);
        searchEd.setSelection(str.length());
        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword=searchEd.getText().toString();
                if(TextUtils.isEmpty(keyword)){
                    CommUtil.showToast("骚年,没输入文字就不要点我了");
                }else{

                    if(type==0){
                         //分享文字到QQ
                        if("on".equals(cd_tv.getTag())){
                            QQShareManager.getInstance(getActivity()).onClickShare(keyword);
                        }else{
                            if(searchListener!=null)
                                searchListener.onClick(keyword);
                        }


                    //分享纯文字到微信
                    }else if(type==1){
                        WechatShareManager.getInstance(getActivity()).shareText(keyword,WechatShareManager.WECHAT_SHARE_TYPE_TALK);
                    }else if(type==2){
                        WechatShareManager.getInstance(getActivity()).shareText(keyword,WechatShareManager.WECHAT_SHARE_TYPE_FRENDS);
                    }

                    SharedUtils.putString(null,"keyWord",keyword);
                }

            }
        });
        cd_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CommUtil.WeChat.equals(CommUtil.FLAG)){
                    if(radioGroup.getVisibility()==View.GONE){
                        radioGroup.setVisibility(View.VISIBLE);
                        radioButton1.setChecked(true);
                        cd_tv.setTextColor(Color.parseColor("#00afec"));
                    }else{
                        radioGroup.setVisibility(View.GONE);
                        tv_tips.setText("搜索相关文字的表情");
                        cd_tv.setTextColor(Color.parseColor("#6D6D6D"));
                        radioGroup.clearCheck();

                        type=0;
                    }

                }else if(CommUtil.QQ.equals(CommUtil.FLAG)){
                    if("off".equals(cd_tv.getTag())){
                        cd_tv.setTag("on");
                        tv_tips.setText("发送带尾巴文字给好友");
                        cd_tv.setTextColor(Color.parseColor("#00afec"));
                    }else {
                        cd_tv.setTag("off");
                        tv_tips.setText("搜索相关文字的表情");
                        cd_tv.setTextColor(Color.parseColor("#6D6D6D"));
                    }
                }



            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton1:
                        type=1;
                        tv_tips.setText("发送带尾巴文字给好友");
                        break;
                    case R.id.radioButton2:
                        type=2;
                        tv_tips.setText("发送带尾巴文字到朋友圈");
                        break;
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if(CommUtil.WeChat.equals(CommUtil.FLAG) ||CommUtil.QQ.equals(CommUtil.FLAG)){
            ciadang.setVisibility(View.VISIBLE);
        }else{
            ciadang.setVisibility(View.GONE);
        }

        if(CommUtil.QQ.equals(CommUtil.FLAG)){
            cd_tv.setTag("off");
        }
    }
}
