package com.channelst.citychoosedemo.locate;

/**
 * Created by Luojingjing on 2017/11/13.
 * 定位服务参数
 */
public class LocationConfig {
    //LocationClientOption params
    //gcj02：国测局坐标；
    //bd09ll：百度经纬度坐标；
    //bd09：百度墨卡托坐标；
    public static final String COORTYPE_BD09LL =  "bd09ll" ;
    public static final String COORTYPE_GCJ02 =  "gcj02" ;
    public static final String COORTYPE_BD09 =  "bd09" ;

    //可选，设置发起定位请求的间隔，int类型，单位ms
    //如果设置为0，则代表单次定位，即仅定位一次，默认为0
    //如果设置非0，需设置1000ms以上才有效
    public static final int ScanSpan =  10000 ;

    public static final boolean IsNeedAddress = true;//是否需要地址信息
    public static final boolean IsNeedLocationDescribe = true;//是否需要地址描述
    public static final boolean NeedDeviceDirect = false;//是否需要设备方向结果
    public static final boolean LocationNotify = false;//是否当gps有效时按照1S1次频率输出GPS结果
    public static final boolean IgnoreKillProcess = true;//是否在stop的时候杀死这个进程
    public static final boolean IsNeedLocationPoiList = false;//是否需要POI结果
    public static final boolean IgnoreCacheException = false;//是否收集CRASH信息
    public static final boolean IsNeedAltitude = false;//定位时是否需要海拔信息

    public static final int ERROR_NO_GPS = 62;

}
