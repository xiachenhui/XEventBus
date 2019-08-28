package com.example.xeventbus.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;

import com.example.xeventbus.MyHermesService;
import com.example.xeventbus.service.HermesService;

import java.util.concurrent.ConcurrentHashMap;

public class ServiceConnectionManager {
    private static final ServiceConnectionManager ourInstance = new ServiceConnectionManager();

    private ServiceConnectionManager() {
    }

    public static ServiceConnectionManager getInstance() {
        return ourInstance;
    }

    //对应得binder 对象保存到hasmap中
    private final ConcurrentHashMap<Class<? extends HermesService>,
            MyHermesService> mHermesService = new ConcurrentHashMap<>();


    private final ConcurrentHashMap<Class<? extends HermesService>,
            HermesServiceConnection> mHermesServiceConnection = new ConcurrentHashMap<>();

    public void bind(Context context, String packageName, Class<HermesService> hermesServiceClass) {
        HermesServiceConnection hermesServiceConnection = new HermesServiceConnection(hermesServiceClass);
        mHermesServiceConnection.put(hermesServiceClass, hermesServiceConnection);
        Intent intent;

        if (TextUtils.isEmpty(packageName)) {
            intent = new Intent(context, hermesServiceClass);
        } else {
            intent = new Intent();
            intent.setClassName(packageName, hermesServiceClass.getName());
        }
        context.bindService(intent, hermesServiceConnection, Context.BIND_AUTO_CREATE);
    }


    private class HermesServiceConnection implements ServiceConnection {
        private Class<? extends HermesService> mClass;

        public HermesServiceConnection(Class<HermesService> hermesServiceClass) {
            mClass = hermesServiceClass;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyHermesService myHermesService = MyHermesService.Stub.asInterface(service);
            mHermesService.put(mClass, myHermesService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mHermesService.remove(mClass);
        }
    }
}
