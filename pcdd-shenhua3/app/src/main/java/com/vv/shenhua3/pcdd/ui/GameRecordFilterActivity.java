package com.vv.shenhua3.pcdd.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.ui.base.BaseTopActivity;
import com.vv.shenhua3.pcdd.util.DateUtil;
import com.vv.shenhua3.pcdd.util.T;

import java.util.Calendar;

/**
 * Created by hang on 2017/2/25.
 * 游戏记录筛选
 */

public class GameRecordFilterActivity extends BaseTopActivity implements View.OnClickListener {

    private TextView tvGameType;
    private TextView tvStartTime;
    private TextView tvEndTime;

    private int gameType = 0;   //1 北京快8   2 加拿大

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_record_filter);
        init();
    }

    private void init() {
        initTopBar("游戏记录");
        tvGameType = getView(R.id.tvGameType);
        tvStartTime = getView(R.id.tvStartTime);
        tvEndTime = getView(R.id.tvEndTime);

        tvGameType.setOnClickListener(this);
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        getView(R.id.btnOK).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.tvGameType:
                showTypeDlg();
                break;

            case R.id.tvStartTime:
                showDateDlg(tvStartTime);
                break;

            case R.id.tvEndTime:
                showDateDlg(tvEndTime);
                break;

            case R.id.btnOK:
                if(!TextUtils.isEmpty(tvStartTime.getText().toString()) || !TextUtils.isEmpty(tvEndTime.getText().toString())) {
                    if(TextUtils.isEmpty(tvStartTime.getText().toString())) {
                        T.showShort("请选择开始时间");
                        return;
                    }
                    if(TextUtils.isEmpty(tvEndTime.getText().toString())) {
                        T.showShort("请选择结束时间");
                        return;
                    }
                    if (DateUtil.compareDay(tvStartTime.getText().toString(), tvEndTime.getText().toString()) < 0) {
                        T.showShort("结束时间不能小于开始时间");
                        return;
                    }
                }

                Intent it = new Intent(this, GameRecordActivity.class);
                it.putExtra("gameType", gameType);
                it.putExtra("startTime", tvStartTime.getText().toString());
                it.putExtra("endTime", tvEndTime.getText().toString());
                startActivity(it);
                break;
        }
    }

    public void showTypeDlg() {
        String[] array1 = {"全部"};
        String[] array2 = getResources().getStringArray(R.array.game_type);
        final String[] items = new String[array1.length+array2.length];
        System.arraycopy(array1, 0, items, 0, array1.length);
        System.arraycopy(array2, 0, items, array1.length, array2.length);
        new AlertDialog.Builder(this).setTitle("游戏类型")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvGameType.setText(items[which]);
                        gameType = which;
                    }
                })
                .show();
    }

    public void showDateDlg(final TextView textView) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                textView.setText(DateUtil.getDate(year, monthOfYear, dayOfMonth));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                .show();
    }
}
