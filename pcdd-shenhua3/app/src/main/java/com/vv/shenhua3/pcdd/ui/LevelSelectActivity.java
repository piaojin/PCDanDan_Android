package com.vv.shenhua3.pcdd.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.manager.UserInfoManager;
import com.vv.shenhua3.pcdd.network.ApiInterface;
import com.vv.shenhua3.pcdd.network.HttpResultCallback;
import com.vv.shenhua3.pcdd.network.MySubcriber;
import com.vv.shenhua3.pcdd.network.bean.RoomLevelInfo;
import com.vv.shenhua3.pcdd.network.request.RoomLevelListRequest;
import com.vv.shenhua3.pcdd.ui.base.BaseTopActivity;
import com.vv.shenhua3.pcdd.ui.fragment.WebLoadFragment;
import com.vv.shenhua3.pcdd.util.T;

import java.util.List;

/**
 * Created by hang on 2017/1/21.
 * 初级、中级、高级房选择
 */
public class LevelSelectActivity extends BaseTopActivity {

    private int levelCount = 3;
    private RelativeLayout[] rlRoomLevel;
    private TextView[] tvLevelName;
    private TextView[] tvLevelDesc;
    private TextView[] tvPeopleCount;
    private ImageView[] ivOddsExplain;

    private String[] titles;
    private int gameType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);
        init();
    }

    public void init() {
        titles = getResources().getStringArray(R.array.game_type);
        gameType = getIntent().getIntExtra("type", 1);
        initTopBar(titles[gameType-1]);

        rlRoomLevel = new RelativeLayout[levelCount];
        tvLevelName = new TextView[levelCount];
        tvLevelDesc = new TextView[levelCount];
        tvPeopleCount = new TextView[levelCount];
        ivOddsExplain = new ImageView[levelCount];

        for(int i=0; i<levelCount; i++) {
            rlRoomLevel[i] = getView(getResources().getIdentifier("rlGameLevel"+(i+1), "id", getPackageName()));
            tvLevelName[i] = getView(getResources().getIdentifier("tvRoomLevelName"+(i+1), "id", getPackageName()));
            tvLevelDesc[i] = getView(getResources().getIdentifier("tvRoomLevelDesc"+(i+1), "id", getPackageName()));
            tvPeopleCount[i] = getView(getResources().getIdentifier("tvRoomPeopleCount"+(i+1), "id", getPackageName()));
            ivOddsExplain[i] = getView(getResources().getIdentifier("ivOddsExplain"+(i+1), "id", getPackageName()));
        }

        loadData();
    }

    public void loadData() {
        RoomLevelListRequest req = new RoomLevelListRequest();
        req.game_type = gameType+"";
        HttpResultCallback<List<RoomLevelInfo>> callback = new HttpResultCallback<List<RoomLevelInfo>>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                T.showShort(e.getMessage());
            }

            @Override
            public void onNext(List<RoomLevelInfo> roomLevelInfos) {
                updateView(roomLevelInfos);
            }
        };
        MySubcriber<List<RoomLevelInfo>> s = new MySubcriber<List<RoomLevelInfo>>(this, callback, true, "加载数据");
        ApiInterface.getRoomLevelList(req, s);
    }

    public void updateView(List<RoomLevelInfo> data) {
        for(int i=0; i<levelCount; i++) {
            RoomLevelInfo item = data.get(i);
            tvLevelName[i].setText(item.area_name);
            tvLevelDesc[i].setText("("+item.feedback_desc+")");
            tvPeopleCount[i].setText(item.people_count+"人");
            rlRoomLevel[i].setOnClickListener(new MyOnClickListener(gameType, item.id, item.min_point));
            ivOddsExplain[i].setOnClickListener(new MyOnClickListener(gameType, item.id, item.min_point));
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        private double minPoint;    //允许进入的最低限额
        private int type;
        private int id;

        MyOnClickListener(int type, int id, double minPoint) {
            this.type = type;
            this.id = id;
            this.minPoint = minPoint;
        }
        @Override
        public void onClick(View v) {
            if(v instanceof RelativeLayout) {
                if(UserInfoManager.getUserInfo(LevelSelectActivity.this).point < minPoint) {
                    T.showShort("积分不足，达到"+minPoint+"可以进入");
                    return;
                }

                Intent it = new Intent(LevelSelectActivity.this, RoomListActivity.class);
                it.putExtra("gameType", type);
                it.putExtra("areaId", id);
                startActivity(it);
            } else if(v instanceof ImageView) {
                Intent it = new Intent(LevelSelectActivity.this, WebLoadActivity.class);
                it.putExtra(WebLoadFragment.PARAMS_TITLE, "赔率说明");
                it.putExtra(WebLoadFragment.PARAMS_URL, ApiInterface.WAP_ODDS_EXPLAIN+"?room_id="+id);
                startActivity(it);
            }
        }
    }
}
