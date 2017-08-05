package com.vv.shenhua3.pcdd.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.network.ApiInterface;
import com.vv.shenhua3.pcdd.network.HttpResultCallback;
import com.vv.shenhua3.pcdd.network.MySubcriber;
import com.vv.shenhua3.pcdd.network.bean.WithdrawInfo;
import com.vv.shenhua3.pcdd.network.request.BaseRequest;
import com.vv.shenhua3.pcdd.network.request.WithdrawRequest;
import com.vv.shenhua3.pcdd.ui.base.BaseTopActivity;
import com.vv.shenhua3.pcdd.util.MD5Utils;
import com.vv.shenhua3.pcdd.util.T;
import com.vv.shenhua3.pcdd.util.ViewUtil;

import static com.vv.shenhua3.pcdd.R.id.edWithdrawPwd;

/**
 * Created by hang on 2017/2/27.
 * 提现
 */

public class WithdrawActivity extends BaseTopActivity implements View.OnClickListener {

    private TextView tvMyPoint;
    private TextView tvNotice;
    private TextView tvRemind;
    private EditText edFee;
    private EditText edPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        init();
        loadWithdrawInfo();
    }

    public void init() {
        initTopBar("提现");
        tvMyPoint = getView(R.id.tvMyPoint);
        tvNotice = getView(R.id.tvWithdrawNotice);
        tvRemind = getView(R.id.tvWithdrawRemind);
        edFee = getView(R.id.edWithdrawFee);
        edPwd = getView(edWithdrawPwd);

        getView(R.id.btnWithdraw).setOnClickListener(this);
    }

    public void loadWithdrawInfo() {
        HttpResultCallback<WithdrawInfo> callback = new HttpResultCallback<WithdrawInfo>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                T.showShort(e.getMessage());
                finish();
            }

            @Override
            public void onNext(WithdrawInfo info) {
                tvMyPoint.setText(getString(R.string.account_balance, info.point+""));
                tvNotice.setText(getString(R.string.withdraw_notice, info.tixian_min_fee+""));
                tvRemind.setText(getString(R.string.withdraw_remind, info.point+"", info.tixian_free_count, info.free_count, (info.tixian_bili*100)+"%"));
            }
        };
        MySubcriber s = new MySubcriber(this, callback, true, "加载数据");
        ApiInterface.getWithdrawInfo(new BaseRequest(), s);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnWithdraw:
                if(ViewUtil.checkEditEmpty(edFee, "请输入提现金额"))
                    return;
                if(ViewUtil.checkEditEmpty(edPwd, "请输入提现密码"))
                    return;
                withdraw();
                break;
        }
    }

    public void withdraw() {
        String pwd = edPwd.getText().toString();
        for (int i=0; i<3; i++)
            pwd = MD5Utils.getMD5String(pwd);

        WithdrawRequest req = new WithdrawRequest();
        req.fee = edFee.getText().toString();
        req.withdrawals_password = pwd;
        req.client = "android";
        HttpResultCallback<String> callback = new HttpResultCallback<String>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCompleted() {
                T.showShort("提现申请提交成功");
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
        MySubcriber s = new MySubcriber(this, callback, true, "提交中");
        ApiInterface.withdraw(req, s);
    }
}
