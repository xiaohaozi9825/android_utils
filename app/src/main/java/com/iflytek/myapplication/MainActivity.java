package com.iflytek.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import pw.xiaohaozi.android_utils.Utils;
import pw.xiaohaozi.android_utils.utils.FileUtils;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE = 0;
    String TAG = "测试";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.init(this);
        Utils.setSPName("");//设置SharePreference保存的文件名（默认config）
        //Android10需要在AndroidManifest.xml文件中的application 节点中添加 android:requestLegacyExternalStorage="true"
        //判断是否有这个权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //2、申请权限: 参数二：权限的数组；参数三：请求码
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_WRITE);
        } else {
            writeToSdCard();
        }
    }

    private void writeToSdCard() {
        Log.i(TAG, "isSDCardMounted(): " + FileUtils.isSDCardMounted());
        Log.i(TAG, "getSDCardDir(): " + FileUtils.getSDCardDir());
        Log.i(TAG, "getCacheDir(): " + FileUtils.getCacheDir());
        Log.i(TAG, "getFilesDir(): " + FileUtils.getFilesDir());
        Log.i(TAG, "getPrivateDir(): " + FileUtils.getPrivateDir());
        Log.i(TAG, "getPublicDir(): " + FileUtils.getPublicDir(FileUtils.Type.DIRECTORY_MUSIC));
        Log.i(TAG, "getPath(): " + FileUtils.getPath(FileUtils.getSDCardDir(), "你妹"));
        Log.i(TAG, "getFile(): " + FileUtils.getFile(FileUtils.getPublicDir(FileUtils.Type.DIRECTORY_MUSIC), "hello.mp3", "你妹", "你妹"));
        Log.i(TAG, "getFile(): " + FileUtils.getFile(FileUtils.getSDCardDir() + File.separator + "你妹", "hello.mp3"));

        File file = FileUtils.getFile(FileUtils.getSDCardDir(), "hello.txt" , "你妹");
        FileUtils.writer(file, "hello,你好呀");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.i(TAG, "读取: " + FileUtils.readString(file));
        }
    }

    //判断授权的方法  授权成功直接调用写入方法  这是监听的回调
    //参数  上下文   授权结果的数组   申请授权的数组
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            writeToSdCard();
        }
    }
}
