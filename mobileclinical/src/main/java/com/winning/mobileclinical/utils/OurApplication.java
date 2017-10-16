package com.winning.mobileclinical.utils;

import android.app.Application;
import android.content.Context;

public class OurApplication extends Application {


    /**
     * 全局的上下文.
     */
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler handler = CrashHandler.getInstance();
        handler.init(getApplicationContext()); //在Appliction里面设置我们的异常处理器为UncaughtExceptionHandler处理器
        mContext = getApplicationContext();
    }



    /**获取Context.
     * @return
     */
    public static Context getContext(){
        return mContext;
    }




}

