package com.example.xeventbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.xeventbus.core.XEventBus;
import com.example.xeventbus.manager.XHermes;
import com.example.xeventbus.service.HermesService;

/**
 * author : xia chen hui
 * email : 184415359@qq.com
 * date : 2019/8/26/026 7:59
 * desc : 在清单文件中设置process，就可以设置为其他进程
 **/
public class OtherProcessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        XHermes.getDefault().connect(this, HermesService.class);
    }

    public void send(View view) {


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}