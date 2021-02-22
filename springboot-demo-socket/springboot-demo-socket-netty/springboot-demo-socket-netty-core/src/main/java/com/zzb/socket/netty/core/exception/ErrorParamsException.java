package com.zzb.socket.netty.core.exception;

/**
 * 类名称：ErrorParamsException
 * 类描述：参数错误异常
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 6:29 下午
 * 修改备注：TODO
 */
public class ErrorParamsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ErrorParamsException() {
        super();
    }

    public ErrorParamsException(String message) {
        super(message);
    }
}
