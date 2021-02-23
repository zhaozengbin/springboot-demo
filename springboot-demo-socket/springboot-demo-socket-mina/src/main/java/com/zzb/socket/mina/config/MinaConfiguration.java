package com.zzb.socket.mina.config;

import cn.hutool.log.Log;
import com.zzb.socket.mina.config.properties.MinaProperties;
import com.zzb.socket.mina.factory.SocketFactory;
import com.zzb.socket.mina.handler.ServerHandler;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;

/**
 * 类名称：MinaConfiguration
 * 类描述：MINA配置相关信息
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/19 6:03 下午
 * 修改备注：TODO
 */
@Configuration
public class MinaConfiguration {

    private static final Log LOG = Log.get(MinaConfiguration.class);

    @Autowired
    private MinaProperties minaProperties;

    @Bean
    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }

    @Bean
    public IoHandler ioHandler() {
        return new ServerHandler();
    }

    @Bean
    public IoAcceptor ioAcceptor() throws Exception {
        IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("logger", loggingFilter());
        // 使用自定义编码解码工厂类
        acceptor.getFilterChain().addLast("coderc", new ProtocolCodecFilter(new SocketFactory(Charset.forName("utf-8"))));
        acceptor.setHandler(ioHandler());
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        SocketAddress addresses = new InetSocketAddress(minaProperties.getPort());
        acceptor.bind(new SocketAddress[]{addresses});
        LOG.info("=====================> Mina服务器在端口：" + minaProperties.getPort() + "已经启动!");
        return acceptor;
    }

}
