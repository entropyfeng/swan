package com.example.swan;

import android.content.Intent;
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
        initTopBar();;
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

    private void setCenterToGuiLin(Bundle savedInstanceState) {

    }

    /**
     * 默认的定位参数
     * @return
     */
    /*private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }*/

    /**
     * 定位监听
     */
    /*AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {

                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if(location.getErrorCode() == 0){
                    sb.append("定位成功" + "\n");
                    sb.append("定位类型: " + location.getLocationType() + "\n");
                    sb.append("经    度    : " + location.getLongitude() + "\n");
                    sb.append("纬    度    : " + location.getLatitude() + "\n");
                    sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                    sb.append("提供者    : " + location.getProvider() + "\n");

                    sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                    sb.append("角    度    : " + location.getBearing() + "\n");
                    // 获取当前提供定位服务的卫星个数
                    sb.append("星    数    : " + location.getSatellites() + "\n");
                    sb.append("国    家    : " + location.getCountry() + "\n");
                    sb.append("省            : " + location.getProvince() + "\n");
                    sb.append("市            : " + location.getCity() + "\n");
                    sb.append("城市编码 : " + location.getCityCode() + "\n");
                    sb.append("区            : " + location.getDistrict() + "\n");
                    sb.append("区域 码   : " + location.getAdCode() + "\n");
                    sb.append("地    址    : " + location.getAddress() + "\n");
                    sb.append("兴趣点    : " + location.getPoiName() + "\n");
                    //定位完成的时间
                    sb.append("定位时间: " + Utils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
                } else {
                    //定位失败
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                }
                sb.append("***定位质量报告***").append("\n");
                sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启":"关闭").append("\n");
                sb.append("* GPS状态：").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
                sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
                sb.append("* 网络类型：" + location.getLocationQualityReport().getNetworkType()).append("\n");
                sb.append("* 网络耗时：" + location.getLocationQualityReport().getNetUseTime()).append("\n");
                sb.append("****************").append("\n");
                //定位之后的回调时间
                sb.append("回调时间: " + Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

                //解析定位结果，
                String result = sb.toString();
                tvResult.setText(result);
            } else {
                tvResult.setText("定位失败，loc is null");
            }
        }
    };*/

    /**
     * 获取GPS状态的字符串
     * @param
     * @return
     */
   /* private String getGPSStatusString(int statusCode){
        String str = "";
        switch (statusCode){
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                break;
        }
        return str;
    }
    */


   private void initTopBar(){
       topBar=findViewById(R.id.main_top_bar);
       QMUIRadiusImageView imageView=new QMUIRadiusImageView(topBar.getContext());
       imageView.setOnClickListener(v->{

           Intent intent = new Intent();
           intent.setClass(MainActivity.this, MenuActivity.class);
           startActivity(intent);
       }
   );
       imageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_icon_user));
       imageView.setCircle(true);
       topBar.addLeftView(imageView,imageView.getId());

       TextView textView=new TextView(topBar.getContext());
       final String DEFAULT_LABEL_CONTENT="查找地点、公交、地铁";

       textView.setOnClickListener(v->{
           Intent intent = new Intent();
           intent.setClass(MainActivity.this, SearchActivity.class);
           startActivity(intent);
       });
       textView.setText(DEFAULT_LABEL_CONTENT);
       topBar.addRightView(textView,textView.getId());

   }

}
