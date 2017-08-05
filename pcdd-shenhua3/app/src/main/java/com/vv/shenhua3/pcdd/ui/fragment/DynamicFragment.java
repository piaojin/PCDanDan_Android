package com.vv.shenhua3.pcdd.ui.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.vv.shenhua3.pcdd.R;
import com.vv.shenhua3.pcdd.impl.BannerImageHolderView;
import com.vv.shenhua3.pcdd.network.ApiInterface;
import com.vv.shenhua3.pcdd.network.HttpResultCallback;
import com.vv.shenhua3.pcdd.network.MySubcriber;
import com.vv.shenhua3.pcdd.network.bean.BannerInfo;
import com.vv.shenhua3.pcdd.network.request.BannerListRequest;
import com.vv.shenhua3.pcdd.ui.WebLoadActivity;
import com.vv.shenhua3.pcdd.ui.base.BaseFragment;
import com.vv.shenhua3.pcdd.ui.widget.MyTabView;
import com.vv.shenhua3.pcdd.util.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hang on 2017/1/16.
 * 动态
 */

public class DynamicFragment extends BaseFragment {

    private ConvenientBanner banner;
    private MyTabView tabView;

    private List<BannerInfo> bannerList;
    private List<String> bannerImgRes = new ArrayList<String>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void init(View rootView) {
        banner = getView(R.id.bannerDynamic);
        tabView = getView(R.id.tabDynamic);

        initTabView();
        loadBanner();
    }

    private void loadBanner() {
        BannerListRequest req = new BannerListRequest();
        req.place = "1";
        HttpResultCallback callback = new HttpResultCallback<List<BannerInfo>>() {
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
            public void onNext(List<BannerInfo> data) {
                bannerList = data;
                bannerImgRes.clear();
                Observable.from(data)
                        .observeOn(Schedulers.newThread())
                        .map(new Func1<BannerInfo, String>() {
                            @Override
                            public String call(BannerInfo bannerInfo) {
                                return bannerInfo.banner_imgurl;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                                setupBannerView();
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(String s) {
                                bannerImgRes.add(s);
                            }
                        });
            }
        };
        MySubcriber<List<BannerInfo>> s = new MySubcriber<List<BannerInfo>>(activity, callback, false, "");
        ApiInterface.getBannerList(req, s);
    }

    public void setupBannerView() {
        banner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new BannerImageHolderView();
            }
        }, bannerImgRes)
                .setPageIndicator(new int[]{R.drawable.dot_nor, R.drawable.dot_sel})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        BannerInfo info = bannerList.get(position);
                        Intent it = new Intent(activity, WebLoadActivity.class);
                        it.putExtra(WebLoadFragment.PARAMS_TITLE, info.banner_name);
                        String url = ApiInterface.WAP_BANNER_DETAIL + "?banner_id=" + info.id;
                        it.putExtra(WebLoadFragment.PARAMS_URL, url);
                        startActivity(it);
                    }
                });
    }

    public void initTabView() {
        List<Map<String,Integer>> titles = new ArrayList<Map<String,Integer>>();
        List<Fragment> fragments = new ArrayList<Fragment>();

        Map<String,Integer> map = new HashMap<String, Integer>();
        map.put("通知公告", null);
        titles.add(map);
        map = new HashMap<String, Integer>();
        map.put("我的消息", null);
        titles.add(map);

        fragments.add(MsgListFragment.getInstance(1));
        fragments.add(MsgListFragment.getInstance(2));

        tabView.createView(titles, fragments, getChildFragmentManager());
    }

    @Override
    public void onStart() {
        super.onStart();
        banner.startTurning(3000);
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopTurning();
    }
}
