package com.vv.shenhua3.pcdd.ui.widget.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.manager.AppGuideManager;


public class GuideShopClassifyWindow extends CommonDialog {
	
	private String guideType;

	public GuideShopClassifyWindow(Context context, String guideType) {
		super(context, R.layout.dlg_guide, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.guideType = guideType;
	}

	@Override
	public void initDlgView() {
		if(AppGuideManager.GUIDE_1.equals(guideType))
			setImageResource(R.id.ivGuideBg, R.drawable.bg1);
		else if(AppGuideManager.GUIDE_2.equals(guideType))
			setImageResource(R.id.ivGuideBg, R.drawable.bg2);
		else if(AppGuideManager.GUIDE_3.equals(guideType))
			setImageResource(R.id.ivGuideBg, R.drawable.bg3);
		else if(AppGuideManager.GUIDE_4.equals(guideType))
			setImageResource(R.id.ivGuideBg, R.drawable.bg4);

		getView(R.id.ivGuideBg).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
	}
}
