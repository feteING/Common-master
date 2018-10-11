package com.fete.common.okhttp;

/**
 *
 */
public class NetApi {


    //基础url
    public static String BASE_URL = "https://api.91jsgo.com";

    //获取banner
    public static final String bannerInfo = BASE_URL + "/s1/banner/bannerInfoNew";
    //密码登录
    public static final String loginPwd = BASE_URL + "/s1/login/pwd";
    //密码注册
    public static final String registPwd = BASE_URL + "/s1/regist/pwd";
    //发送验证码
    public static final String sentCode = BASE_URL + "/s1/login/code";

}