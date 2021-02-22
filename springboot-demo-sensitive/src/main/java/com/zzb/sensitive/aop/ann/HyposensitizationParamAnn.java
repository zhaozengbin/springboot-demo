package com.zzb.sensitive.aop.ann;

import com.zzb.sensitive.enmu.EHandleType;

import java.lang.annotation.*;

/**
 * 类名称：HyposensitizationParamAnn
 * 类描述：TODO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/15 12:10 下午
 * 修改备注：TODO
 */
@Inherited
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HyposensitizationParamAnn {

    /**
     * 待填充对象的名称,默认第一个参数
     */
    String argName() default "";

    /**
     * 脱敏标签,方便标识特定类型,进行相关处理
     */
    String type() default "";

    /*反脱敏字段,可正则,默认字段名匹配*/
    String[] fields() default "";

    /*处理方式,默认字段相等匹配*/
    EHandleType mode() default EHandleType.DEFAULT;
}
