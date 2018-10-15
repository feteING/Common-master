package com.fete.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.fete.basemodel.base.BaseFragment;
import com.fete.basemodel.base.BaseFragmentAdapter;
import com.fete.basemodel.utils.DownloadUtil;
import com.fete.basemodel.utils.LogTest;
import com.fete.basemodel.utils.NetUtil;
import com.fete.common.base.BaseActivity;
import com.fete.common.tools.event.NetStatusEvent;
import com.fete.common.ui.mine.MineFragment;
import com.fete.common.ui.test.TestFragment;
import com.gyf.barlibrary.ImmersionBar;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "MainActivity";

    @Bind(R.id.journalism)
    Button mNews;
    @Bind(R.id.video)
    Button mVideo;
    @Bind(R.id.girl)
    Button mGirl;
    @Bind(R.id.mine)
    Button mMine;
    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    private String[] mTitles;
    private BaseFragment[] fragments;
    int currentTabPosition = 0;
    public static final String CURRENT_TAB_POSITION = "HOME_CURRENT_TAB_POSITION";

    static {
        //vector支持selector
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    /**
     * 主界面不支持左滑退出
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //是被强杀的
        if (App.mAppStatus == -1) {
            startActivity(WelcomeActivity.class);
        }

        if (savedInstanceState != null) {
            currentTabPosition = savedInstanceState.getInt(CURRENT_TAB_POSITION);
            mViewPager.setCurrentItem(currentTabPosition);
        }

        initNetListener();//添加网络监听

    }

    NetUtil.NetConnChangedListener netConnChangedListener = new NetUtil.NetConnChangedListener() {
        @Override
        public void onNetConnChanged(NetUtil.ConnectStatus connectStatus) {
            LogTest.e("MainActivity:" + connectStatus.name());
            boolean netConnected = NetUtil.isNetConnected(MainActivity.this);
            //eventbus 通知网络变化
            EventBus.getDefault().post(new NetStatusEvent(netConnected, connectStatus));

        }
    };

    /**
     * 网络监听
     */
    private void initNetListener() {

        NetUtil.registerNetConnChangedReceiver(this);
        NetUtil.addNetConnChangedListener(netConnChangedListener);
    }


    /**
     * 移除网络监听
     */
    private void removeNetListener() {
        try {
            NetUtil.removeNetConnChangedListener(netConnChangedListener);
            NetUtil.unregisterNetConnChangedReceiver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        DownloadUtil.delApk();//删除已安装的apk
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeNetListener();//移除网络监听
        try {
            UMShareAPI.get(this).release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mTitles = getResources().getStringArray(R.array.main_titles);
        fragments = new BaseFragment[mTitles.length];
        fragments[0] = TestFragment.getInstance();
        fragments[1] = MineFragment.getInstance();
        fragments[2] = MineFragment.getInstance();
        fragments[3] = MineFragment.getInstance();
        BaseFragmentAdapter mAdapter = new BaseFragmentAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
        mNews.setSelected(true);
        //检查是否更新
//        BaseBusinessUtil.getInstance().checkUpdate("http://api.fir.im/apps/latest/58f87d50959d6904280005a3?api_token=9f2408863ff25abccca986e5d4d9d6ba", this);
    }


    /**
     * 存储瞬间的UI状态
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //奔溃前保存位置，方法执行在onStop之前
        outState.putInt(CURRENT_TAB_POSITION, currentTabPosition);
        super.onSaveInstanceState(outState);
    }

    /**
     * 这个方法在onStart()之后
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showToast("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //分发到fragment的onActivityResult，用于解决qq分享接收不到回调
        BaseFragment fragment = fragments[3];
        fragment.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 用于存储持久化数据
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        if (level == TRIM_MEMORY_UI_HIDDEN) {
            //释放资源
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //Empty
    }

    @Override
    public void onPageSelected(int position) {
        resetTab();
        switch (position) {
            case 0:
                mNews.setSelected(true);
                break;
            case 1:
                mVideo.setSelected(true);
                break;
            case 2:
                mGirl.setSelected(true);
                break;
            case 3:
                mMine.setSelected(true);
                break;
            default:
                //其他
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //Empty
    }

    @OnClick({R.id.journalism, R.id.video, R.id.girl, R.id.mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.journalism:
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.video:
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.girl:
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.mine:
                mViewPager.setCurrentItem(3, false);
                break;
            default:
                break;
        }
    }

    private void resetTab() {
        mNews.setSelected(false);
        mVideo.setSelected(false);
        mGirl.setSelected(false);
        mMine.setSelected(false);
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
