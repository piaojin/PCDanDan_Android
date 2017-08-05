package com.vv.shenhua3.pcdd.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.manager.ImageLoadManager;
import com.vv.shenhua3.pcdd.network.ApiInterface;
import com.vv.shenhua3.pcdd.network.HttpResultCallback;
import com.vv.shenhua3.pcdd.network.MySubcriber;
import com.vv.shenhua3.pcdd.network.request.PayCheckRequest;
import com.vv.shenhua3.pcdd.ui.base.BaseTopActivity;
import com.vv.shenhua3.pcdd.util.ApkUtil;
import com.vv.shenhua3.pcdd.util.BitmapTool;
import com.vv.shenhua3.pcdd.util.T;

import java.io.File;

/**
 * Created by hang on 2017/1/25.
 * 线上充值2
 */

public class RechargeOnlineSecondActivity extends BaseTopActivity implements View.OnClickListener {

    private static final String PKG_ALI = "com.eg.android.AlipayGphone";
    private static final String PKG_WX = "com.tencent.mm";

    private ImageView ivQr;

    private int payType; //0 微信 1 支付宝
    private String orderNo;
    private double fee;
    private String qrUrl;
    private String savePath;

    private String[] titles = {"微信", "支付宝"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_online_second);
        init();
    }

    private void init() {
        payType = getIntent().getIntExtra("type", 0);
        orderNo = getIntent().getStringExtra("orderNo");
        fee = getIntent().getDoubleExtra("fee", 0);
        qrUrl = getIntent().getStringExtra("qrUrl");
        savePath = Environment.getExternalStorageDirectory()+ File.separator+orderNo+".png";

        initTopBar(titles[payType]+"充值");

        ivQr = getView(R.id.ivQrCode);

        setText(R.id.tvRechargeTips, getString(R.string.label_recharge_scan_qr, titles[payType]));
        setText(R.id.tvRechargeStepTips, getString(R.string.recharge_step_tips, titles[payType], titles[payType]));
        setText(R.id.tvRechargeNo, getString(R.string.label_order_no, orderNo));
        setText(R.id.tvRechargeFee, getString(R.string.label_recharge_fee, fee));
        ImageLoadManager.getInstance().displayImage(qrUrl, ivQr);

        getView(R.id.btnPrevious).setOnClickListener(this);
        getView(R.id.btnRecharge).setOnClickListener(this);
        getView(R.id.btnPayCompleted).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPrevious:
                finish();
                break;

            case R.id.btnRecharge:
                Drawable drawable = null;
                Bitmap bitmap = null;
                if(ivQr.getDrawable() == null)
                    return;
                drawable = ivQr.getDrawable();
                if((bitmap = ((BitmapDrawable)drawable).getBitmap()) != null) {
                    if (BitmapTool.saveBitmap2Local(this, bitmap, savePath)) {
                        T.showShort("二维码已保存至"+savePath);
                        if(payType == 0) {
                            startAppToPay(PKG_ALI);
                        } else if(payType == 1) {
                            startAppToPay(PKG_WX);
                        }
                    } else {
                        T.showLong("二维码保存失败，请手动截图保存，然后打开支付APP");
                    }
                }
                break;

            case R.id.btnPayCompleted:
                PayCheckRequest req = new PayCheckRequest();
                req.order_no = orderNo;
                HttpResultCallback<Integer> callback = new HttpResultCallback<Integer>() {
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
                    public void onNext(Integer i) {
                        if(i == 1) {
                            new AlertDialog.Builder(RechargeOnlineSecondActivity.this)
                                    .setMessage("如充值未及时到账，请联系客服")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    }).show();
                        } else {
                            T.showShort("正在充值...");
                        }
                    }
                };
                MySubcriber s = new MySubcriber(this, callback, true, "");
                ApiInterface.checkPay(req, s);
                break;
        }
    }

    public void startAppToPay(String pkgName) {
        if(!ApkUtil.startApp(this, pkgName))
            T.showShort("启动应用失败，请手动打开应用支付");
    }
}
