package com.vv.shenhua3.pcdd.ui;

import android.os.Bundle;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.ui.base.BaseTopActivity;

/**
 * Created by hang on 2017/1/23.
 * 充值
 */

public class RechargeActivity extends BaseTopActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        initTopBar("充值");
    }
}
