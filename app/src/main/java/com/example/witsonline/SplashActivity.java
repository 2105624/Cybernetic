package com.example.witsonline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private final static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //the splash logic. It lasts for 2 seconds.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashPageIntent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(splashPageIntent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
