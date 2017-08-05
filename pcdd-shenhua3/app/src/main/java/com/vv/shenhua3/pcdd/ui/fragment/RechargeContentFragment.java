package com.vv.shenhua3.pcdd.ui.fragment;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.RadioGroup;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.ui.AliAcountListActivity;
import com.vv.shenhua3.pcdd.ui.RechargeLogActivity;
import com.vv.shenhua3.pcdd.ui.RechargeOnlineFirstepActivity;
import com.vv.shenhua3.pcdd.ui.base.BaseFragment;
import com.vv.shenhua3.pcdd.util.CheckUtil;
import com.vv.shenhua3.pcdd.util.T;


/**
 * Created by hang on 2017/1/23.
 */

public class RechargeContentFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private int payType = -1; //0 mo宝  1 爱益支付宝  2 多宝支付宝  3 多宝微信 4爱益微信

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recharge_content;
    }

    @Override
    protected void init(View rootView) {
        getView(R.id.btnRecharge).setOnClickListener(this);
        ((RadioGroup) getView(R.id.rgPayType)).setOnCheckedChangeListener(this);
        getView(R.id.tvOfflineBank).setOnClickListener(this);
        getView(R.id.tvOfflineAli).setOnClickListener(this);
        getView(R.id.btnRechargeLog).setOnClickListener(this);
        getView(R.id.btnCusSvr).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id. btnRecharge:
                if(payType == -1) {
                    T.showShort("请选择支付方式");
                    return;
                }
                Intent it = new Intent(activity, RechargeOnlineFirstepActivity.class);
                it.putExtra("type", payType);
                startActivity(it);
                break;

            case R.id.tvOfflineAli:
                Intent ali = new Intent(activity, AliAcountListActivity.class);
                ali.putExtra("type", 2);
                startActivity(ali);
                break;

            case R.id.tvOfflineBank:
                Intent wx = new Intent(activity, AliAcountListActivity.class);
                wx.putExtra("type", 3);
                startActivity(wx);
                break;

            case R.id.btnRechargeLog:
                startActivity(new Intent(activity, RechargeLogActivity.class));
                break;

            case R.id.btnCusSvr:
                CheckUtil.startCusSvr(activity);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch(checkedId) {
            case R.id.rbMoPay:
                payType = 0;
                break;

            case R.id.rbIyiAli:
                payType = 1;
                break;

            case R.id.rbIyiWx:
                payType = 4;
                break;

            case R.id.rbAliPay:
                payType = 2;
                break;

            case R.id.rbWxPay:
                payType = 3;
                break;
        }
    }
}
