package com.yuan.springcloud.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;


@ConfigurationProperties("rocketmq.consumer.defPushConsumer")
@Getter
@Setter
public class RocketMQDefPushConsumerProperties {

    private String consumerGroup;
    private String namesrvAddr;
    private String instanceName;
    private String topic;
    private String tags;
    private String consumeMessageBatchMaxSize;
    private String maxReconsumeTimes;
    private String messageModel;
    private String listenerType;
}
