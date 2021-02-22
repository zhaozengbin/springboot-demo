package com.zzb.socket.netty.core.exception;

/**
 * 类名称：NoUseableChannel
 * 类描述：没有可用的通道异常
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 6:30 下午
 * 修改备注：TODO
 */
public class NoUseableChannel extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoUseableChannel() {
        super();
    }

    public NoUseableChannel(String message) {
        super(message);
    }
}
