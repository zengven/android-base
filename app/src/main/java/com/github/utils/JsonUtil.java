/**
 *
 */

package com.github.utils;


import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.zip.GZIPInputStream;

/**
 * author: zengven
 * date: 2017/7/3
 * Desc: json解析
 */
public class JsonUtil {

    private static final Gson sGson = new Gson();

    public static <T> T parse(String json, Class<T> classOfT) {
        try {
            return sGson.fromJson(json, classOfT);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static <T> T parse(String json, Type type) {
        try {
            StringReader reader = new StringReader(json);
            return parse(reader, type);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static <T> T parse(Reader reader, Type type) {
        return sGson.fromJson(reader, type);
    }

    public static <T> T parse(InputStream is, Type type) {
        return parse(is, false, type);
    }

    public static <T> T parse(InputStream is, boolean isGzip, Type type) {
        if (is != null) {
            Reader reader = null;
            if (isGzip) {
                try {
                    is = new GZIPInputStream(is);
                    reader = new InputStreamReader(is);
                } catch (Exception e) {
                }
            } else {
                reader = new InputStreamReader(is);
            }
            return parse(reader, type);
        }
        return null;
    }

    public static String toJson(Object obj, Type type) {
        return sGson.toJson(obj, type);
    }

    public static String toJson(Object obj) {
        return sGson.toJson(obj);
    }
}
