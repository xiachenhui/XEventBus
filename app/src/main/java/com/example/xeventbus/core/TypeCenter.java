package com.example.xeventbus.core;

import android.text.TextUtils;
import android.util.Log;

import com.example.xeventbus.bean.RequestBean;
import com.example.xeventbus.bean.RequestParameter;
import com.example.xeventbus.utils.TypeUtils;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author : xia chen hui
 * email : 184415359@qq.com
 * date : 2019/8/28/028 8:00
 * desc : XHermes的TypeCenter，缓存方法和类
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

    public void register(Class<?> managerClass) {
        //注册类
        registerClass(managerClass);
        //注册方法
        registerMethod(managerClass);
    }

    //缓存class
    private void registerClass(Class<?> managerClass) {
        String name = managerClass.getName();
        //putIfAbsent 如果是空就不会添加
        mRawClass.putIfAbsent(name, managerClass);

    }

    //缓存method
    private void registerMethod(Class<?> managerClass) {
        Method[] methods = managerClass.getMethods();

        for (Method method : methods) {
            //存储类的方法，先存储类，然后创建map，再通过类获取map
            mRawMethod.putIfAbsent(managerClass, new ConcurrentHashMap<String, Method>());
            // map都不会为空
            ConcurrentHashMap<String, Method> map = mRawMethod.get(managerClass);
            String key = TypeUtils.getMethodId(method);
            //把方法加到类中
            map.put(key, method);
        }
    }

    /**
     * 获取类的类型
     *
     * @param name
     * @return
     */
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

    /**
     * 获取客户端发送的对象的中的方法
     *
     * @param aClass
     * @param requestBean
     * @return
     */
    public Method getMethod(Class<?> aClass, RequestBean requestBean) {
        String methodName = requestBean.getMethodName();//setFriend()
        if (methodName != null) {
            Log.i("XCH", "getMethod: 1=======" + methodName);
            //先保存方法
            mRawMethod.putIfAbsent(aClass, new ConcurrentHashMap<String, Method>());
            ConcurrentHashMap<String, Method> methods = mRawMethod.get(aClass);
            Method method = methods.get(methodName);
            //缓存中有方法，直接返回
            if (method != null) {
                Log.i("XCH", "getMethod: " + method.getName());
                return method;
            }

            //处理方法中的参数
            int pos = methodName.indexOf('(');
            Class[] parameters = null;
            RequestParameter[] requestParameters = requestBean.getRequestParameter();
            if (requestParameters != null && requestParameters.length > 0) {
                parameters = new Class[requestParameters.length];
                for (int i = 0; i < requestParameters.length; i++) {
                    parameters[i] = getClassType(requestParameters[i].getParameterClassName());
                }
            }
            method = TypeUtils.getMethod(aClass, methodName.substring(0, pos), parameters);
            methods.put(methodName, method);
            return method;
        }
        return null;
    }
}
