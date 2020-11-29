package com.example.mytestapp.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

public class CommonUtils {

    public static File getDiskCacheDir(Context context, String uniqueName) {
        if (null == context) {
            return null;
        }
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

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

}
