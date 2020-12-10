package com.example.mytestapp.net;



import com.example.mytestapp.net.builder.DeleteRequestBuilder;
import com.example.mytestapp.net.builder.GetRequestBuilder;
import com.example.mytestapp.net.builder.PostRequestBuilder;
import com.example.mytestapp.net.builder.PutRequestBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

/**
 * Adopt the agent model;
 * This object is a singleton;
 * Provide 4 medium request mode;
 * Created by 30035167 on 2017/9/12.
 */

public class OkHttpProxy {

    private static volatile OkHttpClient mHttpClient = null;
    private static long TIME_OUT = 50;

    private static OkHttpClient init() {
        synchronized (OkHttpProxy.class) {
            if (mHttpClient == null) {
                mHttpClient = new OkHttpClient();
            }
        }
        return mHttpClient;
    }

    public static OkHttpClient getInstance() {
        return mHttpClient == null ? init() : mHttpClient;
    }

    public static void setInstance(OkHttpClient okHttpClient) {
        OkHttpProxy.mHttpClient = okHttpClient;
    }

    /**
     * GET
     *
     * @return
     */
    public static GetRequestBuilder get() {
        initOkHttp();
        return new GetRequestBuilder();
    }

    /**
     * POST
     *
     * @return
     */
    public static PostRequestBuilder post() {
        initOkHttp();
        return new PostRequestBuilder();
    }

    /**
     * PUT
     *
     * @return
     */
    public static PutRequestBuilder put() {
        return new PutRequestBuilder();
    }

    /**
     * DELETE
     *
     * @return
     */
    public static DeleteRequestBuilder delete() {
        return new DeleteRequestBuilder();
    }

    /**
     * Use custom's config.
     */
    public static void initOkHttp() {

        OkHttpClient.Builder builder = OkHttpProxy.getInstance().newBuilder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier());

        OkHttpProxy.setInstance(builder.build());
    }

    /**
     * CANCEL
     *
     * @param tag
     */
    public static void cancel(Object tag) {
        Dispatcher dispatcher = getInstance().dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : dispatcher.runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

}
