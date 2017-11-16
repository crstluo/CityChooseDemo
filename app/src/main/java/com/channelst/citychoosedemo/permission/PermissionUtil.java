package com.channelst.citychoosedemo.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Luojingjing on 2017/11/14.
 * 权限请求工具类,主要方法为检查权限和申请权限
 */
public class PermissionUtil {
    private static final String TAG = "PermissionUtil";

    public static final int LOCATE_PERMISSION_REQUEST = 127;
    private static String[] locatePermissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    public static final int CITY_PERMISSION_REQUEST = 126;
    private static String[] cityPermissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    /**
     * 获取定位所需运行时权限
     * @param context
     */
    public static void getLocatePermissions(Activity context) {
        getPermissions(context, locatePermissions, LOCATE_PERMISSION_REQUEST);
    }
/**
     * 获取初始化城市数据所需运行时权限
     * @param context
     */
    public static void getCityPermissions(Activity context) {
        getPermissions(context, cityPermissions, CITY_PERMISSION_REQUEST);
    }

    /**
     * 获取运行时权限
     * @param context
     * @param permissions
     * @param permissionRequest
     */
    @TargetApi(23)
    public static void getPermissions(Activity context, String[] permissions, int permissionRequest) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissionList = new ArrayList<String>();
            for (String permission:permissions) {
                if(context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
                    permissionList.add(permission);
                }
            }
            Log.i(TAG, "--------permissionList.size() : "
                    + permissionList.size());
            if (permissionList.size() > 0) {
                context.requestPermissions(permissionList.toArray(new String[permissionList.size()]), permissionRequest);
            }
        }
    }

    /**
     * 判断需要申请的运行时权限目前的状态
     * @param context
     * @param permissions
     * @return 返回true时说明所有的权限都已经赋予,反之说明有权限未赋予
     */
    @TargetApi(23)
    public static boolean checkPermissions(Activity context, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission:permissions) {
                if(context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 检查定位所需权限
     * @param context
     * @return
     */
    @TargetApi(23)
    public static boolean checkLocatePermissions(Activity context) {
        return checkPermissions(context, locatePermissions);
    }

    /**
     * 检查初始化城市数据所需权限
     * @param context
     * @return
     */
    @TargetApi(23)
    public static boolean checkCityPermissions(Activity context) {
        return checkPermissions(context, cityPermissions);
    }

}
