package com.example.xeventbus.bean;

/**
 * author : xia chen hui
 * email : 184415359@qq.com
 * date : 2019/8/30/030 7:14
 * desc : 方法的参数
 **/
public class RequestParameter {
    //参数的名字
    private String parameterClassName;
    //参数的值
    private String parameterValue;

    public RequestParameter() {
    }

    public RequestParameter(String parameterClassName, String parameterValue) {
        this.parameterClassName = parameterClassName;
        this.parameterValue = parameterValue;
    }

    public String getParameterClassName() {
        return parameterClassName;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterClassName(String parameterClassName) {
        this.parameterClassName = parameterClassName;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }
}
