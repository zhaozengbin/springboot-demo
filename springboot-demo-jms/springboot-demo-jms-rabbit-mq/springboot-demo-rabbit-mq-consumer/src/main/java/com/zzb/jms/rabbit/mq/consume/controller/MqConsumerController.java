package com.zzb.jms.rabbit.mq.consume.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.zzb.jms.rabbit.mq.consume.handler.QueueService;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类名称：MqConsumerController
 * 类描述：MQ消费者controller
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2021/3/24 6:31 下午
 * 修改备注：TODO
 */
@RestController
public class MqConsumerController {


    @Autowired
    private QueueService queueService;

    @GetMapping("add_new_listener")
    public String addNewListener(String queueName) {
        SimpleMessageListenerContainer container = SpringUtil.getBean(SimpleMessageListenerContainer.class);
        List<String> queueNameList = queueService.getMQJSONArray();
        int count = 0;
        while (!queueNameList.contains(queueName)) {
            queueNameList = queueService.getMQJSONArray();
            count++;
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (count >= 5) {
                return "动态添加监听失败";
            }
        }
        container.addQueueNames(queueName);
        long consumerCount = container.getActiveConsumerCount();
        return "修改成功:现有队列监听者[" + consumerCount + "]个";
    }
}
