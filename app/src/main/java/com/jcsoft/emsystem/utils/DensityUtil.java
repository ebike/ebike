package com.jcsoft.emsystem.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import java.util.logging.Logger;

/**
 * 获取设备的 分辨率 像素等参数
 */
public class DensityUtil {
    private static final String TAG = "dive";

    // 当前屏幕的densityDpi
    private static float dmDensityDpi = 0.0f;
    private static DisplayMetrics dm;
    private static float scale = 0.0f;

    /**
     * 根据构造函数获得当前手机的屏幕系数
     * */
    public DensityUtil(Context context) {
        // 获取当前屏幕
        dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        // 设置DensityDpi
        setDmDensityDpi(dm.densityDpi);
        // 密度因子
        scale = getDmDensityDpi() / 160;
    }

    /**
     * 当前屏幕的density因子
     * @retrun DmDensity Getter
     * */
    public static float getDmDensityDpi() {
        return dmDensityDpi;
    }

    /**
     * 当前屏幕的density因子
     * @retrun DmDensity Setter
     * */
    public static void setDmDensityDpi(float dmDensityDpi) {
        DensityUtil.dmDensityDpi = dmDensityDpi;
    }

    /**
     * 密度转换像素
     * */
    public static int dip2px(float dipValue) {

        return (int) (dipValue * scale + 0.5f);

    }

    /**
     * 像素转换密度
     * */
    public static int px2dip(float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }

    @Override
    public String toString() {
        return " dmDensityDpi:" + dmDensityDpi;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * @param context 上下文，一般为Activity
     * @param dpValue dp数据值
     * @return px像素值
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     * @param context 上下文，一般为Activity
     * @param pxValue px像素值
     * @return dp数据值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 屏幕密度
     * @return
     */
    public static float getDisplayDensity() {
        return dm.density;
    }

    /**
     * 屏幕密度DPI
     * @return
     */
    public static int getDisplayDpi(){
        return dm.densityDpi;
    }

    //获取屏幕宽度
    public static int getWidthPixels(){
        return dm.widthPixels;
    }
}
