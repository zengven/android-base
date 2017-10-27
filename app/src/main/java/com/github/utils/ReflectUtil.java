package com.github.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * author: zengven
 * date: 2017/9/4 17:02
 * desc: 反射工具类
 */
public class ReflectUtil {

    /**
     * 反射执行某对象方法
     *
     * @param target
     * @param methodName
     * @param args
     */
    public static void invokeMethod(Object target, String methodName, Object... args) {
        Class<?> aClass = target.getClass();
        Class[] argsClass = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argsClass[i] = args[i].getClass();
        }
        try {
            Method method = aClass.getMethod(methodName, argsClass);
            method.invoke(target, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
