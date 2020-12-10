package com.example.mytestapp.net.builder;

import android.text.TextUtils;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 30035167 on 2017/9/13.
 */

public abstract class RequestBuilder {

    protected String mUrl;
    protected Map<String, String> mParams;
    protected Object mTag;
    protected Object mRequestBody;
    protected MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    protected Map<String, String> headers;

    abstract Call enqueue(Callback callback);

    abstract Response execute() throws IOException;

    protected void appendHeaders(Request.Builder builder, Map<String, String> headers) {
        if (null == builder)
            throw new IllegalArgumentException("Request.Builder not be null");

        Headers.Builder headerBuilder = new Headers.Builder();

        if (headers == null || headers.isEmpty()) return;
        for (String key : headers.keySet()) {
            if (!TextUtils.isEmpty(key))
                headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    public void setRequestBody(Object requestBody) {
        if (null != requestBody)
            this.mRequestBody = requestBody;
    }

    protected void appendParams(FormBody.Builder builder, Map<String, String> params) {
        if (null == builder)
            throw new IllegalArgumentException("FormBody.Builder not be null");

        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
    }

    protected String appendParams(String url, Map<String, String> params) {
        if (TextUtils.isEmpty(url))
            throw new IllegalArgumentException("url not be null");

        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }

        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }



}
