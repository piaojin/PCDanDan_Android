package com.vv.shenhua3.pcdd.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.network.ApiInterface;
import com.vv.shenhua3.pcdd.network.HttpResultCallback;
import com.vv.shenhua3.pcdd.network.MySubcriber;
import com.vv.shenhua3.pcdd.network.bean.RechargeAccountInfo;
import com.vv.shenhua3.pcdd.network.request.RechargeOfflineRequest;
import com.vv.shenhua3.pcdd.ui.base.BaseTopActivity;
import com.vv.shenhua3.pcdd.util.T;
import com.vv.shenhua3.pcdd.util.Utility;
import com.vv.shenhua3.pcdd.util.ViewUtil;

/**
 * Created by hang on 2017/2/28.
 * 线下银行账号转账
 */

public class TransferBankActivity extends BaseTopActivity implements View.OnClickListener {

    private EditText edBankName;
    private EditText edRealName;
    private EditText edBankAccount;
    private EditText edTransferFee;

    private RechargeAccountInfo data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_bank);
        init();
    }

    public void init() {
        data = (RechargeAccountInfo) getIntent().getSerializableExtra("data");

        initTopBar("填写存款信息");
        edBankName = getView(R.id.edBankName);
        edRealName = getView(R.id.edRealName);
        edBankAccount = getView(R.id.edBankAccount);
        edTransferFee = getView(R.id.edTransferFee);

        setText(R.id.tvBankName, "银行："+data.bank_name);
        setText(R.id.tvRealName, "收款人："+data.real_name);
        setText(R.id.tvBankAccount, "账号："+data.account);
        setText(R.id.tvBranchBank, "开户行："+data.open_card_address);

        getView(R.id.btnCopyName).setOnClickListener(this);
        getView(R.id.btnCopyAccount).setOnClickListener(this);
        getView(R.id.btnCopyBranch).setOnClickListener(this);
        getView(R.id.btnOK).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnCopyName:
                Utility.copy(this, data.real_name);
                T.showShort("复制成功");
                break;

            case R.id.btnCopyAccount:
                Utility.copy(this, data.account);
                T.showShort("复制成功");
                break;

            case R.id.btnCopyBranch:
                Utility.copy(this, data.open_card_address);
                T.showShort("复制成功");
                break;

            case R.id.btnOK:
                if(ViewUtil.checkEditEmpty(edBankName, "请填写所属银行"))
                    return;
                if(ViewUtil.checkEditEmpty(edRealName, "请填写户名"))
                    return;
                if(ViewUtil.checkEditEmpty(edBankAccount, "请填写银行号后4位"))
                    return;
                if(edBankAccount.getText().toString().length() != 4) {
                    T.showShort("请填写银行号后4位");
                    return;
                }
                if(ViewUtil.checkEditEmpty(edTransferFee, "请填写汇款金额"))
                    return;
                submit();
                break;
        }
    }

    public void submit() {
        RechargeOfflineRequest req = new RechargeOfflineRequest();
        req.account_id = data.id+"";
        req.account = edBankAccount.getText().toString();
        req.account_type = "1";
        req.real_name = edRealName.getText().toString();
        req.bank_name = edBankName.getText().toString();
        req.point = edTransferFee.getText().toString();
        HttpResultCallback<String> callback = new HttpResultCallback<String>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCompleted() {
                T.showShort("提交成功，请等待客服审核");
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
        ApiInterface.rechargeOffline(req, s);
    }
}
