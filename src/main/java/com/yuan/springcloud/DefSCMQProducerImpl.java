package com.yuan.springcloud;

import org.apache.log4j.Logger;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;


public class DefSCMQProducerImpl implements DefSCMQProducer
//        ,InitializingBean,DisposableBean
{

    protected final Logger logger = Logger.getLogger(getClass());

    private RocketMQProducerProperties rocketMQProducerProperties;

    private DefaultMQProducer producer;


    public DefSCMQProducerImpl(RocketMQProducerProperties rocketMQProducerProperties) {
        this.rocketMQProducerProperties = rocketMQProducerProperties;
    }

    public SendResult sendMsg(String key, String msg) throws Exception{

        Message message = new Message(rocketMQProducerProperties.getTopic(),
                rocketMQProducerProperties.getTags(),
                key,
                msg.getBytes()
        );
        return producer.send(message);
    }

    public SendResult sendMsg(String msg) throws Exception {
        Message message = new Message(rocketMQProducerProperties.getTopic(),
                rocketMQProducerProperties.getTags(),
                msg.getBytes()
        );
        return producer.send(message);
    }

    @PreDestroy
    public void destroy() throws Exception {
        producer.shutdown();
        logger.info("DefSCMQProducerImpl destroy");
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        logger.info("DefSCMQProducerImpl init");
        producer = this.getProducer();
        if (null == producer)
            throw new Exception("DefaultMQProducer init fail!");
        producer.start();
        logger.info("DefSCMQProducerImpl init end");
    }

    @Bean
    @Singleton
    private DefaultMQProducer getProducer(){

        DefaultMQProducer producer = new DefaultMQProducer(rocketMQProducerProperties.getProducerGroup());
        producer.setNamesrvAddr(rocketMQProducerProperties.getNamesrvAddr());
        producer.setInstanceName(rocketMQProducerProperties.getInstanceName());
        return producer;
    }


}
