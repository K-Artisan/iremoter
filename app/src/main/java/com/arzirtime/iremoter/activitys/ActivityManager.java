package com.arzirtime.iremoter.activitys;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityManager {
    private static List<Activity> activities = new ArrayList<>();

    public static void  addActivity(Activity activity){
        activities.add(activity);
    }

    public static void  removeActivity(Activity activity){
        activities.remove(activity);
    }

    /**
     * 销毁所以活动
     * */
    public static  void finishAllActivity(){
        for (Activity activity: activities) {
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
    }
}
