package com.iflytek.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import pw.xiaohaozi.zkr.Utils;
import pw.xiaohaozi.zkr.utils.DeviceUtils;
import pw.xiaohaozi.zkr.utils.ScreenUtils;
import pw.xiaohaozi.zkr.utils.StrUtils;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    String TAG = "测试";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.init(this);
        Utils.setSPName("");//设置SharePreference保存的文件名（默认config）

        Log.i(TAG, "onCreate: " + StrUtils.isEmpty(" "));
        Log.i(TAG, "onCreate: " + DeviceUtils.getUniqueId());
    }
}
