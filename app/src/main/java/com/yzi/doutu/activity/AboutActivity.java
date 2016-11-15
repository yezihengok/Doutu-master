package com.yzi.doutu.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yzi.doutu.R;

/**
 * Created by yzh on 2016/11/11.
 */
public class AboutActivity extends BaseActivity {

    String version = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
		application.addActivity(this);
		init();
    }

	private void init() {
		findViewById(R.id.Copyright).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.tvtitle)).setText("关于");
		findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView version_val = ((TextView)findViewById(R.id.version_val));

		try {
            PackageInfo pinfo = getPackageManager().getPackageInfo("com.yzi.doutu", PackageManager.GET_CONFIGURATIONS);
            version = pinfo.versionName;
            version_val.setText("V"+version);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}


