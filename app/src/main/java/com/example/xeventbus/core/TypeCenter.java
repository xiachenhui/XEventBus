package com.example.xeventbus.core;

import android.text.TextUtils;

import com.example.xeventbus.bean.RequestBean;
import com.example.xeventbus.bean.RequestParameter;
import com.example.xeventbus.manager.UserManager;
import com.example.xeventbus.utils.TypeUtils;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author : xia chen hui
 * email : 184415359@qq.com
 * date : 2019/8/28/028 8:00
 * desc : XHermes的TypeCenter
 **/
public class TypeCenter {
    private static TypeCenter instance;
    //为了减少反射，所以保存下来。保存方法
    private final ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, Method>> mRawMethod;
    //保存类
    private final ConcurrentHashMap<String, Class<?>> mRawClass;


    private TypeCenter() {
        mRawMethod = new ConcurrentHashMap<>();
        mRawClass = new ConcurrentHashMap<>();
    }

    public synchronized static TypeCenter getInstance() {
        if (instance == null) {
            synchronized (TypeCenter.class) {
                if (instance == null) {
                    instance = new TypeCenter();
                }
            }
        }
        return instance;
    }

    public void register(Class<UserManager> managerClass) {
        //注册类
        registerClass(managerClass);
        //注册方法
        registerMethod(managerClass);
    }

    //缓存class
    private void registerClass(Class<UserManager> managerClass) {
        String name = managerClass.getName();
        //putIfAbsent 如果是空就不会添加
        mRawClass.putIfAbsent(name, managerClass);

    }

    //缓存method
    private void registerMethod(Class<UserManager> managerClass) {
        Method[] methods = managerClass.getMethods();

        for (Method method : methods) {
            ConcurrentHashMap<String, Method> map = new ConcurrentHashMap<>();
            String methodId = TypeUtils.getMethodId(method);
            map.putIfAbsent(methodId, method);
            mRawMethod.putIfAbsent(managerClass, map);
        }
    }
    public Class<?> getClassType(String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        Class<?> clazz = mRawClass.get(name);
        if (clazz == null) {
            try {
                clazz = Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return clazz;
    }
    public Method getMethod(Class<?> aClass, RequestBean requestBean) {
        String methodName = requestBean.getMethodName();//setFriend()
        if (methodName != null) {
            mRawMethod.putIfAbsent(aClass, new ConcurrentHashMap<String, Method>());
            ConcurrentHashMap<String, Method> methods = mRawMethod.get(aClass);
            Method method = methods.get(methodName);
            if(method != null){
                return method;
            }
            int pos = methodName.indexOf('(');

            Class[] paramters = null;
            RequestParameter[] requestParameters = requestBean.getRequestParameter();
            if (requestParameters != null && requestParameters.length > 0) {
                paramters = new Class[requestParameters.length];
                for (int i=0;i<requestParameters.length;i++) {
                    paramters[i]=getClassType(requestParameters[i].getParameterClassName());
                }
            }
            method = TypeUtils.getMethod(aClass, methodName.substring(0, pos), paramters);
            methods.put(methodName, method);
            return method;
        }
        return null;
    }
}
