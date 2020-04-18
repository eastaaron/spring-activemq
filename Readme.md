### ActiveMQ介绍


### 安装ActiveMQ

```
网站：http://activemq.apache.org
下载ActiveMQ 5.15.12：http://activemq.apache.org/components/classic/download
然后解压运行：D:\apache-activemq-5.15.12\bin\win64\activemq.bat
启动后activemq会启动两个端口：
8161是activemq的管理页面，默认的账号密码都是admin http://localhost:8161/admin
61616是程序连接activemq的通讯地址
```

### 引入依赖
推荐使用spring initializr，创建一个项目，https://start.spring.io

```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-activemq</artifactId>
        </dependency>
```

### 修改application.yml
可以把application.properties改成application.yml

```
spring:
  activemq:
    #ActiveMQ通讯地址
    broker-url: tcp://localhost:61616
    #用户名
    user: admin
    #密码
    password: admin
    #是否启用内存模式（就是不安装MQ，项目启动时同时启动一个MQ实例）
    in-memory: false
    packages:
      #信任所有的包
      trust-all: true
    pool:
      #是否替换默认的连接池，使用ActiveMQ的连接池需引入的依赖
      enabled: false
```

### 配置activeMQ

```
@Configuration
@EnableJms
public class ActiveMQConfig {
    //springboot默认只配置queue类型消息，如果要使用topic类型的消息，则需要配置该bean
    @Bean
    public JmsListenerContainerFactory jmsTopicListenerContainerFactory(ConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //这里必须设置为true，false则表示是queue类型
        factory.setPubSubDomain(true);
        return factory;
    }

    @Bean
    public Topic topic() {
        return new ActiveMQTopic("springboot.topic") ;
    }
}
```

### 创建消费者

```
@Service
public class ConsumerService {
    //接收topic类型消息
    //destination对应配置类中ActiveMQTopic("springboot.topic")设置的名字
    //containerFactory对应配置类中注册JmsListenerContainerFactory的bean名称
    @JmsListener(destination="springboot.topic", containerFactory = "jmsTopicListenerContainerFactory")
    public void ListenTopic(String msg){
        System.out.println("接收到topic消息：" + msg);
    }
}
```
### 创建生产者

```
@Service
public class ProducerService {

    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    @Autowired
    private Topic topic;

    //发送topic类型消息
    public void sendTopicMsg(String msg)
    {
        System.out.println("发送topic类型消息【" + msg + "】");
        jmsTemplate.convertAndSend(topic, msg);
    }
}
```

### RestController

```
@RestController
public class ActiveMQController {
    @Autowired
    private ProducerService service;

    //发送topic类型消息
    @GetMapping("/topic")
    public void sendTopicMsg(String msg)
    {
        service.sendTopicMsg(msg);
    }
}
```



### 启动程序测试

```
1. 浏览器中输入：http://localhost:8080/queue?msg=hello
   控制台输出：接收到queue消息：hello
2. 浏览器中输入：http://localhost:8080/topic?msg=hello
   控制台输出：接收到topic消息：hello
3. 浏览器中输入：http://localhost:8080/user?id=1&name=abc&note=test
   控制台输出：接收到user消息：com.glodon.pojo.User@72b2a56f

```

### 参考文章
* [springboot整合activeMQ](https://www.jianshu.com/p/0d6019f83d5f)
* [消息中间件之ActiveMQ](https://www.jianshu.com/p/cd8e037e11ff)
* [ActiveMQ入门以及整合spring boot](https://blog.csdn.net/qq_43652509/article/details/83926758)
* [分布式--ActiveMQ 消息中间件](https://www.jianshu.com/p/8b9bfe865e38)
* [ActiveMQ详细入门使用教程](https://blog.csdn.net/qq_33404395/article/details/80590113)
* [ActiveMQ集群整体认识](https://segmentfault.com/a/1190000014592517)
* [ActiveMQ集群搭建](https://www.cnblogs.com/arjenlee/p/9303229.html)