package com.zzb.socket.netty.client.rpc.util;

import com.zzb.socket.netty.core.MethodInvokeMeta;
import com.zzb.socket.netty.core.exception.NoUseableChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 类名称：ChannelUtil
 * 类描述：通道Util
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/18 10:51 上午
 * 修改备注：TODO
 */
public class ChannelUtil {

    /**
     * 用于记录c-s连接后建立的通道
     */
    private static final Set<Channel> CHANNELS = new ConcurrentSkipListSet<>();
    /**
     * ChannelUtil日志输出
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelUtil.class);
    /**
     * 用于记录通道响应的结果集
     */
    private static final Map<String, Object> RESULT_MAP = new ConcurrentHashMap<>();

    private ChannelUtil() {
    }

    /**
     * 方法：calculateResult
     * 描述：计算结果集（存储响应结果）
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key    : 唯一标识
     * @param result : 结果集
     * @return : void
     * @date: 2021年01月18日 10:51 上午
     */
    public static void calculateResult(String key, Object result) {
        RESULT_MAP.put(key, result);
    }

    /**
     * 方法：getResultKey
     * 描述：获取结果集的key
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key : 保存的唯一标识
     * @return : java.lang.String 结果集的 key (通道标识)
     * @date: 2021年01月18日 10:52 上午
     */
    public static String getResultKey(String key) {

        return (String) getResult(key);
    }

    /**
     * 方法：getResult
     * 描述：根据结果集的 key 获取结果集
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key : 结果集的key
     * @return : java.lang.Object 结果集
     * @date: 2021年01月18日 10:52 上午
     */
    public static Object getResult(String key) {
        return RESULT_MAP.get(key);
    }

    /**
     * 方法：registerChannel
     * 描述：注册通道
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param channel : 通道
     * @return : void
     * @date: 2021年01月18日 10:53 上午
     */
    public static void registerChannel(Channel channel) {
        ChannelId id = channel.id();
        LOGGER.info("{} -> [添加通道] {}", ChannelUtil.class.getName(), id);
        CHANNELS.add(channel);
    }

    /**
     * 方法：remoteCall
     * 描述：获取回调结果
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param methodInvokeMeta : 远程调用方法信息
     * @param key              : 用于取结果的key值
     * @return : void
     * @date: 2021年01月18日 10:53 上午
     */
    public static void remoteCall(MethodInvokeMeta methodInvokeMeta, String key) {

        LOGGER.info("{} -> [远程调用] ", ChannelUtil.class.getName());
        Iterator<Channel> iterator = CHANNELS.iterator();
        Channel channel;
        if (iterator.hasNext()) {
            channel = iterator.next();
        } else {
            LOGGER.error("{} -> [没有活跃的通道] ", ChannelUtil.class);
            throw new NoUseableChannel("没有活跃的通道");
        }
        // 将用于获取结果的key保存,以通道id为键
        String channelID = channel.id().asLongText();
        LOGGER.info("{} -> [保存获取结果的key] key - {} 通道id - {}", ChannelUtil.class, key, channelID);
        RESULT_MAP.put(channelID, key);
        ChannelFuture channelFuture = channel.writeAndFlush(methodInvokeMeta);
//        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }

    public static void remove(Channel channel) {
        CHANNELS.remove(channel);
    }
}
