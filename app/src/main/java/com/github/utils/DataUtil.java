package com.github.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * author: zengven
 * date: 2017/6/12
 * Desc: process data util
 */
public class DataUtil {

    /**
     * keep one decimal
     *
     * @param vlaue
     * @return
     */
    public static double keep1decimal(double vlaue) {
        return Double.parseDouble(new DecimalFormat("0.0").format(vlaue));
    }

    /**
     * keep two decimal
     *
     * @param vlaue
     * @return
     */
    public static double keep2decimal(double vlaue) {
        return Double.parseDouble(new DecimalFormat("0.00").format(vlaue));
    }

    /**
     * keep decimal by digits
     *
     * @param number
     * @param digits 保留的小数位数
     * @return
     */
    public static double keepDecimal(double number, int digits) {
        BigDecimal decimal = new BigDecimal(number);
        return decimal.setScale(digits, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * keep decimal by digits
     *
     * @param number
     * @param digits 保留的小数位数
     * @return
     */
    public static float keepDecimal(float number, int digits) {
        BigDecimal decimal = new BigDecimal(number);
        return decimal.setScale(digits, RoundingMode.HALF_UP).floatValue();
    }

}
