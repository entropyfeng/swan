package com.example.swan.listener;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.amap.api.maps.model.LatLng;
import com.example.swan.R;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

public class PointTouchClickListener implements View.OnClickListener {
    private LatLng position;
    private Context context;
    public PointTouchClickListener(LatLng position,Context context) {
        this.position = position;
        this.context=context;
    }

    public LatLng getPosition() {
        return position;
    }

    @Override
    public void onClick(View v) {
        QMUIBottomSheet qmuiBottomSheet = new QMUIBottomSheet(context);
        Log.i("维度",position.latitude+"");
        Log.i("经度",position.longitude+"");
        //导航按钮
        LayoutInflater inflater = LayoutInflater.from(context);
        View view= inflater.inflate(R.layout.main_bottom,null);
        qmuiBottomSheet.setContentView(view);
        qmuiBottomSheet.show();
    }
}
