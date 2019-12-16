package pw.xiaohaozi.zkr.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

/**
 * SD卡相关辅助类
 */
public class SDCardUtils {

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
     * /storage/sdcard
     *
     * @return
     */
    public static String getSDCardBaseDir() {
        //已经挂载则获取根目录路径，并返回
        if (isSDCardMounted()) {
            File dir = Environment.getExternalStorageDirectory();
            return dir.getAbsolutePath();
        }
        return null;
    }

    /**
     * 获得SDCard的全部空间大小(单位:M)
     *
     * @return
     */
    public static long getSDCardSize() {
        if (isSDCardMounted()) {
            String baseDir = getSDCardBaseDir();   //获取根目录
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
    public static long getSDCardFreeSize() {
        if (isSDCardMounted()) {
            String baseDir = getSDCardBaseDir();   //获取根目录
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
    public static long getSDCardAvailSize() {
        if (isSDCardMounted()) {
            String baseDir = getSDCardBaseDir();  //获取根目录
            StatFs statFs = new StatFs(baseDir);   //获取StatFs对象
            long availBlock = statFs.getAvailableBlocksLong();  //通过StatFs对象获取可用块的数量
            long blockSize = statFs.getBlockSizeLong();         //通过StatFs对象获取每块的大小（字节）
            return (availBlock * blockSize / 1024 / 1024);  //可用块数量*每块大小/1024/1024  转化单位为M
        }
        return 0;
    }

    /**
     * 往SDCard公有目录下保存文件
     * (九大公有目录中的一个，具体由type指定) /storage/sdcard/{type}/{filename}
     * 公有目录即 SDCard跟目录下的 系统创建的文件夹
     *
     * @param data     要写入的数据
     * @param type     文件夹名
     * @param filename 文件名
     * @return boolean
     */
    public static boolean saveData2SDCardPublicDir(byte[] data, String type, String filename) {
        if (isSDCardMounted()) {
            String dir = getSDCardBaseDir() + File.separator + type;  //九大公有目录中的一个
            String file = dir + File.separator + filename;            //文件路径名
            BufferedOutputStream bos = null;                          //缓冲输出流
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(data);  //写入数据
                bos.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {              //finally总是会执行，尽管上面已经return true;了
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    /**
     * 往SDCard的自定义目录中保存数据
     * /storage/sdcard/{dir}
     *
     * @param data     数据
     * @param dir      路径
     * @param filename 文件名
     * @return 数据是否保存成功
     */
    public static boolean saveData2SDCardCustomDir(byte[] data, String dir, String filename) {
        if (isSDCardMounted()) {
            String saveDir = getSDCardBaseDir() + File.separator + dir;
            File saveFile = new File(saveDir);
            //如果不存在就创建该文件
            if (!saveFile.exists()) {
                saveFile.mkdirs();
            }
            String file = saveFile.getAbsolutePath() + File.separator + filename;
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
        }
        return false;
    }

    /**
     * 往SDCard的私有File目录下保存文件
     * /storage/sdcard/Android/data/包名/files/{type}/{filename}
     *
     * @param data
     * @param type
     * @param filename
     * @param context
     * @return
     */
    public static boolean saveData2SDCardPrivateFileDir(byte[] data, String type, String filename, Context context) {
        if (isSDCardMounted()) {
            File dir = context.getExternalFilesDir(type);  //获得私有File目录下
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String file = dir.getAbsolutePath() + File.separator + filename;
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(data);
                bos.flush();
                return true;
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
        }
        return false;
    }

    /**
     * 往SDCard的私有Cache目录下保存文件
     * /storage/sdcard/Android/data/包名/cache/{filename}
     *
     * @param data
     * @param filename
     * @param context
     * @return
     */
    public static boolean saveData2SDCardPrivateCacheDir(byte[] data, String filename, Context context) {
        if (isSDCardMounted()) {
            File dir = context.getExternalCacheDir();  //获取私有Cache目录
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String file = dir.getAbsolutePath() + File.separator + filename;
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(data);
                bos.flush();
                return true;
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
        }
        return false;
    }

    /**
     * 往SDCard的私有Cache目录下保存图像
     * /storage/sdcard/Android/data/包名/cache/{filename}
     *
     * @param bitmap   图片资源的bitmap对象
     * @param filename 文件名
     * @param context  上下文
     * @return boolean
     */
    public static boolean saveBitmap2SDCardPrivateCacheDir(Bitmap bitmap, String filename, Context context) {
        if (isSDCardMounted()) {
            File dir = context.getExternalCacheDir();  //获取私有Cache目录
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String file = dir.getAbsolutePath() + File.separator + filename;
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                //判断为jpg还是png类型
                if (file.endsWith(".jpg") || file.endsWith(".JPG")) {
                    //图片压缩--参数（图片类型，图片质量0-100，输出流）
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                } else if (file.endsWith(".png") || file.endsWith(".PNG")) {
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
        }
        return false;
    }

    /**
     * 从SDCard读取指定文件
     * /storage/sdcard/{filePath}
     *
     * @param filePath 要读取的文件名
     * @return byte[]
     */
    public static byte[] loadFileFromSDCard(String filePath) {
        if (isSDCardMounted()) {
            File dir = new File(getSDCardBaseDir());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String file = dir.getAbsolutePath() + File.separator + filePath;
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
        }
        return null;
    }

    /**
     * 从SDCard读取Bitmap并返回
     * /storage/sdcard/{filePath}
     *
     * @param filePath 要读取的图片名
     * @return Bitmap 返回图片资源
     */
    public static Bitmap loadBitmapFromSDCard(String filePath) {
        if (isSDCardMounted()) {
            String file = getSDCardBaseDir() + File.separator + filePath;
            BufferedInputStream bis = null;
            try {
                bis = new BufferedInputStream(new FileInputStream(file));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] bytes = new byte[1024];
                int hasRead;
                while (true) {
                    hasRead = bis.read(bytes);
                    if (hasRead < 0) {
                        break;
                    }
                    baos.write(bytes);
                }
                byte[] data = baos.toByteArray();
                return BitmapFactory.decodeByteArray(data, 0, data.length);
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
        }
        return null;
    }

    /**
     * 获取SD卡公有目录路径
     * /storage/sdcard/{type}
     *
     * @param type
     * @return
     */
    public static String getSDCardPublicDir(String type) {
        if (isSDCardMounted()) {
            File dir = Environment.getExternalStoragePublicDirectory(type);
            return dir.getAbsolutePath();
        }
        return null;
    }

    /**
     * 获取SDCard私有Cache目录路径
     * /storage/sdcard/Android/data/包名/cache/
     *
     * @param context
     * @return
     */
    public static String getSDCardPrivateCacheDir(Context context) {
        if (isSDCardMounted()) {
            File dir = context.getExternalCacheDir();
            return dir.getAbsolutePath();
        }
        return null;
    }

    /**
     * 获取SDCard私有File目录路径
     * /storage/sdcard/Android/data/包名/files/{type}
     *
     * @param context
     * @param type
     * @return
     */
    public static String getSDCardPrivateFilesDir(Context context, String type) {
        if (isSDCardMounted()) {
            File dir = context.getExternalFilesDir(type);
            return dir.getAbsolutePath();
        }
        return null;
    }

    /**
     * 判断一个文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExists(String filePath) {
        if (isSDCardMounted()) {
            String dir = getSDCardBaseDir() + File.separator + filePath;
            File file = new File(dir);
            return file.exists();
        }
        return false;
    }

    /**
     * 删除一个文件
     *
     * @param filePath
     * @return
     */
    public static boolean removeFileFromSDCard(String filePath) {
        if (isSDCardMounted()) {
            String dir = getSDCardBaseDir() + File.separator + filePath;
            File file = new File(dir);
            if (file.exists()) {
                return file.delete();
            }
        }
        return false;
    }

    /**
     * 保存字符串到指定文件中
     *
     * @param data
     * @param dir
     * @param fileName
     * @return
     */
    public static String saveData2SDCardCustomDir(String data, String dir, String fileName) {
        File file = makeFilePath(dir + File.separator, fileName);
        return saveData2SDCardCustomFile(data, file);
    }

    public static String saveData2SDCardCustomFile(String data, File fileName) {

        try {
            PrintWriter printWriter = new PrintWriter(fileName);
            printWriter.print(data);
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileName.getAbsolutePath();
    }

    /**
     * 读取文本文件
     *
     * @param filePath
     * @return
     */
    public static String loadStringFromSDCard(String filePath) {
        StringBuilder builder = new StringBuilder();
        //防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw;
        //不关闭文件会导致资源的泄露，读写文件都同理
        //Java7的try-with-resources可以优雅关闭文件，异常时自动关闭文件；详细解读https://stackoverflow.com/a/12665271
        try (FileReader reader = new FileReader(filePath);
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
     * 往SDCard的私有Cache目录下保存文件
     * /storage/sdcard/Android/data/包名/cache/{filename}
     *
     * @param data
     * @param filename
     * @param context
     * @return
     */
    public static String saveData2SDCardPrivateCacheDir(Context context, String filename, String data) {
        if (isSDCardMounted()) {
            File dir = context.getExternalCacheDir();  //获取私有Cache目录
            File file = makeFilePath(dir.getAbsolutePath() + File.separator, filename);
            try (PrintWriter printWriter = new PrintWriter(file)) {
                printWriter.print(data);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return file.getAbsolutePath();
        }
        return null;
    }

    /**
     * 获取私有目录下指定路径
     *
     * @param context
     * @param filePath 路径
     * @return 返回/storage/sdcard/Android/data/包名/cache/{filePath}/{fileName}
     */
    public static File getCardPrivateCacheFile(Context context, String filePath, String fileName) {
        if (isSDCardMounted()) {
            File cacheDirFile = context.getExternalCacheDir();  //获取私有Cache目录
            File path = makeRootDirectory(cacheDirFile.getAbsolutePath() + File.separator + filePath);
            return makeFilePath(path.getAbsolutePath() + File.separator, fileName);
        }
        return null;
    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param delFile 要删除的文件夹或文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String delFile) {
        File file = new File(delFile);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile())
                return deleteSingleFile(delFile);
            else
                return deleteDirectory(delFile);
        }
    }

    /**
     * 删除单个文件
     *
     * @param filePath$Name 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e("--Method--", "Copy_Delete.deleteSingleFile: 删除单个文件" + filePath$Name + "成功！");
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
     * @param filePath 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator))
            filePath = filePath + File.separator;
        File dirFile = new File(filePath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (File file : files) {
            // 删除子文件
            if (file.isFile()) {
                flag = deleteSingleFile(file.getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (file.isDirectory()) {
                flag = deleteDirectory(file
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            Log.e("--Method--", "Copy_Delete.deleteDirectory: 删除目录" + filePath + "成功！");
            return true;
        } else {
            return false;
        }
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
}
