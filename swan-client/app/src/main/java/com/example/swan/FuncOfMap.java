package com.example.swan;

import android.content.Intent;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;

import static android.support.v4.content.ContextCompat.startActivity;

public class FuncOfMap {
    private static MyLocationStyle myLocationStyle;
    private static UiSettings mUiSettings;

    protected static void appearBluePot(AMap aMap){
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2000);
        aMap.setMyLocationStyle(myLocationStyle);
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

    protected static void setCenterToGuiLin(AMap aMap){

    }
}
