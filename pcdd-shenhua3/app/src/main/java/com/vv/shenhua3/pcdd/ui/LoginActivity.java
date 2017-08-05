package com.vv.shenhua3.pcdd.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.vv.shenhua3.pcdd.MainActivity;
import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.manager.UserInfoManager;
import com.vv.shenhua3.pcdd.network.ApiInterface;
import com.vv.shenhua3.pcdd.network.HttpResultCallback;
import com.vv.shenhua3.pcdd.network.MySubcriber;
import com.vv.shenhua3.pcdd.network.bean.UserInfo;
import com.vv.shenhua3.pcdd.network.request.LoginOtherRequest;
import com.vv.shenhua3.pcdd.network.request.LoginRequest;
import com.vv.shenhua3.pcdd.ui.base.BaseFragmentActivity;
import com.vv.shenhua3.pcdd.util.MD5Utils;
import com.vv.shenhua3.pcdd.util.ProgressDialogUtil;
import com.vv.shenhua3.pcdd.util.T;
import com.vv.shenhua3.pcdd.util.ViewUtil;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by hang on 2017/2/6.
 */

public class LoginActivity extends BaseFragmentActivity implements View.OnClickListener, PlatformActionListener {

    private EditText edAccount;
    private EditText edPassword;

    private MyHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        edAccount = getView(R.id.edAccount);
        edPassword = getView(R.id.edPassword);

        ViewUtil.setEditWithClearButton(edAccount, R.drawable.btn_close_gray);
        ViewUtil.setEditWithClearButton(edPassword, R.drawable.btn_close_gray);

        edAccount.setText(UserInfoManager.getUserName(this));

        getView(R.id.btnClose).setOnClickListener(this);
        getView(R.id.btnLogin).setOnClickListener(this);
        getView(R.id.tvToRegister).setOnClickListener(this);
        getView(R.id.tvForgetPassword).setOnClickListener(this);
        getView(R.id.btnLoginWechat).setOnClickListener(this);

        mHandler = new MyHandler(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnLogin:
                if(ViewUtil.checkEditEmpty(edAccount, "请输入账号"))
                    return;
                if(ViewUtil.checkEditEmpty(edPassword, "请输入密码"))
                    return;
                login();
                break;

            case R.id.tvToRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                break;

            case R.id.tvForgetPassword:
                startActivity(new Intent(this, ResetLoginPwdActivity.class));
                break;

            case R.id.btnClose:
                finish();
                break;

            case R.id.btnLoginWechat:
                loginType = 2;
//                T.showShort("微信升级中,请注册登录");
                auth(ShareSDK.getPlatform(this, Wechat.NAME));
                break;
        }
    }

    private void login() {
        LoginRequest req = new LoginRequest();
        req.account = edAccount.getText().toString();
        String pwd = edPassword.getText().toString();
        for(int i=0; i<3; i++)
            pwd = MD5Utils.getMD5String(pwd);
        req.password = pwd;
        req.registration_id = "1";
        HttpResultCallback<UserInfo> callback = new HttpResultCallback<UserInfo>() {
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
            public void onNext(UserInfo userInfo) {
                loginSucceed(userInfo);
            }
        };
        MySubcriber<UserInfo> s = new MySubcriber<UserInfo>(this, callback, true, "登录中");
        ApiInterface.login(req, s);
    }

    public void loginSucceed(UserInfo userInfo) {
        UserInfoManager.setUserName(LoginActivity.this, edAccount.getText().toString());
        UserInfoManager.setUserPwd(LoginActivity.this, edPassword.getText().toString());
        loginIM(userInfo);
    }

    public void loginIM(final UserInfo bean) {
        if(EMClient.getInstance().isLoggedInBefore())
            EMClient.getInstance().logout(true);

        ProgressDialogUtil.showProgressDlg(this, "加载用户信息");
        EMClient.getInstance().login(bean.im_account, "123456", new EMCallBack() {
            @Override
            public void onSuccess() {
                ProgressDialogUtil.dismissProgressDlg();
                Log.e("", "环信login succeed");
                EMClient.getInstance().updateCurrentUserNick(bean.account);
                UserInfoManager.saveUserInfo(LoginActivity.this, bean);
                UserInfoManager.setLoginType(LoginActivity.this, 0);
                UserInfoManager.setLoginStatus(LoginActivity.this, true);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onProgress(int arg0, String arg1) {
                ProgressDialogUtil.dismissProgressDlg();
                Log.e("", arg1);
            }

            @Override
            public void onError(int arg0, String arg1) {
                ProgressDialogUtil.dismissProgressDlg();
                Log.e("", "环信login error "+arg1);
                T.showOnThread("环信login error "+arg1, true);
            }
        });
    }

    public void auth(Platform platform) {
        ProgressDialogUtil.showProgressDlg(this, "第三方授权");
        platform.setPlatformActionListener(this);
        platform.showUser(null);
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        ProgressDialogUtil.dismissProgressDlg();
        if(action == Platform.ACTION_AUTHORIZING || action == Platform.ACTION_USER_INFOR) {
            if(platform.isValid()) {
                T.showOnThread("授权成功", true);
                PlatformDb db = platform.getDb();
                String id = db.getUserId();
                String name = db.getUserName();
                String avatar = db.getUserIcon();
                Message msg = Message.obtain();
                Bundle b = new Bundle();
                b.putString("id", id);
                b.putString("name", name);
                b.putString("avatar", avatar);
                msg.setData(b);
                mHandler.sendMessage(msg);
            }
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        ProgressDialogUtil.dismissProgressDlg();
        T.showShort("授权失败");
//        T.showShort("微信升级中,请注册登录");
        System.out.print("****************"+i+"****************" + throwable.getMessage());
        platform.removeAccount();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        ProgressDialogUtil.dismissProgressDlg();
        System.out.print("********************************" + i);
        T.showShort("授权失败");
//        T.showShort("微信升级中,请注册登录");
        platform.removeAccount();
    }

    private int loginType;

    /**
     * 第三方登录
     */
    public void login(String id, String name, String avatar) {
        LoginOtherRequest req = new LoginOtherRequest();
        req.band_id = id;
        req.band_type = loginType+"";
        req.nick_name = name;
        req.user_photo = avatar;
        HttpResultCallback<UserInfo> callback = new HttpResultCallback<UserInfo>() {
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
            public void onNext(UserInfo userInfo) {
                loginSucceed(userInfo);
            }
        };
        MySubcriber s = new MySubcriber(this, callback, true, "登录中");
        ApiInterface.loginOther(req, s);
    }

    private static class MyHandler extends Handler {
        private WeakReference<LoginActivity> reference;

        MyHandler(LoginActivity activity) {
            reference = new WeakReference<LoginActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginActivity activity = reference.get();
            if(activity != null) {
                Bundle b = msg.getData();
                activity.login(b.getString("id"), b.getString("name"), b.getString("avatar"));
            }
        }
    }
}
