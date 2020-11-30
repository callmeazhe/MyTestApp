package com.example.mytestapp;

import android.app.Application;

public class BaseApplication extends Application {

    public static BaseApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
