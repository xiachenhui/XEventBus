package com.example.xeventbus.bean;

public class RequestParameter {
    private String parameterClassName;
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
