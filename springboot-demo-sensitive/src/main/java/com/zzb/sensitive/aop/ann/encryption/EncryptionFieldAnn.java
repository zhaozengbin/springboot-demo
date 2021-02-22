package com.zzb.sensitive.aop.ann.encryption;

import java.lang.annotation.*;

/**
 * 类名称：EncryptionFieldAnn
 * 类描述：加密自定义注解
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/17 1:56 下午
 * 修改备注：TODO
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptionFieldAnn {

    /**
     * 原始字段名称，需要在加密字段上面进行注释，必须与EncryptionEncodeFieldAnn value 配对使用
     */
    String name();

    /**
     * 原始字段类型 根据原始字段实际情况设置
     */
    Class<?> type();

    /**
     * 是否加密字段 true为原始字段需要加密的字段，此字段在拦截后会进行加密 false为加密字段，如果需要此字段在拦截后会进行解密 注意：methodAnn isDecode为true时解密
     */
    boolean isNeedEencryption();

    EncryptionEncodeFieldAnn[] encodeFieldAnn() default @EncryptionEncodeFieldAnn(name = "encodeFieldName");

    EncryptionDecodeFieldAnn decodeFieldAnn() default @EncryptionDecodeFieldAnn();
}
