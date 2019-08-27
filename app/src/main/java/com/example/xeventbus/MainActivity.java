package com.example.xeventbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xeventbus.core.XEventBus;
import com.example.xeventbus.core.XSubscriber;
import com.example.xeventbus.core.XThreadMode;
import com.example.xeventbus.manager.XHermes;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XEventBus.getDefault().register(this);
        textView = findViewById(R.id.getMsg);
        XHermes.getDefault().init(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XEventBus.getDefault().unRegister(this);
    }

    @XSubscriber(threadMode = XThreadMode.POSTING)
    public void receive(Friend friend) {
        textView.setText(friend.toString());
        try {

            Toast.makeText(this, friend.toString(), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void turn(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }

    //跳转到其他进程
    public void turnOtherProcess(View view) {
        startActivity(new Intent(this, OtherProcessActivity.class));
    }
}
