package com.github.utils;

import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * author: zengven
 * date: 2017/6/20 18:03
 * desc: 获取rom 信息
 */
public class RomUtil {

    class RomType {
        public static final int ANDROID_NATIVE = 0;
        public static final int MIUI = 1;
        public static final int FLYME = 2;
    }

    public static int getRomType() {
        if (isMIUI()) {
            return RomType.MIUI;
        }

        if (isFlyme()) {
            return RomType.FLYME;
        }

        return RomType.ANDROID_NATIVE;
    }

    /**
     * is flyme
     * Flyme V4的displayId格式为 [Flyme OS 4.x.x.xA]
     * Flyme V5的displayId格式为 [Flyme 5.x.x.x beta]
     *
     * @return
     */
    private static boolean isFlyme() {
        String displayId = Build.DISPLAY;
        if (!TextUtils.isEmpty(displayId) && displayId.contains("Flyme")) {
            String[] displayIdArray = displayId.split(" ");
            for (String temp : displayIdArray) {
                //版本号4以上，形如4.x.
                if (temp.matches("^[4-9]\\.(\\d+\\.)+\\S*")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * is miui
     * MIUI V6对应的versionCode是4
     * MIUI V7对应的versionCode是5
     *
     * @return
     */
    private static boolean isMIUI() {
        String miuiVersionCodeStr = getSystemProperty("ro.miui.ui.version.code");
        if (!TextUtils.isEmpty(miuiVersionCodeStr)) {
            try {
                int miuiVersionCode = Integer.parseInt(miuiVersionCodeStr);
                if (miuiVersionCode >= 4) {
                    return true;
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    private static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }
}
