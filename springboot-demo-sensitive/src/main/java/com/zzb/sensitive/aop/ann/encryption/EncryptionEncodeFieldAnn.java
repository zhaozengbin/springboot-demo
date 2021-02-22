package com.zzb.sensitive.aop.ann.encryption;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.zzb.sensitive.enmu.EEncryptionType;

import java.lang.annotation.*;

/**
 * 类名称：EncryptionEncodeFieldAnn
 * 类描述：加密字段自定义注解
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/17 1:56 下午
 * 修改备注：TODO
 */
@Inherited
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptionEncodeFieldAnn {

    /**
     * 加密类型
     */
    EEncryptionType value() default EEncryptionType.DEFAULT;

    /**
     * 加密字段名称，如果字段要加密需要虚拟字段，并且数据要做相应的映射，字段类型必须为String
     */
    String name();
}
