package com.fete.common.ui;

import android.content.Context;

import com.fete.basemodel.base.BaseFragment;
import com.fete.basemodel.utils.LogTest;
import com.fete.common.R;
import com.fete.common.tools.event.NetStatusEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import common.feteing.commonutils.bannner.BGABanner;

/**
 * Created by llf on 2017/3/15.
 * 我的
 */

public class CommonFragment extends BaseFragment {
    Context context;
    @Bind(R.id.banner_normal)
    public BGABanner bannerNormal;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NetStatusEvent netStatusEvent) {
        LogTest.e("testFragment:" + netStatusEvent.toString());
    }


    public static CommonFragment getInstance() {
        CommonFragment mineFragment = new CommonFragment();
        return mineFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initView() {
        context = getActivity();


    }

    @Override
    protected void lazyFetchData() {

    }


}
