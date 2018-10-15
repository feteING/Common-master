package com.fete.common.ui.test;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.fete.basemodel.bar.BaseToolbarHelper;
import com.fete.basemodel.bar.ToolbarHelper;
import com.fete.common.base.BaseActivity;
import com.fete.common.R;
import com.fete.common.ui.mine.SettingActivity;

import butterknife.Bind;

public class ToolBarActivity extends BaseActivity {

    @Bind(R.id.toolbar_change)
    public Button toolbarChange;
    @Bind(R.id.toolbar_unchange)
    public Button toolbarUnchange;

    @Bind(R.id.toolbar_hide)
    public Button toolbarHide;
    @Bind(R.id.toolbar_show)
    public Button toolbarShow;

    @Bind(R.id.toolbar_transparent)
    public Button toolbarTransparent;
    @Bind(R.id.toolbar_setcolor)
    public Button toolbarSetcolor;

    @Bind(R.id.rl_title)
    RelativeLayout relativeLayout;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_toolbar;
    }

    @Override
    protected void initView() {
        initToolBar();
        initTest();

    }

    private void initTest() {
        final ToolbarHelper instance = ToolbarHelper.getInstance();
        toolbarChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instance.setStatusIconBlack(mImmersionBar, true);
            }
        });
        toolbarUnchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instance.setStatusIconBlack(mImmersionBar, false);
            }
        });
        toolbarHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instance.setHideBar(mImmersionBar, true);
            }
        });
        toolbarShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instance.setHideBar(mImmersionBar, false);
            }
        });
        toolbarTransparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instance.setBarTransparent(ToolBarActivity.this);
            }
        });
        toolbarSetcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instance.setBarColor(ToolBarActivity.this, R.color.Red);


            }
        });

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
/*        ImmersionBar.with(this)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.colorPrimary)
                .init();*/
    }


}
