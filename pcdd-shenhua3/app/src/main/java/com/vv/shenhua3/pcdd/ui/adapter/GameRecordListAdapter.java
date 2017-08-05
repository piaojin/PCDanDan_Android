package com.vv.shenhua3.pcdd.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.network.bean.GameRecordInfo;
import com.vv.shenhua3.pcdd.ui.adapter.base.BaseRecyclerAdapter;
import com.vv.shenhua3.pcdd.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * Created by hang on 2017/1/23.
 */

public class GameRecordListAdapter extends BaseRecyclerAdapter<GameRecordInfo> {

    private String[] gameTypes;

    public GameRecordListAdapter(Context context, List<GameRecordInfo> data) {
        super(context, data);
        putItemLayoutId(VIEW_TYPE_DEFAULT, R.layout.item_game_record);
        gameTypes = context.getResources().getStringArray(R.array.game_type);
    }

    @Override
    public void onBind(ViewHolder holder, GameRecordInfo item, int position) {
        holder.setText(R.id.tvChoiceNo, gameTypes[item.game_type-1]+"--"+item.choice_no+"期");
        holder.setText(R.id.tvLotteryResult, item.real_result);
        holder.setText(R.id.tvLotteryType, item.result_type);
        holder.setText(R.id.tvBetType, item.choice_name);
        holder.setText(R.id.tvBetPoint, item.point+"元宝");
        if(TextUtils.isEmpty(item.real_result)) {
            //未开奖
            holder.setText(R.id.tvWinningPoint, "--元宝");
            holder.setText(R.id.tvRealPoint, "未开奖");
        } else {
            holder.setText(R.id.tvWinningPoint, item.get_point + "元宝");
            double earnPoint = item.get_point - item.point;
            TextView textView = holder.getView(R.id.tvRealPoint);
            if(earnPoint < 0)
                textView.setTextColor(mContext.getResources().getColor(R.color.btn_green_noraml));
            else
                textView.setTextColor(mContext.getResources().getColor(R.color.text_red));
            textView.setText(earnPoint+"元宝");
        }
    }
}
