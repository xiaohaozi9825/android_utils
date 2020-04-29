package com.iflytek.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import pw.xiaohaozi.android_utils.Utils;
import pw.xiaohaozi.android_utils.manager.ActivityManager;
import pw.xiaohaozi.android_utils.manager.VersionManager;
import pw.xiaohaozi.android_utils.utils.DeviceUtils;
import pw.xiaohaozi.android_utils.utils.FileUtils;
import pw.xiaohaozi.android_utils.utils.SPLastingUtils;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE = 0;
    private static final int INSTALL_PERMISS_CODE = 1;
    String TAG = "测试";
    File app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.init(this);
        Utils.setSPName("");//设置SharePreference保存的文件名（默认config）
        //Android10需要在AndroidManifest.xml文件中的application 节点中添加 android:requestLegacyExternalStorage="true"
        //判断是否有这个权限
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            //2、申请权限: 参数二：权限的数组；参数三：请求码
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                            Manifest.permission.READ_EXTERNAL_STORAGE},
//                    REQUEST_WRITE);
//        } else {
//            writeToSdCard();
//        }
        Log.i(TAG, "getUniqueId: " + DeviceUtils.getUniqueId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SPLastingUtils.init("测试.json");
            SPLastingUtils.clear();
            SPLastingUtils.set("flo", 123.2);
            double flo = SPLastingUtils.get("flo", Double.class);
            Log.i(TAG, "onCreate:flo =  " + flo);


            SPLastingUtils.set("hehe", "hello");
            String hehe = SPLastingUtils.get("hehe", String.class);
            Log.i(TAG, "onCreate:hehe =  " + hehe);

            SPLastingUtils.set("bool", true);
            SPLastingUtils.remove("bool");
            boolean bool = SPLastingUtils.get("bool", false);
            Log.i(TAG, "onCreate:bool =  " + bool);

            SPLastingUtils.set("obj", new P("张三", 18));
            String obj = SPLastingUtils.get("obj", "");
            Log.i(TAG, "onCreate:obj =  " + obj);
        }


        ActivityManager.getInstance().add(this);
        //如果我们一次start好几个Activity，那么只会执行一次onCreate()方法
        //当你finish掉一个后，另一个Activity才会执行onCreate()方法
//        startActivity(new Intent(this, TestActivity.class));
//        startActivity(new Intent(this, TestActivity.class));
//        startActivity(new Intent(this, TestActivity.class));
//        startActivity(new Intent(this, TestActivity.class));

//        new Handler().postDelayed(() -> startActivity(new Intent(this, TestActivity.class)), 1000);
//        new Handler().postDelayed(() -> startActivity(new Intent(this, TestActivity.class)), 2000);
//        new Handler().postDelayed(() -> startActivity(new Intent(this, TestActivity.class)), 3000);
//        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().remove(this);
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

        File file = FileUtils.getFile(FileUtils.getSDCardDir(), "hello.txt", "你妹");
        FileUtils.writer(file, "hello,你好呀");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.i(TAG, "读取: " + FileUtils.readString(file));
        }
        app = FileUtils.getFile(FileUtils.getSDCardDir(), "讯飞语记.apk");
        Log.i(TAG, "app: " + app.getAbsolutePath());
        VersionManager.installProcess(this, app, INSTALL_PERMISS_CODE);
    }

    public class P {
        private String name;
        private int size;

        public P(String name, int size) {
            this.name = name;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        @Override
        public String toString() {
            return "P{" +
                    "name='" + name + '\'' +
                    ", size=" + size +
                    '}';
        }
    }

    //判断授权的方法  授权成功直接调用写入方法  这是监听的回调
    //参数  上下文   授权结果的数组   申请授权的数组
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "requestCode = " + requestCode + " grantResults[0]" + grantResults[0]);
        if (requestCode == REQUEST_WRITE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            writeToSdCard();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INSTALL_PERMISS_CODE) {
            if (resultCode == RESULT_OK) {
                VersionManager.install(app);
            } else {
                //这里可以用弹窗提示用户打开权限
                Log.i(TAG, "onActivityResult: 用户没有开启安装权限");
            }
        }
    }
}
