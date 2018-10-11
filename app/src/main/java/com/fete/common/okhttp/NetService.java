package com.fete.common.okhttp;

import com.fete.common.okhttp.base.BaseService;
import com.fete.common.okhttp.callback.BaseAllCallBack;

import java.util.TreeMap;

/**
 * Created by haier on 2016/8/3.
 */
public class NetService {


    /**
     * 检测更新
     *
     * @param callback
     */
    public static void checkUpdate(String url, BaseAllCallBack callback) {
        TreeMap<String, String> parames = new TreeMap<>();
        BaseService.doGet(url, parames, callback);
    }


    /**
     * 登录
     *
     * @param callback
     */
    public static void loginPwd(String phone, String passwords, BaseAllCallBack callback) {
        TreeMap<String, String> parames = new TreeMap<>();
        parames.put("phone", phone);
        parames.put("passwords", passwords);
        BaseService.doPost(NetApi.loginPwd, parames, callback);
    }

    /**
     * 注册
     *
     * @param callback
     */
    public static void registPwd(String phone, String passwords, String code, BaseAllCallBack callback) {
        TreeMap<String, String> parames = new TreeMap<>();
        parames.put("phone", phone);
        parames.put("passwords", passwords);
        parames.put("code", code);
        BaseService.doPost(NetApi.registPwd, parames, callback);
    }

    /**
     * 发送验证码
     *
     * @param callback
     */
    public static void sendCode(String phone, BaseAllCallBack callback) {
        TreeMap<String, String> parames = new TreeMap<>();
        BaseService.doPost(NetApi.sentCode, parames, callback);
    }


}
