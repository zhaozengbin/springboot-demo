package com.zzb.socket.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 类名称：WebSocketConfig
 * 类描述：开启WebSocket支持
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 1:36 下午
 * 修改备注：TODO
 */
@Configuration
public class WebSocketConfig {

    /**
     * 方法：serverEndpointExporter
     * 描述：扫描并注册带有@ServerEndpoint注解的所有服务端
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : org.springframework.web.socket.server.standard.ServerEndpointExporter
     * @date: 2021年01月15日 1:36 下午
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {

        return new ServerEndpointExporter();
    }
}
