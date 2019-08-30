package com.example.xeventbus.response;

import com.example.xeventbus.Request;
import com.example.xeventbus.Response;
import com.example.xeventbus.bean.RequestBean;
import com.example.xeventbus.bean.RequestParameter;
import com.example.xeventbus.core.ObjectCenter;
import com.example.xeventbus.core.TypeCenter;
import com.google.gson.Gson;

/**
 * author : xia chen hui
 * email : 184415359@qq.com
 * date : 2019/8/30/030 7:30
 * desc : 策略模式，根据不同标记生成不同对象。抽象的创建Response
 **/
public abstract class ResponseMake {
    //UserManage  的Class，返回给客户端的对象
    protected Class<?> resultClass;

    // getInstance()  返回给客户端的参数数组
    protected Object[] mParameters;

    Gson GSON = new Gson();

    protected TypeCenter typeCenter = TypeCenter.getInstance();
    //    怎么生成单例对象
    // getInstance()  参数数组
    //UserManage  的Class

    protected static final ObjectCenter OBJECT_CENTER = ObjectCenter.getInstance();

    //反射调用
    protected abstract Object invokeMethod();
    //设置方法
    protected abstract void setMethod(RequestBean requestBean);

    /**
     * 生成Response对象
     *
     * @param request
     * @return
     */
    public Response makeResponse(Request request) {
        //拿到request
        RequestBean requestBean = GSON.fromJson(request.getData(), RequestBean.class);
        //resultClass  UserManage   getInstance  method() 获取request的类名
        resultClass = typeCenter.getClassType(requestBean.getResultClassName());
        //参数还原    Object[] 获取request的参数
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

        //Method.invoke(null,object[])  重载
        setMethod(requestBean);
        //UserManager  getInstance()
        Object resultObject = invokeMethod();
        // 返回 response
        ResponseBean responseBean = new ResponseBean(resultObject);
        //把的到的结果序列化成字符串  resultObject--->
        String data = GSON.toJson(responseBean);
        Response response = new Response(data);
        return response;
    }
}
