package com.vv.shenhua3.pcdd.util;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogUtil {

	private static ProgressDialog progressDlg;
	
	public static void showProgressDlg(Context context, String message) {
		progressDlg = new ProgressDialog(context);
//		message = TextUtils.isEmpty(message)? "请稍等" : message;
		progressDlg.setMessage(message);
		progressDlg.show();
	}
	
	public static void setCancelable(boolean flag) {
		if(progressDlg != null)
			progressDlg.setCancelable(flag);
	}
	
	public static void dismissProgressDlg() {
		if(progressDlg != null)
			progressDlg.dismiss();
	}
}
