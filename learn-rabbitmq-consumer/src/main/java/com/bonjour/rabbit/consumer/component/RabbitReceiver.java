package com.bonjour.rabbit.consumer.component;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Author: leesanghyuk
 * Date: 2020-02-26 16:04
 * Description:
 */
@Component
public class RabbitReceiver {
    /**
     * 组合使用监听
     * @param message
     * @param channel
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "test-queue-1",
                    durable = "${rabbitmq.bizname.exchange.durable}"),
            exchange=@Exchange(name = "${rabbitmq.bizname.exchange.name}",
                    durable = "${rabbitmq.bizname.exchange.durable}",
                    type = "${rabbitmq.bizname.exchange.type}",
                    ignoreDeclarationExceptions = "true"),
            key = "${rabbitmq.bizname.exchange.routingkey}"
    ))
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws IOException {
        //收到消息后，进行业务端消费处理
        System.err.println("----------消费 消息-------------");
        System.err.println("消息内容："+message.getPayload());

        //成功处理后获取deliveryTag，进行手工的ACK操作，因为我们配置的是手工签收
        Long deliveryTag=(Long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag,false);

    }
}
