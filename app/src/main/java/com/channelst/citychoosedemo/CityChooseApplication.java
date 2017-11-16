package com.channelst.citychoosedemo;

import android.app.Application;
import android.util.Log;

import com.channelst.citychoosedemo.locate.LocationService;

/**
 * Created by Luojingjing on 2017/11/13.
 * 自定义application,主要用于初始化定位服务
 */
public class CityChooseApplication extends Application {
    public LocationService locationService;
    @Override
    public void onCreate() {
        super.onCreate();
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
    }
}
