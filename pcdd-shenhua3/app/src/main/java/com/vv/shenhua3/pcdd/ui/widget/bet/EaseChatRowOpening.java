package com.vv.shenhua3.pcdd.ui.widget.bet;

import android.content.Context;
import android.text.Html;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.entity.BettingJson;

/**
 * Created by hang on 2017/2/27.
 * 开盘
 */

public class EaseChatRowOpening extends EaseChatRow {

    private TextView tvExtContent;

    public EaseChatRowOpening(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(R.layout.ease_row_opening, this);
    }

    @Override
    protected void onFindViewById() {
        tvExtContent = (TextView) findViewById(R.id.tvExtContent);
    }

    @Override
    protected void setUpBaseView() {
    }

    @Override
    protected void onUpdateView() {

    }

    @Override
    protected void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        BettingJson json = new Gson().fromJson(txtBody.getMessage(), BettingJson.class);

        tvExtContent.setText(Html.fromHtml(context.getString(R.string.tip_bet_count_down, json.game_count+"", json.ext_content)));
    }

    @Override
    protected void onBubbleClick() {

    }
}
