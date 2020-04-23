package pw.xiaohaozi.android_utils.utils;

import pw.xiaohaozi.android_utils.Utils;
import pw.xiaohaozi.android_utils.manager.VersionManager;

public class DeviceUtils extends Utils {
    /**
     * 获取设备唯一id
     *
     * @return
     */
    public static String getUniqueId() {
        return GetAndroidUniqueMark.getUniqueId(sContext);
    }


    /**
     * 获取skd版本
     *
     * @return android SDK
     */
    public static int getSdkVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取app当前版本号
     *
     * @return version_code
     */
    public static int getAppVersion() {
        return VersionManager.getVersionCode();
    }


    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getBrand() {
        return android.os.Build.BRAND;
    }

}
