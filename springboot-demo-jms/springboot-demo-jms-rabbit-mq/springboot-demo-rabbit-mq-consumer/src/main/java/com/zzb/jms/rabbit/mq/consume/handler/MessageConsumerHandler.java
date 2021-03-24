package com.zzb.jms.rabbit.mq.consume.handler;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * 类名称：MessageConsumerHandler
 * 类描述：消息消费者处理
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/3/24 6:32 下午
 * 修改备注：TODO
 */
@Component
public class MessageConsumerHandler implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        System.out.println(message.toString());
    }

}
