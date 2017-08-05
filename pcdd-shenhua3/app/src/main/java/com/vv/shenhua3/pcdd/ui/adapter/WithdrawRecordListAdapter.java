package com.vv.shenhua3.pcdd.ui.adapter;

import android.content.Context;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.network.bean.WithdrawRecordInfo;
import com.vv.shenhua3.pcdd.ui.adapter.base.BaseRecyclerAdapter;
import com.vv.shenhua3.pcdd.ui.adapter.base.ViewHolder;
import com.vv.shenhua3.pcdd.util.DateUtil;

import java.util.List;

/**
 * Created by hang on 2017/1/23.
 */

public class WithdrawRecordListAdapter extends BaseRecyclerAdapter<WithdrawRecordInfo> {

    private String[] status = {"提现中", "成功", "失败"};

    public WithdrawRecordListAdapter(Context context, List<WithdrawRecordInfo> data) {
        super(context, data);
        putItemLayoutId(VIEW_TYPE_DEFAULT, R.layout.item_recharge_record);
    }

    @Override
    public void onBind(ViewHolder holder, WithdrawRecordInfo item, int position) {
        holder.setText(R.id.tvRechargeFee, item.fee+"元");
        holder.setText(R.id.tvRechargeTime, DateUtil.getTime(item.create_time, 0));
        holder.setText(R.id.tvRechargeStatus, status[item.status]);
    }
}
