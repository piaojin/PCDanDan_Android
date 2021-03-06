package com.vv.shenhua3.pcdd.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.impl.AliStepHolderView;
import com.vv.shenhua3.pcdd.network.ApiInterface;
import com.vv.shenhua3.pcdd.network.HttpResultCallback;
import com.vv.shenhua3.pcdd.network.MySubcriber;
import com.vv.shenhua3.pcdd.network.bean.RechargeAccountInfo;
import com.vv.shenhua3.pcdd.network.request.RechargeOfflineRequest;
import com.vv.shenhua3.pcdd.ui.base.BaseTopActivity;
import com.vv.shenhua3.pcdd.util.T;
import com.vv.shenhua3.pcdd.util.Utility;
import com.vv.shenhua3.pcdd.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hang on 2017/2/28.
 */

public class TransferAliActivity extends BaseTopActivity implements View.OnClickListener {

    private EditText edAliAccount;
    private EditText edRealName;
    private EditText edTransferFee;
    private ConvenientBanner banner;

    private RechargeAccountInfo data;
    private int type;

    private List<Integer> stepPicRes = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_ali);
        init();
    }

    public void init() {
        data = (RechargeAccountInfo) getIntent().getSerializableExtra("data");
        type = getIntent().getIntExtra("type", 2);

        if(type == 2)
            initTopBar("支付宝转账");
        else
            initTopBar("微信转账");

        edAliAccount = getView(R.id.edAliAccount);
        edRealName = getView(R.id.edRealName);
        edTransferFee = getView(R.id.edTransferFee);
        banner = getView(R.id.bannerAliTransfer);

        setupBannerView();

        setText(R.id.tvAliAccount, data.account);
        setText(R.id.tvRealName, data.real_name);

        getView(R.id.btnOK).setOnClickListener(this);
        getView(R.id.btnCopyName).setOnClickListener(this);
        getView(R.id.btnCopyAccount).setOnClickListener(this);
    }

    public void setupBannerView() {
        stepPicRes.add(R.drawable.step_ali_1);
        stepPicRes.add(R.drawable.step_ali_2);
        stepPicRes.add(R.drawable.step_ali_3);

        banner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new AliStepHolderView();
            }
        }, stepPicRes);
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

            case R.id.btnOK:
                if(ViewUtil.checkEditEmpty(edRealName, "请填写支付宝户名"))
                    return;
                if(ViewUtil.checkEditEmpty(edAliAccount, "请填写支付宝账号"))
                    return;
                submit();
                break;
        }
    }

    public void submit() {
        RechargeOfflineRequest req = new RechargeOfflineRequest();
        req.account_id = data.id+"";
        req.account = edAliAccount.getText().toString();
        req.account_type = type+"";
        req.real_name = edRealName.getText().toString();
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
