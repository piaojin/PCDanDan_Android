package com.vv.shenhua3.pcdd.ui.base;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.vv.shenhua3.pcdd.manager.ImageLoadManager;
import com.vv.shenhua3.pcdd.network.RetrofitHelper;

import retrofit2.Retrofit;

/**
 * Created by hang on 2016/7/16.
 */
public class BaseFragmentActivity extends FragmentActivity {

    protected Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        retrofit = RetrofitHelper.getInstance().getRetrofit();
    }

    public void setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
    }

    public void setText(int viewId, int textId) {
        TextView tv = getView(viewId);
        tv.setText(textId);
    }

    public void setImageResource(int viewId, int drawableId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(drawableId);
    }

    public void setImageBitmap(int viewId, Bitmap bm) {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bm);
    }

    public void setImageByURL(int viewId, final String url) {
        ImageView iv = getView(viewId);
        ImageLoadManager.getInstance().displayImage(url, iv);
    }

    public <T extends View>T getView(int viewId) {
        View view  = findViewById(viewId);
        return (T)view;
    }
}
