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
            //第一个参数:是否支持事务，如果为true，则会忽略第二个参数，被jms服务器设置为SESSION_TRANSACTED
	        //第二个参数为false时，paramB的值可为Session.AUTO_ACKNOWLEDGE，Session.CLIENT_ACKNOWLEDGE，DUPS_OK_ACKNOWLEDGE其中一个。
            //Session.AUTO_ACKNOWLEDGE 为自动确认，客户端发送和接收消息不需要做额外的工作。哪怕是接收端发生异常，也会被当作正常发送成功。
            //Session.CLIENT_ACKNOWLEDGE 客户端确认,客户端接收到消息后，必须调用javax.jms.Message的acknowledge方法。jms服务器才会当作发送成功，并删除消息。
            //Session.DUPS_OK_ACKNOWLEDGE 为自动确认，客户端发送和接收消息不需要做额外的工作。哪怕是接收端发生异常，也会被当作正常发送成功。
            session = connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);
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