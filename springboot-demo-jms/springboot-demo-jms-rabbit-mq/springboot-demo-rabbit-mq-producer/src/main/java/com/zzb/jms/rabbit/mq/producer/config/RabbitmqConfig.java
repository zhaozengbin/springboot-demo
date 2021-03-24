package com.zzb.jms.rabbit.mq.producer.config;

import cn.hutool.log.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * 类名称：RabbitmqConfig
 * 类描述：rabbitMQ
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/3/24 7:01 下午
 * 修改备注：TODO
 */
@Configuration
public class RabbitmqConfig {
    private static final Log LOG = Log.get(RabbitmqConfig.class);

    @Autowired
    private CachingConnectionFactory connectionFactory;


    @Bean
    @Scope("prototype")
    public RabbitTemplate rabbitTemplate() {
        //若使用confirm-callback或return-callback，必须要配置publisherConfirms或publisherReturns为true
        //每个rabbitTemplate只能有一个confirm-callback和return-callback，如果这里配置了，那么写生产者的时候不能再写confirm-callback和return-callback
        //使用return-callback时必须设置mandatory为true，或者在配置中设置mandatory-expression的值为true
        /**
         * 考虑到并发性，与 validErr 消息的 次要性，这里不使用 confirm 模式 和 return 模式
         * 如果使用这两个模式的话，会报异常 channelMax reached。
         * 如果后边需要这两个模式的话，再做解决，可以考虑通过Thread.sleep() 的方式，减少 channel 的积压
         */
        connectionFactory.setPublisherConfirms(false);
        connectionFactory.setPublisherReturns(false);

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
//        /**
//         * 如果消息没有到exchange,则confirm回调,ack=false
//         * 如果消息到达exchange,则confirm回调,ack=true
//         * exchange到queue成功,则不回调return
//         * exchange到queue失败,则回调return(需设置mandatory=true,否则不回回调,消息就丢了)
//         */
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (ack) {
                    LOG.debug("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
                } else {
                    LOG.debug("消息发送失败:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
                }
            }
        });
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                LOG.debug("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message);
            }
        });
        return rabbitTemplate;
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory);
    }
}
