package com.fete.common.okhttp.base;

import android.text.TextUtils;

import com.fete.basemodel.utils.TimeManager;

import java.io.IOException;
import java.util.Date;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http.HttpDate;

public class TimeCalibrationInterceptor implements Interceptor {
    //获取服务器时间更新  TimeManager.getInstance().initServerTime(parse.getTime());
    long minResponseTime = Long.MAX_VALUE;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.nanoTime();
        Response response = chain.proceed(request);
        long responseTime = System.nanoTime() - startTime;

        Headers headers = response.headers();
        calibration(responseTime, headers);
        return response;
    }

    private void calibration(long responseTime, Headers headers) {
        if (headers == null) {
            return;
        }

        //如果这一次的请求响应时间小于上一次，则更新本地维护的时间
        if (responseTime >= minResponseTime) {
            return;
        }

        String standardTime = headers.get("Date");
        if (!TextUtils.isEmpty(standardTime)) {
            Date parse = HttpDate.parse(standardTime);
            if (parse != null) {
                // 客户端请求过程一般大于比收到响应时间耗时，所以没有简单的除2 加上去，而是直接用该时间
                TimeManager.getInstance().initServerTime(parse.getTime());
                minResponseTime = responseTime;
            }
        }
    }
}