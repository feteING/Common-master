package com.llf.common.okhttp.bean;

import java.io.Serializable;

/**
 * Created by A on 2018/8/9.
 */

public class AlipayModel implements Serializable {


    /**
     * code : 1000000
     * msg : 成功
     * data : {"account":"yJM1VP","mchNo":"1533606281J2njOq","orderCode":"2c716a9dce1643d78f03df373e3d1fee","payUrl":"http://www.lanjunshop.com/pay2.html?payId=2b3c41289ae411e8b15d00163e0e5530&price=0.01","price":"0.01","realPrice":"0.01","sign":"2b742619f6a9fe5dd4783764907686e2"}
     */

    private String code;
    private String msg;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * account : yJM1VP
         * mchNo : 1533606281J2njOq
         * orderCode : 2c716a9dce1643d78f03df373e3d1fee
         * payUrl : http://www.lanjunshop.com/pay2.html?payId=2b3c41289ae411e8b15d00163e0e5530&price=0.01
         * price : 0.01
         * realPrice : 0.01
         * sign : 2b742619f6a9fe5dd4783764907686e2
         */

        private String account;
        private String mchNo;
        private String orderCode;
        private String payUrl;
        private String price;
        private String realPrice;
        private String sign;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getMchNo() {
            return mchNo;
        }

        public void setMchNo(String mchNo) {
            this.mchNo = mchNo;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public String getPayUrl() {
            return payUrl;
        }

        public void setPayUrl(String payUrl) {
            this.payUrl = payUrl;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getRealPrice() {
            return realPrice;
        }

        public void setRealPrice(String realPrice) {
            this.realPrice = realPrice;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "account='" + account + '\'' +
                    ", mchNo='" + mchNo + '\'' +
                    ", orderCode='" + orderCode + '\'' +
                    ", payUrl='" + payUrl + '\'' +
                    ", price='" + price + '\'' +
                    ", realPrice='" + realPrice + '\'' +
                    ", sign='" + sign + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AlipayModel{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
