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
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.swan.activity.Constants;
import com.example.swan.activity.MenuActivity;
import com.example.swan.activity.SearchActivity;
import com.example.swan.overlay.PoiOverlay;
import com.example.swan.util.ToastUtil;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.List;


public class MainActivity extends AppCompatActivity implements AMap.OnMarkerClickListener, PoiSearch.OnPoiSearchListener, AMap.InfoWindowAdapter {

    private MapView mapView = null;
    private AMap aMap = null;
    private String keyWords = "";// 要输入的poi搜索关键字
    private int currentPage;
    private QMUITopBar topBar;
    private Marker poiMarker;
    private PoiSearch poiSearch;
    private PoiSearch.Query query;
    private PoiResult poiResult;
    private TextView keywordsTextView;
    private ImageView cleanKeyWords;
    private ProgressDialog progDialog = null;// 搜索时进度条

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


    private void initSearch() {
        cleanKeyWords = findViewById(R.id.main_clean_keywords);
        cleanKeyWords.setOnClickListener(cleanKeyWordListener);
    }

    private void initTopBar() {
        topBar = findViewById(R.id.main_top_bar);
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


    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

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
        poiMarker = aMap.addMarker(new MarkerOptions());
        LatLonPoint point = tip.getPoint();
        if (point != null) {
            LatLng markerPosition = new LatLng(point.getLatitude(), point.getLongitude());
            poiMarker.setPosition(markerPosition);
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 17));
        }
        poiMarker.setTitle(tip.getName());
        poiMarker.setSnippet(tip.getAddress());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.poi_keyword_search_uri, null);
        TextView title = view.findViewById(R.id.poi_keyword_search_title);
        title.setText(marker.getTitle());
        TextView snippet = view.findViewById(R.id.poi_keyword_search_snippet);
        snippet.setText(marker.getSnippet());
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    /**
     * 设置页面监听
     */
    private void setUpMap() {
        aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
        aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
        aMap.getUiSettings().setRotateGesturesEnabled(false);
    }

    /**
     * 输入提示activity选择结果后的处理逻辑
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CODE_INPUT_TIPS && data != null) {
            aMap.clear();
            Tip tip = data.getParcelableExtra(Constants.EXTRA_TIP);
            if (tip.getPoiID() == null || tip.getPoiID().equals("")) {
                doSearchQuery(tip.getName());
            } else {
                addTipMarker(tip);
            }
            keywordsTextView.setText(tip.getName());
            if (!tip.getName().equals("")) {
                cleanKeyWords.setVisibility(View.VISIBLE);
            }
        } else if (resultCode == RESULT_CODE_KEYWORDS && data != null) {
            aMap.clear();
            String keywords = data.getStringExtra(Constants.KEY_WORDS_NAME);
            if (keywords != null && !keywords.equals("")) {
                doSearchQuery(keywords);
            }
            keywordsTextView.setText(keywords);
            if (!keywords.equals("")) {
                cleanKeyWords.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(String keywords) {
        showProgressDialog();// 显示进度框
        currentPage = 1;
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query(keywords, "", Constants.DEFAULT_CITY);
        // 设置每页最多返回多少条poiitem
        query.setPageSize(10);
        // 设置查第一页
        query.setPageNum(currentPage);

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + keyWords);
        progDialog.show();
    }


    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();// 隐藏对话框
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
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
            ToastUtil.showerror(this, rCode);
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
}
