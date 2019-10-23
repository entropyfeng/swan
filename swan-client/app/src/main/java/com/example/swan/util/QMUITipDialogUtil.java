package com.example.swan.util;

import android.content.Context;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

public class QMUITipDialogUtil {
    public static QMUITipDialog getFailTipDialog(Context context, String message) {
        return new QMUITipDialog.Builder(context).setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL).setTipWord(message).create();
    }

    public static QMUITipDialog getSuccessTipDialog(Context context, String message) {
        return new QMUITipDialog.Builder(context).setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS).setTipWord(message).create();
    }


    public static QMUITipDialog getLoadingTipDialog(Context context, String message) {
        return new QMUITipDialog.Builder(context).setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING).setTipWord(message).create();

    }
}
