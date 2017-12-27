package com.yuan.springcloud;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(DefSCMQProducer.class)
@EnableConfigurationProperties(RocketMQProducerProperties.class)
@ConditionalOnProperty(prefix = "rocketmq.producer",value = "enabled",havingValue = "true")
public class DefSCMQProducerAutoConfigure {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private RocketMQProducerProperties rocketMQProducerProperties;

    @Bean
    @ConditionalOnMissingBean
//    @ConditionalOnProperty(prefix = "rocketmq.producer",value = "enabled",havingValue = "true")
    DefSCMQProducer defSCMQProducer(){
        return new DefSCMQProducerImpl(rocketMQProducerProperties);
    }

}
