package com.fete.common.okhttp.base;

import java.io.Serializable;

/**
 * baseservice 头截取
 * Created by haier on 2016/8/3.
 */
public class ResultModel implements Serializable {
    private String msg;
    private int code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    @Override
    public String toString() {
        return "ResultModel{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                '}';
    }
}
