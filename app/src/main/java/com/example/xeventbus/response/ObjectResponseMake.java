package com.example.xeventbus.response;

import com.example.xeventbus.bean.RequestBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/5/23.
 */

public class ObjectResponseMake extends ResponseMake {
    private Method mMethod;

    private Object mObject;

    @Override
    protected Object invokeMethod() {

        Exception exception;
        try {
            return mMethod.invoke(mObject,mParameters);
        } catch (IllegalAccessException e) {
            exception = e;
        } catch (InvocationTargetException e) {
            exception = e;
        }
        return null;
    }
//  1
    @Override
    protected void setMethod(RequestBean requestBean) {
        mObject = OBJECT_CENTER.getObject(reslutClass.getName());
//        getUser()    ---->method
        Method method = typeCenter.getMethod(mObject.getClass(), requestBean);
        mMethod = method;
    }
}
