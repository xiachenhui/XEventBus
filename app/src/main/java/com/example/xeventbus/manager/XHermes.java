package com.example.xeventbus.manager;

import android.content.Context;

import com.example.xeventbus.Request;
import com.example.xeventbus.Response;
import com.example.xeventbus.annotion.ClassId;
import com.example.xeventbus.bean.RequestBean;
import com.example.xeventbus.bean.RequestParameter;
import com.example.xeventbus.core.TypeCenter;
import com.example.xeventbus.core.XHermesInvocationHandler;
import com.example.xeventbus.service.XHermesService;
import com.example.xeventbus.utils.TypeUtils;
import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class XHermes {

    private static XHermes hermes;
    private ServiceConnectionManager serviceConnectionManager;
    private Context mContext;

    private Gson gson = new Gson();

    //得到对象，操作对象
    public static final int TYPE_NEW = 0;
    //得到单例，第一次拼接请求头
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

    /*----------------------服务端------------------------------*/
    public void register(Class<?> managerClass) {
        typeCenter.register(managerClass);
    }


    /*-----------------客户端使用的代码------------------*/
    public void connect(Context context, Class<XHermesService> hermesServiceClass) {
        connectApp(context, null, hermesServiceClass);

    }

    private void connectApp(Context context, String packageName, Class<XHermesService> hermesServiceClass) {
        //这里再次初始化是因为在客户端使用的时候，mContext拿不到
        init(context);
        serviceConnectionManager.bind(context.getApplicationContext(), packageName, hermesServiceClass);

    }

    //获取需要操作的对象
    public <T> T getInstance(Class<T> tClass, Object... parameters) {
        //拼接请求头
        Response response = sendRequest(XHermesService.class, tClass, null, parameters);
        //通过动态代理调用response
        return getProxy(XHermesService.class, tClass);
    }

    //获取服务端代理对象
    private <T> T getProxy(Class<? extends XHermesService> hermesServiceClass, Class tClass) {
        ClassLoader classLoader = hermesServiceClass.getClassLoader();
        T proxy = (T) Proxy.newProxyInstance(classLoader, new Class<?>[]{tClass}, new XHermesInvocationHandler(hermesServiceClass, tClass));
        return proxy;
    }

    /**
     * 拼接请求头
     *
     * @param hermesServiceClass AIDL
     * @param tClass             类
     * @param method             方法
     * @param parameters         参数
     * @param <T>
     * @return 发送到服务端的Response
     */
    private <T> Response sendRequest(Class<XHermesService> hermesServiceClass, Class<T> tClass, Method method, Object[] parameters) {
        RequestBean requestBean = new RequestBean();
        //set全类名
        String className;
        if (tClass.getAnnotation(ClassId.class) == null) {
            //没有注解
            className = tClass.getName();
        } else {
            className = tClass.getAnnotation(ClassId.class).value();

        }
        requestBean.setClassName(className);
        requestBean.setResultClassName(className);

        //set方法
        if (method != null) {
            requestBean.setMethodName(TypeUtils.getMethodId(method));
        }

        //set参数
        RequestParameter[] requestParameters = null;
        if (parameters != null && parameters.length > 0) {
            requestParameters = new RequestParameter[parameters.length];
            //把参数加到RequestParameter中
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                //参数类型
                String parameterClassName = parameter.getClass().getName();
                //参数值
                String parameterValue = gson.toJson(parameter);
                RequestParameter requestParameter = new RequestParameter(parameterClassName, parameterValue);
                requestParameters[i] = requestParameter;
            }
        }
        if (requestParameters != null) {
            requestBean.setRequestParameter(requestParameters);
        }
        //requestBean 转为字符串
        Request request = new Request(gson.toJson(requestBean), TYPE_GET);
        // 发送request
        return serviceConnectionManager.request(hermesServiceClass, request);

    }

    /**
     * 这里是操作服务端对象的方法
     *
     * @param xHermesService
     * @param aClass
     * @param method
     * @param args
     * @param <T>
     * @return
     */
    public <T> Response sendObjectRequest(Class<XHermesService> xHermesService, Class<T> aClass, Method method, Object[] args) {
        RequestBean requestBean = new RequestBean();

        //set全类名
        String className;
        ClassId annotation = aClass.getAnnotation(ClassId.class);
        if (annotation == null) {
            className = aClass.getName();
        } else {
            className = annotation.value();
        }
        requestBean.setClassName(className);
        requestBean.setResultClassName(className);
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
        //获取服务端的对象
        Request request = new Request(gson.toJson(requestBean), TYPE_NEW);
        //发送给服务端
        return serviceConnectionManager.request(xHermesService, request);
    }
}
