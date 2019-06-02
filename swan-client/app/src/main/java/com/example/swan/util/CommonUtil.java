package com.example.swan.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;

public class CommonUtil {
    public static Drawable formatDrawable(Context context, int id) {

        return formatDrawable(context, id, 15);
    }
    public static Drawable formatDrawable(Context context, int id, int size) {

        Drawable res = ContextCompat.getDrawable(context, id);
        int iconShowSize = QMUIDisplayHelper.dp2px(context, size);
        assert res != null;
        res.setBounds(0, 0, iconShowSize, iconShowSize);

        return res;
    }
}
