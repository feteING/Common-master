package com.fete.basemodel.bar;

import android.app.Activity;
import android.graphics.Color;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.fete.basemodel.R;
import com.fete.basemodel.utils.ToastUtil;

/**
 * 1.标题栏自己设置颜色  标题栏与状态栏都是蓝色
 * 2.fragment 在activity中改动比如 mainactivity 切换fragment时改变颜色
 *      ToolbarHelper.getInstance().setBarColor(MainActivity.this,R.color.Green);
 */
public class ToolbarHelper {


    private static ToolbarHelper toolbarHelper = null;

    private ToolbarHelper() {
        // TODO Auto-generated constructor stub
    }


    public static ToolbarHelper getInstance() {
        if (toolbarHelper == null) {
            synchronized (ToolbarHelper.class) {
                if (toolbarHelper == null) {
                    toolbarHelper = new ToolbarHelper();
                }
            }
        }
        return toolbarHelper;
    }

    public void setStatusIconBlack(ImmersionBar mImmersionBar, boolean isBlack) {
        if (isBlack) {
            if (ImmersionBar.isSupportStatusBarDarkFont()) {
                mImmersionBar.statusBarDarkFont(true).init();
            } else {
                ToastUtil.sToastUtil.shortDuration("当前设备不支持状态栏字体变色").setToastBackground(Color.WHITE, R.drawable.toast_radius).show();
            }
        } else {
            mImmersionBar.statusBarDarkFont(false).init();
        }

    }

    public void setHideBar(ImmersionBar mImmersionBar, boolean isHide) {
        if (isHide) {
            mImmersionBar.hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
        } else {
            mImmersionBar.hideBar(BarHide.FLAG_SHOW_BAR).init();
        }

    }

    public void setBarColor(Activity context, int colorId) {
        ImmersionBar.with(context)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(colorId)
                .init();
    }

    public void setBarTransparent(Activity context) {
        ImmersionBar.with(context)
                .reset()
                .init();
    }


}
