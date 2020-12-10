package com.example.mytestapp.net.builder;

import android.text.TextUtils;


import com.example.mytestapp.net.OkCallback;
import com.example.mytestapp.net.OkHttpProxy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 30035167 on 2017/9/13.
 */

public class DeleteRequestBuilder extends RequestBuilder {

    public DeleteRequestBuilder url(String url) {
        if (TextUtils.isEmpty(url))
            throw new IllegalArgumentException("url not be null");
        this.mUrl = url;
        return this;
    }

    public DeleteRequestBuilder setParams(Map<String, String> params) {
        if (null == params)
            throw new IllegalArgumentException("params not be null");
        this.mParams = params;
        return this;
    }

    public DeleteRequestBuilder addParams(String key, String value) {
        if (mParams == null) {
            mParams = new HashMap<>();
        }
        if (!TextUtils.isEmpty(key))
            mParams.put(key, value);
        return this;
    }

    public DeleteRequestBuilder tag(Object tag) {
        this.mTag = tag;
        return this;
    }

    @Override
    public Call enqueue(Callback callback) {
        if (TextUtils.isEmpty(mUrl)) {
            throw new IllegalArgumentException("mUrl can not be null !");
        }

        Request.Builder builder = new Request.Builder().url(mUrl);

        if (mTag != null) {
            builder.tag(mTag);
        }
        //Method ---> Delete
        builder.delete();

        if (mParams != null && mParams.size() > 0) {
            mUrl = appendParams(mUrl, mParams);
        }

        Request request = builder.build();

        if (callback instanceof OkCallback) {
            ((OkCallback) callback).onStart();
        }

        Call call = OkHttpProxy.getInstance().newCall(request);
        call.enqueue(callback);
        return call;
    }

    @Override
    public Response execute() throws IOException {
        if (TextUtils.isEmpty(mUrl)) {
            throw new IllegalArgumentException("mUrl can not be null !");
        }

        Request.Builder builder = new Request.Builder().url(mUrl);

        if (mTag != null) {
            builder.tag(mTag);
        }
        builder.delete();
        if (mParams != null && mParams.size() > 0) {
            mUrl = appendParams(mUrl, mParams);
        }

        Request request = builder.build();
        Call call = OkHttpProxy.getInstance().newCall(request);
        return call.execute();
    }
}
