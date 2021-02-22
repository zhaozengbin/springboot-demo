package com.zzb.sensitive.aop.ann.encryption;

import com.zzb.sensitive.enmu.EMethodType;

import java.lang.annotation.*;

@Inherited
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptionMethodAnn {
    /**
     * 是否验证加密方式 true时会打印加密前后信息，生成建议改为false
     */
    boolean isPrintLog() default false;

    /**
     * 是否对加密字段记性解密 配置为true时即使查询不包含敏感字段也会自动解密赋值，查询方法慎用
     */
    boolean isDecode() default false;

    /**
     * 方法类型 如果是查询方法必须配置isDecode才会自动解码
     */
    EMethodType type();
}
