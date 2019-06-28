package com.power.msg.middleware.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import javax.jms.*;


public class ProducerQueue {
    
    private static final Logger LOG = Logger.getLogger(ProducerQueue.class);
    
    public static void main(String[] args) {
        // 获取连接工厂
        ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, "tcp://127.0.0.1:61616");
        
        /* 获取连接 */
        Connection connection = null;
        try {
            connection = factory.createConnection();
            connection.start();
        } catch (JMSException e) {
            LOG.error("获取连接出现异常", e);
        }
        
        /* 创建会话 */
        Session session = null;
        try {
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            LOG.error("创建会话出现异常", e);
        }
        
        /* 创建消息生产者 */
        Destination destination = null;
        try {
            destination = session.createQueue("TestQueue");
        } catch (JMSException e) {
            LOG.error("创建队列出现异常", e);
        }
        
        /* 创建队列 */
        MessageProducer producer = null;
        try {
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        } catch (JMSException e) {
            LOG.error("创建消息生产者出现异常", e);
        }
        
        /* 发送消息 */
        ObjectMessage message = null;
        try {
            message = session.createObjectMessage("hello world... activemq ppp");
            producer.send(message);
        } catch (JMSException e) {
            LOG.error("发送消息出现异常", e);
        }
        
        try {
            session.commit();
        } catch (JMSException e) {
            LOG.error("提交会话出现异常", e);
        }
        
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                LOG.error("关闭连接出现异常", e);
            }
        }
        
        LOG.info("sent...");
    }

}