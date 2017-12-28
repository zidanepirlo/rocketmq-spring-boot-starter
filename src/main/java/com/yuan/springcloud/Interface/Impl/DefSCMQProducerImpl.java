package com.yuan.springcloud.Interface.Impl;

import com.yuan.springcloud.Interface.DefSCMQProducer;
import com.yuan.springcloud.properties.RocketMQDefProducerProperties;
import org.apache.log4j.Logger;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;


public class DefSCMQProducerImpl implements DefSCMQProducer {

    protected final Logger logger = Logger.getLogger(getClass());

    private RocketMQDefProducerProperties rocketMQProducerProperties;

    private DefaultMQProducer producer;


    public DefSCMQProducerImpl(RocketMQDefProducerProperties rocketMQProducerProperties) {
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
