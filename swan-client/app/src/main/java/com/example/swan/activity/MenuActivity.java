package com.example.swan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.swan.R;
import com.example.swan.util.AccountHelper;
import com.example.swan.util.CommonUtil;
import com.example.swan.util.QMUITipDialogUtil;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

public class MenuActivity extends AppCompatActivity {

    private QMUIGroupListView groupListView;
    private QMUICommonListItemView logStatusItem;
    private final String LOGOUT_LABEL = "退出登录";
    private final String LOGIN_LABEL = "点击登录";
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        groupListView = findViewById(R.id.menu_group_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        QMUICommonListItemView myAccountItem = groupListView.createItemView("我的账户");
        myAccountItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        myAccountItem.setOrientation(QMUICommonListItemView.REDDOT_POSITION_LEFT);

        QMUIGroupListView.Section accountSection = new QMUIGroupListView.Section(this);
        accountSection.addItemView(myAccountItem, myAccountListener);
        accountSection.addTo(groupListView);

        logStatusItem = groupListView.createItemView("");
        adjustLogStatus();
        logStatusItem.setOrientation(QMUICommonListItemView.REDDOT_POSITION_LEFT);


        QMUIGroupListView.Section mapSection = new QMUIGroupListView.Section(this);

        QMUICommonListItemView offlineMapItem = groupListView.createItemView("离线地图");
        offlineMapItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        offlineMapItem.setOrientation(QMUICommonListItemView.REDDOT_POSITION_LEFT);

        mapSection.addItemView(offlineMapItem, offlineMapListener);
        mapSection.addTo(groupListView);


        //底部退出选项
        QMUIGroupListView.Section logStatusSection = new QMUIGroupListView.Section(this);
        logStatusSection.addItemView(logStatusItem, logStatusListener);
        logStatusSection.addTo(groupListView);


    }


    @Override
    protected void onRestart() {
        super.onRestart();
        adjustLogStatus();
    }

    /**
     * 转变logStatusItem 的状态
     */
    private void adjustLogStatus() {
        //如果登录
        if (AccountHelper.isLogin(MenuActivity.this)) {
            logStatusItem.setDetailText(LOGOUT_LABEL);
        } else {
            logStatusItem.setDetailText(LOGIN_LABEL);

        }
    }

    private View.OnClickListener logStatusListener = v -> {
        //如果登录
        if (AccountHelper.isLogin(MenuActivity.this)) {
            AccountHelper.logout(MenuActivity.this);
            logStatusItem.setDetailText(LOGIN_LABEL);
            Log.d("logStatus", "已经登录");
        } else {
            Log.d("logStatus", "未登录");
            Intent intent = new Intent();
            intent.setClass(MenuActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener myAccountListener = v -> {

        if (AccountHelper.isLogin(MenuActivity.this)) {

            Intent intent = new Intent();
            intent.setClass(MenuActivity.this, AccountActivity.class);
            startActivity(intent);


        } else {
            QMUITipDialog tipDialog = QMUITipDialogUtil.getFailTipDialog(MenuActivity.this, "请先登录");

            handler.post(() -> {
                tipDialog.show();
                handler.postDelayed(tipDialog::dismiss, 1000);
            });

        }
    };

    private View.OnClickListener offlineMapListener = v -> {
        Intent intent = new Intent();
        intent.setClass(MenuActivity.this, com.amap.api.maps.offlinemap.OfflineMapActivity.class);
        startActivity(intent);
    };
}
