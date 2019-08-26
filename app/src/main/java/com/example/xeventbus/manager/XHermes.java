package com.example.xeventbus.manager;

import android.content.Context;

import com.example.xeventbus.core.TypeCenter;
import com.example.xeventbus.service.HermesService;

public class XHermes {

    private static XHermes hermes;
    private static ServiceConnectionManager serviceConnectionManager;
    private Context mContext;

    private TypeCenter typeCenter;

    public XHermes() {
        typeCenter = TypeCenter.getInstance();
        serviceConnectionManager=ServiceConnectionManager.getInstance();
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

    public void register(Class<UserManager> managerClass) {
        typeCenter.register(managerClass);
    }


    /*-----------------客户端使用的代码------------------*/
    public void connect(Context context, Class<HermesService> hermesServiceClass) {
        connectApp(context, null, hermesServiceClass);

    }

    private void connectApp(Context context, String  packageName, Class<HermesService> hermesServiceClass) {
        //这里再次初始化是因为在客户端使用的时候，mContext拿不到
        init(context);
        serviceConnectionManager.bind(context.getApplicationContext(),packageName,hermesServiceClass);

    }
}
