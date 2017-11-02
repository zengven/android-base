package com.github.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * author: zengven
 * date: 2017/7/20 17:17
 * desc: 日期相关工具类
 */
public class DateUtil {

    public static class Pattern {
        public static final String FULL = "yyyy-MM-dd HH:mm:ss";
        public static final String DATE = "yyyy-MM-dd";
        public static final String HOUR_NO_SPLIT = "HHmm";
        public static final String HOUR = "HH:mm";
        public static final String DATE_NO_YEAR = "MM.dd";
        public static final String MOUTH_HOUR = "MM-dd HH:mm";
        public static final String DATE_MM_DD_LINE = "MM-dd";
        public static final String YEAR = "yyyy";
        public static final String TIME_NO_SECONDS = "yyyy-MM-dd HH:mm";
    }

    /**
     * 格式化当前时间戳
     *
     * @param pattern {@link Pattern}
     * @return
     */
    public static String timeFormat(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date());
    }

    /**
     * 格式化指定时间戳
     *
     * @param timeMillis ms
     * @param pattern {@link Pattern}
     * @return
     */
    public static String timeFormat(long timeMillis, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date(timeMillis));
    }

    /**
     * 时间戳转换
     *
     * @param seconds s
     * @param format {@link Pattern}
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty())
            format = Pattern.FULL;
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * 时间戳转换
     *
     * @param seconds s
     * @param format  {@link Pattern}
     * @return
     */
    public static String timeStamp2Date(long seconds, String format) {
        if (format == null || format.isEmpty())
            format = Pattern.FULL;
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        return sdf.format(new Date(seconds * 1000));
    }

    /**
     * @return 返回当前系统时间, 单位 ms
     */
    public static long getCurrentTimeMillis() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    /**
     * @return 返回当前系统时间, 单位 s
     */
    public static long getCurrentTimeSecond() {
        return getCurrentTimeMillis() / 1000;
    }

    /**
     * 根据ms获取year
     *
     * @param millis 传-1,获取当前hour
     * @return year
     */
    public static long getYearByMillis(long millis) {
        Calendar calendar = Calendar.getInstance();
        if (-1 != millis)
            calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 根据s获取 year
     *
     * @param seconds s
     * @return year
     */
    public static long getYearBySeconds(long seconds) {
        return getYearByMillis(seconds * 1000);
    }

    /**
     * 根据ms获取day
     *
     * @param millis 传-1,获取当前day
     * @return day
     */
    public static long getDayByMillis(long millis) {
        Calendar calendar = Calendar.getInstance();
        if (-1 != millis)
            calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 根据s获取day
     *
     * @param seconds s
     * @return day
     */
    public static long getDayBySeconds(long seconds) {
        return getDayByMillis(seconds * 1000);
    }

    /**
     * 根据ms获取hour
     *
     * @param millis 传-1,获取当前hour
     * @return hour
     */
    public static long getHourByMillis(long millis) {
        Calendar calendar = Calendar.getInstance();
        if (-1 != millis)
            calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 根据s获取 hour
     *
     * @param seconds s
     * @return hour
     */
    public static long getHourBySeconds(long seconds) {
        return getHourByMillis(seconds * 1000);
    }

    /**
     * 获取文件最后修改时间 ms
     *
     * @param path 文件绝对路径
     * @return ms , -1当前文件不存在
     */
    public static long getFileLastModifiedTime(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.lastModified(); //ms
        }
        return -1;
    }
}
