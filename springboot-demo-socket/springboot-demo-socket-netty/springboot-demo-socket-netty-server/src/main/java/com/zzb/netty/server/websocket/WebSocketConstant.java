package com.zzb.netty.server.websocket;

/**
 * 类名称：WebSocketConstant
 * 类描述：WebSocket常量
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 6:59 下午
 * 修改备注：TODO
 */
public interface WebSocketConstant {

    /**
     * 点对点
     */
    String SEND_TO_USER = "send_to_user";

    /**
     * 群发 广播
     */
    String SEND_TO_USERS = "send_to_users";

    /**
     * 请求成功
     */
    String REQUEST_SUCCESS = "request_success";
}
