package com.channelst.citychoosedemo;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.channelst.citychoosedemo.citychoose.CityListAdapter;
import com.channelst.citychoosedemo.citychoose.PinyinUtil;
import com.channelst.citychoosedemo.citychoose.SharedPreferencesUtil;
import com.channelst.citychoosedemo.citychoose.SideIndexBar;
import com.channelst.citychoosedemo.database.City;
import com.channelst.citychoosedemo.database.DBHelper;
import com.channelst.citychoosedemo.locate.LocationConfig;
import com.channelst.citychoosedemo.locate.LocationService;
import com.channelst.citychoosedemo.permission.PermissionUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * 主Activity
 * 包含权限请求,百度地图定位,以及城市选择功能
 */
public class MainActivity extends AppCompatActivity implements
        CityListAdapter.OnCityClickListener, AdapterView.OnItemClickListener{

    private static final String TAG = "MainActivity";

    private LocationService locationService;//定位Service
    private ArrayList<City> allCityList = new ArrayList<>();//所有城市集合
    private ArrayList<City> cityHotList = new ArrayList<City>();//热门城市集合
    private ArrayList<String> cityHisList = new ArrayList<String>(4);//最近访问城市集合
    CityListAdapter cityListAdapter = null;
    //存放存在的汉语拼音首字母和与之对应的列表位置
    private HashMap<String, Integer> alphaIndexer = new HashMap<>();
    private boolean isLocatePermissionGranted = false;
    private boolean isCityPermissionGranted = false;

    private ListView cityListView = null;
    private String currentCity = null;
    private String locateCity = null;

    private TextView indexDialogText;

    public static final String KEY_RECENT_CITY1 = "KEY_RECENT_CITY1";
    public static final String KEY_RECENT_CITY2 = "KEY_RECENT_CITY2";
    public static final String KEY_RECENT_CITY3 = "KEY_RECENT_CITY3";
    public static final String KEY_RECENT_CITY4 = "KEY_RECENT_CITY4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate()---------");
        setContentView(R.layout.activity_main);
        //主listView
        cityListView = (ListView) findViewById(R.id.list);
        //触摸右侧索引栏需要显示在中间的TextView
        indexDialogText = (TextView) findViewById(R.id.text_dialog);

        //检查权限
        if (!PermissionUtil.checkLocatePermissions(this)){
            PermissionUtil.getLocatePermissions(this);
            isLocatePermissionGranted = false;
        } else {
            isLocatePermissionGranted = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionsResult()---requestCode : " + requestCode
            + ", permissions.length : " + permissions.length +
            ", grantResults : " + grantResults);

        if(requestCode == PermissionUtil.LOCATE_PERMISSION_REQUEST){
            for (int grantResult:grantResults) {
                Log.i(TAG, "---------grantResult : " + grantResult);
                if (grantResult != PackageManager.PERMISSION_GRANTED){
                    isLocatePermissionGranted = false;
                    finish();
                }
            }
            isLocatePermissionGranted = true;
            startLocate();
            initCityData();
        }

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (isLocatePermissionGranted){
            startLocate();
            initCityData();
        }
    }

    /**
     * 开始定位
     */
    private void startLocate(){
        // -----------location config ------------
        locationService = ((CityChooseApplication) getApplication()).locationService;
        locationService.registerListener(mListener);
        //注册监听
        locationService.start();
    }

    /**
     * 初始化城市数据
     */
    private void initCityData(){
        City city = new City("定位", "0"); //定位
        allCityList.add(city);
        city = new City("最近", "1"); // 最近
        allCityList.add(city);
        city = new City("热门", "2"); // 热门
        allCityList.add(city);
        allCityList.addAll(getCityList());
        hotCityInit();
        initRecentCities();

        cityListAdapter = new CityListAdapter(this,
                allCityList, cityHotList, cityHisList);
        cityListAdapter.setOnCityClickListener(this);
        cityListView.setAdapter(cityListAdapter);
        cityListView.setOnItemClickListener(this);

        initAlphaIndex();

        initIndexBar();
    }

    /**
     * 从已有的数据库文件中所有城市列表
     * @return
     */
    private ArrayList<City> getCityList() {
        DBHelper dbHelper = new DBHelper(this);
        ArrayList<City> list = new ArrayList<City>();
        try {
            dbHelper.createDataBase();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from city", null);
            City city;
            while (cursor.moveToNext()) {
                city = new City(cursor.getString(1), cursor.getString(2));
                list.add(city);
            }
            cursor.close();
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(list, comparator);//按拼音字母排序
        /*for (City city:list) {
            Log.i(TAG, city.toString());
        }*/
        return list;
    }


    /**
     * 初始化热门城市
     */
    public void hotCityInit() {
        City city = new City("上海", "2");
        cityHotList.add(city);
        city = new City("北京", "2");
        cityHotList.add(city);
        city = new City("广州", "2");
        cityHotList.add(city);
        city = new City("深圳", "2");
        cityHotList.add(city);
        city = new City("杭州", "2");
        cityHotList.add(city);
        city = new City("南京", "2");
        cityHotList.add(city);
        city = new City("长沙", "2");
        cityHotList.add(city);
        city = new City("西安", "2");
        cityHotList.add(city);
    }

    /**
     * 初始化最近访问城市
     */
    public void initRecentCities() {
        String recentCity1 = SharedPreferencesUtil.getString(this, KEY_RECENT_CITY1);
        String recentCity2 = SharedPreferencesUtil.getString(this, KEY_RECENT_CITY2);
        String recentCity3 = SharedPreferencesUtil.getString(this, KEY_RECENT_CITY3);
        String recentCity4 = SharedPreferencesUtil.getString(this, KEY_RECENT_CITY4);
        if (recentCity1 != null) {
            cityHisList.add(recentCity1);
        }
        if (recentCity2 != null) {
            cityHisList.add(recentCity2);
        }
        if (recentCity3 != null) {
            cityHisList.add(recentCity3);
        }
        if (recentCity4 != null) {
            cityHisList.add(recentCity4);
        }
    }

    /**
     * 将城市首字母存入集合
     * 在触摸索引栏时使用该集合,触摸索引会返回一个索引字母或者前三个特殊字符,
     * 这个字符即为集合的key,通过此key即可get此key对应的value
     */
    private void initAlphaIndex(){
        for (int i = 0; i < allCityList.size(); i++) {
            // 当前汉语拼音首字母
            String currentStr = PinyinUtil.getAlpha(allCityList.get(i).getPinyi());
            // 上一个汉语拼音首字母，如果不存在为" "
            String previewStr = (i - 1) >= 0 ? PinyinUtil.getAlpha(allCityList.get(i - 1)
                    .getPinyi()) : " ";
            if (!previewStr.equals(currentStr)) {
                String name = PinyinUtil.getAlpha(allCityList.get(i).getPinyi());
                alphaIndexer.put(name, i);
                Log.i(TAG, "name : " + name + ", i : " + i);
            }
        }
    }

    /**
     * 初始化右侧索引栏
     */
    private void initIndexBar(){
        SideIndexBar indexBar = (SideIndexBar) findViewById(R.id.index_bar);
        indexBar.setTextDialog(indexDialogText);
        indexBar.setOnLetterChangedListener(new SideIndexBar.OnLetterChangedListener() {
            @Override
            public void onChanged(String s, int position) {
                Log.e(TAG, "onChanged(), s : " + s + ", position:" + position);
                if(null != alphaIndexer.get(s)){
                    int currentPosition = alphaIndexer.get(s);
                    cityListView.setSelection(currentPosition);
                }

            }
        });
    }

    /***
     * Stop location service
     * 清空数据:
     * 1.不清空locateCity则未销毁状态下进入,定位不刷新
     * 2.不清空热门城市则则未销毁状态下进入,热门城市翻倍
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        stopLocate();
        currentCity = null;
        locateCity = null;
        allCityList.clear();
        cityHotList.clear();
        cityHisList.clear();
    }

    /**
     * 停止定位服务
     */
    private void stopLocate() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }

    /**
     * 增加最近访问城市
     * 这里使用SharedPreferences,没有使用数据库存储的形式
     * @param name
     */
    public void insertCity(String name) {
        int length = cityHisList.size();
        if (length == 0) {
            SharedPreferencesUtil.putString(this, KEY_RECENT_CITY1, name);
            cityHisList.add(name);
        } else if (length == 1) {
            String recentCity1 = SharedPreferencesUtil.getString(this, KEY_RECENT_CITY1);
            if (!recentCity1.equalsIgnoreCase(name)) {
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY1, name);
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY2, recentCity1);
                cityHisList.clear();
                cityHisList.add(name);
                cityHisList.add(recentCity1);
            }
        } else if (length == 2) {
            String recentCity1 = SharedPreferencesUtil.getString(this, KEY_RECENT_CITY1);
            String recentCity2 = SharedPreferencesUtil.getString(this, KEY_RECENT_CITY2);
            if (recentCity1.equalsIgnoreCase(name)) {
                return;
            } else if (recentCity2.equalsIgnoreCase(name)) {
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY1, name);
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY2, recentCity1);
                cityHisList.clear();
                cityHisList.add(name);
                cityHisList.add(recentCity1);
            } else {
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY1, name);
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY2, recentCity1);
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY3, recentCity2);
                cityHisList.clear();
                cityHisList.add(name);
                cityHisList.add(recentCity1);
                cityHisList.add(recentCity2);
            }
        } else if (length == 3) {
            String recentCity1 = SharedPreferencesUtil.getString(this, KEY_RECENT_CITY1);
            String recentCity2 = SharedPreferencesUtil.getString(this, KEY_RECENT_CITY2);
            String recentCity3 = SharedPreferencesUtil.getString(this, KEY_RECENT_CITY3);
            if (recentCity1.equalsIgnoreCase(name)) {
                return;
            } else if (recentCity2.equalsIgnoreCase(name)) {
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY1, name);
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY2, recentCity1);
                cityHisList.clear();
                cityHisList.add(name);
                cityHisList.add(recentCity1);
                cityHisList.add(recentCity3);
            } else if (recentCity3.equalsIgnoreCase(name)) {
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY1, name);
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY2, recentCity1);
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY3, recentCity2);
                cityHisList.clear();
                cityHisList.add(name);
                cityHisList.add(recentCity1);
                cityHisList.add(recentCity2);
            } else {
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY1, name);
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY2, recentCity1);
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY3, recentCity2);
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY4, recentCity3);
                cityHisList.clear();
                cityHisList.add(name);
                cityHisList.add(recentCity1);
                cityHisList.add(recentCity2);
                cityHisList.add(recentCity3);
            }

        } else {
            String recentCity1 = SharedPreferencesUtil.getString(this, KEY_RECENT_CITY1);
            String recentCity2 = SharedPreferencesUtil.getString(this, KEY_RECENT_CITY2);
            String recentCity3 = SharedPreferencesUtil.getString(this, KEY_RECENT_CITY3);
            String recentCity4 = SharedPreferencesUtil.getString(this, KEY_RECENT_CITY4);
            if (recentCity1.equalsIgnoreCase(name)) {
                return;
            } else if (recentCity2.equalsIgnoreCase(name)) {
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY1, name);
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY2, recentCity1);
                cityHisList.clear();
                cityHisList.add(name);
                cityHisList.add(recentCity1);
                cityHisList.add(recentCity3);
                cityHisList.add(recentCity4);
            } else if (recentCity3.equalsIgnoreCase(name)) {
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY1, name);
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY2, recentCity1);
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY3, recentCity2);
                cityHisList.clear();
                cityHisList.add(name);
                cityHisList.add(recentCity1);
                cityHisList.add(recentCity2);
                cityHisList.add(recentCity4);
            } else if (recentCity4.equalsIgnoreCase(name)) {
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY1, name);
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY2, recentCity1);
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY3, recentCity2);
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY4, recentCity3);
                cityHisList.clear();
                cityHisList.add(name);
                cityHisList.add(recentCity1);
                cityHisList.add(recentCity2);
                cityHisList.add(recentCity3);
            } else {
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY1, name);
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY2, recentCity1);
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY3, recentCity2);
                SharedPreferencesUtil.putString(this, KEY_RECENT_CITY4, recentCity3);
                cityHisList.clear();
                cityHisList.add(name);
                cityHisList.add(recentCity1);
                cityHisList.add(recentCity2);
                cityHisList.add(recentCity3);
            }
        }
        cityListAdapter.notifyDataSetChanged();
    }


    /*****
     * 定位结果回调，重写onReceiveLocation方法
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            Log.i(TAG, "onReceiveLocation -------location : " + location);
            if (location.getLocType() == LocationConfig.ERROR_NO_GPS){
                Toast.makeText(MainActivity.this,
                        "定位失败,请打开GPS!", Toast.LENGTH_SHORT).show();
            }else {
                Log.i(TAG, "onReceiveLocation -------locateCity : " + locateCity);
                String city = location.getCity();
                if (null == locateCity){
                    refreshLocateCity(city);
                }
                if (!locateCity.equals(city)){
                    refreshLocateCity(city);
                }
            }
        }

    };

    /**
     * 刷新定位城市
     *
     * @param city
     */
    public void refreshLocateCity(String city) {
        final String s = city;
        locateCity = city;
        cityListAdapter.setLocateCity(locateCity);
        cityListAdapter.notifyDataSetChanged();
    }


    /**
     * a-z排序
     */
    @SuppressWarnings("rawtypes")
    Comparator comparator = new Comparator<City>() {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.getPinyi().substring(0, 1);
            String b = rhs.getPinyi().substring(0, 1);
            int flag = a.compareTo(b);
            if (flag == 0) {
                return a.compareTo(b);
            } else {
                return flag;
            }
        }
    };

    @Override
    public void onCityClick(String city) {
        Log.i(TAG, "onCityClick city : " + city);
        currentCity = city;
        insertCity(city);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick position : " + position
                + ", city : " + allCityList.get(position).getName());
        if (position >= 3) {
            currentCity = allCityList.get(position).getName();
            insertCity(currentCity);
        }
    }
}
