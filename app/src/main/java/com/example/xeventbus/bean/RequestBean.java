package com.example.xeventbus.bean;

/**
 * author : xia chen hui
 * email : 184415359@qq.com
 * date : 2019/8/30/030 7:12
 * desc : 请求头的bean类
 **/
public class RequestBean {
    //请求单例的全类名
    private String className;

    //得到的类名
    private String resultClassName;

    private String requestObject;

    //返回方法名字
    private String methodName;

    //    参数
    private RequestParameter[] requestParameter;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getResultClassName() {
        return resultClassName;
    }

    public void setResultClassName(String resultClassName) {
        this.resultClassName = resultClassName;
    }

    public String getRequestObject() {
        return requestObject;
    }

    public void setRequestObject(String requestObject) {
        this.requestObject = requestObject;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public RequestParameter[] getRequestParameter() {
        return requestParameter;
    }

    public void setRequestParameter(RequestParameter[] requestParameter) {
        this.requestParameter = requestParameter;
    }
}
