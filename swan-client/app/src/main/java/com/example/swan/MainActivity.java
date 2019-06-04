package com.example.swan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.swan.activity.LoginActivity;
import com.example.swan.activity.MenuActivity;
import com.example.swan.activity.SearchActivity;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PoiSearch.OnPoiSearchListener {

    private TextView tvResult;

    private MapView mapView = null;
    private AMap aMap = null;
    private UiSettings uiSettings = null;//定义一个UiSettings对象

    private QMUITopBar topBar;
    private PoiSearch poiSearch;
    private PoiSearch.Query query;
    private PoiResult poiResult;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        checkPermission();
        //获取地图控件引用
        mapView = (MapView) findViewById(R.id.main_map);
        initTopBar();
        //在activity执行onCreate时执行mapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        initMyLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 初始化定位
     */
    private void initMyLocation() {
        FuncOfMap.appearBluePot(aMap);
        FuncOfMap.appearIndoorMap(aMap);
        FuncOfMap.appearControls(aMap);
    }


    private void initTopBar() {
        topBar = findViewById(R.id.main_top_bar);
        topBar.addLeftImageButton(R.drawable.ic_icon_user, 0).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, MenuActivity.class);
            startActivity(intent);
        });
        TextView textView = new TextView(topBar.getContext());
        final String DEFAULT_LABEL_CONTENT = "查找地点、公交、地铁";
        textView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });
        textView.setText(DEFAULT_LABEL_CONTENT);
        textView.setTextColor(Color.parseColor("#9E9E9E"));
        textView.setHeight(80);
        textView.setWidth(600);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(Color.parseColor("#9E9E9E"));
        textView.setBackgroundResource(R.drawable.main_top_bar_text_view_border);
        topBar.setCenterView(textView);
    }

    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
    }


    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},
                   10086);//自定义的code
        }
    }

}
