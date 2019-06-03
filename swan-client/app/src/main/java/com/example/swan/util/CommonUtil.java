package com.example.swan.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;

import com.example.swan.R;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;

public class CommonUtil {
    public static Drawable formatDrawable(Context context, int id) {

        return zoomImage(context,id,100,100);
    }
    public static Drawable formatDrawable(Context context, int id, int size) {

        Drawable res = ContextCompat.getDrawable(context, id);
        int iconShowSize = QMUIDisplayHelper.dp2px(context, size);
        assert res != null;
        res.setBounds(0, 0, iconShowSize, iconShowSize);

        //得到图片的分辨率，获取宽度

        DisplayMetrics dm =QMUIDisplayHelper.getDisplayMetrics(context);
        int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        int mScreenHeight = dm.heightPixels;
//加载图片
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.logo);

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        //需要判断屏幕宽度和图片宽度的大小，来决定是放大还是缩小，如果是放大，应该还需要加上图片本身宽度，即：（倍数+1）为缩放倍数float num = ((float)bitmapWidth/mScreenWidth)+1.0f;
//得到图片宽度比
        float num = mScreenWidth / (float)bitmapWidth;


        Matrix matrix = new Matrix();
        matrix.postScale(num, num);
// 产生缩放后的Bitmap对象
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth,
                bitmapHeight, matrix, true);
        Drawable drawable = new BitmapDrawable(resizeBitmap);


        return res;
    }
    /**
     * 将本地资源图片大小缩放
     * @param resId 资源id
     * @param weight 图片宽
     * @param height 图片长
     * @return {@link Drawable}
     */
    public static Drawable zoomImage(Context context,int resId, int weight, int height){
        Resources res = context.getResources();
        Bitmap oldBmp = BitmapFactory.decodeResource(res, resId);
        Bitmap newBmp = Bitmap.createScaledBitmap(oldBmp,weight, height, true);
        return new BitmapDrawable(res, newBmp);
    }

}
