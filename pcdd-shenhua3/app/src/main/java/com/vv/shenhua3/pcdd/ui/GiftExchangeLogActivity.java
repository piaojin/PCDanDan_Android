package com.vv.shenhua3.pcdd.ui;

import android.os.Bundle;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.network.ApiInterface;
import com.vv.shenhua3.pcdd.network.HttpResultCallback;
import com.vv.shenhua3.pcdd.network.MySubcriber;
import com.vv.shenhua3.pcdd.network.bean.GiftLogInfo;
import com.vv.shenhua3.pcdd.network.request.RechargeLogRequest;
import com.vv.shenhua3.pcdd.ui.adapter.GiftExchangeLogAdapter;
import com.vv.shenhua3.pcdd.ui.base.BaseTopActivity;
import com.vv.shenhua3.pcdd.ui.widget.refresh.PullLoadMoreRecyclerView;
import com.vv.shenhua3.pcdd.util.T;

import java.util.List;

/**
 * Created by hang on 2017/3/4.
 * 礼物兑换记录
 */

public class GiftExchangeLogActivity extends BaseTopActivity implements PullLoadMoreRecyclerView.PullLoadMoreListener {

    private PullLoadMoreRecyclerView rvData;

    private int pageNo = 1;
    private int pageSize = 10;
    private GiftExchangeLogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_exchange_log);
        init();
    }

    private void init() {
        initTopBar("兑换记录");

        rvData = getView(R.id.rvData);
        rvData.setLinearLayout();
        rvData.setOnPullLoadMoreListener(this);

        rvData.setRefreshing(true);
        onRefresh();
    }

    public void loadData() {
        RechargeLogRequest req = new RechargeLogRequest();
        req.page_no = pageNo+"";
        req.page_size = pageSize+"";
        HttpResultCallback<List<GiftLogInfo>> callback = new HttpResultCallback<List<GiftLogInfo>>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCompleted() {
                rvData.setPullLoadMoreCompleted();
            }

            @Override
            public void onError(Throwable e) {
                rvData.setPullLoadMoreCompleted();
                T.showShort(e.getMessage());
            }

            @Override
            public void onNext(List<GiftLogInfo> data) {
                updateView(data);
            }
        };
        MySubcriber s = new MySubcriber(callback);
        ApiInterface.getGiftExchangeLog(req, s);
    }

    public void updateView(List<GiftLogInfo> data) {
        if(data.size() == 0)
            T.showShort("暂无数据");

        if(pageNo == 1) {
            adapter = new GiftExchangeLogAdapter(this, data);
            rvData.setAdapter(adapter);
            rvData.setLessDataLoadMore();
        } else {
            adapter.getmData().addAll(data);
            adapter.notifyDataSetChanged();
        }
        pageNo++;
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        loadData();
    }

    @Override
    public void onLoadMore() {
        loadData();
    }
}
