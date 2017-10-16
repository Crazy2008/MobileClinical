package com.winning.mobileclinical.utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/10/17.
 */

public class LogUtils {
    private static Boolean isLog=true;
    public static void showLog(String str){
        if(isLog){
            Log.d("tag",str);
        }
    }


}
