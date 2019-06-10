package com.example.swan.listener;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.example.swan.MainActivity;
import com.example.swan.R;
import com.example.swan.activity.RouteActivity;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

public class PointTouchClickListener implements View.OnClickListener {
    private LatLng position;
    private MainActivity mainActivity;
    private LatLonPoint startPoint;
    private LatLonPoint endPoint;
    public PointTouchClickListener(LatLng position, MainActivity mainActivity) {
        this.position = position;
        this.mainActivity = mainActivity;
    }

    public LatLng getPosition() {
        return position;
    }


    @Override
    public void onClick(View v) {
        QMUIBottomSheet qmuiBottomSheet = new QMUIBottomSheet(mainActivity);
        Log.i("维度", position.latitude + "");
        Log.i("经度", position.longitude + "");
        //导航按钮
        LayoutInflater inflater = LayoutInflater.from(mainActivity);
        View bottomView = inflater.inflate(R.layout.main_bottom, null);

        TextView routeView = bottomView.findViewById(R.id.main_bottom_route);
        routeView.setOnClickListener(view -> {
            endPoint = new LatLonPoint(position.latitude, position.longitude);
            mainActivity.searchForDestination(endPoint);
        });
        qmuiBottomSheet.setContentView(bottomView);
        qmuiBottomSheet.show();


    }

}
