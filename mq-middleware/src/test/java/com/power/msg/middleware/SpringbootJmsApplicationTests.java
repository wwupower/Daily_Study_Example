package com.power.msg.middleware;

import javax.jms.Destination;

import com.power.msg.middleware.activemq.springboot.Producer;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootJmsApplicationTests {

    @Autowired
    private Producer producer;

    @Test
    public void contextLoads() throws InterruptedException {
        //消息队列Queue
        Destination destination = new ActiveMQQueue("springboot.demo.queue");
        for (int i = 0; i < 1; i++) {
            producer.sendMessage(destination, "hello word springboot.demo.queue");
        }
       /* Destination destination = new ActiveMQTopic("springboot.demo.topic");
        for (int i = 0; i < 10; i++) {
            producer.sendMessage(destination, "hello word !this is springboot.demo.topic");
        }*/
    }

}