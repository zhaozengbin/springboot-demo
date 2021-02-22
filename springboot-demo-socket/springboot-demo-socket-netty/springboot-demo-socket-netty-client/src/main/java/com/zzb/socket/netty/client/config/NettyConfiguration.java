package com.zzb.socket.netty.client.config;

import com.zzb.socket.netty.client.rpc.util.NettyBeanScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类名称：NettyConfiguration
 * 类描述：Netty相关的初始化入口
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/18 10:48 上午
 * 修改备注：TODO
 */
@Configuration
public class NettyConfiguration {


    /**
     * 方法：initNettyBeanScanner
     * 描述：初始化加载Netty相关bean的配置方法
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param basePackage : 配置的包名
     * @param clientName  : 配置的Netty实例对象名
     * @return : com.zzb.socket.netty.client.rpc.util.NettyBeanScanner
     * @date: 2021年01月18日 10:48 上午
     */
    @Bean
    public static NettyBeanScanner initNettyBeanScanner(@Value("${netty.basePackage}") String basePackage,
                                                        @Value("${netty.clientName}") String clientName) {
        // 创建对象
        return new NettyBeanScanner(basePackage, clientName);
    }
}
