package com.optimusprime.xframe.utils;

/**
 * Author:　Created by benjamin
 * DATE :  2017/2/15 14:07
 * 本项目中的log，上线时可以关闭log日志输出
 */

import android.util.Log;

/**
 * Log统一管理类
 */
public class XLog {

    private XLog() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
    private static final String TAG = "way";

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (isDebug)
            Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.v(tag, msg);
    }

    public static void _assert(boolean condition,String message){
        if(isDebug == false)
            return;
        if(condition){
            throw new AssertionError(message);
        }
    }

    public static void _assert(String message){
        if(isDebug == false)
            return;
        throw new AssertionError(message);
    }
}
