package com.example.xeventbus.core;

import java.util.concurrent.ConcurrentHashMap;

/**
 * author : xia chen hui
 * email : 184415359@qq.com
 * date : 2019/8/30/030 7:00
 * desc : 保存类的信息
 **/
public class ObjectCenter {

    private static final String TAG = "ObjectCenter";

    private static volatile ObjectCenter sInstance = null;

    private final ConcurrentHashMap<String, Object> mObjects;

    private ObjectCenter() {
        mObjects = new ConcurrentHashMap<>();
    }

    public static ObjectCenter getInstance() {
        if (sInstance == null) {
            synchronized (ObjectCenter.class) {
                if (sInstance == null) {
                    sInstance = new ObjectCenter();
                }
            }
        }
        return sInstance;
    }

    public Object getObject(String name) {
        return mObjects.get(name);
    }

    public void putObject(String name, Object object) {
        mObjects.put(name, object);
    }

}

