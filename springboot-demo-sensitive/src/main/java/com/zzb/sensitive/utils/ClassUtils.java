package com.zzb.sensitive.utils;

import lombok.experimental.PackagePrivate;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
@PackagePrivate
public class ClassUtils {

    /**
     * 方法：setStringValue
     * 描述：设置字符串值
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param str   :
     * @param value :
     * @return : void
     * @date: 2020年12月15日 1:16 下午
     */
    public static void setStringValue(Object str, Object value) {
        char[] chars = String.valueOf(value).toCharArray();
        try {
            //获取string 类中的value字段
            Field valueField = String.class.getDeclaredField("value");
            //设置private字段可以被修改
            valueField.setAccessible(true);
            //把chars设置到value字段的内容
            valueField.set(str, chars);
        } catch (Exception e) {
            log.error("setStringValueError", e);
        }
    }


    /**
     * 方法：swapBaseType
     * 描述：基本类型值切换
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param i :
     * @param j :
     * @return : void
     * @date: 2020年12月15日 1:17 下午
     */
    public static <T> void swapBaseType(T i, Object j) {
        try {
            Field field = i.getClass().getDeclaredField("value");
            field.setAccessible(true);
            field.set(i, j);
        } catch (Exception e) {
            log.error("swapBaseTypeError", e);
        }
    }

    /**
     * 方法：isWrapClass
     * 描述： 判断是基本封装类 .isPrimitive()是用来判断是否是基本类型的：void.isPrimitive() //true;
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param clz :
     * @return : boolean
     * @date: 2020年12月15日 1:17 下午
     */
    public static boolean isWrapClass(Class<?> clz) {
        try {
            return ((Class<?>) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 方法：isBaseType
     * 描述：判断object是否为基本类型
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param cls :
     * @return : boolean
     * @date: 2020年12月15日 1:17 下午
     */
    public static boolean isBaseType(Class<?> cls) {
        return cls.equals(int.class) ||
            cls.equals(byte.class) ||
            cls.equals(long.class) ||
            cls.equals(double.class) ||
            cls.equals(float.class) ||
            cls.equals(char.class) ||
            cls.equals(short.class) ||
            cls.equals(boolean.class);
    }
}
