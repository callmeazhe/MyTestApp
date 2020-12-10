package com.example.mytestapp.net.parser;

import java.io.IOException;

import okhttp3.Response;

/**
 * Responsible for parsing the return value,
 * using the policy design pattern
 * Created by 30035167 on 2017/9/13.
 */

public abstract class OkBaseParser<T> {

    protected int code;

    protected abstract T parse(Response response) throws IOException;

    public T parseResponse(Response response) throws IOException {
        code = response.code();
        return parse(response);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
