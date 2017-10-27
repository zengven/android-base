package com.github.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.github.R;

import java.util.List;

/**
 * author: zengven
 * date: 2017/6/12 15:32
 * desc: application相关工具类
 */

public class AppUtil {

    private static final String KEY_SHORTCUT = "ShortCut";

    /**
     * 获取应用程序名称
     *
     * @param context
     */
    public static String getAppName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int labelRes = packageInfo.applicationInfo.labelRes;
        return context.getResources().getString(labelRes);
    }

    /**
     * get app version name
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 程序是否在前台运行
     *
     * @return true: Foreground, false: background
     */
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取渠道名
     *
     * @param context
     * @return Y: channel name, N: null
     */
    public static String getChannelName(Context context) {
        if (context == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = applicationInfo.metaData.getString("UMENG_CHANNEL");
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }


    /**
     * get mate data value by name
     *
     * @param context
     * @param name
     * @return
     */
    public static Object getApplicationMetaDataValue(Context context, String name) {
        if (context == null)
            return null;
        Object value = null;
        PackageManager packageManager = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != applicationInfo && null != applicationInfo.metaData) {
                value = applicationInfo.metaData.get(name);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 创建桌面快捷方式
     *
     * @param activity
     */
    public static void createShortCut(Activity activity) {
        Context context = activity.getApplicationContext();
        if (PreferencesUtil.getInstance(context).getBoolean(KEY_SHORTCUT, false))
            return;
        Intent shortcutIntent = new Intent();
        shortcutIntent.setClass(context, activity.getClass());
        //以下两句是为了在卸载应用的时候同时删除桌面快捷方式
        shortcutIntent.setAction("android.intent.action.MAIN");
        shortcutIntent.addCategory("android.intent.category.LAUNCHER");

        //创建快捷方式的Intent
        Intent addIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //不允许重复创建
        addIntent.putExtra("duplicate", false);
        //快捷名称名称
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
        //快捷图片
        Intent.ShortcutIconResource shortcutIconResource = Intent.ShortcutIconResource.fromContext(context, R.mipmap.ic_launcher);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, shortcutIconResource);
        //点击快捷图片，运行的程序主入口
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        context.sendBroadcast(addIntent);
        PreferencesUtil.getInstance(context).putBoolean(KEY_SHORTCUT, true);
    }

    /**
     * 移除快捷方式
     *
     * @param activity
     */
    public static void removeShortCut(Activity activity) {
        Context context = activity.getApplicationContext();
        Intent shortcutIntent = new Intent();
        shortcutIntent.setClass(context, activity.getClass());
        //以下两句是为了在卸载应用的时候同时删除桌面快捷方式
        shortcutIntent.setAction("android.intent.action.MAIN");
        shortcutIntent.addCategory("android.intent.category.LAUNCHER");

        Intent removeIntent = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        removeIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
        removeIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        removeIntent.putExtra("duplicate", false);
        context.sendBroadcast(removeIntent);
    }


}
