package com.vv.shenhua3.pcdd.network.request;

import com.vv.shenhua3.pcdd.annotation.Encrypt;

/**
 * Created by hang on 2017/2/27.
 */

public class BindBankRequest extends BaseRequest {
    @Encrypt
    public String withdrawals_password; //提现密码 三次加密
    public String real_name;        //开户姓名
    public String bank_name;        //银行名称
    public String bank_no;          //银行卡号
    public String open_card_address;//开户地址
}

