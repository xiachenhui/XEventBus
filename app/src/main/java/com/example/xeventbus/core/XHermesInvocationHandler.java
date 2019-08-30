package com.example.xeventbus.core;

import android.text.TextUtils;

import com.example.xeventbus.Response;
import com.example.xeventbus.manager.XHermes;
import com.example.xeventbus.response.ResponseBean;
import com.example.xeventbus.service.XHermesService;
import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class XHermesInvocationHandler implements InvocationHandler {
    private Class aClass;
    private Class xHermesService;
    private Gson gson = new Gson();

    public XHermesInvocationHandler(Class<? extends XHermesService> xHermesService, Class aClass) {
        this.aClass = aClass;
        this.xHermesService = xHermesService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //获取服务端返回的对象，反射调用里面的方法
        Response response = XHermes.getDefault().sendObjectRequest(xHermesService, aClass, method, args);
        if (response != null && !TextUtils.isEmpty(response.getData())) {
            //服务端的对象
            ResponseBean responseBean = gson.fromJson(response.getData(), ResponseBean.class);
            //对象里面的数据
            Object responseBeanData = responseBean.getData();
            //解析数据
            if (responseBeanData != null) {
                String data = gson.toJson(responseBeanData);
                //获取类的类型
                Class returnType = method.getReturnType();
                //转为对应的对象
                Object obj = gson.fromJson(data, returnType);
                return obj;

            }
        }
        return null;

    }
}
