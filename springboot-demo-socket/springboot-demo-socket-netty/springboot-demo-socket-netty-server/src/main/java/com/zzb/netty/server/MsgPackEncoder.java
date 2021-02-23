package com.zzb.netty.server;

import cn.hutool.log.Log;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * 方法： MsgPack编码器
 * 描述：TODO
 * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
 *
 * @param null :
 * @date: 2021年01月15日 7:06 下午
 * @return : null
 */
public class MsgPackEncoder extends MessageToByteEncoder<Object> {

    private static final Log LOG = Log.get(MsgPackEncoder.class);

    /**
     * 方法：encode
     * 描述：编码 Object -> byte[]
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param channelHandlerContext :
     * @param msg                   :
     * @param out                   :
     * @return : void
     * @date: 2021年01月15日 7:07 下午
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf out) throws Exception {
        LOG.info("\n\t⌜⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓\n" +
                "\t├ [编码]: {}\n" +
                "\t⌞⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓", msg);
        MessagePack messagePack = new MessagePack();
        // 序列化
        byte[] write = messagePack.write(msg);
        out.writeBytes(write);
    }
}
