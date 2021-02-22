package com.zzb.socket.voovan.splitter;

import org.voovan.network.IoSession;
import org.voovan.network.MessageSplitter;

import java.nio.ByteBuffer;

/**
 * 类名称：LineMessageSplitter
 * 类描述：按换行对消息分割
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/26 11:11 上午
 * 修改备注：TODO
 */
public class LineMessageSplitter implements MessageSplitter {
    @Override
    public int canSplite(IoSession session, ByteBuffer byteBuffer) {
        byteBuffer.position(byteBuffer.limit() - 1);
        byte lastByte = byteBuffer.get();
        byteBuffer.position(0);
        if (byteBuffer.limit() > 1 && lastByte == '\n') {
            return byteBuffer.limit();
        }
        return -1;
    }

}
