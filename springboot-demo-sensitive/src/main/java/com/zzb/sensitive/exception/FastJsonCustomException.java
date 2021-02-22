package com.zzb.sensitive.exception;

import lombok.Getter;

/**
 * 类名称：FastJsonCustomException
 * 类描述：脱敏后数据返回
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/12/15 11:42 上午
 * 修改备注：TODO
 */
public class FastJsonCustomException extends RuntimeException {


    @Getter
    private final Object rt;

    public FastJsonCustomException(Object rt) {
        this.rt = rt;
    }
}