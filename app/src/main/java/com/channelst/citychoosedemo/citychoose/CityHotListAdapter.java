package com.channelst.citychoosedemo.citychoose;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.channelst.citychoosedemo.R;
import com.channelst.citychoosedemo.database.City;

import java.util.List;

/**
 * Created by luojingjing on 2017/5/11.
 * 热门城市适配器
 */

public class CityHotListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<City> hotCitys;

    public CityHotListAdapter(Context context, List<City> hotCitys) {
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        this.hotCitys = hotCitys;
    }

    @Override
    public int getCount() {
        return hotCitys.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.city_grid_item, null);
        TextView city = (TextView) convertView.findViewById(R.id.city_grid_item_text);
        city.setText(hotCitys.get(position).getName());
        return convertView;
    }
}
