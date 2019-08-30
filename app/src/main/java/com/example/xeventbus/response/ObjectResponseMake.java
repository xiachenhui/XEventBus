package com.example.xeventbus.response;

import com.example.xeventbus.bean.RequestBean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * author : xia chen hui
 * email : 184415359@qq.com
 * date : 2019/8/30/030 7:01
 * desc : 创建对象Response
 **/
public class ObjectResponseMake extends ResponseMake {
    private Method mMethod;

    private Object mObject;

    @Override
    protected Object invokeMethod() {

        Exception exception;
        try {
            return mMethod.invoke(mObject, mParameters);
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
        mObject = OBJECT_CENTER.getObject(resultClass.getName());
        //  getUser()    ---->method 获取方法
        Method method = typeCenter.getMethod(mObject.getClass(), requestBean);
        mMethod = method;
    }
}
