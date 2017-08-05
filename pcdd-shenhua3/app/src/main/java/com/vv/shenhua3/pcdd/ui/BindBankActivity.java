package com.vv.shenhua3.pcdd.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.manager.UserInfoManager;
import com.vv.shenhua3.pcdd.network.ApiInterface;
import com.vv.shenhua3.pcdd.network.HttpResultCallback;
import com.vv.shenhua3.pcdd.network.MySubcriber;
import com.vv.shenhua3.pcdd.network.bean.UserInfo;
import com.vv.shenhua3.pcdd.network.request.BindBankRequest;
import com.vv.shenhua3.pcdd.ui.base.BaseTopActivity;
import com.vv.shenhua3.pcdd.util.MD5Utils;
import com.vv.shenhua3.pcdd.util.T;
import com.vv.shenhua3.pcdd.util.ViewUtil;

/**
 * Created by hang on 2017/1/23.
 * 绑定银行卡
 */

public class BindBankActivity extends BaseTopActivity implements View.OnClickListener {

    private EditText edRealName;
    private EditText edBankName;
    private EditText edBankNo;
    private EditText edBankAddr;
    private EditText edWithdrawPwd;

    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_bank);
        init();
    }

    private void init() {
        initTopBar("绑定银行卡");
        edRealName = getView(R.id.edRealName);
        edBankName = getView(R.id.edBankName);
        edBankNo = getView(R.id.edBankNo);
        edBankAddr = getView(R.id.edBankAddr);
        edWithdrawPwd = getView(R.id.edWithdrawPwd);

        userInfo = UserInfoManager.getUserInfo(this);
        edRealName.setText(userInfo.real_name);
        edBankName.setText(userInfo.bank_name);
        edBankNo.setText(userInfo.bank_no);
        edBankAddr.setText(userInfo.open_card_address);

        getView(R.id.btnOK).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnOK:
                if(ViewUtil.checkEditEmpty(edRealName, "请填写真实姓名"))
                    return;
                if(ViewUtil.checkEditEmpty(edBankName, "请填写真实姓名"))
                    return;
                if(ViewUtil.checkEditEmpty(edBankNo, "请填写真实姓名"))
                    return;
                if(ViewUtil.checkEditEmpty(edBankAddr, "请填写真实姓名"))
                    return;
                if(ViewUtil.checkEditEmpty(edWithdrawPwd, "请填写真实姓名"))
                    return;
                bind();
                break;
        }
    }

    public void bind() {
        String pwd = edWithdrawPwd.getText().toString();
        for (int i=0; i<3; i++)
            pwd = MD5Utils.getMD5String(pwd);
        final BindBankRequest req = new BindBankRequest();
        req.withdrawals_password = pwd;
        req.real_name = edRealName.getText().toString();
        req.bank_name = edBankName.getText().toString();
        req.bank_no = edBankNo.getText().toString();
        req.open_card_address = edBankAddr.getText().toString();
        HttpResultCallback<String> callback = new HttpResultCallback<String>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCompleted() {
                T.showShort("绑定成功");
                userInfo.real_name = req.real_name;
                userInfo.bank_name = req.bank_name;
                userInfo.bank_no = req.bank_no;
                userInfo.open_card_address = req.open_card_address;
                UserInfoManager.saveUserInfo(BindBankActivity.this, userInfo);
                finish();
            }

            @Override
            public void onError(Throwable e) {
                T.showShort(e.getMessage());
            }

            @Override
            public void onNext(String s) {
            }
        };
        MySubcriber s = new MySubcriber(this, callback, true, "绑定中");
        ApiInterface.bindBank(req, s);
    }
}
