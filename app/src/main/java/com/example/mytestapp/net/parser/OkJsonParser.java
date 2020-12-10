package com.example.mytestapp.net.parser;

import android.os.Build;

import com.example.mytestapp.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * Created by 30035167 on 2017/9/13.
 */
public class OkJsonParser<T> extends OkBaseParser<T> {

    protected Gson mGson;
    public Type mType;

    public OkJsonParser() {
        mType = getSuperclassTypeParameter(getClass());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            GsonBuilder gsonBuilder = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.FINAL,
                            Modifier.TRANSIENT,
                            Modifier.STATIC);
            mGson = gsonBuilder.create();
        } else {
            mGson = new Gson();
        }
    }

    @Override
    protected T parse(Response response) throws IOException {
        String body = response.body().string();
        LogUtil.outLog(body);
        LogUtil.outLog("mType=" + mType.toString());
        return mGson.fromJson(body, mType);
    }

    private static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameter = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameter.getActualTypeArguments()[0]);
    }
}
