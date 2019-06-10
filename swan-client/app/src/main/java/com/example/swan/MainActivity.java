package com.example.swan;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.example.swan.activity.Constants;
import com.example.swan.activity.MenuActivity;
import com.example.swan.activity.RouteActivity;
import com.example.swan.activity.SearchActivity;
import com.example.swan.listener.PointTouchClickListener;
import com.example.swan.overlay.DrivingRouteOverlay;
import com.example.swan.overlay.PoiOverlay;
import com.example.swan.overlay.RideRouteOverlay;
import com.example.swan.overlay.WalkRouteOverlay;
import com.example.swan.route.BusResultListAdapter;
import com.example.swan.route.DriveRouteDetailActivity;
import com.example.swan.route.RideRouteDetailActivity;
import com.example.swan.route.WalkRouteDetailActivity;
import com.example.swan.util.AMapUtil;
import com.example.swan.util.ToastUtil;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AMap.OnMapClickListener, AMap.OnInfoWindowClickListener, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter {

    private MapView mapView = null;
    private AMap aMap = null;
    private PoiSearch.Query query;
    private PoiResult poiResult;
    private TextView keywordsTextView;
    private ImageView cleanKeyWords;
    private ProgressDialog progDialog = null;// 搜索时进度条
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器

    private LatLonPoint aStartPoint = new LatLonPoint(25.063734,110.300496);//起点
    private static LatLonPoint aEndPoint= new LatLonPoint(25.261406,110.28231);//终点
    private String mCurrentCityName = "桂林";

    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private WalkRouteResult mWalkRouteResult;
    private RideRouteResult mRideRouteResult;
    private BusRouteResult mBusRouteResult;

    private final int ROUTE_TYPE_BUS = 1;
    private final int ROUTE_TYPE_DRIVE = 2;
    private final int ROUTE_TYPE_WALK = 3;
    private final int ROUTE_TYPE_RIDE = 4;

    private LinearLayout mBusResultLayout;
    private ListView mBusResultList;
    private RelativeLayout mBottomRoute;
    private TextView mRotueTimeDes, mRouteDetailDes;

    public static final int REQUEST_CODE = 100;
    public static final int RESULT_CODE_INPUT_TIPS = 101;
    public static final int RESULT_CODE_KEYWORDS = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        getSupportActionBar().hide();
        checkPermission();
        //获取地图控件引用
        mapView = findViewById(R.id.main_map);
        initTopBar();
        //在activity执行onCreate时执行mapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        initMyLocation();
        initSearch();
        initRoute();
    }

    private void initRoute() {
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(onRouteSearchListener);
        //mBottomRoute = findViewById(R.id.bottom_layout);
        mRotueTimeDes = findViewById(R.id.firstline);
        mRouteDetailDes = findViewById(R.id.secondline);
        //mBusResultLayout = (LinearLayout) findViewById(R.id.bus_result);
        //mBusResultList = (ListView) findViewById(R.id.bus_result_list);
        //mBottomRoute = (RelativeLayout) findViewById(R.id.routemap_header);
        //mBottomRoute.setVisibility(View.GONE);
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
        acquireLatLonofBluePot();
    }

    /**
     * 获取当前定位点相关信息
     */
    private void acquireLatLonofBluePot(){
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }

    /**
     * 注册监听
     */
    private void registerListener() {
        aMap.setOnMapClickListener(MainActivity.this);
        aMap.setOnMarkerClickListener(MainActivity.this);
        aMap.setOnInfoWindowClickListener(MainActivity.this);
        aMap.setInfoWindowAdapter(MainActivity.this);

    }

    private void initSearch() {
        cleanKeyWords = findViewById(R.id.main_clean_keywords);
        cleanKeyWords.setOnClickListener(cleanKeyWordListener);
    }

    private void initTopBar() {
        QMUITopBar topBar = findViewById(R.id.main_top_bar);
        topBar.addLeftImageButton(R.drawable.ic_icon_user, 0).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, MenuActivity.class);
            startActivity(intent);
        });
        keywordsTextView = new TextView(topBar.getContext());
        final String DEFAULT_LABEL_CONTENT = "查找地点、公交、地铁";
        keywordsTextView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SearchActivity.class);

            // 向SearchActivity 请求数据

            startActivityForResult(intent, REQUEST_CODE);
        });
        keywordsTextView.setText(DEFAULT_LABEL_CONTENT);
        keywordsTextView.setTextColor(Color.parseColor("#9E9E9E"));
        keywordsTextView.setHeight(80);
        keywordsTextView.setWidth(600);
        keywordsTextView.setGravity(Gravity.CENTER);
        keywordsTextView.setBackgroundColor(Color.parseColor("#9E9E9E"));
        keywordsTextView.setBackgroundResource(R.drawable.main_top_bar_text_view_border);
        topBar.setCenterView(keywordsTextView);
    }



    //检查权限
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    10086);//自定义的code
        }
    }

    /**
     * 用marker展示输入提示list选中数据
     *
     * @param tip
     */
    private void addTipMarker(Tip tip) {
        if (tip == null) {
            return;
        }
        Marker poiMarker = aMap.addMarker(new MarkerOptions());
        LatLonPoint point = tip.getPoint();
        if (point != null) {
            LatLng markerPosition = new LatLng(point.getLatitude(), point.getLongitude());
            poiMarker.setPosition(markerPosition);
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 17));
        }
        poiMarker.setTitle(tip.getName());
        poiMarker.setSnippet(tip.getAddress());
    }


    AMap.OnMarkerClickListener onMarkerClickListener= marker -> {
        marker.showInfoWindow();
        return false;
    };

    AMap.InfoWindowAdapter infoWindowAdapter=new AMap.InfoWindowAdapter() {
        @Override
        public View getInfoWindow(Marker marker) {
            View view = getLayoutInflater().inflate(R.layout.poi_keyword_search_uri, null);
            TextView title = view.findViewById(R.id.poi_keyword_search_title);
            title.setText(marker.getTitle());
            TextView snippet = view.findViewById(R.id.poi_keyword_search_snippet);
            snippet.setText(marker.getSnippet());
            PointTouchClickListener pointTouchClickListener=new PointTouchClickListener(marker.getPosition(),MainActivity.this);
            view.setOnClickListener(pointTouchClickListener);
            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    };



    /**
     * 设置页面监听
     */
    private void setUpMap() {
        aMap.setOnMarkerClickListener(onMarkerClickListener);// 添加点击marker监听事件
        aMap.setInfoWindowAdapter(infoWindowAdapter);// 添加显示infowindow监听事件
        aMap.getUiSettings().setRotateGesturesEnabled(false);
    }

    /**
     * 输入提示activity选择结果后的处理逻辑
     *向Search Activity 请求
     * @param requestCode 请求码 是 {@link MainActivity 的} REQUEST_CODE 属性
     * @param resultCode {@link SearchActivity }返回的状态码
     * @param data 携带的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //反回提示的具体的信息
        if (resultCode == RESULT_CODE_INPUT_TIPS && data != null) {

            aMap.clear();
            Tip tip = data.getParcelableExtra(Constants.EXTRA_TIP);
            //如果返回提示为空
            if (tip.getPoiID() == null || tip.getPoiID().equals("")) {
                doSearchQuery(tip.getName());
            } else {
                addTipMarker(tip);
            }
            keywordsTextView.setText(tip.getName());
            if (!tip.getName().equals("")) {
                cleanKeyWords.setVisibility(View.VISIBLE);
            }
            //没有选择具体的提示信息，返回输入的信息返
        } else if (resultCode == RESULT_CODE_KEYWORDS && data != null) {
            aMap.clear();
            String keywords = data.getStringExtra(Constants.KEY_WORDS_NAME);
            if (keywords != null && !keywords.equals("")) {
                doSearchQuery(keywords);
            }
            keywordsTextView.setText(keywords);
            if (!"".equals(keywords)) {
                cleanKeyWords.setVisibility(View.VISIBLE);
            }
        }
    }

    public LatLonPoint getStartLatLonPoint(){
        return aStartPoint;
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(String keywords) {
        showProgressDialog();// 显示进度框
        int currentPage = 1;
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query(keywords, "", Constants.DEFAULT_CITY);
        // 设置每页最多返回多少条poiitem
        query.setPageSize(10);
        // 设置查第一页
        query.setPageNum(currentPage);

        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(poiSearchListener);
        poiSearch.searchPOIAsyn();
    }

    PoiSearch.OnPoiSearchListener poiSearchListener=new PoiSearch.OnPoiSearchListener() {

        /**
         * POI信息查询回调方法
         */
        @Override
        public void onPoiSearched(PoiResult result, int rCode) {
            dismissProgressDialog();// 隐藏对话框
            if (rCode == 1000) {
                if (result != null && result.getQuery() != null) {// 搜索poi的结果
                    if (result.getQuery().equals(query)) {// 是否是同一条
                        poiResult = result;
                        // 取得搜索到的poiitems有多少页
                        List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                        List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                        if (poiItems != null && poiItems.size() > 0) {
                            aMap.clear();// 清理之前的图标
                            PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                            poiOverlay.removeFromMap();
                            poiOverlay.addToMap();
                            poiOverlay.zoomToSpan();
                        } else if (suggestionCities != null && suggestionCities.size() > 0) {
                            showSuggestCity(suggestionCities);
                        } else {
                            ToastUtil.show(MainActivity.this,
                                    R.string.poi_no_result);
                        }
                    }
                } else {
                    ToastUtil.show(MainActivity.this,
                            R.string.poi_no_result);
                }
            } else {
                ToastUtil.showerror(MainActivity.this, rCode);
            }

        }



        @Override
        public void onPoiItemSearched(PoiItem poiItem, int i) {

        }
    };
    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        // 要输入的poi搜索关键字
        String keyWords = "";
        progDialog.setMessage("正在搜索:\n"+ keyWords);
        progDialog.show();
    }


    /**
     * 隐藏进度框
     */
    private void dismissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }


    View.OnClickListener cleanKeyWordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            keywordsTextView.setText("");
            aMap.clear();
            cleanKeyWords.setVisibility(View.GONE);
        }
    };

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String information = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            information += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtil.show(MainActivity.this, information);

    }



    public void setfromandtoMarker(LatLonPoint mStartPoint ,LatLonPoint mEndPoint) {
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));

        searchRouteResult(mStartPoint,mEndPoint,ROUTE_TYPE_BUS, RouteSearch.DrivingDefault);
    }


        public AMapLocationListener mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。
                        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        amapLocation.getLatitude();//获取纬度
                        amapLocation.getLongitude();//获取经度
                        aStartPoint = new LatLonPoint(amapLocation.getLatitude(),amapLocation.getLongitude());
                        amapLocation.getAccuracy();//获取精度信息
//获取定位时间
                        mCurrentCityName = amapLocation.getCity();//城市信息
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(amapLocation.getTime());
                        df.format(date);

                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }

            }
        };

public void searchForDestination(LatLonPoint endPoint){
        //setfromandtoMarker(aStartPoint,endPoint);

        Intent intent = new Intent(MainActivity.this,RouteActivity.class);
        intent.putExtra("startPointLat",aStartPoint.getLatitude());
        intent.putExtra("startPointLon",aStartPoint.getLongitude());
        intent.putExtra("endPointLat",endPoint.getLatitude());
        intent.putExtra("endPointLon",endPoint.getLongitude());
        intent.putExtra("mCurrentCityName",mCurrentCityName);
        startActivity(intent);
}
    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(LatLonPoint mStartPoint ,LatLonPoint mEndPoint,int routeType, int mode) {
        if (mStartPoint == null) {
            Toast.makeText(this,"定位中，稍后再试...",Toast.LENGTH_SHORT).show();
            return;
        }
        if (mEndPoint == null) {
            Toast.makeText(this,"终点未设置",Toast.LENGTH_SHORT).show();
        }
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        }
        if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, mode);
            mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
        }
        if (routeType == ROUTE_TYPE_RIDE) {// 骑行路径规划
            RouteSearch.RideRouteQuery query = new RouteSearch.RideRouteQuery(fromAndTo, mode);
            mRouteSearch.calculateRideRouteAsyn(query);// 异步路径规划骑行模式查询
        }
        if (routeType == ROUTE_TYPE_BUS) {// 公交路径规划
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, mode,
                    mCurrentCityName, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
        }
    }



    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    RouteSearch.OnRouteSearchListener onRouteSearchListener=new RouteSearch.OnRouteSearchListener() {
        @Override
        public void onBusRouteSearched(BusRouteResult result, int errorCode) {
            dismissProgressDialog();
            aMap.clear();// 清理地图上的所有覆盖物
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                if (result != null && result.getPaths() != null) {
                    if (result.getPaths().size() > 0) {
                        mBusRouteResult = result;
                        BusResultListAdapter mBusResultListAdapter = new BusResultListAdapter(MainActivity.this, mBusRouteResult);
                        mBusResultList.setAdapter(mBusResultListAdapter);
                    } else if (result != null && result.getPaths() == null) {
                        Toast.makeText(MainActivity.this,R.string.poi_no_result,Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this,R.string.poi_no_result,Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this,errorCode,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
            dismissProgressDialog();
            aMap.clear();// 清理地图上的所有覆盖物
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                if (result != null && result.getPaths() != null) {
                    if (result.getPaths().size() > 0) {
                        mDriveRouteResult = result;
                        final DrivePath drivePath = mDriveRouteResult.getPaths().get(0);
                        DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                                MainActivity.this, aMap, drivePath,
                                mDriveRouteResult.getStartPos(),
                                mDriveRouteResult.getTargetPos(), null);
                        drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                        drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                        drivingRouteOverlay.removeFromMap();
                        drivingRouteOverlay.addToMap();
                        drivingRouteOverlay.zoomToSpan();
                        int dis = (int) drivePath.getDistance();
                        int dur = (int) drivePath.getDuration();
                        String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                        mRotueTimeDes.setText(des);
                        mRouteDetailDes.setVisibility(View.VISIBLE);
                        int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                        mRouteDetailDes.setText("打车约"+taxiCost+"元");
                        mBottomRoute.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this,DriveRouteDetailActivity.class);
                                intent.putExtra("drive_path", drivePath);
                                intent.putExtra("drive_result",mDriveRouteResult);
                                startActivity(intent);
                            }
                        });

                    } else if (result != null && result.getPaths() == null) {
                        Toast.makeText(MainActivity.this,R.string.poi_no_result,Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this,R.string.poi_no_result,Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this,errorCode,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
            dismissProgressDialog();
            aMap.clear();// 清理地图上的所有覆盖物
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                if (result != null && result.getPaths() != null) {
                    if (result.getPaths().size() > 0) {
                        mWalkRouteResult = result;
                        final WalkPath walkPath = mWalkRouteResult.getPaths()
                                .get(0);
                        if(walkPath == null) {
                            return;
                        }
                        WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                                MainActivity.this, aMap, walkPath,
                                mWalkRouteResult.getStartPos(),
                                mWalkRouteResult.getTargetPos());
                        walkRouteOverlay.removeFromMap();
                        walkRouteOverlay.addToMap();
                        walkRouteOverlay.zoomToSpan();

                        //mBottomRoute.setVisibility(View.VISIBLE);
                        int dis = (int) walkPath.getDistance();
                        int dur = (int) walkPath.getDuration();
                        String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                        //mRotueTimeDes.setText(des);
                        //mRouteDetailDes.setVisibility(View.GONE);
                    /*mBottomRoute.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this,
                                    WalkRouteDetailActivity.class);
                            intent.putExtra("walk_path", walkPath);
                            intent.putExtra("walk_result",
                                    mWalkRouteResult);
                            startActivity(intent);
                        }
                    });*/

                    } else if (result != null && result.getPaths() == null) {
                        Toast.makeText(MainActivity.this,R.string.poi_no_result,Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this,R.string.poi_no_result,Toast.LENGTH_SHORT).show();
                }
            } else {
                ToastUtil.showerror(MainActivity.this.getApplicationContext(), errorCode);
            }
        }

        @Override
        public void onRideRouteSearched(RideRouteResult result, int errorCode) {
            dismissProgressDialog();
            aMap.clear();// 清理地图上的所有覆盖物
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                if (result != null && result.getPaths() != null) {
                    if (result.getPaths().size() > 0) {
                        mRideRouteResult = result;
                        final RidePath ridePath = mRideRouteResult.getPaths()
                                .get(0);
                        if(ridePath == null) {
                            return;
                        }
                        RideRouteOverlay rideRouteOverlay = new RideRouteOverlay(
                                MainActivity.this, aMap, ridePath,
                                mRideRouteResult.getStartPos(),
                                mRideRouteResult.getTargetPos());
                        rideRouteOverlay.removeFromMap();
                        rideRouteOverlay.addToMap();
                        rideRouteOverlay.zoomToSpan();
                        //mBottomLayout.setVisibility(View.VISIBLE);
                        int dis = (int) ridePath.getDistance();
                        int dur = (int) ridePath.getDuration();
                        String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                        //mRotueTimeDes.setText(des);
                        //mRouteDetailDes.setVisibility(View.GONE);
                        /*mBottomLayout.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext,
                                        RideRouteDetailActivity.class);
                                intent.putExtra("ride_path", ridePath);
                                intent.putExtra("ride_result",
                                        mRideRouteResult);
                                startActivity(intent);
                            }
                        });*/
                    } else if (result != null && result.getPaths() == null) {
                        Toast.makeText(MainActivity.this,R.string.poi_no_result,Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this,R.string.poi_no_result,Toast.LENGTH_SHORT).show();
                }
            } else {
                ToastUtil.showerror(MainActivity.this.getApplicationContext(), errorCode);
            }
        }

    };

}
