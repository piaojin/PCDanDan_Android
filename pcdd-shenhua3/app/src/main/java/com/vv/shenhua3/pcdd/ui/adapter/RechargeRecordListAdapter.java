package com.vv.shenhua3.pcdd.ui.adapter;

import android.content.Context;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.network.bean.RechargeRecordInfo;
import com.vv.shenhua3.pcdd.ui.adapter.base.BaseRecyclerAdapter;
import com.vv.shenhua3.pcdd.ui.adapter.base.ViewHolder;
import com.vv.shenhua3.pcdd.util.DateUtil;

import java.util.List;

/**
 * Created by hang on 2017/1/23.
 */

public class RechargeRecordListAdapter extends BaseRecyclerAdapter<RechargeRecordInfo> {

    private String[] status = {"待确认", "成功"};

    public RechargeRecordListAdapter(Context context, List<RechargeRecordInfo> data) {
        super(context, data);
        putItemLayoutId(VIEW_TYPE_DEFAULT, R.layout.item_recharge_record);
    }

    @Override
    public void onBind(ViewHolder holder, RechargeRecordInfo item, int position) {
        holder.setText(R.id.tvRechargeFee, item.total_fee+"");
        holder.setText(R.id.tvRechargeTime, DateUtil.getTime(item.create_time, 0));
        holder.setText(R.id.tvRechargeStatus, status[item.status]);
    }
}
