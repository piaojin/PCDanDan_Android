package com.vv.shenhua3.pcdd.network.request;

import com.vv.shenhua3.pcdd.annotation.Encrypt;

/**
 * Created by hang on 2017/2/6.
 */

public class RegisterRequest extends BaseRequest {
    public String account;
    public String code; //邀请码

    @Encrypt
    public String password;
}
