package com.channelst.citychoosedemo.citychoose;

import java.util.regex.Pattern;

/**
 * Created by Luojingjing on 2017/11/15.
 *
 * 通过汉语拼音获取因为首字母
 */

public class PinyinUtil {
    /**
     * 获得汉语拼音首字母
     */
    public static String getAlpha(String str) {
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else if (str.equals("0")) {
            return "定位";
        } else if (str.equals("1")) {
            return "最近";
        } else if (str.equals("2")) {
            return "热门";
        } else {
            return "#";
        }
    }
}
