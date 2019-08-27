package com.example.xeventbus.core;

import java.lang.reflect.Method;

/**
 * author : xia chen hui
 * email : 184415359@qq.com
 * date : 2019/8/27/027 7:33
 * desc : 注册类中的方法信息
 **/
public class SubscriberMethod {
    /**
     * 注册方法
     */
    private Method method;

    /**
     * 线程类型
     */
    private XThreadMode threadMode;
    /**
     * 参数类型
     */
    private Class<?> eventType;

    public SubscriberMethod(Method method, XThreadMode threadMode, Class<?> eventType) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public XThreadMode getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(XThreadMode threadMode) {
        this.threadMode = threadMode;
    }

    public Class<?> getEventType() {
        return eventType;
    }

    public void setEventType(Class<?> eventType) {
        this.eventType = eventType;
    }
}
