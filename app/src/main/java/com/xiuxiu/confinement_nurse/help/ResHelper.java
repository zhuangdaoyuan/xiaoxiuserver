package com.xiuxiu.confinement_nurse.help;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.xiuxiu.confinement_nurse.utlis.AdaptScreenUtils;
import com.xiuxiu.confinement_nurse.utlis.LogUtils;
import com.xiuxiu.confinement_nurse.utlis.Utils;

import java.util.HashMap;


public final class ResHelper {
    private static final String TAG = ResHelper.class.getSimpleName();

    private ResHelper() {
    }

    public static String getString(int resId) {
        try {
            return Utils.getApp().getString(resId);
        } catch (Throwable throwable) {
            LogUtils.e(TAG, throwable.getMessage());
        }
        return "";
    }

    public static Drawable getDrawable(int resId) {
        try {
            return ContextCompat.getDrawable(Utils.getApp(), resId);
        } catch (Throwable throwable) {
            LogUtils.e(TAG, throwable.getMessage());
        }
        return new ColorDrawable(Color.TRANSPARENT);
    }

    public static int getColor(int resId) {
        try {
            return ContextCompat.getColor(Utils.getApp(), resId);
        } catch (Throwable throwable) {
            LogUtils.e(TAG, throwable.getMessage());
        }
        return 0;
    }


    public static int pt2Px(int i){
        return AdaptScreenUtils.pt2Px(Utils.getApp(),i);
    }
    /**
     * 对svg染色
     */
    public static Drawable svgTit(Context context, int colorRes, int drawabeRes) {
        Drawable drawable=null;
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            drawable = VectorDrawableCompat.create(context.getResources(), drawabeRes, null);
//        } else {
//
//        }
        drawable =getDrawable(drawabeRes);
        if (drawable == null) {
            return null;
        }
        int color = ResHelper.getColor(colorRes);
        if (color == 0) {
            return drawable;
        }
        drawable = DrawableCompat.wrap(drawable).mutate();
        DrawableCompat.setTint(drawable, color);
        return drawable;
    }

    /**
     * 设置textview 的左侧drawable
     * @param view
     * @param drawableId
     * @param width
     * @param height
     */
    public static void  setTextLeftDrawable(TextView view,int drawableId,int width,int height){
        Drawable leftDrawable = ResHelper.getDrawable(drawableId);
        int _50 = AdaptScreenUtils.pt2Px(view.getContext(), width);
        int _51 = AdaptScreenUtils.pt2Px(view.getContext(), height);
        leftDrawable.setBounds(0, 0, _50, _51);
        view.setCompoundDrawables(leftDrawable, null, null, null);
    }



    public static Bitmap getCurrentVideoBitmap(String url){
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

        try {
            mediaMetadataRetriever.setDataSource(url,new HashMap<String, String>());
            //取得指定时间的Bitmap，即可以实现抓图（缩略图）功能
            bitmap = mediaMetadataRetriever.getFrameAtTime(1000,MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                mediaMetadataRetriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }

        if (bitmap == null) {
            return null;
        }

        //bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        bitmap = Bitmap.createBitmap(bitmap);
        return bitmap;
    }



}
