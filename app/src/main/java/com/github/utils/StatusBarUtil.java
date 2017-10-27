package com.github.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * author: zengven
 * date: 2017/6/20 18:02
 * desc: 状态栏工具类
 */
public class StatusBarUtil {

    private StatusBarUtil() {
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity
     * @param dark
     */
    public static void setStatusBarStyle(Activity activity, boolean dark) {
        int romType = PreferencesUtil.getInstance(activity).getInt("romType", -1);
        if (romType == -1) {
            romType = RomUtil.getRomType();
            PreferencesUtil.getInstance(activity.getApplicationContext()).putInt("romType", romType);
        }
        if (romType == RomUtil.RomType.MIUI) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                setTranslucentStatus(activity, true);
            }
            setMIUILightStatusBar(activity, dark);
        } else if (romType == RomUtil.RomType.FLYME) {
            setFlymeLightStatusBar(activity, dark);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setNativeMorOboveStatusBarStyle(activity, dark);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setNativeLStatusBarStyle(activity, Color.BLACK);
            }
        }

    }

    /**
     * 下面是调用状态栏 是否为darkmode。
     *
     * @param activity
     * @param darkmode
     */
    private static void setMIUILightStatusBar(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * miuiv6只支持4.4及以上版本，调用状态栏透明的方法可以直接用原生的安卓方法
     *
     * @param activity
     * @param on
     */
    @TargetApi(19)
    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private static void setFlymeLightStatusBar(Activity activity, boolean dark) {
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
            } catch (Exception e) {
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void setNativeMorOboveStatusBarStyle(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            int flags = decor.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decor.setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(Color.WHITE);
        } else {
            // We want to change tint color to white again.
            // You can also record the flags in advance so that you can turn UI back completely if
            // you have set other flags before, such as translucent or full screen.
            decor.setSystemUiVisibility(0);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setNativeLStatusBarStyle(Activity activity, int color) {
        Window window = activity.getWindow();
        //After LOLLIPOP not translucent status bar
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //Then call setStatusBarColor.
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }
}
