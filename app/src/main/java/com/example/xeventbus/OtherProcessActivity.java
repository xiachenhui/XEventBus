package com.example.xeventbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.xeventbus.manager.IUserManager;
import com.example.xeventbus.manager.XHermes;
import com.example.xeventbus.service.XHermesService;

/**
 * author : xia chen hui
 * email : 184415359@qq.com
 * date : 2019/8/26/026 7:59
 * desc : 在清单文件中设置process，就可以设置为其他进程，测试XHermes
 **/
public class OtherProcessActivity extends AppCompatActivity {

    private IUserManager iUerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_process);
        XHermes.getDefault().connect(this, XHermesService.class);

    }

    public void getMessage(View view) {
        Toast.makeText(this, iUerManager.getFriend().toString(), Toast.LENGTH_SHORT).show();

    }

    public void getObj(View view) {
        //获取发送的对象，客户端
        iUerManager = XHermes.getDefault().getInstance(IUserManager.class);
        Log.d("XCH---1", iUerManager.getFriend().toString());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
