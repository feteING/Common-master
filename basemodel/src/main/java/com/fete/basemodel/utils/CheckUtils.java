package com.fete.basemodel.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtils {


    private CheckUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }




    /**
     * 判断手机号码是否合理
     */
    public static boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11) && isMobileNO(phoneNums)) {
            return true;
        }
        return false;
    }
    /**
     * 判断固定号码是否合理
     */
    public static boolean judgeSolidPhone(String solidPhone){
        String regex = "^0\\d{2,3}\\d{7,8}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(solidPhone);
        if(matcher.find()){
            return true;
        }else{
            return false;
        }

    }



    /**
     * 判断密码是否合理
     * 1 代表通过验证
     * -1 代表中间有空格
     * -2 代表中间有汉字
     * -3 代表有特殊符号
     * -4 代表全是数字
     */
    public static int judgePassword(String password) {

        if (password.length() > 0) {
            //判断是否有空格字符串
            for (int t = 0; t < password.length(); t++) {
                String b = password.substring(t, t + 1);
                if (b.equals(" ")) {
                    return -1;//代表有空格
                }
            }

            //判断是否有汉字
            int count = 0;
            String regEx = "[\\u4e00-\\u9fa5]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(password);
            while (m.find()) {
                for (int i = 0; i <= m.groupCount(); i++) {
                    count = count + 1;
                }
            }

            if(count>0){
                return -2;// 代表有汉字
            }

            //判断是否是字母和数字
            int numberCounter = 0;
            for (int i = 0; i < password.length(); i++) {
                char c = password.charAt(i);
                if (!Character.isLetterOrDigit(c)) {
                    return -3;// 代表有 不是字母也不是数字的字符.
                }
                if (Character.isDigit(c)) {
                    numberCounter++;
                }
            }
            if(password.length()==numberCounter){
                return -4;// 代表全是数字
            }
        }
        return 1;
    }

    /**
     * 判断身份证号是否合理
     * [1-9]{6}(19|20)[0-9]{2}((0[1-9]{1})|(1[1-2]{1}))((0[1-9]{1})|([1-2]{1}[0-9]{1}|(3[0-1]{1})))[0-9]{3}[0-9xX]{1}
     */
    public static boolean judgeIDCard(String idcard){
//        String regex = "[1-9]{6}(19|20)[0-9]{2}((0[1-9]{1})|(1[1-2]{1}))((0[1-9]{1})|([1-2]{1}[0-9]{1}|(3[0-1]{1})))[0-9]{3}[0-9xX]{1}";
        String regex = "[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(idcard);
        if(m.find()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断车牌号是否合理
     */
    public static boolean judgeCPH(String cph){
        String regex = "(([\\u4e00-\\u9fa5]{1}[A-Z]{1})[-]?|([wW][Jj][\\u4e00-\\u9fa5]{1}[-]?)|([a-zA-Z]{2}))([A-Za-z0-9]{5}|[DdFf][A-HJ-NP-Za-hj-np-z0-9][0-9]{4}|[0-9]{5}[DdFf])";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(cph);
        if(m.find()){
            return true;
        }else{
            return false;
        }
    }









    /**
     * 判断字符串的长度是否与给定的长度相匹配
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

}
