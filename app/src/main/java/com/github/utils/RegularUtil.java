package com.github.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * author: zengven
 * date: 2017/8/2 18:03
 * desc: 常用正则匹配工具类
 */
public class RegularUtil {

    private RegularUtil() {
        throw new UnsupportedOperationException("shit .....");
    }

    /**
     * 验证手机号（简单）
     */
    private static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";

    /**
     * 验证手机号（精确）
     * <p>
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188
     * <p>联通：130、131、132、145、155、156、175、176、185、186
     * <p>电信：133、153、173、177、180、181、189
     * <p>全球星：1349
     * <p>虚拟运营商：170
     */
    private static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-8])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$";

    /**
     * 验证座机号,正确格式：xxx/xxxx-xxxxxxx/xxxxxxxx/
     */
    private static final String REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}";

    /**
     * 验证邮箱
     */
    private static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    /**
     * 验证url
     */
    private static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?";

    /**
     * 验证汉字
     */
    private static final String REGEX_CHZ = "^[\\u4e00-\\u9fa5]+$";

    /**
     * 验证用户名,取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
     */
    private static final String REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";

    /**
     * 验证IP地址
     */
    private static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";

    /**
     * 验证少数民族姓名(最少两位{2,})
     */
    private static final String REGEX_NATION_MINORITY = "^[\\u4e00-\\u9fa5]+[·•][\\u4e00-\\u9fa5]{2,}+$";

    /**
     * 验证民族姓名(最少两位{2,})
     */
    private static final String REGEX_NATION = "^[\\u4e00-\\u9fa5]{2,}+$";

    /**
     * 验证身份证号码,15位纯数字,18位纯数字或者17位纯数字+X (注:身份证为X,输入支持X.x,上传需转换成X)
     */
    private static final String REGEX_CARD_ID = "(^\\d{15}$)|(^\\d{17}([0-9]|[xX])$)";

    /**
     * 验证6-10位纯数字
     */
    public static final String REGEX_PURE_DIGITAL = "^\\d{6,10}$"; // == ^[0-9]{6,10}$


    /**
     * @param string 待验证文本
     * @return 是否符合手机号（简单）格式
     */
    public static boolean isMobileSimple(String string) {
        return isMatch(REGEX_MOBILE_SIMPLE, string);
    }

    /**
     * @param string 待验证文本
     * @return 是否符合手机号（精确）格式
     */
    public static boolean isMobileExact(String string) {
        return isMatch(REGEX_MOBILE_EXACT, string);
    }

    /**
     * @param string 待验证文本
     * @return 是否符合座机号码格式
     */
    public static boolean isTel(String string) {
        return isMatch(REGEX_TEL, string);
    }

    /**
     * @param string 待验证文本
     * @return 是否符合邮箱格式
     */
    public static boolean isEmail(String string) {
        return isMatch(REGEX_EMAIL, string);
    }

    /**
     * @param string 待验证文本
     * @return 是否符合网址格式
     */
    public static boolean isURL(String string) {
        return isMatch(REGEX_URL, string);
    }

    /**
     * @param string 待验证文本
     * @return 是否符合汉字
     */
    public static boolean isChz(String string) {
        return isMatch(REGEX_CHZ, string);
    }

    /**
     * @param string 待验证文本
     * @return 是否符合用户名
     */
    public static boolean isUsername(String string) {
        return isMatch(REGEX_USERNAME, string);
    }

    /**
     * @param name 待验证名字
     * @return 是否符合
     */
    public static boolean isLegalName(String name) {
        if (name.contains("·") || name.contains("•")) {
            return isMatch(REGEX_NATION_MINORITY, name);
        } else {
            return isMatch(REGEX_NATION, name);
        }
    }

    /**
     * @param cardId 待验证的身份证号码
     * @return 身份证是否合法
     */
    public static boolean isLegalCardID(String cardId) {
        return isMatch(REGEX_CARD_ID, cardId);
    }

    /**
     * @param regex  正则表达式字符串
     * @param string 要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    public static boolean isMatch(String regex, String string) {
        return !TextUtils.isEmpty(string) && Pattern.matches(regex, string);
    }
}