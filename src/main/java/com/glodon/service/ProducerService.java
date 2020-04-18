package com.glodon.service;

import com.glodon.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Queue;
import javax.jms.Topic;

@Service
public class ProducerService {

    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    @Autowired
    private Queue queue;

    @Autowired
    private Topic topic;

    //发送queue类型消息
    public void sendQueueMsg(String msg)
    {
        System.out.println("发送queue类型消息【" + msg + "】");
        jmsTemplate.convertAndSend(queue, msg);
    }

    //发送topic类型消息
    public void sendTopicMsg(String msg)
    {
        System.out.println("发送topic类型消息【" + msg + "】");
        jmsTemplate.convertAndSend(topic, msg);
    }

    //发送user queue类型消息
    public void sendUser(User user)
    {
        System.out.println("发送user消息【" + user + "】");
        // 使用自定义地址发送对象
        jmsTemplate.convertAndSend("my-user-queue", user);
    }
}
