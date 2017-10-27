package com.github.manager;

import android.app.Activity;
import android.os.Process;
import android.text.TextUtils;

import java.util.Iterator;
import java.util.Stack;

/**
 * author: zengven
 * date: 2017/9/27 10:03
 * desc: activity 管理类
 */

public class ActivityManager {

    private static ActivityManager sInstance;
    private static Stack<Activity> sActivities = new Stack<>(); //管理activity栈,先进先出

    public static synchronized ActivityManager getInstance() {
        if (null == sInstance) {
            sInstance = new ActivityManager();
        }
        return sInstance;
    }

    public Stack<Activity> getActivities() {
        return sActivities;
    }

    /**
     * 向stack中添加activity
     *
     * @param activity
     * @return
     */
    public boolean add(Activity activity) {
        return sActivities.add(activity);
    }

    /**
     * 从stack移除指定activity ,内部调用removeElement
     *
     * @param activity
     * @return
     */
    public boolean remove(Activity activity) {
        return sActivities.remove(activity);
    }

    /**
     * Adds the specified activity at the end of this stack.
     *
     * @param activity
     */
    public void addElement(Activity activity) {
        sActivities.addElement(activity);
    }

    /**
     * remove activity from activity stack
     *
     * @param activity
     * @return {@code true} if the specified object was found, {@code false}
     * otherwise.
     */
    public boolean removeElement(Activity activity) {
        return sActivities.removeElement(activity);
    }

    /**
     * get current activity
     *
     * @return
     */
    public Activity currentActivity() {
        return sActivities.lastElement();
    }

    /**
     * 清空activity stack
     */
    public void clear() {
        sActivities.clear();
    }

    /**
     * finish all activity
     */
    public void finishAll() {
        if (!sActivities.isEmpty()) {
            for (Activity activity : sActivities) {
                if (activity != null) {
                    activity.finish();
                }
            }
            clear();
        }
    }

    /**
     * finish other activity,retain the current activity
     */
    public void finishOther() {
        if (sActivities.isEmpty())
            return;
        Iterator<Activity> iterator = sActivities.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (!TextUtils.equals(activity.getClass().getName(), currentActivity().getClass().getName())) {
                activity.finish();
                iterator.remove();
            }
        }
    }

    /**
     * finish current activity
     */
    public void finishCurrent() {
        Activity activity = currentActivity();
        activity.finish();
        sActivities.pop();
    }

    /**
     * exit app
     */
    public void appExit() {
        finishAll();
//        System.exit(0); //一般不调用,加速下次启动速度
        Process.killProcess(Process.myPid()); ////一般不调用,加速下次启动速度
    }
}
