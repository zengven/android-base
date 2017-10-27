package com.github.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * author: zengven
 * date: 2017/7/20 17:17
 * desc: 日期相关工具类
 */
public class DateUtil {

    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String HOUR_PATTERN_NO_SPLIT = "HHmm";
    public static final String HOUR_PATTERN = "HH:mm";
    public static final String DATE_PATTERN_NO_YEAR = "MM.dd";
    public static final String MOUTH_HOUR_PATTERN = "MM-dd HH:mm";
    public static final String DATE_PATTERN_MM_DD_LINE = "MM-dd";
    public static final String YEAR_DATE = "yyyy";
    public static final String TIME_PATTERN_NO_SECONDS = "yyyy-MM-dd HH:mm";

    /**
     * 格式化当前时间戳
     *
     * @param pattern
     * @return
     */
    public static String timeFormat(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date());
    }

    /**
     * 格式化指定时间戳
     *
     * @param timeMillis
     * @param pattern
     * @return
     */
    public static String timeFormat(long timeMillis, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date(timeMillis));
    }

}
