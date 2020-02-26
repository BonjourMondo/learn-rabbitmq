package com.bonjour.rabbit.producer.component;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitSenderTest {
    @Autowired
    private RabbitSender rabbitSender;

    @Test
    public void send() throws InterruptedException {
        Map<String,Object> properties=new HashMap<>();
        properties.put("attr1",12345);
        properties.put("attr2","abcde");

        rabbitSender.send("hello rabbitmq!",properties);

        Thread.sleep(1000);
    }
}
