package com.example.mytestapp.disklrucache;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.example.mytestapp.nfc.utils.LogUtil;
import com.example.mytestapp.utils.CommonUtils;
import com.example.mytestapp.utils.MyFileUtil;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;

public class DiskLruCacheHelper {

    private Context mContext;

    public DiskLruCacheHelper(Context context) {
        this.mContext = context;
    }

    private File getDiskCacheDir(Context context, String uniqueName) {
        return MyFileUtil.getDiskCacheDir(context, uniqueName);
    }

    private int configDiskCacheMaxSize() {
        return 1024 * 1024 * 10;
    }

    public DiskLruCache open() {
        DiskLruCache diskLruCache = null;
        try {
            File file = getDiskCacheDir(mContext, Environment.DIRECTORY_DOCUMENTS);
            if (null == file)
                return null;
            LogUtil.i("TAG","File------"+file.getAbsolutePath());
            diskLruCache = DiskLruCache.open(file,
                    CommonUtils.getAppVersion(mContext), 1, configDiskCacheMaxSize());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return diskLruCache;
    }


}
