package pw.xiaohaozi.android_utils.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 自定义Gson中Type，适用于集合 ， 类似:
 * //        Type type = new TypeToken<HashMap<String, Integer>>() {
 * //        }.getType();
 * <p>
 * <p>
 * 1、HashMap类型，HashMap<String, Integer> 可以这样使用：
 * Type type = new TypeToken( HashMap.class,String.class，Integer.class)
 * <p>
 * 2、多层泛型嵌套
 * 如果有一个泛型类 class A<T>
 * 当我们想要解析出返回类型为: ArrayList<A<String>>的类型时,可以写成这样：
 * Type type = new TypeToken( ArrayList.class,new TypeToken(A.class,String.class))
 *
 * <p>
 * 参考博客
 * https://www.jianshu.com/p/d62c2be60617
 */
public class TypeToken implements ParameterizedType {
    private final Class raw;
    private final Type[] args;

    /**
     * 以 HasMap<String,Integer>为例
     *
     * @param raw  HasMap.class
     * @param args String.class, Integer.class
     */
    public TypeToken(Class raw, Type... args) {
        this.raw = raw;
        this.args = args != null ? args : new Type[0];
    }

    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }

    @Override
    public Type getRawType() {
        return raw;
    }

    // 用于这个泛型上中包含了内部类的情况,一般返回null
    @Override
    public Type getOwnerType() {
        return null;
    }
}
