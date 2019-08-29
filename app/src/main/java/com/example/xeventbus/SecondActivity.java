package com.example.xeventbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.xeventbus.core.XEventBus;

/**
 * author : xia chen hui
 * email : 184415359@qq.com
 * date : 2019/8/26/026 7:59
 * desc : 测试XEventBus
 **/
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        XEventBus.getDefault().register(this);
    }

    public void send(View view) {
        XEventBus.getDefault().post(new Friend("aa", "12" + Thread.currentThread()));
        finish();
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                XEventBus.getDefault().post(new Friend("aa", "12" + Thread.currentThread()));
                finish();
            }
        }).start();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XEventBus.getDefault().unRegister(this);
    }

    public void get(View view) {
    }
}
