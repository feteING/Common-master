package com.fete.common.ui.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fete.common.base.BaseActivity;
import com.fete.common.App;
import com.fete.common.R;
import com.fete.common.WelcomeActivity;

import butterknife.Bind;

/**
 * Created by llf on 2017/3/14.
 * 设置
 */

public class SettingActivity extends BaseActivity {
    private static final String TAG = "SettingActivity";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //是被强杀的
        if (App.mAppStatus == -1) {
            startActivity(WelcomeActivity.class);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setContentInsetStartWithNavigation(0);
    }
}
