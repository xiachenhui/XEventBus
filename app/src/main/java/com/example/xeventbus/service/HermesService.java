package com.example.xeventbus.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.xeventbus.MyHermesService;
import com.example.xeventbus.Request;
import com.example.xeventbus.Response;
import com.example.xeventbus.manager.XHermes;
import com.example.xeventbus.response.InstanceResponseMake;
import com.example.xeventbus.response.ObjectResponseMake;
import com.example.xeventbus.response.ResponseMake;

public class HermesService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private MyHermesService.Stub mBinder = new MyHermesService.Stub() {
        @Override
        public Response send(Request request) throws RemoteException {
            ResponseMake responceMake = null;
            switch (request.getType()) {
                case XHermes.TYPE_GET:  //获取单列
                    responceMake = new InstanceResponseMake();
                    break;
                case XHermes.TYPE_NEW:
                    responceMake = new ObjectResponseMake();
                    break;
            }
            return responceMake.makeResponce(request);
        }
    };
}
