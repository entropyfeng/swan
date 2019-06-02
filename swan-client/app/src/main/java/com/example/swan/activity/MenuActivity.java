package com.example.swan.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.swan.R;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

public class MenuActivity extends AppCompatActivity {

    private QMUIGroupListView groupListView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        groupListView=findViewById(R.id.menu_group_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        QMUICommonListItemView logoutItem=groupListView.createItemView("退出登录");
        logoutItem.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        logoutItem.setOrientation(QMUICommonListItemView.REDDOT_POSITION_LEFT);

        QMUIGroupListView.Section section= new QMUIGroupListView.Section(this);
        section.addItemView(logoutItem,null);
        section.addTo(groupListView);

    }
}
