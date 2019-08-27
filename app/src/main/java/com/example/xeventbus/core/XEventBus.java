package com.example.xeventbus.core;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class XEventBus {

    private static XEventBus xEventBus;

    private Map<Object, List<SubscriberMethod>> cacheMap;

    private Handler xHandler;
    private ExecutorService executorService;

    public static XEventBus getDefault() {
        if (xEventBus == null) {
            synchronized (XEventBus.class) {
                if (xEventBus == null) {
                    xEventBus = new XEventBus();
                }
            }
        }
        return xEventBus;
    }

    private XEventBus() {
        cacheMap = new HashMap<>();
        xHandler = new Handler(Looper.getMainLooper());
        executorService = Executors.newCachedThreadPool();
    }

    /**
     * 注册
     *
     * @param subscriber 一般是类
     */
    public void register(Object subscriber) {
        //保存订阅者信息

        Class<?> subscriberClass = subscriber.getClass();
        List<SubscriberMethod> subscriberMethods = cacheMap.get(subscriberClass);
        //如果已经注册，就不需要注册了，
        if (subscriberMethods == null) {
            subscriberMethods = getSubscriberMethods(subscriber);
            cacheMap.put(subscriber, subscriberMethods);
        }


    }

    /**
     * 遍历能够接收事件的方法
     *
     * @param subscriber
     * @return
     */
    private List<SubscriberMethod> getSubscriberMethods(Object subscriber) {
        //有XSubscriber注解的方法集合
        List<SubscriberMethod> list = new ArrayList<>();
        Class<?> subscriberClass = subscriber.getClass();
        //需要在他父类中查找是否添加了注解的方法subscriber -->BaseActivity-->Activity,在Activity中不需要查找了
        while (subscriberClass != null) {
            //源码中的Activity不需要查找,判断是在哪个包下
            String name = subscriberClass.getName();
            if (name.startsWith("java.")
                    || name.startsWith("javax.")
                    || name.startsWith("android.")
                    || name.startsWith("androidx.")) {
                break;
            }
            //拿到所有的方法集合
            Method[] declaredMethods = subscriberClass.getDeclaredMethods();
            //遍历是否添加了注解
            for (Method method : declaredMethods) {
                XSubscriber annotation = method.getAnnotation(XSubscriber.class);
                if (annotation == null) {
                    continue;
                }
                //拿到注解之后,获取方法的参数类型
                Class<?>[] parameterTypes = method.getParameterTypes();
                //EventBus接收的参数只有一个对象
                if (parameterTypes.length != 1) {
                    throw new RuntimeException("XEventBus 只能接收一个参数");
                }
                //符合要求
                XThreadMode xThreadMode = annotation.threadMode();
                SubscriberMethod subscriberMethod = new SubscriberMethod(method, xThreadMode, parameterTypes[0]);
                //把有XSubscriber注解的方法信息添加到集合
                list.add(subscriberMethod);
            }
            //当前类查找完，查找他的父类
            subscriberClass = subscriberClass.getSuperclass();
        }


        return list;
    }

    /**
     * 取消注册
     *
     * @param subscriber 一般是类
     */
    public void unRegister(Object subscriber) {

        List<SubscriberMethod> list = cacheMap.get(subscriber);
        //如果有，就移除
        if (list != null) {
            cacheMap.remove(subscriber);
        }

    }

    /**
     * 发送
     *
     * @param object 类中方法的参数
     */
    public void post(final Object object) {
        //拿到所有的类
        Set<Object> set = cacheMap.keySet();
        Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()) {
            //拿到注册类
            final Object next = iterator.next();
            //获取类中所有添加注解的方法
            List<SubscriberMethod> list = cacheMap.get(next);

            for (final SubscriberMethod subMethod : list) {
                //判断方法是否应该接受事件 isAssignableFrom类的参数类型是来自哪里，如果是来自这个object，就往下执行
                if (subMethod.getEventType().isAssignableFrom(object.getClass())) {
                    switch (subMethod.getThreadMode()) {
                        case MAIN://接收方法在主线程

                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                //post方法执行在主线程中
                                invoke(subMethod, next, object);
                            } else {
                                //post方法执行在子线程中
                                xHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subMethod, next, object);
                                    }
                                });
                            }
                            break;
                        case ASYNC://接收方法在子线程

                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                //post执行在主线程
                                executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subMethod, next, object);
                                    }
                                });
                            } else {
                                //post执行在子线程
                                invoke(subMethod, next, object);
                            }

                            break;
                        case POSTING://接收方法与post在相同线程

                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                //post执行在主线程
                                xHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subMethod, next, object);
                                    }
                                });
                            } else {
                                //post执行在子线程
                                invoke(subMethod, next, object);
                            }
                            break;
                    }

                }
            }
        }

    }

    /**
     * 执行XSubscriber方法
     *
     * @param subMethod  注册类中的方法信息
     * @param next  类的实例
     * @param object    类中方法的参数
     */
    private void invoke(SubscriberMethod subMethod, Object next, Object object) {
        Method method = subMethod.getMethod();
        try {
            method.invoke(next, object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
