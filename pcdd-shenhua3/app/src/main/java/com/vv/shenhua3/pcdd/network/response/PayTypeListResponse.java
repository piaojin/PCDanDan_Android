package com.vv.shenhua3.pcdd.network.response;


import com.vv.shenhua3.pcdd.network.bean.PayTypeInfo;

import java.util.List;

/**
 * Created by hang on 2017/5/10.
 */

public class PayTypeListResponse {
    public List<PayTypeInfo> offline;   //线下支付
    public List<PayTypeInfo> online;    //线上支付
}
