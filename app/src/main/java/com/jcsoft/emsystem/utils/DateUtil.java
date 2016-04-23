package com.jcsoft.emsystem.utils;

/**
 * 时间工具类
 * Created by jimmy on 16/4/23.
 */
public class DateUtil {

    /**
     * yyyy年MM月dd日 转 yyyy-MM-dd
     */
    public static String changeDateFormat(String date) {
        if (!CommonUtils.strIsEmpty(date) && date.length() == 11) {
            return date.substring(0, 4) + "-" + date.substring(5, 7) + "-" + date.substring(8, 10);
        }
        return "";
    }
}
