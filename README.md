# AndroidUtils使用 #
## 1、添加依赖库 ##

在app build.gradle文件中android下添加：

        
    allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
        }
    }

在dependencies 中添加依赖：

    
    implementation 'com.github.xiaohaozi9825:AndroidUtils:v1.2'

## 2、初始化 ##
在application中调用
     
        Utils.init(this);
        Utils.setSPName("");//设置SharePreference保存的文件名（默认config）

## 3、各类的功能 ##
- StrUtils //字符串工具类
- DeviceUtils //设备信息相关工具类
- SDCardUtils sd卡操作相关工具类
- ScreenUtils 屏幕相关工具类
- MD5Utils MD5加密工具
- DisplayUtils 单位转换工具
- BindingUtils databinding工具类，不需要手动调用
- ArithUtil 精确数学四则运算工具

