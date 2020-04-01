package com.xts.picgame.utils;

import java.text.DecimalFormat;

public class NumberUtil {
    /**
     * 将float数字转换成小数位为2位的字符串  如:3.34343 -->3.34
     * @param data
     * @return
     */
    public static String parseFloat2String(float data){
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(data);
    }
}
