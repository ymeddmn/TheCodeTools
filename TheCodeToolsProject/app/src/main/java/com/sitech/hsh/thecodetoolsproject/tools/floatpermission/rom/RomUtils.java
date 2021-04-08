package com.sitech.hsh.thecodetoolsproject.tools.floatpermission.rom;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * author  :mayong
 * function:
 * date    :2021/4/8
 **/
public class RomUtils {
    private static final String TAG = "RomUtils";

    public RomUtils() {
    }

    public static double getEmuiVersion() {
        try {
            String var0 = getSystemProperty("ro.build.version.emui");
            String var1 = var0.substring(var0.indexOf("_") + 1);
            return Double.parseDouble(var1);
        } catch (Exception var2) {
            var2.printStackTrace();
            return 4.0D;
        }
    }

    public static int getMiuiVersion() {
        String var0 = getSystemProperty("ro.miui.ui.version.name");
        if (var0 != null) {
            try {
                return Integer.parseInt(var0.substring(1));
            } catch (Exception var2) {
                Log.e("RomUtils", "get miui version code error, version : " + var0);
            }
        }

        return -1;
    }

    public static String getSystemProperty(String var0) {
        BufferedReader var2 = null;

        Object var4;
        try {
            Process var3 = Runtime.getRuntime().exec("getprop " + var0);
            var2 = new BufferedReader(new InputStreamReader(var3.getInputStream()), 1024);
            String var1 = var2.readLine();
            var2.close();
            return var1;
        } catch (IOException var14) {
            Log.e("RomUtils", "Unable to read sysprop " + var0, var14);
            var4 = null;
        } finally {
            if (var2 != null) {
                try {
                    var2.close();
                } catch (IOException var13) {
                    Log.e("RomUtils", "Exception while closing InputStream", var13);
                }
            }

        }

        return (String)var4;
    }

    public static boolean checkIsHuaweiRom() {
        return Build.MANUFACTURER.contains("HUAWEI");
    }

    public static boolean checkIsMiuiRom() {
        return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"));
    }

    public static boolean checkIsMeizuRom() {
        String var0 = getSystemProperty("ro.build.display.id");
        if (TextUtils.isEmpty(var0)) {
            return false;
        } else {
            return var0.contains("flyme") || var0.toLowerCase().contains("flyme");
        }
    }

    public static boolean checkIs360Rom() {
        return Build.MANUFACTURER.contains("QiKU") || Build.MANUFACTURER.contains("360");
    }

    public static boolean checkIsOppoRom() {
        return Build.MANUFACTURER.contains("OPPO") || Build.MANUFACTURER.contains("oppo");
    }
}
