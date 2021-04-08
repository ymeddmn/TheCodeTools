package com.sitech.hsh.thecodetoolsproject.tools.floatpermission.rom;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * author  :mayong
 * function:
 * date    :2021/4/8
 **/
public class MiuiUtils {
    private static final String TAG = "MiuiUtils";

    public MiuiUtils() {
    }

    public static int getMiuiVersion() {
        String var0 = RomUtils.getSystemProperty("ro.miui.ui.version.name");
        if (var0 != null) {
            try {
                return Integer.parseInt(var0.substring(1));
            } catch (Exception var2) {
                Log.e("MiuiUtils", "get miui version code error, version : " + var0);
                Log.e("MiuiUtils", Log.getStackTraceString(var2));
            }
        }

        return -1;
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
                Log.e("MiuiUtils", Log.getStackTraceString(var6));
            }
        } else {
            Log.e("MiuiUtils", "Below API 19 cannot invoke!");
        }

        return false;
    }

    public static void applyMiuiPermission(Context var0) {
        int var1 = getMiuiVersion();
        if (var1 == 5) {
            goToMiuiPermissionActivity_V5(var0);
        } else if (var1 == 6) {
            goToMiuiPermissionActivity_V6(var0);
        } else if (var1 == 7) {
            goToMiuiPermissionActivity_V7(var0);
        } else if (var1 == 8) {
            goToMiuiPermissionActivity_V8(var0);
        } else {
            Log.e("MiuiUtils", "this is a special MIUI rom version, its version code " + var1);
        }

    }

    private static boolean isIntentAvailable(Intent var0, Context var1) {
        if (var0 == null) {
            return false;
        } else {
            return var1.getPackageManager().queryIntentActivities(var0, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
        }
    }

    public static void goToMiuiPermissionActivity_V5(Context var0) {
        Intent var1 = null;
        String var2 = var0.getPackageName();
        var1 = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        Uri var3 = Uri.fromParts("package", var2, (String)null);
        var1.setData(var3);
        var1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(var1, var0)) {
            ((Activity)var0).startActivityForResult(var1, 102);
        } else {
            Log.e("MiuiUtils", "intent is not available!");
        }

    }

    public static void goToMiuiPermissionActivity_V6(Context var0) {
        Intent var1 = new Intent("miui.intent.action.APP_PERM_EDITOR");
        var1.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        var1.putExtra("extra_pkgname", var0.getPackageName());
        var1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(var1, var0)) {
            ((Activity)var0).startActivityForResult(var1, 102);
        } else {
            Log.e("MiuiUtils", "Intent is not available!");
        }

    }

    public static void goToMiuiPermissionActivity_V7(Context var0) {
        Intent var1 = new Intent("miui.intent.action.APP_PERM_EDITOR");
        var1.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        var1.putExtra("extra_pkgname", var0.getPackageName());
        var1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(var1, var0)) {
            ((Activity)var0).startActivityForResult(var1, 102);
        } else {
            Log.e("MiuiUtils", "Intent is not available!");
        }

    }

    public static void goToMiuiPermissionActivity_V8(Context var0) {
        Intent var1 = new Intent("miui.intent.action.APP_PERM_EDITOR");
        var1.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
        var1.putExtra("extra_pkgname", var0.getPackageName());
        var1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(var1, var0)) {
            ((Activity)var0).startActivityForResult(var1, 102);
        } else {
            var1 = new Intent("miui.intent.action.APP_PERM_EDITOR");
            var1.setPackage("com.miui.securitycenter");
            var1.putExtra("extra_pkgname", var0.getPackageName());
            var1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (isIntentAvailable(var1, var0)) {
                ((Activity)var0).startActivityForResult(var1, 102);
            } else {
                Log.e("MiuiUtils", "Intent is not available!");
            }
        }

    }
}
