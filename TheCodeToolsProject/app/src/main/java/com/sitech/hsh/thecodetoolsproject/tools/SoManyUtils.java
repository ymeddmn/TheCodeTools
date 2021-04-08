package com.sitech.hsh.thecodetoolsproject.tools;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.UUID;
import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.LOCATION_SERVICE;

/**
 * author  :mayong
 * function:
 * date    :2020-05-01
 **/
public class SoManyUtils {
    /**
     * 获取运营商名字
     *
     * @param context context
     * @return int
     */
    public static String getOperatorName(Context context) {
        String ProvidersName = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "-1";
        } else {
//            String IMSI = telephonyManager.getSubscriberId();
            String IMSI = getOperatorCode(context);
            Log.i("qweqwes", "运营商代码" + IMSI);
            if (IMSI != null) {
                if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                    ProvidersName = "中国移动";
                } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006")) {
                    ProvidersName = "中国联通";
                } else if (IMSI.startsWith("46003")) {
                    ProvidersName = "中国电信";
                } else {
                    ProvidersName = "-1";
                }
                return ProvidersName;
            } else {
                return "-1";
            }
        }

    }

    /**
     * 获取imsi
     *
     * @param context
     * @return
     */
    public static String getImsi(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "-1";
        }
        String IMSI = telephonyManager.getSubscriberId();
        return IMSI;
    }

    /**
     * 获取运营商编码
     *
     * @param context
     * @return
     */
    public static String getOperatorCode(Context context) {
        /*
         * getSimOperatorName()就可以直接获取到 运营商的名字
         * 也可以使用IMSI获取，getSimOperator()，然后根据返回值判断，例如"46000"为移动
         * IMSI相关链接：http://baike.baidu.com/item/imsi
         */
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simOperator = telephonyManager.getSimOperator();
        return TextUtils.isEmpty(simOperator) ? "-1" : simOperator;
    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public static String getOsVersion() {
//        return Build.VERSION.SDK_INT;
        return Build.VERSION.RELEASE;
    }

    /**
     * 获得app版本号
     *
     * @param context
     * @return
     */
    public static int getAppCode(Context context) {
        PackageManager pManager = context.getPackageManager();
        PackageInfo pInfo = null;
        try {
            pInfo = pManager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获得进程id
     *
     * @return
     */
    public static int getPID() {
        return android.os.Process.myPid();
    }

    /**
     * 获得包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获得手机品牌
     *
     * @return
     */
    public static String getBrand() {
        return Build.BRAND;
    }

    /**
     * 获得手机型号
     *
     * @return
     */
    public static String getBrandModel() {
        return Build.MODEL;
    }

    /**
     * 获得手机的机器码
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            String uniqueId = deviceUuid.toString();
            return uniqueId.replaceAll("-", "");
        }
        return "获取失败";
    }

    /**
     * 判断手机是否root
     *
     * @return
     */
    public static boolean isRoot() {
        String binPath = "/system/bin/su";
        String xBinPath = "/system/xbin/su";

        if (new File(binPath).exists() && isCanExecute(binPath)) {
            return true;
        }
        if (new File(xBinPath).exists() && isCanExecute(xBinPath)) {
            return true;
        }
        return false;
    }

    /**
     * 获得手机分辨率  星号隔开
     *
     * @param context
     * @return
     */
    public static String getScreenPixel(Context context) {
//        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//        return metrics.widthPixels + "*" + metrics.heightPixels;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();// 手机屏幕的宽度
        int height = windowManager.getDefaultDisplay().getHeight();// 手机屏幕的高度
        return width + "*" + height;
    }

    /**
     * 获取用户使用的语言
     *
     * @return
     */
    public static String getUsedLanguge() {
        return Locale.getDefault().getDisplayLanguage();
    }

    private static boolean isCanExecute(String filePath) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("ls -l " + filePath);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String str = in.readLine();
            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x')
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return false;
    }

    /**
     * 当前使用内存占总内存的比例
     *
     * @param context
     */
    public static float heapPercent(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        //最大分配内存
        int memory = activityManager.getMemoryClass();
        //最大分配内存获取方法2
        float maxMemory = (float) (Runtime.getRuntime().maxMemory() * 1.0 / (1024 * 1024));
        //当前分配的总内存
        float totalMemory = (float) (Runtime.getRuntime().totalMemory() * 1.0 / (1024 * 1024));
        //剩余内存
        float freeMemory = (float) (Runtime.getRuntime().freeMemory() * 1.0 / (1024 * 1024));
        return totalMemory / maxMemory;
    }

    /**
     * 最大分配内存获取方法2
     *
     * @return
     */
    public static float getMaxMemory() {
        return (float) (Runtime.getRuntime().maxMemory()) /** 1.0 / (1024 * 1024))*/;
    }

    /**
     * 当前分配的总内存
     *
     * @return
     */
    public static float getTotalMemory() {
        return (float) (Runtime.getRuntime().totalMemory() /** 1.0 / (1024 * 1024)*/);
    }

    /**
     * 剩余内存
     *
     * @return
     */
    public static float getFreeMemory() {
        return (float) (Runtime.getRuntime().freeMemory() /** 1.0 / (1024 * 1024)*/);
    }

    /**
     * 获得系统可用内存，
     *
     * @return
     */
    public static long getSysAvailableMemory(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        //获得MemoryInfo对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();


//获得系统可用内存，保存在MemoryInfo对象上
        mActivityManager.getMemoryInfo(memoryInfo);

        long memSize = memoryInfo.availMem;
        return memSize;
    }

    /**
     * 获取手机剩余存储空间
     *
     * @return 没有获取到返回 -1
     */
    public static long remainStorageSize() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //如果存储卡存在，则获取存储文件的路径
            File path = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(path.getPath());//创建StatFs对象
            long blockSize = sf.getBlockSize();//获得blockSize
            long totalBlock = sf.getBlockCount();//获得全部block
            long availableBlock = sf.getAvailableBlocks();//获取可用的block
            //用String数组来存放Block信息
//            String[] total=fileSize(totalBlock*blockSize);
//            String[] available=fileSize(availableBlock*blockSize);
            //在ProgressBar中显示可用空间的大小
//            int a=Integer.parseInt(available[0]);
//            String s="SD卡中空间总共有："+total[0]+total[1]+"\n";
//            s+="剩余空间大小："+available[0]+available[1];
            long size = (availableBlock * blockSize);
            return size;
        } else if (Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED)) {
//            存储卡已经被移除
        }
        return -1;
    }

    /**
     * 获取内部存储空间
     *
     * @return
     */
    public static long getSystemStorageSize() {
        File root = Environment.getRootDirectory();
        StatFs sf = new StatFs(root.getPath());
        long blockSize = sf.getBlockSize();
        long blockCount = sf.getBlockCount();
        long availCount = sf.getAvailableBlocks();
        Log.d("", "block大小:" + blockSize + ",block数目:" + blockCount + ",总大小:" + blockSize * blockCount / 1024 + "KB");
        Log.d("", "可用的block数目：:" + availCount + ",可用大小:" + availCount * blockSize / 1024 + "KB");
        return availCount * blockSize;
    }



    /**
     * 手机gps是否开启
     *
     * @param context
     * @return
     */
    public static boolean isGpsOpen(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 获取屏幕方向
     *
     * @return
     */
    public static String getScreenOrientation(Context context) {
//        return activity.getRequestedOrientation();
        Configuration newConfig = context.getResources().getConfiguration();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
            return "landscape";
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            return "portrait";
        }
        return "";
    }

    /**
     * 判断手机是否是竖向的 横向像素大于竖向未横向
     *
     * @param activity
     * @return
     */
    public static boolean isScreenVertical(Activity activity) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels < displayMetrics.heightPixels;
    }

    /**
     * 判断是否开启了屏幕自动旋转
     *
     * @param context
     * @return
     */
    public static boolean isScreenAutoRotate(Context context) {
        int gravity = 0;
        try {
            gravity = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return gravity == 1;
    }

    /**
     * 获得sdk版本
     *
     * @return
     */
    public static String getSdkVersion() {
        return Build.VERSION.SDK_INT + "";
    }

    /**
     * 进行md5加密
     *
     * @param string
     * @return
     */
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
