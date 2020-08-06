package pw.xiaohaozi.android_utils.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import androidx.annotation.RequiresApi;
import pw.xiaohaozi.android_utils.Utils;

/**
 * SD卡权限配置
 * 参考地址
 * https://blog.csdn.net/c529283955/article/details/104266083
 * 6.0以前，在AndroidManifest.xml中添加
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 * <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
 * <p>
 * 6.0开始需要动态权限，理论上 WRITE_EXTERNAL_STORAGE 或 READ_EXTERNAL_STORAGE 中一个就行
 * 8.0开始，WRITE_EXTERNAL_STORAGE 和 READ_EXTERNAL_STORAGE两个都要
 * 9.0模拟器测试貌似和8.0一样，真机没测
 * 10.0 需要在AndroidManifest.xml文件中的application 节点中添加 android:requestLegacyExternalStorage="true"
 * <pre class="prettyprint">
 * if (ContextCompat.checkSelfPermission(this,
 *             Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
 *         //2、申请权限: 参数二：权限的数组；参数三：请求码
 *         ActivityCompat.requestPermissions(this,
 *                 new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
 *                         Manifest.permission.READ_EXTERNAL_STORAGE},
 *                 REQUEST_WRITE);
 *     } else {
 *         writeToSdCard();
 *     }
 * }
 * </pre>
 *
 * <pre class="prettyprint">
 *  private static final int REQUEST_WRITE = 0;
 *  @Override
 * public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
 *     super.onRequestPermissionsResult(requestCode, permissions, grantResults);
 *     if (requestCode == REQUEST_WRITE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
 *         writeToSdCard();
 *     }
 * }
 *  </pre>
 */
public class FileUtils extends Utils {
    private static final String TAG = "FileUtils";

    /*-----------内部存储操作-------------------------------------------------------------------------------------------------------*/

    /**
     * 获取内部存储缓存
     *
     * @return data/data/包名/cache/
     */
    public static String getCacheDir() {
        return sContext.getCacheDir().getAbsolutePath() + File.separator;
    }

    /**
     * 获取内部存储文件
     *
     * @return data/data/包名/files/
     */
    public static String getFilesDir() {
        return sContext.getFilesDir().getAbsolutePath() + File.separator;
    }

    /*--------------SD卡相关操作-------------------------------------------------------------------------------------------*/

    /**
     * 判断SDCard是否挂载
     * 外部储存状态，只有值为MOUNTED的时候才会认为已经挂载
     *
     * @return 已经挂载返回true
     */
    public static boolean isSDCardMounted() {
        //外部储存状态，只有值为MOUNTED的时候才会认为已经挂载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;  //已经挂载返回true
        }
        return false;
    }

    /**
     * 获得SDCard的根目录
     * /storage/sdcard/
     *
     * @return
     */
    public static String getSDCardDir() {
        //已经挂载则获取根目录路径，并返回
        if (isSDCardMounted()) {
            File dir = Environment.getExternalStorageDirectory();
            return dir.getAbsolutePath() + File.separator;
        }
        return null;
    }

    /**
     * 获得SDCard的全部空间大小(单位:M)
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getSDCardSize() {
        if (isSDCardMounted()) {
            String baseDir = getSDCardDir();   //获取根目录
            StatFs statFs = new StatFs(baseDir);   //获取StatFs对象
            long blockCount = statFs.getBlockCountLong();  //通过StatFs对象获取块的数量
            long blockSize = statFs.getBlockSizeLong();    //通过StatFs对象获取每块的大小（字节）
            return (blockCount * blockSize / 1024 / 1024);  //块数量*每块大小/1024/1024  转化单位为M
        }
        return 0;
    }

    /**
     * 获取SDCard空闲空间的大小(单位:M)
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getSDCardFreeSize() {
        if (isSDCardMounted()) {
            String baseDir = getSDCardDir();   //获取根目录
            StatFs statFs = new StatFs(baseDir);   //获取StatFs对象
            long freeBlock = statFs.getFreeBlocksLong();//通过StatFs对象获取空闲块的数量
            long blockSize = statFs.getBlockSizeLong();  //通过StatFs对象获取每块的大小（字节）
            return (freeBlock * blockSize / 1024 / 1024);  //空闲块数量*每块大小/1024/1024  转化单位为M
        }
        return 0;
    }

    /**
     * 获取SDCard可用空间的大小(单位:M)
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getSDCardAvailSize() {
        if (isSDCardMounted()) {
            String baseDir = getSDCardDir();  //获取根目录
            StatFs statFs = new StatFs(baseDir);   //获取StatFs对象
            long availBlock = statFs.getAvailableBlocksLong();  //通过StatFs对象获取可用块的数量
            long blockSize = statFs.getBlockSizeLong();         //通过StatFs对象获取每块的大小（字节）
            return (availBlock * blockSize / 1024 / 1024);  //可用块数量*每块大小/1024/1024  转化单位为M
        }
        return 0;
    }

    /*-------------获取相关文件夹或者文件，如果没有则创建-----------------------------------------------------------------------------------*/

    /**
     * 获取SD卡公有目录路径
     * /storage/sdcard/{type}/
     *
     * @param type
     * @return
     */
    public static String getPublicDir(Type type) {
        if (isSDCardMounted()) {
            File dir = Environment.getExternalStoragePublicDirectory(type.getType());
            return dir.getAbsolutePath() + File.separator;
        }
        return null;
    }

    /**
     * 获取SDCard私有Cache目录路径
     * /storage/sdcard/Android/data/包名/cache/
     *
     * @return
     */
    public static String getPrivateDir() {
        if (isSDCardMounted()) {
            File dir = sContext.getExternalCacheDir();
            return dir.getAbsolutePath() + File.separator;
        }
        return null;
    }

    /**
     * 获取路径
     *
     * @param dir
     * @param paths
     * @return
     */
    public static String getPath(String dir, String... paths) {
        if (!StrUtils.isEmpty(dir)) {
            StringBuilder builder = new StringBuilder();
            builder.append(dir);
            builder.append(separator(dir));
            for (int i = 0; i < paths.length; i++) {
                builder.append(paths[i]);
                builder.append(File.separator);
            }
            File path = makeRootDirectory(builder.toString());
            if (path == null) return null;
            return path.getAbsolutePath() + File.separator;
        }
        return null;
    }

    /**
     * 获取文件
     *
     * @param dir      文件夹，可以是内部存储目录，也可以是外部存储目录（公有和私有）。 如：
     *                 getCacheDir()
     *                 getFilesDir()
     *                 getSDCardDir()
     *                 getPublicDir(Type type)
     *                 getPrivateCacheDir()
     *                 <p>
     * @param fileName 文件名
     * @param paths    文件路径，添加在dir和fileName中间的路径，dir/paths[0]/paths[0].../fileName
     * @return
     */
    public static File getFile(String dir, String fileName, String... paths) {
        String path = getPath(dir, paths);
        if (path == null) return null;
        return makeFilePath(path, fileName);
    }

    /**
     * 获取文件
     *
     * @param path     文件路径，全路径
     * @param fileName 文件名
     * @return
     */
    public static File getFile(String path, String fileName) {
        if (path == null) return null;
        path += separator(path);
        return makeFilePath(path, fileName);
    }
    /*-------------删除文件或文件夹-----------------------------------------------------------------------------------*/

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param file 要删除的文件夹或文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(File file) {
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile())
                return deleteFile(file);
            else
                return deleteDirectory(file);
        }
    }

    /**
     * 删除单个文件
     *
     * @param file 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(File file) {
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e(TAG, "文件：" + file + " 删除成功！");
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(File dir) {
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dir.exists()) || (!dir.isDirectory())) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dir.listFiles();
        for (File file : files) {
            // 删除子文件
            if (file.isFile()) {
                flag = deleteFile(file);
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (file.isDirectory()) {
                flag = deleteDirectory(file);
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            return false;
        }
        // 删除当前目录
        if (dir.delete()) {
            Log.e(TAG, "文件夹：" + dir + " 删除成功！");
            return true;
        } else {
            return false;
        }
    }
    /*-------------文件写入操作-----------------------------------------------------------------------------------*/

    /**
     * 向指定文件中写入字符串
     *
     * @param file 文件
     * @param data 需要写入的内容
     * @return true 成功 ； false 失败
     */
    public static boolean writer(File file, String data) {
        try {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.print(data);
            printWriter.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 写入byte数组到指定文杰中
     *
     * @param file 文件
     * @param data 需要写入的数据
     * @return true 成功 ； false 失败
     */
    public static boolean writer(File file, byte[] data) {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(data);
            bos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {     //finally总是会执行，尽管上面已经return true;了
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * @param file
     * @param bitmap
     * @return true 成功 ； false 失败
     */
    public static boolean writer(File file, Bitmap bitmap) {
        String s = file.getAbsolutePath();
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(s));
            //判断为jpg还是png类型
            if (s.endsWith(".jpg") || s.endsWith(".JPG")) {
                //图片压缩--参数（图片类型，图片质量0-100，输出流）
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            } else if (s.endsWith(".png") || s.endsWith(".PNG")) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            }
            bos.flush();
            return true;  //写入成功返回true
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    /*-------------文件读取操作-----------------------------------------------------------------------------------*/

    /**
     * 读取文件内容
     *
     * @param file
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String readString(File file) {
        StringBuilder builder = new StringBuilder();
        //防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw;
        //不关闭文件会导致资源的泄露，读写文件都同理
        //Java7的try-with-resources可以优雅关闭文件，异常时自动关闭文件；详细解读https://stackoverflow.com/a/12665271
        try (FileReader reader = new FileReader(file);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    /**
     * 读取文件内容
     *
     * @param file
     * @return
     */
    public static byte[] readByte(File file) {
        BufferedInputStream bis = null;
        byte[] bytes = new byte[1024];
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int hasRead;
            while (true) {
                hasRead = bis.read(bytes);
                if (hasRead < 0) {
                    break;
                }
                baos.write(bytes, 0, hasRead);
            }
            baos.flush();
            return baos.toByteArray();  //返回byte的数组
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 读取文件内容
     * 建议使用 readBitmap(String file)
     *
     * @param file
     * @return
     */
    @Deprecated
    public static Bitmap readBitmap(File file) {
//        byte[] data = readByte(file);
//        if (data == null) {
//            return null;
//        }
//        return BitmapFactory.decodeByteArray(data, 0, data.length);

        return readBitmap(file.getAbsoluteFile());
    }

    public static Bitmap readBitmap(String file) {
        return BitmapFactory.decodeFile(file);
    }



    /**
     * 按比例压缩
     *
     * @param file
     * @param ratio
     * @return
     */
    public static Bitmap readBitmap(String file, float ratio) {
        int reqWidth, reqHeight;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, options);
        //如果图片过大，则压缩后再识别，否则识别不出二维码
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, (int) (options.outWidth * ratio + .5f),
                (int) (options.outHeight * ratio + .5f));
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file, options);
    }

    /**
     * 获取压缩后的图像
     * 实际拿到的是和我们需求相近的图片，但是这种方法效率高，内存占用小
     *
     * @param file
     * @param minSize 将最小的边压缩到该值,另一边则按比例缩放
     * @return
     */
    public static Bitmap readBitmap(String file, int minSize) {
        int reqWidth, reqHeight;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//不返回位图，只返回图片信息
        BitmapFactory.decodeFile(file, options);
        options.inJustDecodeBounds = false;
        //如果图片过大，则压缩后再识别，否则识别不出二维码
        int min = Math.min(options.outWidth, options.outHeight);
        if (min > minSize) {
            float ratio = minSize / (float) min;
            // Calculate inSampleSize
            Log.i(TAG, "ratio: " + ratio);
            options.inSampleSize = calculateInSampleSize(options, (int) (options.outWidth * ratio + .5f),
                    (int) (options.outHeight * ratio + .5f));
            Log.i(TAG, "inSampleSize: " + options.inSampleSize);
            // Decode bitmap with inSampleSize set
            return BitmapFactory.decodeFile(file, options);
        } else {
            return BitmapFactory.decodeFile(file);
        }

    }
    /*-------------辅助方法-----------------------------------------------------------------------------------*/
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            //计算最大的inSampleSize值，该值以2为幂，并保持高度和宽度都大于请求的高度和宽度。
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    /**
     * 生成文件
     * 如果文件存在则忽略，如果不存在则生成文件
     *
     * @param filePath
     * @param fileName
     * @return
     */
    private static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 生成文件夹
     * 如果文件夹不存在，则生成文件夹
     *
     * @param filePath
     */
    private static File makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            Log.i("getTemplateListError:", e + "");
        }
        return file;
    }

    /**
     * 返回路径分隔符，如果原来路径中最后有分隔符，则返回空
     *
     * @param path
     * @return File.separator
     */
    private static String separator(String path) {
        if (!path.endsWith(File.separator)) return File.separator;
        return "";
    }

    /**
     * 规范公有目录type类型
     */
    public enum Type {
        /**
         * 音乐
         */
        DIRECTORY_MUSIC(Environment.DIRECTORY_MUSIC),
        /**
         * 博客
         */
        DIRECTORY_PODCASTS(Environment.DIRECTORY_PODCASTS),
        /**
         * 铃声
         */
        DIRECTORY_RINGTONES(Environment.DIRECTORY_RINGTONES),
        /**
         * 闹钟
         */
        DIRECTORY_ALARMS(Environment.DIRECTORY_ALARMS),
        /**
         * 通知
         */
        DIRECTORY_NOTIFICATIONS(Environment.DIRECTORY_NOTIFICATIONS),
        /**
         * 图片
         */
        DIRECTORY_PICTURES(Environment.DIRECTORY_PICTURES),
        /**
         * 电影
         */
        DIRECTORY_MOVIES(Environment.DIRECTORY_MOVIES),
        /**
         * 下载
         */
        DIRECTORY_DOWNLOADS(Environment.DIRECTORY_DOWNLOADS),
        /**
         * 缩略图缓存
         */
        DIRECTORY_DCIM(Environment.DIRECTORY_DCIM),
        /**
         * 文档
         */
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        DIRECTORY_DOCUMENTS(Environment.DIRECTORY_DOCUMENTS);
        private String type;

        Type(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

}
