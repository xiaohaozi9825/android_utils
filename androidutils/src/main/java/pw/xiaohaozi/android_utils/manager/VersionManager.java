package pw.xiaohaozi.android_utils.manager;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
        PackageManager packageManager =Utils.sContext.getPackageManager();
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

        PackageManager packageManager =Utils.sContext.getPackageManager();
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
     * 安装一个app
     *
     * @param file 需要安装的文件路径
     * @return
     */
    public static void install(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//判读版本是否在7.0以上
            Uri apkUri = FileProvider.getUriForFile(Utils.sContext,
                    Utils.sContext.getPackageName() + ".fileprovider", //注意名字和注册的authorities一致
                    file);//在AndroidManifest中的android:authorities值
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
        } else {
            Uri parse = Uri.parse("file://" + file.getAbsolutePath());
            intent.setDataAndType(parse, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        Utils.sContext.startActivity(intent);
    }

    public static boolean isAppExist(String appPath, String appName) {
        return getFile(appPath, appName).exists();
    }

    public static File getFile(String appPath, String appName) {
        return new File(Environment.getExternalStorageDirectory(), appPath + appName + ".apk");
    }

    public static String getFilePath(String appPath, String appName) {
        File file = new File(Environment.getExternalStorageDirectory(), appPath + appName + ".apk");
        return file.getAbsolutePath();
    }


    /**
     * 通过包名启动其他应用，假如应用已经启动了在后台运行，则会将应用切到前台
     *
     * @param packName
     */
    public static void startActivityForPackage(String packName) {
        Intent intent = Utils.sContext.getPackageManager().getLaunchIntentForPackage(packName);
        Utils.sContext.startActivity(intent);
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
