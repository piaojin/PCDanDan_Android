package com.vv.shenhua3.pcdd.network.request;


import com.vv.shenhua3.pcdd.annotation.Encrypt;

/**
 * Created by hang on 2017/3/19.
 */

public class DuobaoPayRequest extends BaseRequest {
    @Encrypt
    public String order_no;
    public String fee;
    public String pay_type; //1支付宝 2微信
}
