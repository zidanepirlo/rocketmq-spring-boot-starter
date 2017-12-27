package com.yuan.springcloud;


import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;


@ConfigurationProperties("rocketmq.producer")
@Getter
@Setter
public class RocketMQProducerProperties {

    private String producerGroup;
    private String namesrvAddr;
    private String instanceName;
    private String topic;
    private String tags;
}
