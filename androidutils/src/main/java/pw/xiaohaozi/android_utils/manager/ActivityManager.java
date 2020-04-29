package pw.xiaohaozi.android_utils.manager;

import android.app.Activity;
import java.util.ArrayList;
import java.util.LinkedList;



/**
 * ProjectName: My Application
 * Package: pw.xiaohaozi.android_utils.manager
 * ClassName: AppManager2
 * Description: 管理应用中的activity
 * Author: 小耗子
 * CreateDate: 2020/4/29 0029 14:09
 * Version: 1.0
 * 博客：https://www.jianshu.com/u/2a2ea7b43087
 * github：https://github.com/xiaohaozi9825
 */
public class ActivityManager {
    //该类添加和删除的使用频率会小于查找的频率，所以是用来额ArrayList
    private ArrayList<Activity> mActivities = new ArrayList<>();
    private static ActivityManager instance = new ActivityManager();

    private ActivityManager() {
    }

    public static ActivityManager getInstance() {
        return instance;
    }

    /**
     * 添加activity 到集合中，onCreate()方法调用
     *
     * @param activity
     */
    public void add(Activity activity) {
        mActivities.add(activity);
    }

    /**
     * 从集合中移除指定 Activity，onDestroy()方法中调用
     *
     * @param activity
     * @return
     */
    public boolean remove(Activity activity) {
        return mActivities.remove(activity);
    }

    /**
     * 从集合中移除指定 Activity，可以是多个
     *
     * @param classes
     * @return
     */
//    public boolean remove(Class<?>... classes) {
//        if (classes == null) return false;
//        if (classes.length == 0) return false;
//        LinkedList<Activity> removes = new LinkedList<>();
//        for (Activity activity : mActivities) {
//            for (Class<?> claA : classes) {
//                if (activity.getClass().equals(claA)) removes.add(activity);
//            }
//        }
//        if (removes.size() == 0) return false;
//        return mActivities.removeAll(removes);
//    }

    /**
     * finish掉指定Activity，并从集合中移除
     *
     * @param classes
     * @return
     */
    public boolean finish(Class<?>... classes) {
        if (classes == null) return false;
        if (classes.length == 0) return false;
        LinkedList<Activity> removes = new LinkedList<>();
        for (Activity activity : mActivities) {
            for (Class<?> claA : classes) {
                if (activity.getClass().equals(claA)) {
                    activity.finish();
                    removes.add(activity);
                }
            }
        }
        if (removes.size() == 0) return false;
        return mActivities.removeAll(removes);
    }

    /**
     * 移除所有activity
     *
     * @return
     */
    public void finishAll() {
        for (Activity activity : mActivities) {
            activity.finish();
        }
        mActivities.clear();
    }

    /**
     * 获取所有Activity
     *
     * @return
     */
    public ArrayList<Activity> get() {
        return mActivities;
    }

    /**
     * 获取最后一个Actitity，最近加入集合的Activity
     *
     * @return
     */
    public Activity getLast() {
        return mActivities.get(mActivities.size() - 1);
    }

    /**
     * 获取第一个Activity
     *
     * @return
     */
    public Activity getFirst() {
        return mActivities.get(0);
    }

    /**
     * 获取指定Acityity
     *
     * @param aClass
     * @return
     */
    public ArrayList<Activity> get(Class<?> aClass) {
        if (aClass == null) return null;
        ArrayList<Activity> activities = new ArrayList<>();
        for (Activity activity : mActivities) {
            if (activity.getClass().equals(aClass)) activities.add(activity);
        }
        return activities;
    }


    /**
     * 获取最后一个Actitity，最近加入集合的Activity
     *
     * @return
     */
    public Activity getLast(Class<?> aClass) {
        if (aClass == null) return null;
        for (int i = mActivities.size() - 1; i >= 0; i++) {
            Activity activity = mActivities.get(i);
            if (activity.getClass().equals(aClass)) return activity;
        }
        return null;
    }

    /**
     * 获取第一个Activity
     *
     * @return
     */
    public Activity getFirst(Class<?> aClass) {
        if (aClass == null) return null;
        for (int i = 0; i < mActivities.size(); i++) {
            Activity activity = mActivities.get(i);
            if (activity.getClass().equals(aClass)) return activity;
        }
        return null;
    }

}
