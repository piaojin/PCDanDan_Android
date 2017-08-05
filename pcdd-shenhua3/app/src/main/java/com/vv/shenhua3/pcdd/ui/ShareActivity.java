package com.vv.shenhua3.pcdd.ui;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.ui.base.BaseFragmentActivity;
import com.vv.shenhua3.pcdd.util.BitmapTool;
import com.vv.shenhua3.pcdd.util.T;

import java.io.File;

/**
 * Created by hang on 2017/3/29.
 */

public class ShareActivity extends BaseFragmentActivity implements View.OnClickListener {

    private ImageView ivQrCode;

    private String url;

    private String savePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        init();
    }

    public void init() {
        url = getIntent().getStringExtra("url");
        savePath = Environment.getExternalStorageDirectory() + File.separator + "share_url.png";

        ivQrCode = getView(R.id.ivQrCode);
        ivQrCode.setImageBitmap(BitmapTool.createQRCodeBitmap(url));

        getView(R.id.ivBack).setOnClickListener(this);
        getView(R.id.btnSaveQrCode).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;

            case R.id.btnSaveQrCode:
                if(BitmapTool.saveBitmap2Local(this, ((BitmapDrawable)ivQrCode.getDrawable()).getBitmap(), savePath)) {
                    T.showShort("二维码已保存至"+savePath);
                } else {
                    T.showLong("二维码保存失败，请手动截图");
                }
                break;
        }
    }
}
