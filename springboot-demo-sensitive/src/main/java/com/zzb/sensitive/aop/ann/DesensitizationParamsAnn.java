package com.zzb.sensitive.aop.ann;

import java.lang.annotation.*;

/**
 * 类名称：DesensitizationParamsAnn
 * 类描述：脱敏自定义字段注解
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/15 11:39 上午
 * 修改备注：TODO
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DesensitizationParamsAnn {


    /**
     * 脱敏自定义字段
     */
    DesensitizationParamAnn[] value();

}
