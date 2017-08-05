package com.vv.shenhua3.pcdd.ui;

import android.os.Bundle;
import android.os.Handler;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.ui.base.BaseFragmentActivity;

public class LoadingActivity extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				finish();
			}
		}, 1500);
	}

	@Override
	public void onBackPressed() {
	}
}
