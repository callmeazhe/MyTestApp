package com.example.mytestapp.net.builder;

import android.text.TextUtils;


import com.example.mytestapp.net.OkCallback;
import com.example.mytestapp.net.OkHttpProxy;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 30035167 on 2017/9/13.
 */
public class PostRequestBuilder extends RequestBuilder {



    public PostRequestBuilder url(String url) {
        if (TextUtils.isEmpty(url))
            throw new IllegalArgumentException("url not be null");
        this.mUrl = url;
        return this;
    }

    public PostRequestBuilder setParams(Map<String, String> params) {
        if (null == params)
            throw new IllegalArgumentException("params not be null");

        this.mParams = params;
        return this;
    }

    public PostRequestBuilder addParams(String key, String value) {
        if (mParams == null) {
            mParams = new IdentityHashMap<>();
        }
        if (!TextUtils.isEmpty(key))
            mParams.put(key, value);
        return this;
    }

    public PostRequestBuilder setHeaders(Map<String, String> headers) {
        if (null == headers)
            throw new IllegalArgumentException("headers not be null");
        this.headers = headers;
        return this;
    }

    public PostRequestBuilder addHeader(String key, String values) {
        if (headers == null) {
            headers = new IdentityHashMap<>();
        }
        if (!TextUtils.isEmpty(key))
            headers.put(key, values);
        return this;
    }

    public PostRequestBuilder tag(Object tag) {
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

        FormBody.Builder encodingBuilder = new FormBody.Builder();
        appendHeaders(builder, headers);
        if (mParams != null) {
            mUrl = appendParams(mUrl, mParams);
            builder.url(mUrl);
        }
        /**
         * mRequestBody usually is file.
         */
        if (mRequestBody != null) {
            /*MultipartBody.Builder requestBody = new MultipartBody.Builder();
            requestBody.addPart(RequestBody.create(JSON, new Gson().toJson(mRequestBody)));*/
            RequestBody requestBody = RequestBody.create(JSON, new Gson().toJson(mRequestBody));
            builder.post(requestBody);
        } else {
            builder.post(encodingBuilder.build());
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

        FormBody.Builder encodingBuilder = new FormBody.Builder();
        appendHeaders(builder, headers);
        if (mParams != null) {
            mUrl = appendParams(mUrl, mParams);
            builder.url(mUrl);
        }

        if (mRequestBody != null) {
            MultipartBody.Builder requestBody = new MultipartBody.Builder();
            requestBody.addPart(RequestBody.create(JSON, new Gson().toJson(mRequestBody)));
            builder.post(requestBody.build());
        } else {
            builder.post(encodingBuilder.build());
        }
        Request request = builder.build();

        Call call = OkHttpProxy.getInstance().newCall(request);
        return call.execute();
    }
}
