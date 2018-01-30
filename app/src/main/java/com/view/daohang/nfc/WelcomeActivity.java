package com.view.daohang.nfc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import Utils.HttpUtil;

public class WelcomeActivity extends AppCompatActivity {

    private String realName = HttpUtil.getRealName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome);

        final Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (realName == null){
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                }else {
                    startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                    WelcomeActivity.this.finish();
                }
            }
        };
        timer.schedule(timerTask,3000);
    }
}
