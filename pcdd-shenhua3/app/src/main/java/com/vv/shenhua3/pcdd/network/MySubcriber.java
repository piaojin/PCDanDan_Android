package com.vv.shenhua3.pcdd.network;

import android.content.Context;

import com.vv.shenhua3.pcdd.app.PcddApp;
import com.vv.shenhua3.pcdd.util.ProgressDialogUtil;

import rx.Subscriber;

/**
 * Created by hang on 2016/12/1.
 */
public class MySubcriber<T> extends Subscriber<T> {

    private Context context;
    private HttpResultCallback<T> callback;
    private boolean isShowProgress; //是否显示进度框
    private String tipMsg;

    public MySubcriber(HttpResultCallback<T> callback) {
        this(PcddApp.applicationContext, callback, false, null);
    }

    public MySubcriber(Context context, HttpResultCallback<T> callback, boolean isShowProgress, String tipMsg) {
        this.context = context;
        this.callback = callback;
        this.isShowProgress = isShowProgress;
        this.tipMsg = tipMsg;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(isShowProgress)
            ProgressDialogUtil.showProgressDlg(context, tipMsg);
    }

    @Override
    public void onCompleted() {
        if(isShowProgress)
            ProgressDialogUtil.dismissProgressDlg();
        if(callback != null)
            callback.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        if(isShowProgress)
            ProgressDialogUtil.dismissProgressDlg();
        if(callback != null)
            callback.onError(e);
    }

    @Override
    public void onNext(T t) {
        if(isShowProgress)
            ProgressDialogUtil.dismissProgressDlg();
        if(callback != null)
            callback.onNext(t);
    }
}
