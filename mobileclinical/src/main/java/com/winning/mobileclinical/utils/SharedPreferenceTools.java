package com.winning.mobileclinical.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by xx on 2016/5/16.
 */
public class SharedPreferenceTools {
    private static final String SP_NAME = "mobile_doctouch";
    private static SharedPreferences mSp;

    //保存布尔值
    public static void saveBoolean(Context context, String key, boolean value) {
        if (mSp == null) {
            mSp = context.getSharedPreferences(SP_NAME, Context
                    .MODE_PRIVATE);
        }
        mSp.edit().putBoolean(key, value).commit();
    }
    //获取布尔值
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        if (mSp == null) {
            mSp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        boolean result = mSp.getBoolean(key, defValue);
        return result;
    }


    //保存字符串
    public static void saveString(Context context, String key, String value) {
        if (mSp == null) {
            mSp = context.getSharedPreferences(SP_NAME, Context
                    .MODE_PRIVATE);
        }
        mSp.edit().putString(key, value).commit();

    }
    //获取字符串
    public static String getString(Context context, String key, String defValue) {
        if (mSp == null) {
            mSp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        String result = mSp.getString(key, defValue);
        return result;
    }

    /*清除sp某个内容*/
    public static void removeString(Context context, String key) {
        if (mSp == null) {
            mSp = context.getSharedPreferences(SP_NAME, Context
                    .MODE_PRIVATE);
        }
        mSp.edit().remove(key).commit();

    }


    //保存字符串
    public static void saveInt(Context context, String key, int value) {
        if (mSp == null) {
            mSp = context.getSharedPreferences(SP_NAME, Context
                    .MODE_PRIVATE);
        }
        mSp.edit().putInt(key, value).commit();

    }
    //获取字符串
    public static int getInt(Context context, String key, int defValue) {
        if (mSp == null) {
            mSp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        int result = mSp.getInt(key, defValue);
        return result;
    }




}
