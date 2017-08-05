package com.vv.shenhua3.pcdd.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.ui.base.BaseTopActivity;
import com.vv.shenhua3.pcdd.ui.fragment.WebLoadFragment;

public class WebLoadActivity extends BaseTopActivity {
	
	private String title;
	private String url;
	
	private WebLoadFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_load);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		
		title = getIntent().getStringExtra(WebLoadFragment.PARAMS_TITLE);
		url = getIntent().getStringExtra(WebLoadFragment.PARAMS_URL);
		
		initTopBar(title);
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		fragment = WebLoadFragment.getInstance(url);
		t.replace(R.id.rlContent, fragment);
		t.commit();
	}

	@Override
	public void onBackPressed() {
		fragment.goBack();
	}
}
