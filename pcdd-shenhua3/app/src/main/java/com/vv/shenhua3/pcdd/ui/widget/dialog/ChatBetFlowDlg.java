package com.vv.shenhua3.pcdd.ui.widget.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.network.ApiInterface;
import com.vv.shenhua3.pcdd.ui.GameRecordActivity;
import com.vv.shenhua3.pcdd.ui.WebLoadActivity;
import com.vv.shenhua3.pcdd.ui.fragment.WebLoadFragment;

/**
 * Created by hang on 2017/2/27.
 */

public class ChatBetFlowDlg extends CommonDialog implements View.OnClickListener {

    private int gameType;   ////1北京快乐8 2加拿大快乐8
    private int roomId;

    public ChatBetFlowDlg(Context context, int gameType, int roomId) {
        super(context, R.layout.dlg_chat_bet_flow, 100, 125);
        this.gameType = gameType;
        this.roomId = roomId;
    }

    @Override
    public void initDlgView() {
        getView(R.id.tvBetLog).setOnClickListener(this);
        getView(R.id.tvHowToPlay).setOnClickListener(this);
        getView(R.id.tvTrendChart).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.tvBetLog:
                Intent log = new Intent(context, GameRecordActivity.class);
                log.putExtra("gameType", gameType);
                log.putExtra("roomId", roomId);
                context.startActivity(log);
                break;

            case R.id.tvHowToPlay:
                String url = gameType==1? ApiInterface.WAP_BEIJING28_EXPLAIN : ApiInterface.WAP_CANADA28_EXPLAIN;
                Intent explain = new Intent(context, WebLoadActivity.class);
                explain.putExtra(WebLoadFragment.PARAMS_TITLE, "玩法介绍");
                explain.putExtra(WebLoadFragment.PARAMS_URL, url);
                context.startActivity(explain);
                break;

            case R.id.tvTrendChart:
                Intent trend = new Intent(context, WebLoadActivity.class);
                trend.putExtra(WebLoadFragment.PARAMS_TITLE, "走势图");
                trend.putExtra(WebLoadFragment.PARAMS_URL, ApiInterface.WAP_TREND_CHART+"?game_type="+gameType);
                context.startActivity(trend);
                break;
        }
    }
}
