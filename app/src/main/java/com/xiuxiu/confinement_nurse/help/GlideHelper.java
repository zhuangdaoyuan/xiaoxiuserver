package com.xiuxiu.confinement_nurse.help;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.noober.background.drawable.DrawableCreator;
import com.xiuxiu.confinement_nurse.R;

import java.io.File;

public final class GlideHelper {

    public static void loadVideImage(Object uri, ImageView imageView) {
        Glide.with(imageView.getContext())
                .asBitmap() // some .jpeg files are actually gif
                .load(uri)
                .apply(new RequestOptions()
                        .override(ResHelper.pt2Px(1000), ResHelper.pt2Px(576))
                        .centerCrop())
                .into(imageView);
    }

    public static void loadGardenImage(String url, ImageView imageView){
        Glide.with(imageView).load(url)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .placeholder(R.color.color_backdrop_placeholder)
                .error(R.color.color_backdrop_placeholder)
                .into(imageView);
    }

    public static void loadImage(String url, ImageView imageView) {
        Drawable build = new DrawableCreator.Builder().setSolidColor(ResHelper.getColor(R.color.color_backdrop_default)).setCornersRadius(ResHelper.pt2Px(23)).build();
        Glide.with(imageView).load(url).placeholder(build)
                .error(build).into(imageView);
    }


    public static void loadUriImage(Uri url, ImageView imageView) {
        loadUriImage(url, 23, imageView);
    }


    public static void loadUriImage(Uri url, int radius, ImageView imageView) {
        Drawable build = new DrawableCreator.Builder().setSolidColor(ResHelper.getColor(R.color.color_backdrop_default))
                .setCornersRadius(ResHelper.pt2Px(radius)).build();
        Glide.with(imageView).load(url).placeholder(build)
                .override(300, 300)
                .error(build).into(imageView);
    }
}
