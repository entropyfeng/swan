package com.example.swan.route;
 
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
 
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.example.swan.R;
import com.example.swan.util.AMapUtil;

/**
 * Created by Zeng on 2019/06/05.
 * 公交路线详情
 */
 
public class DriveRouteDetailActivity extends Activity {
 
    private DrivePath mDrivePath;
    private DriveRouteResult mDriveRouteResult;
    private TextView mTitle, mTitleDriveRoute, mDesDriveRoute;
    private ListView mDriveSegmentList;
    private DriveSegmentListAdapter mDriveSegmentListAdapter;
 
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        getIntentData();
        init();
    }
 
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mDrivePath = intent.getParcelableExtra("drive_path");
        mDriveRouteResult = intent.getParcelableExtra("drive_result");
    }
 
    private void init() {
        mTitle = findViewById(R.id.title_center);
        mTitleDriveRoute = findViewById(R.id.firstline);
        mDesDriveRoute = findViewById(R.id.secondline);
        mTitle.setText("驾车路线详情");
        String dur = AMapUtil.getFriendlyTime((int) mDrivePath.getDuration());
        String dis = AMapUtil.getFriendlyLength((int) mDrivePath.getDistance());
        mTitleDriveRoute.setText(dur + "(" + dis + ")");
        int taxiCost = (int) mDriveRouteResult.getTaxiCost();
        mDesDriveRoute.setText("打车约"+taxiCost+"元");
        mDesDriveRoute.setVisibility(View.VISIBLE);
        configureListView();
    }
 
    private void configureListView() {
        mDriveSegmentList = findViewById(R.id.bus_segment_list);
        mDriveSegmentListAdapter = new DriveSegmentListAdapter(
                this.getApplicationContext(), mDrivePath.getSteps());
        mDriveSegmentList.setAdapter(mDriveSegmentListAdapter);
    }
 
    public void onBackClick(View view) {
        this.finish();
    }
}