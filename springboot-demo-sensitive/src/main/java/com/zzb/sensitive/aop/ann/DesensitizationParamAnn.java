package com.zzb.sensitive.aop.ann;

import com.zzb.sensitive.enmu.EHandleType;
import com.zzb.sensitive.enmu.ESensitiveType;

import java.lang.annotation.*;

/**
 * 类名称：DesensitizationParamAnnotation
 * 类描述：脱敏参数注解
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/15 11:39 上午
 * 修改备注：TODO
 */
@Inherited
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DesensitizationParamAnn{

    /**
     * 脱敏类型,默认电话号码
     */
    ESensitiveType type() default ESensitiveType.MOBILE_PHONE;

    /**
     * 脱敏字段,默认phone
     */
    String[] fields() default "phone";

    /**
     * 处理方式,默认字段相等匹配
     */
    EHandleType mode() default EHandleType.DEFAULT;

    /***
     * 身份证和手机号前面保留几位
     */
    int idFront() default 3;

    /***
     * 身份证和手机号后面保留几位
     */
    int idBack() default 3;

    /***
     * 地址默认保留前几位
     */
    int addSize() default 8;

    /***
     * 自定义正则匹配规则
     */
    String regExp() default "";

    /***
     * 正则替换字符
     */
    String regStr() default "*";

}
