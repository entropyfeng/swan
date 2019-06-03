package com.example.swan;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.example.swan.activity.MenuActivity;
import com.example.swan.activity.SearchActivity;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.QMUITopBar;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;

    private MapView mapView = null;
    private AMap aMap = null;
    private UiSettings uiSettings = null;//定义一个UiSettings对象

    private QMUITopBar topBar;

    //private AMapLocationClient locationClient = null;
    //private AMapLocationClientOption locationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        //获取地图控件引用
        mapView = (MapView) findViewById(R.id.main_map);
        initTopBar();

        //在activity执行onCreate时执行mapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);


        if (aMap == null) {
            aMap = mapView.getMap();
        }
        initMyLocation();
        // 定义北京市经纬度坐标（此处以北京坐标为例）
        LatLng centerBJPoint = new LatLng(25.273566, 110.290195);
        // 定义了一个配置 AMap 对象的参数类
        AMapOptions mapOptions = new AMapOptions();
        // 设置了一个可视范围的初始化位置
        // CameraPosition 第一个参数： 目标位置的屏幕中心点经纬度坐标。
        // CameraPosition 第二个参数： 目标可视区域的缩放级别
        // CameraPosition 第三个参数： 目标可视区域的倾斜度，以角度为单位。
        // CameraPosition 第四个参数： 可视区域指向的方向，以角度为单位，从正北向顺时针方向计算，从0度到360度
        mapOptions.camera(new CameraPosition(centerBJPoint, 10f, 0, 0));
        // 定义一个 MapView 对象，构造方法中传入 mapOptions 参数类
        MapView mapView = new MapView(this, mapOptions);
        // 调用 onCreate方法 对 MapView LayoutParams 设置
        System.out.println("chenggong");
        mapView.onCreate(savedInstanceState);


        //在Activity页面调用startActvity启动离线地图组件
        //        startActivity(new Intent(MainActivity.getApplicationContext(),
        //                com.amap.api.maps.offlinemap.OfflineMapActivity.class));


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
        /*
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        */
        System.out.println("执行到了调用BluePot");
        FuncOfMap.appearBluePot(aMap);
        FuncOfMap.appearIndoorMap(aMap);
        FuncOfMap.appearControls(aMap);
    }

    /*private void setCenterToGuiLin(Bundle savedInstanceState) {

    }*/

    private void initTopBar() {
        topBar = findViewById(R.id.main_top_bar);
        /*QMUIRadiusImageView imageView = new QMUIRadiusImageView(topBar.getContext());

        imageView.setOnClickListener(v -> {

                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, MenuActivity.class);
                    startActivity(intent);
                }
        );
        imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_icon_user));
        imageView.setCircle(true);*/

        //topBar.addLeftView(imageView, imageView.getId());
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
        //topBar.addRightView(textView, textView.getId());
        textView.setTextColor(Color.parseColor("#9E9E9E"));


        topBar.setCenterView(textView);

    }

}
