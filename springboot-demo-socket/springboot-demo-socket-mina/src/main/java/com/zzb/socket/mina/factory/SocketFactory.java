package com.zzb.socket.mina.factory;

import com.zzb.socket.mina.socket.SocketDecoder;
import com.zzb.socket.mina.socket.SocketEncoder;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import java.nio.charset.Charset;

/**
 * 类名称：SocketFactory
 * 类描述：自定义编码解码工厂类
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/19 6:01 下午
 * 修改备注：TODO
 */
public class SocketFactory implements ProtocolCodecFactory {

    private final SocketDecoder decoder;
    private final SocketEncoder encoder;

    public SocketFactory(Charset charset) {
        encoder = new SocketEncoder(charset);
        decoder = new SocketDecoder(charset);
    }

    public SocketFactory() {
        this(Charset.defaultCharset());
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
        return decoder;
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
        return encoder;
    }
}
