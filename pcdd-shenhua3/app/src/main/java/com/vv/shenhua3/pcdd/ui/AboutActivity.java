package com.vv.shenhua3.pcdd.ui;

import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.network.ApiInterface;
import com.vv.shenhua3.pcdd.network.HttpResultCallback;
import com.vv.shenhua3.pcdd.network.MySubcriber;
import com.vv.shenhua3.pcdd.network.bean.KefuInfo;
import com.vv.shenhua3.pcdd.network.bean.ShareParamsInfo;
import com.vv.shenhua3.pcdd.network.request.BaseRequest;
import com.vv.shenhua3.pcdd.ui.base.BaseTopActivity;
import com.vv.shenhua3.pcdd.util.BitmapTool;
import com.vv.shenhua3.pcdd.util.T;
import com.vv.shenhua3.pcdd.util.Utility;

import java.io.File;

/**
 * Created by hang on 2017/3/4.
 */

public class AboutActivity extends BaseTopActivity implements View.OnClickListener {

    private String website = "";
    private String qq = "";
    private String wechat = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init();
        loadKefuInfo();
        loadShareParams();
    }

    private void init() {
        initTopBar("关于");

        try {
            setText(R.id.tvVersion, getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        getView(R.id.btnCopyWebSite).setOnClickListener(this);
        getView(R.id.btnCopyQQ).setOnClickListener(this);
        getView(R.id.btnCopyWechat).setOnClickListener(this);
    }

    public void loadKefuInfo() {
        HttpResultCallback<KefuInfo> callback = new HttpResultCallback<KefuInfo>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCompleted() {
                setText(R.id.tvWebSite, website);
                setText(R.id.tvQQ, qq);
                setText(R.id.tvWechat, wechat);
            }

            @Override
            public void onError(Throwable e) {
                T.showShort(e.getMessage());
            }

            @Override
            public void onNext(KefuInfo kefuInfo) {
                website = kefuInfo.kefu_guanwang;
                qq = kefuInfo.kefu_qq;
                wechat = kefuInfo.kefu_weixin;
            }
        };
        MySubcriber s = new MySubcriber(this, callback, true, "加载数据");
        ApiInterface.getKefuInfo(new BaseRequest(), s);
    }

    public void loadShareParams() {
        HttpResultCallback<ShareParamsInfo> callback = new HttpResultCallback<ShareParamsInfo>() {
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
            public void onNext(ShareParamsInfo data) {
                if(!TextUtils.isEmpty(data.share_url)) {
                    setupSharePanel(data.share_url);
                }
            }
        };
        MySubcriber s = new MySubcriber(callback);
        ApiInterface.getShareParams(new BaseRequest(), s);
    }

    public void setupSharePanel(String url) {
        final String savePath = Environment.getExternalStorageDirectory() + File.separator + "share_url.png";
        ((ViewStub)getView(R.id.stubShare)).inflate();
        final ImageView ivQrCode = getView(R.id.ivQrCode);
        ivQrCode.setImageBitmap(BitmapTool.createQRCodeBitmap(url));
        getView(R.id.btnSaveQrCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BitmapTool.saveBitmap2Local(AboutActivity.this,
                        ((BitmapDrawable)ivQrCode.getDrawable()).getBitmap(), savePath)) {
                    T.showShort("二维码已保存至"+savePath);
                } else {
                    T.showLong("二维码保存失败，请手动截图");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnCopyWebSite:
                Utility.copy(this, website);
                T.showShort("已复制");
                break;

            case R.id.btnCopyQQ:
                Utility.copy(this, qq);
                T.showShort("已复制");
                break;

            case R.id.btnCopyWechat:
                Utility.copy(this, wechat);
                T.showShort("已复制");
                break;
        }
    }
}
