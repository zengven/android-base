package com.github.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

/**
 * author: zengven
 * date: 2017/9/22 15:30
 * desc: 本地广播注册发送工具类
 */
public class LocalBroadcastUtil {

    public static class Action {
        // TODO: 2017/9/22 add action in this
        public static final String ACTION_TICKET_PRINT = "com.ihuijiayou.ticket.PRINT"; //小票打印action
    }

    private LocalBroadcastUtil() {
    }

    /**
     * 注册广播
     *
     * @param context
     * @param receiver
     * @param actions
     */
    public static void registerReceiver(Context context, BroadcastReceiver receiver, String... actions) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context.getApplicationContext());
        IntentFilter intentFilter = new IntentFilter();
        for (String action : actions) {
            intentFilter.addAction(action);
        }
        localBroadcastManager.registerReceiver(receiver, intentFilter);
    }

    /**
     * 取消注册广播
     *
     * @param context
     * @param receiver
     */
    public static void unregisterReceiver(Context context, BroadcastReceiver receiver) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context.getApplicationContext());
        localBroadcastManager.unregisterReceiver(receiver);
    }

    /**
     * 发送广播
     *
     * @param context
     * @param action
     */
    public static void sendBroadcast(Context context, String action) {
        sendBroadcast(context, action, null);
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
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context.getApplicationContext());
        localBroadcastManager.sendBroadcast(intent);
    }

    /**
     * 发送同步广播
     *
     * @param context
     * @param action
     */
    public static void sendBroadcastSync(Context context, String action) {
        sendBroadcastSync(context, action, null);
    }

    /**
     * 发送同步广播
     *
     * @param context
     * @param action
     * @param extras
     */
    public static void sendBroadcastSync(Context context, String action, Bundle extras) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (extras != null) {
            intent.putExtras(extras);
        }
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context.getApplicationContext());
        localBroadcastManager.sendBroadcastSync(intent);
    }
}
