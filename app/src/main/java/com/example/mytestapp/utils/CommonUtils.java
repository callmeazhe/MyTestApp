package com.example.mytestapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mytestapp.nfc.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sun.misc.BASE64Encoder;

public class CommonUtils {

    public static int getAppVersion(Context context) {
        int version = 0;
        if (null != context)
            try {
                PackageInfo info =
                        context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                version = info.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        return version;
    }

    /**
     * The timestamp is converted into a date
     *
     * @param time
     * @return
     */
    public static String getStrTime(long time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd  HH:mm");
        re_StrTime = sdf.format(new Date(time));
        return re_StrTime;
    }

    public static String encodeByMD5(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }

        String encodedText = "";
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        encodedText = base64Encoder.encode(messageDigest.digest(text.getBytes(StandardCharsets.UTF_8)));

        return encodedText;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(AppCompatActivity context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimens.xml");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (statusHeight == -1) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                statusHeight = context.getResources().getDimensionPixelSize(resourceId);
            }

        }

        if (statusHeight == -1) {
            Rect rectangle = new Rect();
            context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
            //屏幕
            DisplayMetrics dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);
            //应用区域
            Rect outRect1 = new Rect();
            context.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
            statusHeight = dm.heightPixels - outRect1.height();  //状态栏高度=屏幕高度-应用区域高度


        }
        return statusHeight;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void setWindowStatusBarColor(Activity activity, int colorResId) {

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                Window window = activity.getWindow();

                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                window.setStatusBarColor(activity.getResources().getColor(colorResId));


                //底部导航栏

                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static Map<String, String> collectDeviceInfo(Context context) {
        Map<String, String> info = new HashMap<String, String>();

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (null != packageInfo) {
                String versionName = packageInfo.versionName == null ? "null"
                        : packageInfo.versionName;
                String versionCode = packageInfo.versionCode + "";
                info.put("VersionName", versionName);
                info.put("VersionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.outLog("an error occured when collect package info" + e.toString());
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                info.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                LogUtil.outLog("an error occured when collect crash info" + e.toString());
            }
        }
        return info;
    }

    public static String saveCrashInfoToFile(Throwable throwable,HashMap<String, String> info) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : info.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            stringBuffer.append(key + "=" + value + "\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        Throwable cause = throwable.getCause();
        if (null != cause) {
            cause.printStackTrace(printWriter);
        }
        printWriter.close();
        String result = writer.toString();
        stringBuffer.append(result);
        try {
            String time = formatter.format(new Date());
            String fileName = "error-" + time + "-" + System.currentTimeMillis() + ".txt";
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                String path = MyFileUtil.getInnerDirectoryPath();
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(dir);
                fos.write(stringBuffer.toString().getBytes());
                fos.flush();
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            LogUtil.outLog("an error occured while writing file..." + e.toString());
        }
        return null;
    }

}
