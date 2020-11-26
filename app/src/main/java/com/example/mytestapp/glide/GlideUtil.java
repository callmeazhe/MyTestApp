package com.example.mytestapp.glide;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

public class GlideUtil {

    public static void loadImage(Context context, String url, int placeHolder, ImageView imageView) {
        Glide.with(context).load(url).placeholder(placeHolder).into(imageView);
    }

}
