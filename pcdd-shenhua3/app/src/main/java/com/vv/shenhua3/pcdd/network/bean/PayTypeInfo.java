package com.vv.shenhua3.pcdd.network.bean;

import java.io.Serializable;

/**
 * Created by hang on 2017/5/10.
 */

public class PayTypeInfo implements Serializable {
    public int id;
    public String name; //显示名字
    public String type_key; //用于请求对应的帐号
}
