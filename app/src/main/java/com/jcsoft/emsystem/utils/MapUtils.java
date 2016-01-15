package com.jcsoft.emsystem.utils;

/**
 * Created by jimmy on 16/1/15.
 */
public class MapUtils {

    /**
     * 根据航向返回反向
     * @param heading
     * @return
     */
    public static String directionStr(int heading)
    {
        String d = "";
        if(heading > 22.5 && heading <= 67.5)
        {
            d = "东北方向";
        }
        if(heading > 67.5 && heading <= 112.5)
        {
            d = "正北方向";
        }
        if(heading > 112.5 && heading <= 157.5)
        {
            d = "东南方向";
        }
        if(heading > 157.5 && heading <= 202.5)
        {
            d = "正南方向";
        }
        if(heading > 202.5 && heading <= 247.5)
        {
            d = "西南方向";
        }
        if(heading > 247.5 && heading <= 292.5)
        {
            d = "正西方向";
        }
        if(heading > 292.5 && heading <= 337.5)
        {
            d = "西北方向";
        }
        if(heading > 337.5 || heading <= 22.5)
        {
            d = "正北方向";
        }
        return d;
    }
}
