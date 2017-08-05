package com.vv.shenhua3.pcdd.ui.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.manager.UserInfoManager;
import com.vv.shenhua3.pcdd.ui.base.BaseFragment;
import com.vv.shenhua3.pcdd.util.BitmapTool;
import com.vv.shenhua3.pcdd.util.T;
import com.vv.shenhua3.pcdd.util.Utility;

import java.io.File;

/**
 * Created by hang on 2017/3/29.
 */

public class ProxyShareFragment extends BaseFragment implements View.OnClickListener {

    private ImageView ivQrCode;

    public String shareUrl;

    private String savePath;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_proxy_share;
    }

    @Override
    protected void init(View rootView) {
        ivQrCode = getView(R.id.ivQrCode);

        ivQrCode.setImageBitmap(BitmapTool.createQRCodeBitmap(shareUrl));
        setText(R.id.tvShareUrl, shareUrl);
        setText(R.id.tvShareId, getString(R.string.label_share_id, UserInfoManager.getUserId(activity)+""));

        getView(R.id.btnSaveQrCode).setOnClickListener(this);
        getView(R.id.tvCopyUrl).setOnClickListener(this);

        savePath = Environment.getExternalStorageDirectory() + File.separator + "share.png";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSaveQrCode:
                if(BitmapTool.saveBitmap2Local(activity, ((BitmapDrawable)ivQrCode.getDrawable()).getBitmap(), savePath))
                    T.showLong("已保存至"+savePath);
                break;

            case R.id.tvCopyUrl:
                Utility.copy(activity, shareUrl);
                T.showShort("已复制");
                break;
        }
    }
}
