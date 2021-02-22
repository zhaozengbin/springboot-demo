package com.zzb.sensitive.aop.ann.encryption;

import java.lang.annotation.*;

/**
 * 类名称：EncryptionEntityAnn
 * 类描述：加密自定义注解
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/17 1:56 下午
 * 修改备注：TODO
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptionEntityAnn {
}
