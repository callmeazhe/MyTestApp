package com.example.mytestapp.net.parser;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by 30035167 on 2017/9/13.
 */

public class OkTextParser extends OkBaseParser<String> {
    @Override
    protected String parse(Response response) {
        if (response.isSuccessful()) {
            try {
                return response.body().string();
            } catch (IOException e) {
                return null;
            }
        }

        return null;
    }
}
