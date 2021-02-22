package com.zzb.netty.server;

import cn.hutool.log.Log;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * 类名称：MsgPackDecoder
 * 类描述：MsgPack解码器
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 7:03 下午
 * 修改备注：TODO
 */
public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf> {
    private static final Log LOGGER = Log.get(MsgPackDecoder.class);

    /**
     * 方法：decode
     * 描述：解码 byte[] -> Object
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param channelHandlerContext :
     * @param msg                   :
     * @param out                   :
     * @return : void
     * @date: 2021年01月15日 7:04 下午
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext
            , ByteBuf msg, List<Object> out) throws Exception {
        LOGGER.info("\n\t⌜⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓\n" +
                "\t├ [解码]: {}\n" +
                "\t⌞⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓", msg);
        final int length = msg.readableBytes();
        final byte[] array = new byte[length];
        msg.getBytes(msg.readerIndex(), array, 0, length);
        MessagePack messagePack = new MessagePack();
        out.add(messagePack.read(array));
        LOGGER.info("\n\t⌜⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓\n" +
                "\t├ [out]: {}\n" +
                "\t⌞⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓", out);
    }
}
