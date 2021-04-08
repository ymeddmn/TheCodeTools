package com.sitech.hsh.thecodetoolsproject.tools.floatpermission.rom;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;

/**
 * author  :mayong
 * function:
 * date    :2021/4/8
 **/
public class HuaweiUtils {
    private static final String TAG = "HuaweiUtils";

    public HuaweiUtils() {
    }

    public static boolean checkFloatWindowPermission(Context var0) {
        int var1 = Build.VERSION.SDK_INT;
        return var1 >= 19 ? checkOp(var0, 24) : true;
    }

    public static void applyPermission(Context var0) {
        Intent var2;
        ComponentName var3;
        try {
            Intent var1 = new Intent();
            var1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName var7 = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity");
            var1.setComponent(var7);
            if (RomUtils.getEmuiVersion() == 3.1D) {
                var0.startActivity(var1);
            } else {
                var7 = new ComponentName("com.huawei.systemmanager", "com.huawei.notificationmanager.ui.NotificationManagmentActivity");
                var1.setComponent(var7);
                ((Activity)var0).startActivityForResult(var1, 102);
            }
        } catch (SecurityException var4) {
            var2 = new Intent();
            var2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            var3 = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
            var2.setComponent(var3);
            ((Activity)var0).startActivityForResult(var2, 102);
            Log.e("HuaweiUtils", Log.getStackTraceString(var4));
        } catch (ActivityNotFoundException var5) {
            var2 = new Intent();
            var2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            var3 = new ComponentName("com.Android.settings", "com.android.settings.permission.TabItem");
            var2.setComponent(var3);
            ((Activity)var0).startActivityForResult(var2, 102);
            var5.printStackTrace();
            Log.e("HuaweiUtils", Log.getStackTraceString(var5));
        } catch (Exception var6) {
            Toast.makeText(var0, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show();
            Log.e("HuaweiUtils", Log.getStackTraceString(var6));
        }

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
                Log.e("HuaweiUtils", Log.getStackTraceString(var6));
            }
        } else {
            Log.e("HuaweiUtils", "Below API 19 cannot invoke!");
        }

        return false;
    }
}
