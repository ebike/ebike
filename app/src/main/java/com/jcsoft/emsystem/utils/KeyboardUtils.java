package com.jcsoft.emsystem.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jimmy on 2015/11/9.
 */
public class KeyboardUtils {
    /**
     * 隐藏软件输入法
     *
     * @param activity Activity
     */
    public static void hideSoftInput(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static void hideSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //得到InputMethodManager的实例
        if (imm.isActive()) {//如果开启
            //关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 关闭已经显示的输入法窗口。
     *
     * @param context      上下文对象，一般为Activity
     * @param focusingView 输入法所在焦点的View
     */
    public static void closeSoftInput(Context context, View focusingView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(focusingView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public static void showSoftInput(Context ctx, View view) {
        if (ctx == null || view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void toggleSoftInput(Context ctx, View view) {
        if (ctx == null || view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    public static void openKeybord(final Context context) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 200);
    }

    public static void openKeybord(final Context context, final View view) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(view, 0);
            }
        }, 200);
    }

}
