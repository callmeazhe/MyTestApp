package com.example.mytestapp.utils;

import android.content.Context;
import android.os.Environment;

import com.example.mytestapp.BaseApplication;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileUtil {

    public static String getInnerFilePath() {
        String filePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            filePath = BaseApplication.mContext.getExternalFilesDir("").getPath();
        } else {
            filePath = BaseApplication.mContext.getFilesDir().getPath();
        }
        return filePath;
    }

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

    public static String getAssetsFileContent(Context context, String fileName) {

        //Return an AssetManager instance for your application's package
        InputStream is = null;
        try {
            is = context.getAssets().open(fileName);

            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);

            // Convert the buffer into a string.
            String text = new String(buffer, StandardCharsets.UTF_8);
            // Finally stick the string into the text view.

            return text;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static String getBackupFileUrl() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        String curDateStr = formatter.format(curDate);
        String current_bak = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory().getPath() + "/sushi/backup/";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            current_bak = path + "backup_" + curDateStr + ".bak";
        }
        return current_bak;
    }

    public static void createNewFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
    }

    public static boolean isMountedSDCard() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            return true;
        } else {
            return false;
        }
    }

    public static List<String> getCSVFileNameList() {
        String path = getInnerFilePath() + "/sushi/backup/";
        File file = new File(path);
        List<String> result = new ArrayList<String>();
        if (!file.isDirectory()) {
            System.out.println(file.getAbsolutePath());

        } else {
            // Used to filter file types.
            File[] directoryList = file.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    if (file.isFile() && file.getName().toUpperCase().endsWith(".BAK")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            if (directoryList != null)
                for (int i = 0; i < directoryList.length; i++) {
                    result.add(directoryList[i].getAbsolutePath());
                }
        }
        return result;
    }
}
