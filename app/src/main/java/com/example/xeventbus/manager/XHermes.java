package com.example.xeventbus.manager;

import android.content.Context;

import com.example.xeventbus.Request;
import com.example.xeventbus.Response;
import com.example.xeventbus.annotion.ClassId;
import com.example.xeventbus.bean.RequestBean;
import com.example.xeventbus.bean.RequestParameter;
import com.example.xeventbus.core.TypeCenter;
import com.example.xeventbus.core.XHermesInvocationHandler;
import com.example.xeventbus.service.HermesService;
import com.example.xeventbus.utils.TypeUtils;
import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class XHermes {

    private static XHermes hermes;
    private  ServiceConnectionManager serviceConnectionManager;
    private Context mContext;

    private Gson gson = new Gson();

    //得到对象
    public static final int TYPE_NEW = 0;
    //得到单例
    public static final int TYPE_GET = 1;
    private TypeCenter typeCenter;

    public XHermes() {
        typeCenter = TypeCenter.getInstance();
        serviceConnectionManager = ServiceConnectionManager.getInstance();
    }

    public synchronized static XHermes getDefault() {
        if (hermes == null) {
            synchronized (XHermes.class) {
                if (hermes == null) {
                    hermes = new XHermes();
                }
            }
        }
        return hermes;
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void register(Class<UserManager> managerClass) {
        typeCenter.register(managerClass);
    }


    /*-----------------客户端使用的代码------------------*/
    public void connect(Context context, Class<HermesService> hermesServiceClass) {
        connectApp(context, null, hermesServiceClass);

    }

    public <T> T getInstance(Class<T> tClass, Object... parameters) {
        Response response = sendRequest(HermesService.class, tClass, null, parameters);
        return getProxy(HermesService.class, tClass);
    }

    private <T> T getProxy(Class<HermesService> hermesServiceClass, Class<T> tClass) {
        ClassLoader classLoader = hermesServiceClass.getClassLoader();
        T proxy = (T) Proxy.newProxyInstance(classLoader, new Class<?>[]{tClass}, new XHermesInvocationHandler(hermesServiceClass, tClass));
        return proxy;
    }

    private <T> Response sendRequest(Class<HermesService> hermesServiceClass, Class<T> tClass, Method method, Object[] parameters) {
        RequestBean requestBean = new RequestBean();
        //set全类名
        String className = null;
        if (tClass.getAnnotation(ClassId.class) == null) {
            requestBean.setClassName(tClass.getName());
            requestBean.setResultClassName(tClass.getName());
        } else {
            requestBean.setClassName(tClass.getAnnotation(ClassId.class).value());
            requestBean.setResultClassName(tClass.getAnnotation(ClassId.class).value());
        }


        //set方法
        if (method != null) {
            requestBean.setMethodName(TypeUtils.getMethodId(method));
        }

        //set参数
        RequestParameter[] requestParameters = null;
        if (parameters != null && parameters.length > 0) {
            requestParameters = new RequestParameter[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                String parameterClassName = parameter.getClass().getName();
                String parameterValue = gson.toJson(parameter);
                RequestParameter requestParameter = new RequestParameter(parameterClassName, parameterValue);
                requestParameters[i] = requestParameter;
            }
        }
        if (requestParameters != null) {
            requestBean.setRequestParameter(requestParameters);
        }

        Request request = new Request(gson.toJson(requestBean), TYPE_GET);

        return serviceConnectionManager.request(hermesServiceClass, request);

    }

    private void connectApp(Context context, String packageName, Class<HermesService> hermesServiceClass) {
        //这里再次初始化是因为在客户端使用的时候，mContext拿不到
        init(context);
        serviceConnectionManager.bind(context.getApplicationContext(), packageName, hermesServiceClass);

    }

    public <T> Response sendObjectRequest(Class xHermesService, Class<T> aClass, Method method, Object[] args) {
        RequestBean requestBean = new RequestBean();

        //set全类名
        String className = null;
        ClassId annotation = aClass.getAnnotation(ClassId.class);
        if (annotation == null) {
            requestBean.setClassName(aClass.getName());
            requestBean.setResultClassName(aClass.getName());
        } else {
            requestBean.setClassName(annotation.value());
            requestBean.setResultClassName(annotation.value());
        }

        //set方法
        if (method != null) {
            requestBean.setMethodName(TypeUtils.getMethodId(method));
        }

        //set参数
        RequestParameter[] requestParameters = null;
        if (args != null && args.length > 0) {
            requestParameters = new RequestParameter[args.length];
            for (int i = 0; i < args.length; i++) {
                Object parameter = args[i];
                String parameterClassName = parameter.getClass().getName();
                String parameterValue = gson.toJson(parameter);

                RequestParameter requestParameter = new RequestParameter(parameterClassName, parameterValue);
                requestParameters[i] = requestParameter;
            }
        }

        if (requestParameters != null) {
            requestBean.setRequestParameter(requestParameters);
        }

        Request request = new Request(gson.toJson(requestBean), TYPE_NEW);

        return serviceConnectionManager.request(xHermesService, request);
    }
}
