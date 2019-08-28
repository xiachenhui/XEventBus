package com.example.xeventbus;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
    private int i = 0;
    private static final String TAG= "XCH";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "i=" + i);
    }
}
