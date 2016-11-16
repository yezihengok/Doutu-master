package com.yzi.doutu.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import com.yzi.doutu.R;
import com.yzi.doutu.utils.CommUtil;

/**
 * Created by yzh on 2016/11/11
 */
public class AboutActivity extends BaseActivity {

    String version = null;
    TextView tvgithub;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
		application.addActivity(this);
		init();
    }

	private void init() {
		findViewById(R.id.Copyright).setVisibility(View.GONE);
		((TextView)findViewById(R.id.tvtitle)).setText("关于");
		findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView version_val = ((TextView)findViewById(R.id.version_val));

		tvgithub= ((TextView)findViewById(R.id.tvgithub));
		String str=CommUtil.getString(R.string.github);
		SpannableString sp=new SpannableString(str);
		sp.setSpan(new URLSpan("https://github.com/yezihengok"), 10,str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvgithub.setText(sp);
		tvgithub.setMovementMethod(LinkMovementMethod.getInstance());

		try {
            PackageInfo pinfo = getPackageManager().getPackageInfo("com.yzi.doutu", PackageManager.GET_CONFIGURATIONS);
            version = pinfo.versionName;
            version_val.setText("V"+version);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}


