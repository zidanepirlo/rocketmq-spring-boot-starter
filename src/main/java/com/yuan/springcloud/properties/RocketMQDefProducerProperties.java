package com.yuan.springcloud.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;


@ConfigurationProperties("rocketmq.producer.defProducer")
@Getter
@Setter
public class RocketMQDefProducerProperties {

    private String producerGroup;
    private String namesrvAddr;
    private String instanceName;
    private String topic;
    private String tags;
}
