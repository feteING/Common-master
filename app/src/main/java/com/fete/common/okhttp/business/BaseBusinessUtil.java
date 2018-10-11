package com.fete.common.okhttp.business;

import android.content.Context;

import com.fete.basemodel.dialog.UpdateDialog;
import com.fete.basemodel.utils.AppInfoUtil;
import com.fete.basemodel.utils.DownloadUtil;
import com.fete.basemodel.utils.JsonUtils;
import com.fete.basemodel.utils.LogUtil;
import com.fete.common.App;
import com.fete.common.okhttp.NetService;
import com.fete.common.okhttp.bean.UpdateAppModel;
import com.fete.common.okhttp.callback.BaseOneCallBack;

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
        NetService.checkUpdate(url, new BaseOneCallBack() {
            @Override
            public void onResponse(String response, int id) {
                LogUtil.d("应用更新:" + response);
                try {
                    final UpdateAppModel entity = JsonUtils.deserialize(response, UpdateAppModel.class);

                    if (AppInfoUtil.getVersionCode(App.instance) < Integer.parseInt(entity.getVersion())) {
                        String content = String.format("最新版本：%1$s\napp名字：%2$s\n\n更新内容\n%3$s", entity.getVersionShort(), entity.getName(), entity.getChangelog());
                        UpdateDialog.show(context, content, new UpdateDialog.OnUpdate() {
                            @Override
                            public void cancel() {

                            }

                            @Override
                            public void ok() {

                                DownloadUtil.downloadApk(App.instance, entity.getInstall_url(), entity.getName(), entity.getChangelog(), DownloadUtil.APPNAME);
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
