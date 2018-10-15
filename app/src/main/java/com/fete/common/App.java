package com.fete.common;

import android.os.Build;
import android.os.StrictMode;

import com.bumptech.glide.Glide;
import com.fete.basemodel.base.BaseApplication;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

/**
 * Created by llf on 2017/3/10.
 */

public class App extends BaseApplication {
    //用于判断应用是否被强杀
    public static int mAppStatus = -1;

    {
        PlatformConfig.setWeixin("wxcc653fe774b6e168", "c321015f332c874d0fbb1b716df1668e");
        //豆瓣RENREN平台目前只能在服务器端配置
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        initFix();//兼容
        initSdk();//sdk
    }

    private void initSdk() {
        //bugly
        CrashReport.initCrashReport(getApplicationContext(), "2b5ae88355", false);
        //友盟统计
        UMConfigure.init(this, "5bc00a42f1f556a052000087", "", UMConfigure.DEVICE_TYPE_PHONE, null);
//        UMConfigure.setLogEnabled(true);

    }

    private void initFix() {
        /**
         * 解决7.0无法使用file://格式的URI的第二种方法
         */
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }

    /**
     * 内存紧张时会走这个方法
     *
     * @param level
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
        //1.清理内存中的图片2.清理掉Activity只保留Root Activity
        switch (level) {
            case TRIM_MEMORY_COMPLETE:
                //表示 App 退出到后台，并且已经处于 LRU List 比较考靠前的位置
                break;
            case TRIM_MEMORY_RUNNING_CRITICAL:
                //表示 App 正在正常运行，但是系统已经开始根据 LRU List 的缓存规则杀掉了一部分缓存的进程
                break;
            case TRIM_MEMORY_UI_HIDDEN:
                Glide.get(this).clearMemory();
                break;
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }
}
