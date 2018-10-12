package com.fete.basemodel.utils;

import android.graphics.Color;

/**
 * Created by wangshengru on 16/1/20.
 * 获取颜色
 */
public class ColorUtil {

    public static int white(){
        return Color.rgb(255,255,255);
    }

    public static int red(){
        return Color.rgb(255,0,0);
    }

    public static int orange(){
        return Color.parseColor("#ff8903");
    }

    public static int green(){
        return Color.rgb(19,197,78);
    }

    public static int gray(){
        return Color.rgb(151,151,151);
    }

    public static int blue6c(){
        return Color.parseColor("#6C94F7");
    }

    public static int blackFont(){
        return Color.rgb(74,74,74);
    }

    public static int greyLine(){
        return Color.rgb(238,238,238);
    }

    public static int blue(){
        return Color.rgb(78,143,255);
    }

    public static int GREY_AFAFAF = Color.parseColor("#AFAFAF");
    public static int BLACK_373737 = Color.parseColor("#373737");
    public static int ORANGE_FONT = Color.parseColor("#FF8903");   // 橙色颜色
    public static int BLACK_TABLE_FONT = Color.parseColor("#373737"); // 表格字体
    public static int TABLE_FIRST_ROW_BG = Color.parseColor("#FFF8ED"); // 表格第一行背景色
    public static int TABLE_ROW_BG_DOUBLE = Color.parseColor("#F8F8F8"); // 表格单数行背景色
    public static int TABLE_ROW_BG_SINGLE = Color.parseColor("#FFFFFF");// 双数行背景色

    /**
     * 计算从startColor过度到endColor过程中百分比为franch时的颜色值
     * @param startColor 起始颜色 int类型
     * @param endColor 结束颜色 int类型
     * @param franch franch 百分比0.5
     * @return 返回int格式的color
     */
    public static int caculateColor(int startColor, int endColor, float franch){
        String strStartColor = "#" + Integer.toHexString(startColor);
        String strEndColor = "#" + Integer.toHexString(endColor);
        return Color.parseColor(caculateColor(strStartColor, strEndColor, franch));
    }

    /**
     * 计算从startColor过度到endColor过程中百分比为franch时的颜色值
     * @param startColor 起始颜色 （格式#FFFFFFFF）
     * @param endColor 结束颜色 （格式#FFFFFFFF）
     * @param franch 百分比0.5
     * @return 返回String格式的color（格式#FFFFFFFF）
     */
    public static String caculateColor(String startColor, String endColor, float franch){

        int startAlpha = Integer.parseInt(startColor.substring(1, 3), 16);
        int startRed = Integer.parseInt(startColor.substring(3, 5), 16);
        int startGreen = Integer.parseInt(startColor.substring(5, 7), 16);
        int startBlue = Integer.parseInt(startColor.substring(7), 16);

        int endAlpha = Integer.parseInt(endColor.substring(1, 3), 16);
        int endRed = Integer.parseInt(endColor.substring(3, 5), 16);
        int endGreen = Integer.parseInt(endColor.substring(5, 7), 16);
        int endBlue = Integer.parseInt(endColor.substring(7), 16);

        int currentAlpha = (int) ((endAlpha - startAlpha) * franch + startAlpha);
        int currentRed = (int) ((endRed - startRed) * franch + startRed);
        int currentGreen = (int) ((endGreen - startGreen) * franch + startGreen);
        int currentBlue = (int) ((endBlue - startBlue) * franch + startBlue);

        return "#" + getHexString(currentAlpha) + getHexString(currentRed)
                + getHexString(currentGreen) + getHexString(currentBlue);

    }

    /**
     * 将10进制颜色值转换成16进制。
     */
    public static String getHexString(int value) {
        String hexString = Integer.toHexString(value);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        return hexString;
    }

    /**
     * 将16进制颜色值转化成int类型的颜色值
     * @param hexStr #****** 比如 #898989
     * @return int类型的颜色值 比如
     */
    public static int parseColor(String hexStr){
        int color ;
        try {
            color = Color.parseColor(hexStr);
        }catch (IllegalArgumentException ignored){
            color = 0; // 防止颜色配置错误直接崩溃
        }
        return color;
    }


}
