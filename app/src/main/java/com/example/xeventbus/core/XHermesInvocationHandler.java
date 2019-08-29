package com.example.xeventbus.core;

import android.text.TextUtils;

import com.example.xeventbus.Response;
import com.example.xeventbus.manager.XHermes;
import com.example.xeventbus.response.ResponseBean;
import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class XHermesInvocationHandler implements InvocationHandler {
    private Class aClass;
    private Class xHermesService;
    private Gson gson = new Gson();

    public XHermesInvocationHandler(Class aClass, Class xHermesService) {
        this.aClass = aClass;
        this.xHermesService = xHermesService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Response response = XHermes.getDefault().sendObjectRequest(xHermesService, aClass, method, args);
        if (!TextUtils.isEmpty(response.getData())) {
            ResponseBean responseBean = gson.fromJson(response.getData(), ResponseBean.class);
            Object responseBeanData = responseBean.getData();
            if (responseBeanData != null) {
                String data = gson.toJson(responseBeanData);
                Class<?> returnType = method.getReturnType();
                Object obj = gson.fromJson(data, returnType);
                return obj;

            }
        }
        return null;

    }
}
