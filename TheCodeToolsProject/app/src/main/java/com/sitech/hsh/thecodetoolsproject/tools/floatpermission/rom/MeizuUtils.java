package com.sitech.hsh.thecodetoolsproject.tools.floatpermission.rom;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * author  :mayong
 * function:
 * date    :2021/4/8
 **/
public class MeizuUtils {
    private static final String TAG = "MeizuUtils";

    public MeizuUtils() {
    }

    public static boolean checkFloatWindowPermission(Context var0) {
        int var1 = Build.VERSION.SDK_INT;
        return var1 >= 19 ? checkOp(var0, 24) : true;
    }

    public static void applyPermission(Context var0) {
        Intent var1 = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        var1.putExtra("packageName", var0.getPackageName());
        var1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ((Activity)var0).startActivityForResult(var1, 102);
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
                Log.e("MeizuUtils", Log.getStackTraceString(var6));
            }
        } else {
            Log.e("MeizuUtils", "Below API 19 cannot invoke!");
        }

        return false;
    }
}
