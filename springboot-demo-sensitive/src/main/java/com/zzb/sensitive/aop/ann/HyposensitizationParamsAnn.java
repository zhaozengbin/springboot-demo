package com.zzb.sensitive.aop.ann;

import java.lang.annotation.*;

/**
 * 类名称：HyposensitizationParamsAnn
 * 类描述：TODO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/15 12:13 下午
 * 修改备注：TODO
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HyposensitizationParamsAnn {
    /**
     * 反脱敏参数
     */
    HyposensitizationParamAnn[] value();
}
