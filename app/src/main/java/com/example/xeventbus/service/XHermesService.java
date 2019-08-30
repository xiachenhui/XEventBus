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

/**
 * author : xia chen hui
 * email : 184415359@qq.com
 * date : 2019/8/30/030 7:27
 * desc : 服务端Service
 **/
public class XHermesService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private MyHermesService.Stub mBinder = new MyHermesService.Stub() {
        @Override
        public Response send(Request request) throws RemoteException {
            //拿到客户端发送的数据
            ResponseMake responseMake = null;
            switch (request.getType()) {
                case XHermes.TYPE_GET:
                    //获取单列
                    responseMake = new InstanceResponseMake();
                    break;
                case XHermes.TYPE_NEW:
                    //获取对象
                    responseMake = new ObjectResponseMake();
                    break;
            }
            //生成Response对象
            return responseMake.makeResponse(request);
        }
    };
}
