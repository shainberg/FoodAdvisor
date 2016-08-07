package com.foodadvisor;

import android.app.Application;
import android.content.Context;

/**
 * Created by Ori on 07/08/2016.
 */

public class MyApplication extends Application{
    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
