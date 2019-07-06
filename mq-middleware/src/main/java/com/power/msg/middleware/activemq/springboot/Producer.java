package com.power.msg.middleware.activemq.springboot;
  
import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
  
@Service("producer")  
public class Producer {

    // 也可以注入JmsTemplate，JmsMessagingTemplate对JmsTemplate进行了封装
    @Autowired
    private JmsMessagingTemplate jmsTemplate;

  /*  @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;*/


    //    @Scheduled(fixedDelay=5000) // 5s执行一次   只有无参的方法才能用该注解
    public void sendMessage(Destination destination, String message){
//        jmsTemplate.convertAndSend(destinationName, payload, messagePostProcessor);
        this.jmsTemplate.convertAndSend(destination, message);
    }




// 双向队列
       // @JmsListener(destination="out.queue") 
       // public void consumerMessage(String text){ 
       // System.out.println("从out.queue队列收到的回复报文为:"+text); 
       // }
}