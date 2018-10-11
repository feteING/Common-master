package com.fete.common.okhttp;

import android.content.Context;

import com.fete.basemodel.dp.ACache;

import java.util.ArrayList;

/**
 * Created by haier on 2016/8/3.
 */
public class DbService {


    /**
     * 是否第一次登陆app
     */
    public static boolean isFirstLogin(Context context) {
        boolean isFirst = true;
        ACache mCache = ACache.get(context);
        String isFirstLogin = mCache.getAsString(DbApi.ISFIRSTLOGIN);

        if ("false".equals(isFirstLogin)) {
            isFirst = false;
        } else {
            isFirst = true;
            mCache.put(DbApi.ISFIRSTLOGIN, "false");
        }
        return isFirst;

    }

    public static ArrayList<String> getUserNamePwd(Context context) {
        ArrayList list = new ArrayList();
        ACache mCache = ACache.get(context);
        String userName = mCache.getAsString(DbApi.USERNAME);
        String userPwd = mCache.getAsString(DbApi.USERPWD);
        list.add(userName);
        list.add(userPwd);
        return list;
    }

    public static void setUserNamePwd(Context context, String name, String pwd) {
        ACache mCache = ACache.get(context);
        mCache.put(DbApi.USERNAME, name);
        mCache.put(DbApi.USERPWD, pwd);
    }


}
