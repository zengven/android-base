package com.github.utils;

import android.util.Log;



/**
 * author: zengven
 * date: 2017/6/7 11:50
 * desc: log日志打印
 */
public class Logger {

    private static final String APP_LOG_TAG = "Ticket";
    private static final boolean isDebug = true;//发布环境需置为false

    private Logger() {
    }

    public static void e(Object msg) {
        if (isDebug)
            Log.e(getTag(4), msg == null ? "null" : msg.toString());
    }

    public static void d(Object msg) {
        if (isDebug)
            Log.d(getTag(4), msg == null ? "null" : msg.toString());
    }

    public static void i(Object msg) {
        if (isDebug)
            Log.i(getTag(4), msg == null ? "null" : msg.toString());
    }

    public static void w(Object msg) {
        if (isDebug)
            Log.w(getTag(4), msg == null ? "null" : msg.toString());
    }

    private static String getTag(int level) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[level];
        StringBuilder sb = new StringBuilder();
        sb.append(APP_LOG_TAG);
        sb.append("-[");
        sb.append(getSimpleClassName(ste.getClassName()));
        sb.append('.');
        sb.append(ste.getMethodName());
        sb.append("]-");
        sb.append('(');
        sb.append(ste.getLineNumber());
        sb.append(')');
        return sb.toString();
    }

    private static String getSimpleClassName(String path) {
        int index = path.lastIndexOf('.');
        if (index < 0) {
            return path;
        } else {
            return path.substring(index + 1);
        }
    }
}
