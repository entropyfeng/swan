package com.example.swan;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.MyLocationStyle;

public class FuncOfMap {
    private static MyLocationStyle myLocationStyle;

    protected static void appearBluePot(AMap aMap){
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2000);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
    }

    protected static void appearIndoorMap(AMap aMap){
        aMap.showIndoorMap(true);
    }


}
