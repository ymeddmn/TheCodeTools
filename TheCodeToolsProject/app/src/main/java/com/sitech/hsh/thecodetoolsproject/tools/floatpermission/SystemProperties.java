package com.sitech.hsh.thecodetoolsproject.tools.floatpermission;

import android.text.TextUtils;

import java.lang.reflect.Method;

/**
 * author  :mayong
 * function:
 * date    :2021/4/8
 **/
public class SystemProperties {
    private static final Method getStringProperty = getMethod(getClass("android.os.SystemProperties"));

    public SystemProperties() {
    }

    private static Class<?> getClass(String var0) {
        try {
            Class var1 = Class.forName(var0);
            if (var1 == null) {
                throw new ClassNotFoundException();
            } else {
                return var1;
            }
        } catch (ClassNotFoundException var4) {
            try {
                return ClassLoader.getSystemClassLoader().loadClass(var0);
            } catch (ClassNotFoundException var3) {
                return null;
            }
        }
    }

    private static Method getMethod(Class<?> var0) {
        if (var0 == null) {
            return null;
        } else {
            try {
                return var0.getMethod("get", String.class);
            } catch (Exception var2) {
                return null;
            }
        }
    }

    public static String get(String var0) {
        if (getStringProperty != null) {
            try {
                Object var1 = getStringProperty.invoke((Object)null, var0);
                if (var1 == null) {
                    return "";
                }

                return trimToEmpty(var1.toString());
            } catch (Exception var2) {
            }
        }

        return "";
    }

    public static String get(String var0, String var1) {
        if (getStringProperty != null) {
            try {
                String var2 = (String)getStringProperty.invoke((Object)null, var0);
                return defaultString(trimToNull(var2), var1);
            } catch (Exception var3) {
            }
        }

        return var1;
    }

    private static String defaultString(String var0, String var1) {
        return var0 == null ? var1 : var0;
    }

    private static String trimToNull(String var0) {
        String var1 = trim(var0);
        return TextUtils.isEmpty(var1) ? null : var1;
    }

    private static String trimToEmpty(String var0) {
        return var0 == null ? "" : var0.trim();
    }

    private static String trim(String var0) {
        return var0 == null ? null : var0.trim();
    }
}
