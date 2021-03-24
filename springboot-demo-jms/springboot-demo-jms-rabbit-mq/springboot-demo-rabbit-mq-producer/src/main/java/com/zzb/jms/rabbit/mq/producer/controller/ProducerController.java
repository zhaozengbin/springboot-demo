package com.zzb.jms.rabbit.mq.producer.controller;

import cn.hutool.http.HttpUtil;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 类名称：ProducerController
 * 类描述：TODO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/3/24 7:02 下午
 * 修改备注：TODO
 */
@RestController
@AllArgsConstructor
public class ProducerController {


    private RabbitTemplate rabbit;

    private RabbitAdmin rabbitAdmin;


    @GetMapping("/queue_test")
    public String produce(String queueName, String message) {
        createMQIfNotExist(queueName, queueName);
        rabbit.convertAndSend(queueName, message);
        return "消息已经发送";
    }

    private void createMQIfNotExist(String queueName, String exchangeName) {
        //判断队列是否存在
        Properties properties = rabbitAdmin.getQueueProperties(queueName);
        if (properties == null) {
            Queue queue = new Queue(queueName, true, false, false, null);
            FanoutExchange fanoutExchange = new FanoutExchange(exchangeName);
            rabbitAdmin.declareQueue(queue);
            rabbitAdmin.declareExchange(fanoutExchange);
            rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(fanoutExchange));
            //新启动一个线程，通知消费者新增listener
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String res = callAddNewListener(queueName);
                    if (!StringUtils.isEmpty(res)) {
                        System.out.println("-->>调用创建新的 listener feign 失败");
                    }
                }
            }).start();

        }


    }

    private String callAddNewListener(String queueName) {
        String url = "http://localhost:8020/add_new_listener";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("queueName", queueName);
        try {
            HttpUtil.get(url, param);
        } catch (Exception e) {
            e.printStackTrace();
            return "调用添加listener feign失败";
        }
        return null;

    }

}
