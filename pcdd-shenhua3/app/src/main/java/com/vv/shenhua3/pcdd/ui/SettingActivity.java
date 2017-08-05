package com.vv.shenhua3.pcdd.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hyphenate.chat.EMClient;
import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.manager.UserInfoManager;
import com.vv.shenhua3.pcdd.ui.base.BaseTopActivity;
import com.vv.shenhua3.pcdd.util.T;

/**
 * Created by hang on 2017/2/6.
 */

public class SettingActivity extends BaseTopActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    private void init() {
        initTopBar("设置");
        getView(R.id.llLogout).setOnClickListener(this);
        getView(R.id.llLoginPwd).setOnClickListener(this);
        getView(R.id.llWithdrawPwd).setOnClickListener(this);
        getView(R.id.llBindMobile).setOnClickListener(this);
        getView(R.id.llBindBank).setOnClickListener(this);
        getView(R.id.llClearCache).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.llLogout:
                new AlertDialog.Builder(this).setTitle("是否确认退出？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确认退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout();
                            }
                        }).show();
                break;

            case R.id.llWithdrawPwd:
                startActivity(new Intent(this, EditWithdrawPwdActivity.class));
                break;

            case R.id.llBindMobile:
                startActivity(new Intent(this, BindMobileActivity.class));
                break;

            case R.id.llBindBank:
                startActivity(new Intent(this, BindBankActivity.class));
                break;

            case R.id.llLoginPwd:
                startActivity(new Intent(this, EditLoginPwdActivity.class));
                break;

            case R.id.llClearCache:
//                DataCleanManager.clearAllCache(this);
                T.showShort("清除缓存成功");
                break;
        }
    }

    public void logout() {
        EMClient.getInstance().logout(true);
        UserInfoManager.clearUserInfo(this);
        setResult(RESULT_OK);
        finish();
    }
}
