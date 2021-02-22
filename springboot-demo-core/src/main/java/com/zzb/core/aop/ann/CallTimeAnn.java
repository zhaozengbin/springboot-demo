package com.zzb.core.aop.ann;

import java.lang.annotation.*;

/**
 * 类名称：CallTimeAnn
 * 类描述：执行时间注解
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/2 2:17 下午
 * 修改备注：TODO
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CallTimeAnn {
}
