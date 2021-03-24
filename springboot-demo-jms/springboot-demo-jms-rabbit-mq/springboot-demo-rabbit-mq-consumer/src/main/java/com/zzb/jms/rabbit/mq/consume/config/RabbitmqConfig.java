package com.zzb.jms.rabbit.mq.consume.config;

import com.zzb.jms.rabbit.mq.consume.handler.MessageConsumerHandler;
import com.zzb.jms.rabbit.mq.consume.handler.QueueService;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.util.List;

/**
 * 类名称：RabbitmqConfig
 * 类描述：rabbit配置类
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/3/24 6:29 下午
 * 修改备注：TODO
 */

@Configuration
public class RabbitmqConfig {
    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Autowired
    private MessageConsumerHandler handler;

    @Autowired
    private QueueService queueService;


    @Bean
    @Order(value = 2)
    public SimpleMessageListenerContainer mqMessageContainer() throws AmqpException, IOException {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        List<String> list = queueService.getMQJSONArray();
        container.setQueueNames(list.toArray(new String[list.size()]));
        container.setExposeListenerChannel(true);
        container.setPrefetchCount(1);//设置每个消费者获取的最大的消息数量
        container.setConcurrentConsumers(100);//消费者个数
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);//设置确认模式为手工确认
        container.setMessageListener(handler);//监听处理类
        return container;
    }

    @Bean
    public void start() {
        try {
            mqMessageContainer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
