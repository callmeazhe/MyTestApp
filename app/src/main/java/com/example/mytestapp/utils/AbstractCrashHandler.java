package com.example.mytestapp.utils;

import android.content.Context;
import android.os.Looper;

import com.example.mytestapp.BaseApplication;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Handle UncaughtException
 * When the program takes Uncaught exception,
 * there is the class to take over the program and record the error report
 */

public abstract class AbstractCrashHandler implements Thread.UncaughtExceptionHandler {

    protected Thread.UncaughtExceptionHandler mDefaultHandler;
    protected Map<String, String> mInfo = new HashMap<String, String>();
    protected DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    public AbstractCrashHandler(){
        init();
    }

    public void init() {
        //The system default UncaughtException handler
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(final Thread thread, Throwable throwable) {
        if (!handleException(throwable) && null != mDefaultHandler) {
            //Android will deal with the throwable
            mDefaultHandler.uncaughtException(thread, throwable);
        }
    }

    protected boolean handleException(Throwable throwable) {
        if (throwable == null) {
            return false;
        }
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                showUncaughtErrorDialog();
                Looper.loop();
            }
        }.start();
        // collect device info
        collectDeviceInfo(BaseApplication.mContext);
        // save crash log info to file
        saveCrashInfoToFile(throwable);
        return true;
    }

    protected abstract void showUncaughtErrorDialog();

    protected abstract void saveCrashInfoToFile(Throwable throwable);

    protected abstract void collectDeviceInfo(Context context);
}
