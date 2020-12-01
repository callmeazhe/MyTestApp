package com.example.mytestapp.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.example.mytestapp.BaseApplication;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileUtil {

    public static String getInnerDirectoryPath() {
        String filePath;
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
            int result = is.read(buffer);
            if (result != -1) {
                // Convert the buffer into a string.
                // Finally stick the string into the text view.
                return new String(buffer, StandardCharsets.UTF_8);
            }
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
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());

    }

    public static List<String> getCSVFileNameList() {
        String path = getInnerDirectoryPath() + "/sushi/backup/";
        File file = new File(path);
        List<String> result = new ArrayList<String>();
        if (!file.isDirectory()) {
            System.out.println(file.getAbsolutePath());

        } else {
            // Used to filter file types.
            File[] directoryList = file.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return file.isFile() && file.getName().toUpperCase().endsWith(".BAK");
                }
            });
            if (directoryList != null)
                for (File _file : directoryList) {
                    result.add(_file.getAbsolutePath());
                }
        }
        return result;
    }

    private void beganMigrateOldFiles(String newDirectoryName, File oldFile) {
        if (null == oldFile || !oldFile.exists())
            return;
        if (TextUtils.isEmpty(newDirectoryName))
            newDirectoryName = getInnerDirectoryPath();

        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        File newFile;
        byte[] buffer = new byte[1024 * 4];

        if (oldFile.isDirectory()) {
            if (oldFile.getName().endsWith("sushi")) {
                newDirectoryName = newDirectoryName + File.separator;
            } else {
                newDirectoryName = newDirectoryName + File.separator + oldFile.getName();
            }
            newFile = new File(newDirectoryName);
            newFile.mkdir();
            if (newFile.exists())
                return;
            File[] oldFiles = oldFile.listFiles();
            if (null == oldFiles || oldFiles.length == 0)
                return;
            for (File file : oldFiles) {
                if (null == file || !file.exists() || file.length() == 0 || !file.canRead())
                    continue;
                beganMigrateOldFiles(newDirectoryName, file);
            }
        } else {
            try {
                fileInputStream = new FileInputStream(oldFile);

                String newFileName = newDirectoryName + File.separator + oldFile.getName();
                newFile = new File(newFileName);
                if (newFile.exists())
                    return;
                fileOutputStream = new FileOutputStream(newFile);

                int len;
                while ((len = fileInputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                }
                fileOutputStream.flush();
            } catch (Exception e) {
                if (e.toString().contains("ENOSPC")) {
                    //TODO
                }
            } finally {
                try {
                    if (null != fileOutputStream)
                        fileOutputStream.close();
                    if (null != fileInputStream)
                        fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
