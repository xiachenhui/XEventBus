package com.example.xeventbus.core;

import java.lang.reflect.Method;

public class SubscriberMethod {
    /**
     * 注册方法
     */
    private Method method;

    /**
     * 线程类型
     */
    private XThreadMode xThreadMode;
    /**
     * 参数类型
     */
    private Class<?> eventType;

    public SubscriberMethod(Method method, XThreadMode xThreadMode, Class<?> eventType) {
        this.method = method;
        this.xThreadMode = xThreadMode;
        this.eventType = eventType;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public XThreadMode getxThreadMode() {
        return xThreadMode;
    }

    public void setxThreadMode(XThreadMode xThreadMode) {
        this.xThreadMode = xThreadMode;
    }

    public Class<?> getEventType() {
        return eventType;
    }

    public void setEventType(Class<?> eventType) {
        this.eventType = eventType;
    }
}
