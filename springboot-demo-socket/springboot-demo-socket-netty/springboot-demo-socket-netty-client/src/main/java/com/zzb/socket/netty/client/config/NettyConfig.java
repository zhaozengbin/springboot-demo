package com.zzb.socket.netty.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 类名称：NettyConfig
 * 类描述：netty客户端配置
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/18 10:47 上午
 * 修改备注：TODO
 */
@Component
@ConfigurationProperties(prefix = "netty")
@Data
public class NettyConfig {

    private String url;

    private int port;
}
