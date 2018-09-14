package com.llf.common.okhttp.business;

/**
 * Created by A on 2018/6/14.
 */

public class CoreBusinessUtil {


    private static CoreBusinessUtil coreBusinessUtil = null;


    private CoreBusinessUtil() {
    }


    public static CoreBusinessUtil getInstance() {
        if (coreBusinessUtil == null) {
            synchronized (CoreBusinessUtil.class) {
                if (coreBusinessUtil == null) {
                    coreBusinessUtil = new CoreBusinessUtil();
                }
            }
        }
        return coreBusinessUtil;
    }




}
