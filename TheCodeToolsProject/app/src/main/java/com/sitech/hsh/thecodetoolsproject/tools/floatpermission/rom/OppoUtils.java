package com.sitech.hsh.thecodetoolsproject.tools.floatpermission.rom;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.ComponentName;
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
public class OppoUtils {
    private static final String TAG = "OppoUtils";

    public OppoUtils() {
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
                Log.e("OppoUtils", Log.getStackTraceString(var6));
            }
        } else {
            Log.e("OppoUtils", "Below API 19 cannot invoke!");
        }

        return false;
    }

    public static void applyOppoPermission(Context var0) {
        try {
            Intent var1 = new Intent();
            var1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName var2 = new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity");
            var1.setComponent(var2);
            var0.startActivity(var1);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }
}
