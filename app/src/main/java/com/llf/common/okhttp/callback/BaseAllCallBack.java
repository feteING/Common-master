package com.llf.common.okhttp.callback;

/**
 * Created by haier on 2016/7/18.
 */
public abstract class BaseAllCallBack {

    /**
     * @param e
     * @param id
     */
    public void onError(Exception e, int id) {
    }

    /**
     * @param id
     */
    public void onAfter(int id) {
    }

    /**
     * @param progress
     * @param total
     * @param id
     */
    public void inProgress(float progress, long total, int id) {
    }

    /**
     * @param response
     * @param id
     */
    public void onResponse(String response, int id) {
    }


}
