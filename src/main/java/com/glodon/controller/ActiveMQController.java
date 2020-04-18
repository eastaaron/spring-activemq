package com.glodon.controller;

import com.glodon.pojo.User;
import com.glodon.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ActiveMQController {
    @Autowired
    private ProducerService service;

    //发送queue类型消息
    @GetMapping("/queue")
    @ResponseBody
    public Map<String, Object> sendQueueMsg(String msg){
        service.sendQueueMsg(msg);
        return result(true, msg);
    }

    // 测试User对象的发送
    @ResponseBody
    @GetMapping("/user")
    public Map<String, Object> sendUser(Long id,String userName, String note)
    {
        User user = new User(id, userName, note);
        service.sendUser(user);
        return result(true, user);
    }

    //发送topic类型消息
    @GetMapping("/topic")
    public void sendTopicMsg(String msg)
    {
        service.sendTopicMsg(msg);
    }

    private Map<String, Object> result(Boolean success, Object message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", message);
        return result;
    }
}