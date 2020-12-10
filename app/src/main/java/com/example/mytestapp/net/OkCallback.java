package com.example.mytestapp.net;



import com.example.mytestapp.net.parser.OkBaseParser;
import com.example.mytestapp.nfc.utils.LogUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by 30035167 on 2017/9/12.
 */

public abstract class OkCallback<T> implements okhttp3.Callback {
    private OkBaseParser<T> mParser;

    public OkCallback(OkBaseParser<T> parser) {
        if (parser == null) {
            throw new IllegalArgumentException("Parser can't be null");
        }
        this.mParser = parser;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        LogUtil.e(this, e.getMessage());
        onFailure(e);
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        try {
            final T t = mParser.parseResponse(response);
            final int code = mParser.getCode();
            if (response.isSuccessful() && t != null) {
                onSuccess(code, t);
            } else {
                onFailure(new Exception(response.body().string()));
            }
        } catch (final Exception e) {
            onFailure(e);
        }
    }

    public abstract void onSuccess(int code, T t);

    public abstract void onFailure(Throwable e);

    public void onStart() {

    }
}
