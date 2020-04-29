package com.iflytek.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import pw.xiaohaozi.android_utils.manager.ActivityManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {
    private AppCompatButton mBtn1;
    private AppCompatButton mBtn2;
    private AppCompatButton mBtn3;
    private AppCompatButton mBtn4;
    private AppCompatButton mBtn5;
    private AppCompatButton mBtn6;
    ActivityManager instance = ActivityManager.getInstance();
    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Log.i(TAG, "onCreate: ");
        instance.add(this);


        mBtn1 = findViewById(R.id.btn1);
        mBtn2 = findViewById(R.id.btn2);
        mBtn3 = findViewById(R.id.btn3);
        mBtn4 = findViewById(R.id.btn4);
        mBtn5 = findViewById(R.id.btn5);
        mBtn6 = findViewById(R.id.btn6);


        mBtn1.setOnClickListener(v -> {
            ArrayList<Activity> activities = instance.get();
            for (Activity acitity : activities) {
                Log.i(TAG, "所有的Activity: " + acitity.getClass());
            }
        });
        mBtn2.setOnClickListener(v -> {
            ArrayList<Activity> activities = instance.get(getClass());
            for (Activity acitity : activities) {
                Log.i(TAG, "获取所有名字为TestActivity: " + acitity.getClass());
            }
        });
        mBtn3.setOnClickListener(v -> {
            Activity activities = instance.getFirst();
            Log.i(TAG, "第一个: " + activities.getClass());
        });
        mBtn4.setOnClickListener(v -> {
            Activity activities = instance.getFirst(TestActivity.class);
            Log.i(TAG, "指定类名第一个: " + activities.getClass());
        });
        mBtn5.setOnClickListener(v -> {
            instance.finish(TestActivity.class);
        });
        mBtn6.setOnClickListener(v -> {
//            instance.finishAll();
            instance.getFirst().finish();
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance.remove(this);
    }
}
