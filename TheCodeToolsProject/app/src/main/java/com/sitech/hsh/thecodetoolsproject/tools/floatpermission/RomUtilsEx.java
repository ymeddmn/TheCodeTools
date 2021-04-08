package com.sitech.hsh.thecodetoolsproject.tools.floatpermission;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.sitech.hsh.thecodetoolsproject.tools.floatpermission.rom.HuaweiUtils;
import com.sitech.hsh.thecodetoolsproject.tools.floatpermission.rom.MeizuUtils;
import com.sitech.hsh.thecodetoolsproject.tools.floatpermission.rom.MiuiUtils;
import com.sitech.hsh.thecodetoolsproject.tools.floatpermission.rom.OppoUtils;
import com.sitech.hsh.thecodetoolsproject.tools.floatpermission.rom.QikuUtils;
import com.sitech.hsh.thecodetoolsproject.tools.floatpermission.rom.RomUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * author  :mayong
 * function:
 * date    :2021/4/8
 **/
public class RomUtilsEx {
    private static final String TAG = "RomUtils";
    static final String ROM_MIUI = "MIUI";
    static final String ROM_EMUI = "EMUI";
    static final String ROM_VIVO = "VIVO";
    static final String ROM_OPPO = "OPPO";
    static final String ROM_FLYME = "FLYME";
    static final String ROM_SMARTISAN = "SMARTISAN";
    static final String ROM_QIKU = "QIKU";
    static final String ROM_LETV = "LETV";
    static final String ROM_LENOVO = "LENOVO";
    static final String ROM_NUBIA = "NUBIA";
    static final String ROM_ZTE = "ZTE";
    static final String ROM_COOLPAD = "COOLPAD";
    static final String ROM_UNKNOWN = "UNKNOWN";
    private static Dialog dialog;
    private static Context mContext;
    private static final String SYSTEM_VERSION_MIUI = "ro.miui.ui.version.name";
    private static final String SYSTEM_VERSION_EMUI = "ro.build.version.emui";
    private static final String SYSTEM_VERSION_VIVO = "ro.vivo.os.version";
    private static final String SYSTEM_VERSION_OPPO = "ro.build.version.opporom";
    private static final String SYSTEM_VERSION_FLYME = "ro.build.display.id";
    private static final String SYSTEM_VERSION_SMARTISAN = "ro.smartisan.version";
    private static final String SYSTEM_VERSION_LETV = "ro.letv.eui";
    private static final String SYSTEM_VERSION_LENOVO = "ro.lenovo.lvp.version";

    public RomUtilsEx() {
    }

    private static String getSystemProperty(String var0) {
        return SystemProperties.get(var0, (String)null);
    }

    public static String getRomName() {
        if (isMiuiRom()) {
            return "MIUI";
        } else if (isHuaweiRom()) {
            return "EMUI";
        } else if (isVivoRom()) {
            return "VIVO";
        } else if (isOppoRom()) {
            return "OPPO";
        } else if (isMeizuRom()) {
            return "FLYME";
        } else if (isSmartisanRom()) {
            return "SMARTISAN";
        } else if (is360Rom()) {
            return "QIKU";
        } else if (isLetvRom()) {
            return "LETV";
        } else if (isLenovoRom()) {
            return "LENOVO";
        } else if (isZTERom()) {
            return "ZTE";
        } else {
            return isCoolPadRom() ? "COOLPAD" : "UNKNOWN";
        }
    }

    public static boolean isMiuiRom() {
        return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"));
    }

    public static boolean isHuaweiRom() {
        return !TextUtils.isEmpty(getSystemProperty("ro.build.version.emui"));
    }

    public static boolean isVivoRom() {
        return !TextUtils.isEmpty(getSystemProperty("ro.vivo.os.version"));
    }

    public static boolean isOppoRom() {
        return !TextUtils.isEmpty(getSystemProperty("ro.build.version.opporom"));
    }

    public static boolean isMeizuRom() {
        String var0 = getSystemProperty("ro.build.display.id");
        return !TextUtils.isEmpty(var0) && var0.toUpperCase().contains("FLYME");
    }

    public static boolean isSmartisanRom() {
        return !TextUtils.isEmpty(getSystemProperty("ro.smartisan.version"));
    }

    public static boolean is360Rom() {
        String var0 = Build.MANUFACTURER;
        return !TextUtils.isEmpty(var0) && var0.toUpperCase().contains("QIKU");
    }

    public static boolean isLetvRom() {
        return !TextUtils.isEmpty(getSystemProperty("ro.letv.eui"));
    }

    public static boolean isLenovoRom() {
        return !TextUtils.isEmpty(getSystemProperty("ro.lenovo.lvp.version"));
    }

    public static boolean isCoolPadRom() {
        String var0 = Build.MODEL;
        String var1 = Build.FINGERPRINT;
        return !TextUtils.isEmpty(var0) && var0.toLowerCase().contains("COOLPAD") || !TextUtils.isEmpty(var1) && var1.toLowerCase().contains("COOLPAD");
    }

    public static boolean isZTERom() {
        String var0 = Build.MANUFACTURER;
        String var1 = Build.FINGERPRINT;
        return !TextUtils.isEmpty(var0) && (var1.toLowerCase().contains("NUBIA") || var1.toLowerCase().contains("ZTE")) || !TextUtils.isEmpty(var1) && (var1.toLowerCase().contains("NUBIA") || var1.toLowerCase().contains("ZTE"));
    }

    public static boolean isDomesticSpecialRom() {
        return isMiuiRom() || isHuaweiRom() || isMeizuRom() || is360Rom() || isOppoRom() || isVivoRom() || isLetvRom() || isZTERom() || isLenovoRom() || isCoolPadRom();
    }

    public static boolean checkPermission(Context var0) {
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                return miuiPermissionCheck(var0);
            }

            if (RomUtils.checkIsMeizuRom()) {
                return meizuPermissionCheck(var0);
            }

            if (RomUtils.checkIsHuaweiRom()) {
                return huaweiPermissionCheck(var0);
            }

            if (RomUtils.checkIs360Rom()) {
                return qikuPermissionCheck(var0);
            }

            if (RomUtils.checkIsOppoRom()) {
                return oppoROMPermissionCheck(var0);
            }
        }

        return commonROMPermissionCheck(var0);
    }

    private static boolean oppoROMPermissionCheck(Context var0) {
        return OppoUtils.checkFloatWindowPermission(var0);
    }

    private static boolean huaweiPermissionCheck(Context var0) {
        return HuaweiUtils.checkFloatWindowPermission(var0);
    }

    private static boolean miuiPermissionCheck(Context var0) {
        return MiuiUtils.checkFloatWindowPermission(var0);
    }

    private static boolean meizuPermissionCheck(Context var0) {
        return MeizuUtils.checkFloatWindowPermission(var0);
    }

    private static boolean qikuPermissionCheck(Context var0) {
        return QikuUtils.checkFloatWindowPermission(var0);
    }

    private static boolean commonROMPermissionCheck(Context var0) {
        if (RomUtils.checkIsMeizuRom()) {
            return meizuPermissionCheck(var0);
        } else {
            Boolean var1 = true;
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    Class var2 = Settings.class;
                    Method var3 = var2.getDeclaredMethod("canDrawOverlays", Context.class);
                    var1 = (Boolean)var3.invoke((Object)null, var0);
                } catch (Exception var4) {
                    Log.e("RomUtils", Log.getStackTraceString(var4));
                }
            }

            return var1;
        }
    }

    public static void applyPermission(Context var0) {
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                miuiROMPermissionApply(var0);
            } else if (RomUtils.checkIsMeizuRom()) {
                meizuROMPermissionApply(var0);
            } else if (RomUtils.checkIsHuaweiRom()) {
                huaweiROMPermissionApply(var0);
            } else if (RomUtils.checkIs360Rom()) {
                ROM360PermissionApply(var0);
            } else if (RomUtils.checkIsOppoRom()) {
                oppoROMPermissionApply(var0);
            }
        }

        commonROMPermissionApply(var0);
    }

    private static void ROM360PermissionApply(final Context var0) {
        showConfirmDialog(var0, new RomUtilsEx.OnConfirmResult() {
            public void confirmResult(boolean var1) {
                if (var1) {
                    QikuUtils.applyPermission(var0);
                } else {
                    Log.e("RomUtils", "ROM:360, user manually refuse OVERLAY_PERMISSION");
                }

            }
        });
    }

    private static void huaweiROMPermissionApply(final Context var0) {
        showConfirmDialog(var0, new RomUtilsEx.OnConfirmResult() {
            public void confirmResult(boolean var1) {
                if (var1) {
                    HuaweiUtils.applyPermission(var0);
                } else {
                    Log.e("RomUtils", "ROM:huawei, user manually refuse OVERLAY_PERMISSION");
                }

            }
        });
    }

    private static void meizuROMPermissionApply(final Context var0) {
        showConfirmDialog(var0, new RomUtilsEx.OnConfirmResult() {
            public void confirmResult(boolean var1) {
                if (var1) {
                    MeizuUtils.applyPermission(var0);
                } else {
                    Log.e("RomUtils", "ROM:meizu, user manually refuse OVERLAY_PERMISSION");
                }

            }
        });
    }

    private static void miuiROMPermissionApply(final Context var0) {
        showConfirmDialog(var0, new RomUtilsEx.OnConfirmResult() {
            public void confirmResult(boolean var1) {
                if (var1) {
                    MiuiUtils.applyMiuiPermission(var0);
                } else {
                    Log.e("RomUtils", "ROM:miui, user manually refuse OVERLAY_PERMISSION");
                }

            }
        });
    }

    private static void oppoROMPermissionApply(final Context var0) {
        showConfirmDialog(var0, new RomUtilsEx.OnConfirmResult() {
            public void confirmResult(boolean var1) {
                if (var1) {
                    OppoUtils.applyOppoPermission(var0);
                } else {
                    Log.e("RomUtils", "ROM:oppo, user manually refuse OVERLAY_PERMISSION");
                }

            }
        });
    }

    private static void commonROMPermissionApply(final Context var0) {
        if (RomUtils.checkIsMeizuRom()) {
            meizuROMPermissionApply(var0);
        } else if (Build.VERSION.SDK_INT >= 23) {
            showConfirmDialog(var0, new RomUtilsEx.OnConfirmResult() {
                public void confirmResult(boolean var1) {
                    if (var1) {
                        try {
                            Class var2 = Settings.class;
                            Field var3 = var2.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");
                            Intent var4 = new Intent(var3.get((Object)null).toString());
                            var4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            var4.setData(Uri.parse("package:" + var0.getPackageName()));
                            ((Activity)var0).startActivityForResult(var4, 102);
                        } catch (Exception var5) {
                            Log.e("RomUtils", Log.getStackTraceString(var5));
                        }
                    } else {
                        Log.d("RomUtils", "user manually refuse OVERLAY_PERMISSION");
                    }

                }
            });
        }

    }

    private static void showConfirmDialog(Context var0, RomUtilsEx.OnConfirmResult var1) {
        showConfirmDialog(var0, "您的手机没有授予悬浮窗权限，音视频聊天部分功能将无法使用", var1);
    }

    private static void showConfirmDialog(Context var0, String var1, final RomUtilsEx.OnConfirmResult var2) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = (new AlertDialog.Builder(var0)).setCancelable(true).setTitle("").setMessage(var1).setCancelable(false).setPositiveButton("开启", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface var1, int var2x) {
                var2.confirmResult(true);
                var1.dismiss();
            }
        }).create();
        dialog.show();
    }

    private interface OnConfirmResult {
        void confirmResult(boolean var1);
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RomName {
    }
}
