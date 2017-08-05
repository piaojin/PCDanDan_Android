package com.vv.shenhua3.pcdd.ui.adapter;

import android.content.Context;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.network.bean.RoomInfo;
import com.vv.shenhua3.pcdd.ui.adapter.base.BaseRecyclerAdapter;
import com.vv.shenhua3.pcdd.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * Created by hang on 2017/2/7.
 */

public class RoomGridAdapter extends BaseRecyclerAdapter<RoomInfo> {

    private int[] iconRes = {R.drawable.icon_vip_1, R.drawable.icon_vip_2, R.drawable.icon_vip_3, R.drawable.icon_vip_4};

    public RoomGridAdapter(Context context, List<RoomInfo> data) {
        super(context, data);
        putItemLayoutId(VIEW_TYPE_DEFAULT, R.layout.item_room);
    }

    @Override
    public void onBind(ViewHolder holder, RoomInfo item, int position) {
        holder.setImageResource(R.id.ivRoomIcon, iconRes[position%4]);
        holder.setText(R.id.tvRoomName, item.room_name);
        holder.setText(R.id.tvPeopleCount, item.people_count+"人");
    }
}
