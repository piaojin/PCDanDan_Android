package com.vv.shenhua3.pcdd.ui.adapter;

import android.content.Context;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.network.bean.EarningInfo;
import com.vv.shenhua3.pcdd.ui.adapter.base.BaseRecyclerAdapter;
import com.vv.shenhua3.pcdd.ui.adapter.base.ViewHolder;
import com.vv.shenhua3.pcdd.util.DateUtil;

import java.util.List;

/**
 * Created by hang on 2017/4/14.
 */

public class EarningListAdapter extends BaseRecyclerAdapter<EarningInfo> {

    public EarningListAdapter(Context context, List<EarningInfo> data) {
        super(context, data);
        putItemLayoutId(VIEW_TYPE_DEFAULT, R.layout.item_earning);
    }

    @Override
    public void onBind(ViewHolder holder, EarningInfo item, int position) {
        holder.setText(R.id.tvEarnName, item.nick_name);
        holder.setText(R.id.tvEarnDate, DateUtil.getTime(item.create_time, 2));
        holder.setText(R.id.tvBetCount, item.point_num+"");
        holder.setText(R.id.tvEarnPointTotal, item.point+"");
        holder.setText(R.id.tvEarnPoint, item.fenxiao_point);
    }
}
