package com.channelst.citychoosedemo.citychoose;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.channelst.citychoosedemo.R;

import java.util.List;

/**
 * Created by luojingjing on 2017/5/11.
 * 已访问城市适配器
 */

public class CityHisListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> hisCitys;

    public CityHisListAdapter(Context context, List<String> hisCitys) {
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        this.hisCitys = hisCitys;
    }

    @Override
    public int getCount() {
        return hisCitys.size();
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
        city.setText(hisCitys.get(position));
        return convertView;
    }
}
