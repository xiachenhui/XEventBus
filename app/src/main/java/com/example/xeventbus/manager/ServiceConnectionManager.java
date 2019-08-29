package com.example.xeventbus.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.example.xeventbus.MyHermesService;
import com.example.xeventbus.Request;
import com.example.xeventbus.Response;
import com.example.xeventbus.service.HermesService;

import java.util.concurrent.ConcurrentHashMap;

/**
 * author : xia chen hui
 * email : 184415359@qq.com
 * date : 2019/8/29/029 7:01
 * desc : 连接的管理类
 **/
public class ServiceConnectionManager {
    private static final ServiceConnectionManager ourInstance = new ServiceConnectionManager();

    //对应得binder 对象保存到hashMap中
    private final ConcurrentHashMap<Class<? extends HermesService>,
            MyHermesService> mHermesService = new ConcurrentHashMap<>();
    // 缓存HermesServiceConnection
    private final ConcurrentHashMap<Class<? extends HermesService>,
            HermesServiceConnection> mHermesServiceConnection = new ConcurrentHashMap<>();

    private ServiceConnectionManager() {
    }

    public static ServiceConnectionManager getInstance() {
        return ourInstance;
    }


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

    public Response request(Class<HermesService> hermesServiceClass, Request request) {
        MyHermesService myEventBusService = mHermesService.get(hermesServiceClass);
        if(myEventBusService != null){
            try {
                Response response = myEventBusService.send(request);
                return response;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
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
