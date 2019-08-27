package com.example.xeventbus.core;

public enum XThreadMode {

    /**
     * 事件的处理在和事件的发送在相同的进程,Subscriber会在post event的所在线程回调，故它不需要切换线程来分发事件，因此开销最小。它要求task完成的要快，不能请求MainThread，适用简单的task
     */
    POSTING,

    /**
     * Subscriber会在主线程（有时候也被叫做UI线程）回调，如果post event所在线程是MainThread,则可直接回调。注意不能阻塞主线程。
     */
    MAIN,

    /**
     * Subscriber会在主线程（有时候也被叫做UI线程）回调，如果post event所在线程是MainThread,则事件将总是排队等待传递。注意不能阻塞主线程。
     */
    MAIN_ORDERED,

    /**
     *  Subscriber会在后台线程中回调。如果post event所在线程不是MainThread，那么可直接回调；如果是MainThread,EventBus会用单例background thread来有序地分发事件。注意不能阻塞background thread。
     */
    BACKGROUND,

    /**
     * 当处理事件的Method是耗时的，需要使用此模式。尽量避免同时触发大量的耗时较长的异步操作，EventBus使用线程池高效的复用已经完成异步操作的线程。
     */
    ASYNC
}
