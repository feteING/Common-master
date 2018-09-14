package com.llf.common.okhttp;

import com.llf.common.okhttp.base.BaseService;
import com.llf.common.okhttp.callback.BaseAllCallBack;

import java.util.TreeMap;

/**
 * Created by haier on 2016/8/3.
 */
public class MyService {


    /**
     * 检测更新
     *
     * @param callback
     */
    public static void checkUpdate(String url, BaseAllCallBack callback) {
        TreeMap<String, String> parames = new TreeMap<>();
        BaseService.doGet(url, parames, callback);
    }


}
