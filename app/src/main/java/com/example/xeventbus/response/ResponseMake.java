package com.example.xeventbus.response;

import com.example.xeventbus.Request;
import com.example.xeventbus.Response;
import com.example.xeventbus.bean.RequestBean;
import com.example.xeventbus.bean.RequestParameter;
import com.example.xeventbus.core.ObjectCenter;
import com.example.xeventbus.core.TypeCenter;
import com.google.gson.Gson;

public abstract class ResponseMake {
    //UserManage  的Class
    protected Class<?> reslutClass;

    // getInstance()  参数数组
    protected Object[] mParameters;

    Gson GSON = new Gson();

    protected TypeCenter typeCenter = TypeCenter.getInstance();
//    怎么生成单例对象
// getInstance()  参数数组
//UserManage  的Class

    protected static final ObjectCenter OBJECT_CENTER = ObjectCenter.getInstance();


    protected abstract Object invokeMethod();

    protected abstract void setMethod(RequestBean requestBean);

    public Response makeResponse(Request request) {
        RequestBean requestBean = GSON.fromJson(request.getData(), RequestBean.class);
//reslutClass  UserManage   getInstance  method()
        reslutClass = typeCenter.getClassType(requestBean.getResultClassName());
//        参数还原    Object[]
        RequestParameter[] requestParameters = requestBean.getRequestParameter();
        if (requestParameters != null && requestParameters.length > 0) {
            mParameters = new Object[requestParameters.length];
            for (int i = 0; i < requestParameters.length; i++) {
                RequestParameter requestParameter = requestParameters[i];
                Class<?> clazz = typeCenter.getClassType(requestParameter.getParameterClassName());
                mParameters[i] = GSON.fromJson(requestParameter.getParameterValue(), clazz);
            }
        } else {
            mParameters = new Object[0];
        }

//        Method.invoke(null,object[])  重载
        setMethod(requestBean);
//        UserManager  getInstance()
        Object resultObject = invokeMethod();
//    返回
        ResponseBean responseBean = new ResponseBean(resultObject);
//把的到的结果序列化成字符串  resultObject--->
        String data = GSON.toJson(responseBean);
        Response response = new Response(data);
        return response;
    }
}
