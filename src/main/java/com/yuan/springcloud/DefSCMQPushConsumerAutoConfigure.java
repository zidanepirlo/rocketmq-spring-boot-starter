package com.yuan.springcloud;


import com.yuan.springcloud.Interface.DefSCMQPushConsumerConcurrently;
import com.yuan.springcloud.Interface.Impl.DefSCMQPushConsumerConcurrentlyImpl;
import com.yuan.springcloud.properties.RocketMQDefPushConsumerProperties;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnClass(DefSCMQPushConsumerConcurrently.class)
//@ConditionalOnClass({ DefSCMQProducer.class,
//        DefSCMQPushConsumerConcurrently.class })
@EnableConfigurationProperties(RocketMQDefPushConsumerProperties.class)
@ConditionalOnProperty(prefix = "rocketmq.consumer.defPushConsumer",value = "enabled",havingValue = "true")

public class DefSCMQPushConsumerAutoConfigure {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private RocketMQDefPushConsumerProperties rocketMQDefPushConsumerProperties;

    @Bean
    @ConditionalOnMissingBean
    DefSCMQPushConsumerConcurrently defSCMQPushConsumerConcurrently(){
        return new DefSCMQPushConsumerConcurrentlyImpl(rocketMQDefPushConsumerProperties);
    }
}
