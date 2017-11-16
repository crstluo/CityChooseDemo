package com.channelst.citychoosedemo.citychoose;

import android.widget.GridView;

/**
 * 自定义GridView,主要用于重新计算view大小
 * 在热门城市和已访问城市中用到
 */
public class MyGridView extends GridView {
	public MyGridView(android.content.Context context,
                      android.util.AttributeSet attrs) {
		super(context, attrs);
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}