package com.glodon.service;

import com.glodon.pojo.User;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
    //接收queue类型消息
    //destination对应配置类中ActiveMQQueue("springboot.queue")设置的名字
    @JmsListener(destination="springboot.queue")
    public void ListenQueue(String msg)
    {
        System.out.println("接收到queue消息：" + msg);
    }

    //接收user queue类型消息
    @JmsListener(destination="my-user-queue")
    public void ListenQueue(User user)
    {
        System.out.println("接收到user消息：" + user);
    }

    //接收topic类型消息
    //destination对应配置类中ActiveMQTopic("springboot.topic")设置的名字
    //containerFactory对应配置类中注册JmsListenerContainerFactory的bean名称
    @JmsListener(destination="springboot.topic", containerFactory = "jmsTopicListenerContainerFactory")
    public void ListenTopic(String msg){
        System.out.println("接收到topic消息：" + msg);
    }
}
