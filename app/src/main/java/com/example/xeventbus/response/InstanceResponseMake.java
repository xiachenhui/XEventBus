package com.example.xeventbus.response;

import android.util.Log;

import com.example.xeventbus.bean.RequestBean;
import com.example.xeventbus.bean.RequestParameter;
import com.example.xeventbus.utils.TypeUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * author : xia chen hui
 * email : 184415359@qq.com
 * date : 2019/8/29/029 20:45
 * desc : 创建Response
 **/
public class InstanceResponseMake extends ResponseMake {
    private Method mMethod;
    @Override
    protected Object invokeMethod() {

        Object object = null;
        try {
            object = mMethod.invoke(null, mParameters);
            Log.i("XCH", "invokeMethod: " + object.toString());
//
            //            保存对象
            OBJECT_CENTER.putObject(object.getClass().getName(), object);
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        return null;
    }


    @Override
    protected void setMethod(RequestBean requestBean) {
        //解析参数 去找 getInstance()      ----UserManager
        RequestParameter[] requestParameters = requestBean.getRequestParameter();
        /**
         * {
         * "parameterClassName":"java.lang.String"
         * parameterValue:"lisi"
         * }
         */
        Class<?>[] parameterTypes = null;
        if (requestParameters != null && requestParameters.length > 0) {
            parameterTypes = new Class<?>[requestParameters.length];
            for (int i = 0; i < requestParameters.length; ++i) {
                parameterTypes[i] = typeCenter.getClassType(requestParameters[i].getParameterClassName());
            }
        }
        String methodName = requestBean.getMethodName(); //可能出现重载
        Method method = TypeUtils.getMethodForGettingInstance(reslutClass, methodName, parameterTypes);
        mMethod = method;
    }
}
