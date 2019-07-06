package com.power.msg.middleware.activemq.springboot;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class Consumer2 {


    // 使用JmsListener配置消费者监听的队列，其中text是接收到的消息
    @JmsListener(destination = "springboot.demo.topic")
    // @SendTo("out.queue") 为了实现双向队列
    public void receiveTopic(String text) {
        if (!StringUtils.isEmpty(text)) {
            System.out.println("Consumer2收到的报文为:" + text);
            System.out.println("把报警信息[" + text + "]发送邮件给xxx");
            System.out.println("把报警信息[" + text + "]发送短信给xxx");
            System.out.println("");
        }
    }



}