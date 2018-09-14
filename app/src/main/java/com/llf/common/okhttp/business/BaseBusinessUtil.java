package com.llf.common.okhttp.business;

import android.content.Context;

import com.llf.basemodel.dialog.UpdateDialog;
import com.llf.basemodel.utils.AppInfoUtil;
import com.llf.basemodel.utils.DownloadUtil;
import com.llf.basemodel.utils.JsonUtils;
import com.llf.basemodel.utils.LogUtil;
import com.llf.common.App;
import com.llf.common.entity.ApplicationEntity;
import com.llf.common.okhttp.MyService;
import com.llf.common.okhttp.callback.BaseOneCallBack;

/**
 * Created by A on 2018/6/14.
 */

public class BaseBusinessUtil {

    public abstract class CoreOneCallBack extends CoreTwoCallBack {
        public abstract void onSucess();
    }

    public abstract class CoreTwoCallBack {
        public abstract void onSucess();

        public abstract void onFaile();
    }

    private static BaseBusinessUtil baseBusinessUtil = null;


    private BaseBusinessUtil() {
    }


    public static BaseBusinessUtil getInstance() {
        if (baseBusinessUtil == null) {
            synchronized (BaseBusinessUtil.class) {
                if (baseBusinessUtil == null) {
                    baseBusinessUtil = new BaseBusinessUtil();
                }
            }
        }
        return baseBusinessUtil;
    }

    public void checkUpdate(String url, final Context context) {
        MyService.checkUpdate(url, new BaseOneCallBack() {
            @Override
            public void onResponse(String response, int id) {
                LogUtil.d("应用更新:" + response);
                try {
                    final ApplicationEntity entity = JsonUtils.deserialize(response, ApplicationEntity.class);

                    if (AppInfoUtil.getVersionCode(App.instance) < Integer.parseInt(entity.getVersion())) {
                        String content = String.format("最新版本：%1$s\napp名字：%2$s\n\n更新内容\n%3$s", entity.getVersionShort(), entity.getName(), entity.getChangelog());
                        UpdateDialog.show(context, content, new UpdateDialog.OnUpdate() {
                            @Override
                            public void cancel() {

                            }

                            @Override
                            public void ok() {

                                DownloadUtil.downloadApk(App.instance, entity.getInstall_url(), entity.getName(), entity.getChangelog(), "xiuqu.apk");
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
