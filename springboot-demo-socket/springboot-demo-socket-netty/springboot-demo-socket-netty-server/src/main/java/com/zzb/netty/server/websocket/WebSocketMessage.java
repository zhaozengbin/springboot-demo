package com.zzb.netty.server.websocket;

import lombok.Data;

import java.io.Serializable;

/**
 * 类名称：WebSocketMessage
 * 类描述：WebSocketMessage
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 7:00 下午
 * 修改备注：TODO
 */
@Data
public class WebSocketMessage implements Serializable {

    private static final long serialVersionUID = -4666429837358506065L;

    private String accept;
    private String content;
    private Type header;

    enum Type {
        send_user, send_users, request_success;
    }


}
