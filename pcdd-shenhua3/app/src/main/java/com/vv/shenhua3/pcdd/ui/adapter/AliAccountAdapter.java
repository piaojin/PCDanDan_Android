package com.vv.shenhua3.pcdd.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.network.bean.RechargeAccountInfo;
import com.vv.shenhua3.pcdd.ui.TransferAliActivity;
import com.vv.shenhua3.pcdd.ui.adapter.base.BaseRecyclerAdapter;
import com.vv.shenhua3.pcdd.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * Created by hang on 2017/2/28.
 */

public class AliAccountAdapter extends BaseRecyclerAdapter<RechargeAccountInfo> {

    private int type;

    public AliAccountAdapter(Context context, List<RechargeAccountInfo> data, int type) {
        super(context, data);
        putItemLayoutId(VIEW_TYPE_DEFAULT, R.layout.item_ali_account);
        this.type = type;
    }

    @Override
    public void onBind(ViewHolder holder, final RechargeAccountInfo item, int position) {
        holder.setText(R.id.tvAliAccount, item.account);
        holder.setText(R.id.tvRealName, item.real_name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, TransferAliActivity.class);
                it.putExtra("data", item);
                it.putExtra("type", type);
                mContext.startActivity(it);
            }
        });
    }
}
