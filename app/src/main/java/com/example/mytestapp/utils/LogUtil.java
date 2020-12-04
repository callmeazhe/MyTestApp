package com.example.mytestapp.utils;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import static com.example.mytestapp.nfc.utils.LogUtil.KEY_TAG;

/**
 * Record debug logs and needRetryToGetPV logs that include runtime exceptions
 */

public class LogUtil {

    public static final boolean IS_SAVE_LOG = false;
    public static final boolean IS_OUT_CONSOLE_LOG = false;

    public static void saveExceptionFile(Throwable throwable) {
        StringBuffer stringBuffer = new StringBuffer();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        Throwable cause = throwable.getCause();
        if (null != cause) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        stringBuffer.append(result);
        saveLogToFile(stringBuffer.toString());
    }

    public static void saveLogToFile(String log) {
        if (!IS_SAVE_LOG)
            return;
        try {
            String path = MyFileUtil.selectAppSpecificDirectory();
            String logFileAbsPath = path + "log.txt";
            String oldLogFileAbsPath = path + "log.old";

            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File logFile = new File(logFileAbsPath);
            if ((logFile.length() / 1024 / 1024) >= 5) {
                File logOldFile = new File(oldLogFileAbsPath);
                if (logOldFile.exists()) {
                    logOldFile.delete();
                }
                logFile.renameTo(new File(oldLogFileAbsPath));
            }
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            FileWriter fw = new FileWriter(logFileAbsPath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(log);
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (Exception e) {
            outLog("an error occurred while writing file..." + e.toString());
        }
    }


    //out log
    public static void outLog(String log) {
        if (IS_OUT_CONSOLE_LOG)
            Log.i(KEY_TAG, log);
        saveLogToFile(log);
    }

    public static void i(String tag, String value) {
        Log.i(tag, value);
    }
    public static void i(Object tag, String value) {
        Log.i(tag.getClass().getName(), value);
    }
    public static void e(Object tag, String value) {
        Log.e(tag.getClass().getName(), value);
    }
}