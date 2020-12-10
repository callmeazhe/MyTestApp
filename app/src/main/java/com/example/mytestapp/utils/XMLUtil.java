package com.example.mytestapp.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;

import com.example.mytestapp.nfc.modbus.Constants;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

public class XMLUtil {

    public interface ParserDataCallback {
        void onSuccess(Object o);

        void onFailed(int failedCode);

    }

    public static void serializerData(Context context, String path, String name, List<?> data, ParserDataCallback callback) {
        new AsyncTask<String, Integer, File>() {

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                if (null == file && null != callback) {
                    callback.onFailed(Constants.ERROR_CODE);
                } else {
                    MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, null);
                    if (null != callback) {
                        callback.onSuccess(file);
                    }
                }
            }

            @Override
            protected File doInBackground(String... strings) {
                if (null == data || null == path || TextUtils.isEmpty(path) || data.isEmpty()) {
                    return null;
                }
                File destFile;
                try {
                    File dir = new File(path);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    destFile = new File(path + name);
                    if (destFile.exists()) {
                        destFile.delete();
                        destFile.createNewFile();
                    }
                    XStream xStream = new XStream(new DomDriver("UTF-8"));
                    String str = xStream.toXML(data);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        RandomAccessFile randomAccessFile = new RandomAccessFile(destFile, "rws");
                        randomAccessFile.seek(destFile.length());
                        randomAccessFile.write(str.getBytes());
                        randomAccessFile.close();
                    } else {
                        FileOutputStream outputStream = new FileOutputStream(destFile);
                        outputStream.write(str.getBytes());
                        outputStream.flush();
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    destFile = null;
                }
                return destFile;
            }
        }.execute();
    }
}
