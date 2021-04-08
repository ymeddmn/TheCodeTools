package com.sitech.hsh.thecodetoolsproject.tools.floatpermission.rom;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * author  :mayong
 * function:
 * date    :2021/4/8
 **/
public class QikuUtils {
    private static final String TAG = "QikuUtils";

    public QikuUtils() {
    }

    public static boolean checkFloatWindowPermission(Context var0) {
        int var1 = Build.VERSION.SDK_INT;
        return var1 >= 19 ? checkOp(var0, 24) : true;
    }

    @TargetApi(19)
    private static boolean checkOp(Context var0, int var1) {
        int var2 = Build.VERSION.SDK_INT;
        if (var2 >= 19) {
            @SuppressLint("WrongConstant") AppOpsManager var3 = (AppOpsManager)var0.getSystemService("appops");

            try {
                Class var4 = AppOpsManager.class;
                Method var5 = var4.getDeclaredMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
                return 0 == (Integer)var5.invoke(var3, var1, Binder.getCallingUid(), var0.getPackageName());
            } catch (Exception var6) {
                Log.e("QikuUtils", Log.getStackTraceString(var6));
            }
        } else {
            Log.e("", "Below API 19 cannot invoke!");
        }

        return false;
    }

    public static void applyPermission(Context var0) {
        Intent var1 = new Intent();
        var1.setClassName("com.android.settings", "com.android.settings.Settings$OverlaySettingsActivity");
        var1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(var1, var0)) {
            ((Activity)var0).startActivityForResult(var1, 102);
        } else {
            var1.setClassName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
            if (isIntentAvailable(var1, var0)) {
                ((Activity)var0).startActivityForResult(var1, 102);
            } else {
                Log.e("QikuUtils", "can't open permission page with particular name, please use \"adb shell dumpsys activity\" command and tell me the name of the float window permission page");
            }
        }

    }

    private static boolean isIntentAvailable(Intent var0, Context var1) {
        if (var0 == null) {
            return false;
        } else {
            return var1.getPackageManager().queryIntentActivities(var0, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
        }
    }
}
