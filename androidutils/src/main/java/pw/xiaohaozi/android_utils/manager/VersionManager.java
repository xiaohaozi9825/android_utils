package pw.xiaohaozi.android_utils.manager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import pw.xiaohaozi.android_utils.Utils;

public class VersionManager {
    /**
     * 是否已经安装某个app
     *
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(String packageName) {
        PackageManager packageManager = Utils.sContext.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }

    /**
     * 获取app版本号
     *
     * @return
     */
    public static int getVersionCode() {
        PackageManager packageManager = Utils.sContext.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                int versionCode = pinfo.get(i).versionCode;
                if (pinfo.get(i).packageName.equals(Utils.sContext.getPackageName())) {
                    return versionCode;
                }
            }
        }
        return 0;
    }

    public static String getVersionName() {
        PackageManager packageManager = Utils.sContext.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String versionName = pinfo.get(i).versionName;
                if (pinfo.get(i).packageName.equals(Utils.sContext.getPackageName())) {
                    return versionName;
                }
            }
        }
        return "1.0";
    }


    /**
     * 安装未知来源应用
     * 1、配置权限
     * <pre class="prettyprint">
     *      <!-- android8.0安装app权限 -->
     *      <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
     * </pre>
     * <p>
     * 2、创建文件 res/xml/file_paths (这里我已经建好了，可以跳过该步骤)
     * <pre class="prettyprint">
     *      <?xml version="1.0" encoding="utf-8"?>
     *      <resources>
     *      <!--    name：一个引用字符串。-->
     *      <!--    path：文件夹“相对路径”，完整路径取决于当前的标签类型。path可以为空，表示指定目录下的所有文件、文件夹都可以被共享。-->
     *          <paths>
     *              <external-path
     *                  name="download"
     *                  path=""/>
     *          </paths>
     *      </resources>
     * </pre>
     * <p>
     * 3、AndroidManifest.xml文件 application节点下添加
     * <pre class="prettyprint">
     *     <!--  android:name：provider你可以使用v4包提供的FileProvider，或者自定义的，只需要在name申明就好了，一般使用系统的就足够了。-->
     *     <!--  android:authorities：类似schema，命名空间之类，后面会用到。-->
     *     <!--  android:exported：false表示我们的provider不需要对外开放。-->
     *     <!--  android:grantUriPermissions：申明为true，你才能获取临时共享权限。-->
     *      <provider
     *           android:name="androidx.core.content.FileProvider"
     *           android:authorities="包名.fileprovider"
     *           android:exported="false"
     *           android:grantUriPermissions="true">
     *           <meta-data
     *               android:name="android.support.FILE_PROVIDER_PATHS"
     *               android:resource="@xml/file_paths" />
     *      </provider>
     * </pre>
     * <p>
     * 4、在调用installProcess()方法的activity中重写onActivityResult()方法
     * <pre class="prettyprint">
     *     @Override
     *     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
     *         super.onActivityResult(requestCode, resultCode, data);
     *         //INSTALL_PERMISS_CODE为installProcess()方法中第三个形参 requestCode
     *         if (requestCode == INSTALL_PERMISS_CODE) {
     *             if (resultCode == RESULT_OK) {
     *                 VersionManager.install(app);
     *             } else {
     *                 //这里可以用弹窗提示用户打开权限
     *                 Log.i(TAG, "onActivityResult: 用户没有开启安装权限");
     *             }
     *         }
     *     }
     * </pre>
     *
     * @param activity
     * @param app
     * @param requestCode
     */
    public static void installProcess(Activity activity, File app, int requestCode) {
        //INSTALL_PERMISS_CODE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先获取是否有安装未知来源应用的权限
            if (!activity.getPackageManager().canRequestPackageInstalls()) {//没有权限
                startInstallPermissionSettingActivity(activity, requestCode);
                return;
            }
            //有权限，开始安装应用程序
            install(app);
        }
        //有权限，开始安装应用程序
        install(app);
    }

    /**
     * 安装一个app
     *
     * @param file 需要安装的文件路径
     * @return
     */
    public static void install(File file) {
        if (file == null) return;
        if (!file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri apkUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//判读版本是否在7.0以上
            apkUri = FileProvider.getUriForFile(Utils.sContext,
                    Utils.sContext.getPackageName() + ".fileprovider", //注意名字和注册的authorities一致
                    file);//在AndroidManifest中的android:authorities值
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
        } else {
            apkUri = Uri.parse("file://" + file.getAbsolutePath());
        }
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.sContext.startActivity(intent);
    }

    /**
     * 打开权限设置界面
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void startInstallPermissionSettingActivity(Activity activity, int requestCode) {
        //注意这个是8.0新API
        Uri packageURI = Uri.parse("package:" + activity.getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        activity.startActivityForResult(intent, requestCode);
    }


//    /**
//     * 获取自身app当前版本号
//     *
//     * @return
//     */
//    public static int getLocalVersion() {
//        int localVersion = 0;
//        try {
//            PackageInfo packageInfo =
//                    Utils.sContext
//                    .getPackageManager()
//                    .getPackageInfo(Utils.sContext.getPackageName(), 0);
//            localVersion = packageInfo.versionCode;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return localVersion;
//    }
}
