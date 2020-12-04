package com.example.mytestapp.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.example.mytestapp.BaseApplication;

public class PermissionUtil {

    public static final String[] LOCATION_PERMISSIONS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    public static final String[] EXTERNAL_STORAGE_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final String[] ALL_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    public static boolean hasAppointedPermission(String[] permissions) {
        if (null == permissions || permissions.length == 0)
            return false;
        int flag = 0;

        for (String permission : permissions) {
            //when sdk version is higher than android 6.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(BaseApplication.mContext, permission) == PackageManager.PERMISSION_GRANTED) {
                    flag++;
                }
            } else {
                if (PermissionChecker.checkSelfPermission(BaseApplication.mContext, permission) == PermissionChecker.PERMISSION_GRANTED) {
                    flag++;
                }
            }
        }
        return flag == permissions.length;
    }

}
