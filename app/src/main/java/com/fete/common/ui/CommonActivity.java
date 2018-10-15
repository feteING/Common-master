package com.fete.common.ui;

import android.view.ViewGroup;

import com.gyf.barlibrary.ImmersionBar;
import com.fete.basemodel.bar.BaseToolbarHelper;
import com.fete.common.base.BaseActivity;
import com.fete.common.R;
import com.fete.common.ui.mine.SettingActivity;

public class CommonActivity extends BaseActivity {




    @Override
    protected int getLayoutId() {
        return R.layout.activity_scrollview;
    }

    @Override
    protected void initView() {
        initToolBar();
        initTest();

    }

    private void initTest() {


    }

    private void initToolBar() {
        ViewGroup viewById = (ViewGroup) findViewById(android.R.id.content);
        BaseToolbarHelper.getInstance()
                .initToolbarHelper(viewById, this)
                .setCenterText("测试")
                .setRightText("设置", new BaseToolbarHelper.ToolBarCallBack() {
                    @Override
                    public void onClick() {
                        startActivity(SettingActivity.class);
                    }
                })
                .setToolBarHide(true);

    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        ImmersionBar.with(this)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.colorPrimary)
                .init();
    }


}
