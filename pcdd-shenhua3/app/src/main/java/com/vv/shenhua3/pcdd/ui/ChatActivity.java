package com.vv.shenhua3.pcdd.ui;

import android.content.Intent;
import android.os.Bundle;

import com.hyphenate.easeui.EaseConstant;
import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.manager.AppGuideManager;
import com.vv.shenhua3.pcdd.ui.base.BaseFragmentActivity;
import com.vv.shenhua3.pcdd.ui.fragment.ChatBetFragment;
import com.vv.shenhua3.pcdd.ui.widget.dialog.GuideShopClassifyWindow;
import com.vv.shenhua3.pcdd.util.CommonUtil;

public class ChatActivity extends BaseFragmentActivity {
	
	public static ChatActivity activityInstance;
    private ChatBetFragment chatFragment;

    String title;
    String toChatUsername;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		
		activityInstance = this;
        //房间id
		//聊天界面title
		title = getIntent().getExtras().getString("title");
        //聊天人或群id
        toChatUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        //可以直接new EaseChatFratFragment使用
        chatFragment = new ChatBetFragment();
        //传入参数
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
	}

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus) {
            if(!AppGuideManager.getGuide4()) {
                new GuideShopClassifyWindow(this, AppGuideManager.GUIDE_4).show();
                AppGuideManager.setGuide4();
            }
        }
    }
	
	@Override
	protected void onPause() {
		super.onPause();
		CommonUtil.closeKeyBoard(this);
	}
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra(EaseConstant.EXTRA_USER_ID);
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }
    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
    }
    
    public String getToChatUsername(){
        return toChatUsername;
    }
}
