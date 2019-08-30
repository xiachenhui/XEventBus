package com.example.xeventbus.response;

/**
 * author : xia chen hui
 * email : 184415359@qq.com
 * date : 2019/8/30/030 7:00
 * desc : 响应的实体类
 **/
public class ResponseBean {
    private Object data;//UserManager

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResponseBean(Object data) {
        this.data = data;
    }
}
