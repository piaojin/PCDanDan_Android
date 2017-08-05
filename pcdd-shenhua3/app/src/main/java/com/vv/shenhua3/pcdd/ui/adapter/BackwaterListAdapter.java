package com.vv.shenhua3.pcdd.ui.adapter;

import android.content.Context;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.network.bean.BackwaterInfo;
import com.vv.shenhua3.pcdd.ui.adapter.base.BaseRecyclerAdapter;
import com.vv.shenhua3.pcdd.ui.adapter.base.ViewHolder;
import com.vv.shenhua3.pcdd.util.DateUtil;

import java.util.List;

/**
 * Created by hang on 2017/1/23.
 */

public class BackwaterListAdapter extends BaseRecyclerAdapter<BackwaterInfo> {

    private String[] status = {"待处理", "处理", "未满足"};

    public BackwaterListAdapter(Context context, List<BackwaterInfo> data) {
        super(context, data);
        putItemLayoutId(VIEW_TYPE_DEFAULT, R.layout.item_backwater);
    }

    @Override
    public void onBind(ViewHolder holder, BackwaterInfo item, int position) {
        holder.setText(R.id.tvBackWaterDate, DateUtil.getTime(item.create_time, 2));
        holder.setText(R.id.tvBackWaterPoint, item.hui_shui_point+"元");
        holder.setText(R.id.tvBackWaterRate, "回水"+(item.bili*100)+"%");
        holder.setText(R.id.tvBackWaterStatus, status[item.status]);
    }
}
