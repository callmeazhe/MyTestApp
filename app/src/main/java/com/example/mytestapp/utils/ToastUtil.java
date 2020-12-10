package com.example.mytestapp.utils;

import android.widget.Toast;

import com.example.mytestapp.BaseApplication;

/**
 * Created by 30038070 on 3/18/2019.
 */

public class ToastUtil {
    private static Toast mToast;

    public static void showToast(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(BaseApplication.mContext, BaseApplication.mContext.getString(resId), Toast.LENGTH_LONG);
        } else {
            mToast.setText(BaseApplication.mContext.getString(resId));
        }
        mToast.show();
    }

    public static void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(BaseApplication.mContext, msg, Toast.LENGTH_LONG);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
