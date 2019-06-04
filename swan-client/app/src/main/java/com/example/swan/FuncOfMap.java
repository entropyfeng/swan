package com.example.swan;

import com.amap.api.maps.AMap;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.MyLocationStyle;

public class FuncOfMap {
    private static MyLocationStyle myLocationStyle;
    private static UiSettings mUiSettings;

    protected static void appearBluePot(AMap aMap){
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2000);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);  //设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        myLocationStyle.showMyLocation(true);
    }

    protected static void appearIndoorMap(AMap aMap){
        aMap.showIndoorMap(true);
    }

    protected static void changeLanguage(AMap aMap){
        aMap.setMapLanguage("en");
    }

    protected static void appearControls(AMap aMap){
        mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);
    }

    protected static void searchPoi(AMap aMap){

    }
}
