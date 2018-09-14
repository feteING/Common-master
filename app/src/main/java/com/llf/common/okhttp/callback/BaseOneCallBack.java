package com.llf.common.okhttp.callback;

import java.text.ParseException;

/**
 * Created by haier on 2016/7/18.
 */
public abstract class BaseOneCallBack extends BaseAllCallBack {


    /**
     * 返回 200 时，成功连接
     *
     * @param response
     * @param id
     * @throws ParseException
     */
    public abstract void onResponse(String response, int id);


}
