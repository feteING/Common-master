package com.fete.common.okhttp.business;

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

    /**
     * 直售与延期发货的 取消订单
     *
     * @param context
     * @param orderId
     * @param coreTwoCallBack
     */
/*    public void cancelOrderDirect(Context context, String orderId, CoreAllCallBack coreTwoCallBack) {
        UserData user = SPUtils.getUser(context);
        MyService.orderCancelDirect(user, orderId, new BaseOneCallBack() {
            @Override
            public void onResponse(String response, int id) {
                BaseBean baseBean = BaseService.parseObject(response, BaseBean.class);
                if (baseBean != null && baseBean.getCode().equals(Constant.CODE_SUCCESS)) {
                    coreTwoCallBack.onSucess();
                } else {
                    try {
                        LogTest.e("CoreBusinessUtil_orderCancelDirect:" + baseBean.getMsg());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    coreTwoCallBack.onFaile();
                }

            }
        });


    }*/


}
