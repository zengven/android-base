package com.github.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

/**
 * author: zengven
 * date: 2017/9/27 16:54
 * desc: 全局广播注册发送工具类
 */
public class BroadcastUtil {

    private BroadcastUtil() {
        throw new RuntimeException(" don't fuck me !");
    }

    /**
     * 注册广播
     *
     * @param context
     * @param receiver
     * @param actions
     */
    public static void registerReceiver(Context context, BroadcastReceiver receiver, String... actions) {
        IntentFilter intentFilter = new IntentFilter();
        for (String action : actions) {
            intentFilter.addAction(action);
        }
        context.registerReceiver(receiver, intentFilter);
    }

    /**
     * 取消注册广播
     *
     * @param context
     * @param receiver
     */
    public static void unregisterReceiver(Context context, BroadcastReceiver receiver) {
        context.unregisterReceiver(receiver);
    }

    /**
     * 发送广播
     *
     * @param context
     * @param action
     */
    public static void sendBroadcast(Context context, String action) {
        sendBroadcast(context, action);
    }

    /**
     * 发送广播
     *
     * @param context
     * @param action
     * @param extras
     */
    public static void sendBroadcast(Context context, String action, Bundle extras) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.sendBroadcast(intent);
    }
}
