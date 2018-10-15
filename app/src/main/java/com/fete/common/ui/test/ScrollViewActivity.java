package com.fete.common.ui.test;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fete.basemodel.bar.BaseToolbarHelper;
import com.fete.common.base.BaseActivity;
import com.fete.basemodel.scrollview.BounceScrollView;
import com.fete.common.R;
import com.fete.common.ui.mine.SettingActivity;

import butterknife.Bind;

public class ScrollViewActivity extends BaseActivity {


    @Bind(R.id.scrollview)
    public BounceScrollView scrollview;
    @Bind(R.id.ll_title)
    LinearLayout llTitle;
    @Bind(R.id.rl_title)
    RelativeLayout rlTitle;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_scrollview;
    }

    @Override
    protected void initView() {
        initToolBar();
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
                .initScollViewHeader(ScrollViewActivity.this, scrollview, mImmersionBar);
//                .initHeader(ScrollViewActivity.this, R.color.main_color);


    }


}
