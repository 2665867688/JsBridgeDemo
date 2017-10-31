package com.yue.jsbridge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yue.jsbridge.activity.SimpleTest01Activity;
import com.yue.jsbridge.activity.SimpleTestActivity;

/**
 * @ClassName:MainActivity
 * @auther:shimy
 * @date:2017/10/30 0030 下午 4:37
 * @description: 协议://类名:html给native的回调对象/方法?json参数
 * http://localhost:8080/login.action?username=""&password=""
 * hybrid://JSBridge:1538351/method?{“message”:”msg”}
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onClick();
    }

    private void onClick() {
        findViewById(R.id.btn_main_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SimpleTestActivity.class));
            }
        });

        findViewById(R.id.btn_main_frame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SimpleTest01Activity.class));
            }
        });
    }
}
