package com.zzb.sensitive.aop.ann;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zzb.sensitive.enmu.ESensitiveType;
import com.zzb.sensitive.serializer.SensitiveInfoSerialize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 方法：
 * 描述：脱敏AOP注解
 * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
 *
 * @param null :
 * @date: 2020年12月15日 1:55 下午
 * @return : null
 */
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveInfoSerialize.class)
public @interface SensitiveInfoAnn {

    ESensitiveType value() default ESensitiveType.RGE_EXP;

    /**
     * 身份证和手机号前面保留几位
     *
     * @return
     */
    int idFront() default 3;

    /**
     * 身份证和手机号后面保留几位
     *
     * @return
     */
    int idBack() default 3;

    /**
     * 地址默认保留前几位
     *
     * @return
     */
    int addSize() default 8;

    /**
     * 自定义正则匹配规则
     *
     * @return
     */
    String regExp() default "";

    /**
     * 正则替换字符
     *
     * @return
     */
    String regStr() default "*";
}
