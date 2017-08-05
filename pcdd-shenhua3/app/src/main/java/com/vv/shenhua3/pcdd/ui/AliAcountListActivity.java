package com.vv.shenhua3.pcdd.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.network.ApiInterface;
import com.vv.shenhua3.pcdd.network.HttpResultCallback;
import com.vv.shenhua3.pcdd.network.MySubcriber;
import com.vv.shenhua3.pcdd.network.bean.RechargeAccountInfo;
import com.vv.shenhua3.pcdd.network.request.AccountListRequest;
import com.vv.shenhua3.pcdd.ui.adapter.AliAccountAdapter;
import com.vv.shenhua3.pcdd.ui.adapter.manager.FullyLinearLayoutManager;
import com.vv.shenhua3.pcdd.ui.base.BaseTopActivity;
import com.vv.shenhua3.pcdd.util.T;

import java.util.List;

/**
 * Created by hang on 2017/2/28.
 * 线下充值支付宝账号
 */

public class AliAcountListActivity extends BaseTopActivity {

    private RecyclerView rvData;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_ali_list);

        type = getIntent().getIntExtra("type", 2);

        init();
        loadAccountList();
    }

    public void init() {
        initTopBar("选择账号");
        rvData = getView(R.id.rvData);
        rvData.setLayoutManager(new FullyLinearLayoutManager(this));
    }

    public void loadAccountList() {
        AccountListRequest req = new AccountListRequest();
        req.type = type +"";
        HttpResultCallback<List<RechargeAccountInfo>> callback = new HttpResultCallback<List<RechargeAccountInfo>>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                T.showShort(e.getMessage());
            }

            @Override
            public void onNext(List<RechargeAccountInfo> data) {
                rvData.setAdapter(new AliAccountAdapter(AliAcountListActivity.this, data, type));
            }
        };
        MySubcriber s = new MySubcriber(this, callback, true, "加载中");
        ApiInterface.getAccountList(req, s);
    }
}
