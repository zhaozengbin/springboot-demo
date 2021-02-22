package com.zzb.netty.server.websocket;

import cn.hutool.log.Log;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.internal.PlatformDependent;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 类名称：WebSocketUsers
 * 类描述：WebSocket用户集
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/1/15 7:03 下午
 * 修改备注：TODO
 */
public class WebSocketUsers {


    /**
     * 用户集
     */
    private static final ConcurrentMap<String, Channel> USERS = PlatformDependent.newConcurrentHashMap();

    private static final Log LOGGER = Log.get(WebSocketUsers.class);

    private static WebSocketUsers ourInstance = new WebSocketUsers();

    private WebSocketUsers() {
    }

    public static WebSocketUsers getInstance() {
        return ourInstance;
    }

    /**
     * 方法：put
     * 描述：存储通道
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key     : 唯一键
     * @param channel : 通道
     * @return : void
     * @date: 2021年01月15日 7:05 下午
     */
    public static void put(String key, Channel channel) {
        USERS.put(key, channel);
    }

    /**
     * 方法：remove
     * 描述：移除通道
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param channel : channel 通道
     * @return : boolean
     * @date: 2021年01月15日 7:05 下午
     */
    public static boolean remove(Channel channel) {

        String key = null;
        boolean b = USERS.containsValue(channel);
        if (b) {
            Set<Map.Entry<String, Channel>> entries = USERS.entrySet();
            for (Map.Entry<String, Channel> entry : entries) {
                Channel value = entry.getValue();
                if (value.equals(channel)) {
                    key = entry.getKey();
                    break;
                }
            }
        } else {
            return true;
        }
        return remove(key);
    }

    /**
     * 方法：remove
     * 描述：移出通道
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param key :
     * @return : boolean
     * @date: 2021年01月15日 7:05 下午
     */
    public static boolean remove(String key) {

        Channel remove = USERS.remove(key);
        boolean containsValue = USERS.containsValue(remove);
        LOGGER.info("\n\t⌜⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓\n" +
                "\t├ [移出结果]: {}\n" +
                "\t⌞⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓", containsValue ? "失败" : "成功");
        return containsValue;
    }

    /**
     * 方法：getUSERS
     * 描述：获取在线用户列表
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : java.util.concurrent.ConcurrentMap<java.lang.String,io.netty.channel.Channel> 返回用户集合
     * @date: 2021年01月15日 7:04 下午
     */
    public static ConcurrentMap<String, Channel> getUSERS() {
        return USERS;
    }

    /**
     * 方法：sendMessageToUsers
     * 描述：群发消息
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param message : 消息内容
     * @return : void
     * @date: 2021年01月15日 7:04 下午
     */
    public static void sendMessageToUsers(String message) {

        Collection<Channel> values = USERS.values();
        for (Channel value : values) {
            value.write(new TextWebSocketFrame(message));
            value.flush();
        }
    }

    /**
     * 方法：sendMessageToUser
     * 描述：给某个人发送消息
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param userName : key
     * @param message  : 消息
     * @return : void
     * @date: 2021年01月15日 7:04 下午
     */
    public static void sendMessageToUser(String userName, String message) {
        Channel channel = USERS.get(userName);
        channel.write(new TextWebSocketFrame(message));
        channel.flush();
    }
}