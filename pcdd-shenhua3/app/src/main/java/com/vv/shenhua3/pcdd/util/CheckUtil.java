package com.vv.shenhua3.pcdd.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.vv.shenhua3.pcdd.manager.UserInfoManager;
import com.vv.shenhua3.pcdd.network.ApiInterface;
import com.vv.shenhua3.pcdd.network.HttpResultCallback;
import com.vv.shenhua3.pcdd.network.MySubcriber;
import com.vv.shenhua3.pcdd.network.bean.KefuInfo;
import com.vv.shenhua3.pcdd.network.bean.UserInfo;
import com.vv.shenhua3.pcdd.network.request.BaseRequest;
import com.vv.shenhua3.pcdd.ui.BindBankActivity;
import com.vv.shenhua3.pcdd.ui.BindMobileActivity;
import com.vv.shenhua3.pcdd.ui.CusSvrActivity;
import com.vv.shenhua3.pcdd.ui.EditWithdrawPwdActivity;

/**
 * Created by hang on 2017/2/27.
 */

public class CheckUtil {

    /** 检查绑定情况 */
    public static boolean checkBind(Context context) {
        return checkBindMobile(context) && checkSetWithdrawPwd(context) && checkBindBank(context);
    }

    /** 是否绑定手机 */
    public static boolean checkBindMobile(Context context) {
        UserInfo userInfo = UserInfoManager.getUserInfo(context);
        if(TextUtils.isEmpty(userInfo.mobile)) {
            T.showShort("您还未绑定手机，请先绑定手机");
            context.startActivity(new Intent(context, BindMobileActivity.class));
            return false;
        }
        return true;
    }

    /** 是否设置提现密码 */
    public static boolean checkSetWithdrawPwd(Context context) {
        UserInfo userInfo = UserInfoManager.getUserInfo(context);
        if(TextUtils.isEmpty(userInfo.withdrawals_password)) {
            T.showShort("您还未设置提现密码，请先设置提现密码");
            context.startActivity(new Intent(context, EditWithdrawPwdActivity.class));
            return false;
        }
        return true;
    }

    /** 是否绑定银行卡 */
    public static boolean checkBindBank(Context context) {
        UserInfo userInfo = UserInfoManager.getUserInfo(context);
        if(TextUtils.isEmpty(userInfo.bank_no)) {
            T.showShort("您还未绑定银行卡，请先绑定银行卡");
            context.startActivity(new Intent(context, BindBankActivity.class));
            return false;
        }
        return true;
    }

    public static void startCusSvr(final Context context) {
        HttpResultCallback<KefuInfo> callback = new HttpResultCallback<KefuInfo>() {
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
            public void onNext(KefuInfo kefuInfo) {
                TransferTempDataUtil.getInstance().setData(kefuInfo);
                context.startActivity(new Intent(context, CusSvrActivity.class));
            }
        };
        MySubcriber s = new MySubcriber(context, callback, true, "加载数据");
        ApiInterface.getKefuQAList(new BaseRequest(), s);
    }
}
