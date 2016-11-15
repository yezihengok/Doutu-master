package com.yzi.doutu.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.yzi.doutu.R;

public class LoadDialog extends Dialog {

    private TextView tvMessage;
    private String msg="";
    private Boolean cancelAble=true;
	public LoadDialog(Context context, String msg, Boolean cancelAble) {
		super(context, R.style.comm_load_dialog);
		
		this.msg=msg;
		this.cancelAble=cancelAble;
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comm_load_dialog);
		tvMessage=(TextView)this.findViewById(R.id.tvLoadDialog_Message);


		if(TextUtils.isEmpty(msg))
		{
			tvMessage.setVisibility(View.GONE);
		}
		else
		{
			tvMessage.setVisibility(View.VISIBLE);
			tvMessage.setText(msg);
            if(msg.length()>3){
                //if(msg.equals(CommentUtils.getString("R.string.content_loading"))){
                    JumpingBeans jump = JumpingBeans.with(tvMessage)
                            .makeTextJump( msg.length()-3, msg.length())
                            .setIsWave(true)
                            .setLoopDuration(1500)
                            .build();
                //}

            }
		}

	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (!cancelAble)
			return false;
		return super.onTouchEvent(event);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			Utils.log( "LoadDialog:KEYCODE_BACK");
			if(!cancelAble)return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
