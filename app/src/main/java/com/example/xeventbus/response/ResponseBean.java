package com.example.xeventbus.response;

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
