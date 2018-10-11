package com.fete.common.okhttp.base;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.fete.basemodel.base.BaseApplication;
import com.fete.basemodel.utils.CommonUtils;
import com.fete.basemodel.utils.LogTest;
import com.fete.basemodel.utils.ToastUtil;
import com.fete.common.okhttp.AppConfig;
import com.fete.common.okhttp.callback.BaseAllCallBack;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.OkHttpClient;


/**
 * base
 */
public class BaseService {

    static Context context;

    /**
     * 使用初始化
     */
    public static void init(Context con) {
        context = con;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(15000L, TimeUnit.MILLISECONDS)
                .readTimeout(15000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new TimeCalibrationInterceptor())//保存服务器时间
                .sslSocketFactory(getSocketFactory())
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
//                .addInterceptor(new RetryIntercepter(4))//重试次数
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

    }


    /**
     * 获取公共请求头
     *
     * @return
     */
    public static Map<String, String> getHeader(TreeMap<String, String> parames) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)");
        return map;
    }


    /**
     * 获取公共body
     *
     * @return
     */
    public static TreeMap<String, String> getBody(TreeMap<String, String> parames) {
        TreeMap<String, String> map = new TreeMap<>();
        map.putAll(parames);
        //packageid处理
        String packageId = CommonUtils.getAppPkgName(BaseApplication.instance);
        //appVersion
        String appVersion = CommonUtils.getVersionName(BaseApplication.instance);
        map.put("appVersion", appVersion);
        map.put("packageId", packageId);
        map.put("appType", "1");
        return map;
    }


    /**
     * 错误处理_默认
     */
    public static void doNetError(Exception e, int id) {
        LogTest.e("httpurlinfoerror:" + curUrl + ":" + e.toString());
        if (context != null) {
            if (!CommonUtils.isNetworkAvailable(context)) {
                ToastUtil.sToastUtil.shortDuration(AppConfig.IsNetWorkError);
            } else {
                ToastUtil.sToastUtil.shortDuration(AppConfig.IsServiceError);
            }
        }

    }

    //当前url
    static String curUrl;

    /**
     * 网络请求
     *
     * @param url
     * @param params
     * @param baseCallBack
     */
    public static void doGet(final String url, final TreeMap<String, String> params, final BaseAllCallBack baseCallBack) {
        curUrl = url;
        //头部
        Map<String, String> body = getBody(params);
        Map<String, String> header = BaseService.getHeader(params);
        LogTest.e("httpurlinfo:" + curUrl + "_httpHeaderParames:" + header.toString() + "httpBodyParames:" + body.toString());
        OkHttpUtils
                .get()
                .url(url)
                .headers(header)
                .params(body)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        doNetError(e, id);
                        baseCallBack.onError(e, id);
                    }

                    /**
                     * @param response
                     * @param id
                     */
                    @Override
                    public void onResponse(String response, int id) {
                        ResultModel resultModel = null;
                        try {
                            resultModel = BaseService.parseObject(response, ResultModel.class);
                            int code = resultModel.getCode();
                            String msg = resultModel.getMsg();
                            if (code != 1000000) {
                                LogTest.e("====<错误接口url:" + curUrl + "   result:" + resultModel.toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            baseCallBack.onResponse(response, id);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });

    }

    private static SSLSocketFactory getSocketFactory() {
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        // Install the all-trusting trust manager
        final SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            return sslSocketFactory;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 网络请求
     *
     * @param url
     * @param params
     * @param baseCallBack
     */
    public static void doPost(final String url, final TreeMap<String, String> params, final BaseAllCallBack baseCallBack) {

        curUrl = url;
        //头部
        TreeMap<String, String> body = getBody(params);
        Map<String, String> header = BaseService.getHeader(body);

        LogTest.e("httpurlinfo:" + curUrl + "_httpBodyParames:" + body.toString());
        try {
            OkHttpUtils
                    .post()
                    .url(url)
//                    .headers(header)
                    .params(body)
//                    .hostnameVerifier(new HostnameVerifier() {
//                        @Override
//                        public boolean verify(String hostname, SSLSession session) {
//                            return true;
//                        }
//                    })
                    .build()
//                    .sslSocketFactory(getSocketFactory())
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            doNetError(e, id);
                            baseCallBack.onError(e, id);
                        }

                        @Override
                        public void onResponse(String response, int id) {

                            LogTest.e("baseservice_json", response);
                            ResultModel resultModel = null;
                            try {
                                resultModel = BaseService.parseObject(response, ResultModel.class);
                                int code = resultModel.getCode();
                                String msg = resultModel.getMsg();
                                if (code != 1000000) {
                                    LogTest.e("httpurlinfo:" + curUrl + "   result:" + resultModel.toString());
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            try {
                                baseCallBack.onResponse(response, id);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param response
     * @param model
     * @param <T>
     * @return
     */
    public static <T extends Serializable> T parseObject(String response, Class model) {
        Serializable serializable = null;
        try {
            serializable = (Serializable) JSON.parseObject(response, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) serializable;
    }


}
