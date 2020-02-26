package com.bonjour.rabbit.producer.component;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * Author: leesanghyuk
 * Date: 2020-02-26 15:43
 * Description:
 */
@Component
public class RabbitSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //确认消息ack的回调监听接口，用于确定消息是否发送到broker
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        /**
         * @param correlationData 消息的唯一标识
         * @param b 消息是否到达broker
         * @param s 如果失败的异常信息
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean b, String s) {
            System.err.println("消息ACK:"+b+","+correlationData);
        }
    };

    /**
     * 发送消息的方法
     *
     * @param message    具体内容
     * @param properties 额外附加的属性
     */
    public void send(Object message, Map<String, Object> properties) {
        MessageHeaders messageHeaders = new MessageHeaders(properties);

        Message msg = MessageBuilder.createMessage(message, messageHeaders);

        rabbitTemplate.setConfirmCallback(confirmCallback);
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        MessagePostProcessor messagePostProcessor=new MessagePostProcessor() {
            @Override
            public org.springframework.amqp.core.Message postProcessMessage(org.springframework.amqp.core.Message message) throws AmqpException {
                System.err.println("-->post to do "+message);
                return message;
            }
        };
        rabbitTemplate.convertAndSend("amq.topic", "bizname.routingkey.part1",
                msg, messagePostProcessor,correlationData);
    }

}
