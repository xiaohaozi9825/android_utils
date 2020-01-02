package pw.xiaohaozi.android_utils.utils;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import java.math.BigDecimal;

/**
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精 确的浮点数运算，包括加减乘除和四舍五入。
 */
public class ArithUtil {
    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    // 这个类不能实例化
    private ArithUtil() {
    }

    /**
     * 加法运算
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2);
    }

    /**
     * 加法运算
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        return add(Double.toString(v1), Double.toString(v2)).doubleValue();
    }

    /**
     * 减法运算
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal sub(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2);
    }

    /**
     * 减法运算
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        return sub(Double.toString(v1), Double.toString(v2)).doubleValue();
    }

    /**
     * 乘法运算
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal mul(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2);
    }

    /**
     * 乘法运算
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        return mul(Double.toString(v1), Double.toString(v2)).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    public static BigDecimal div(String v1, String v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        return div(Double.toString(v1), Double.toString(v2), scale).doubleValue();
    }

    public static BigDecimal div(String v1, String v2, int scale) {
        if (scale < 0) throw new IllegalArgumentException("参数 scale 必须是正整数或零");
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v 需要四舍五入的数字
     *          小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v) {
        BigDecimal b = new BigDecimal(v);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 四舍五入保留两位
     *
     * @param v
     * @return
     */
    public static String format(double v) {
        return String.format("%.2f", round(v));
    }

    /**
     * 将字符串转换成价格格式
     * 小数点后字体偏小
     *
     * @param price
     * @return
     */
    public static String toPrice(String price) {
        //<small>￥</small><big>" + orderBottom.getTotalAmount() + "</big>.<small>00</small>
        StringBuilder builder = new StringBuilder();
        builder.append("<small>¥</small><big>");
        builder.append(price.substring(0, price.length() - 3));
        builder.append("</big><small>");
        builder.append(price.substring(price.length() - 3));
        builder.append("</small>");

//        return String.valueOf(Html.fromHtml(builder.toString()));
        return builder.toString();
    }

    /**
     * 将字符串转换成价格格式
     * 小数点后字体偏小
     *
     * @param price
     * @return
     */
    public static String toPrice(double price) {
        return toPrice(format(price));
    }

    public static Spanned toPriceFromHtml(double price) {
        return Html.fromHtml(toPrice(price));
    }


//	常见用法：
//	初始化 BigDecimal a= new BigDecimal("1.35");
//	对数值取值：
//			1.a.getScale(1,BigDecimal.ROUND_DOWN);
//	取一位小数，直接删除后面多余位数，故取值1.3.
//			2.a.getScale(1,BigDecimal.ROUND_UP);
//	取一位小数，删除后面位数，进一位，故取值1.4.
//			3.a.getScale(1,BigDecimal.ROUND_HALF_UP);
//	取一位小数，四舍五入，故取值1.4.
//			4.a.getScale(1,BigDecimal.ROUND_HALF_DOWN);
//	取一位小数，四舍五入，但是5也是舍弃，故取值1.3.


}