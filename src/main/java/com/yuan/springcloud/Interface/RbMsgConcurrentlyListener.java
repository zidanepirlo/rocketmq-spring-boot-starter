package com.yuan.springcloud.Interface;

import com.yuan.springcloud.entity.ConsumerMsg;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;

import java.util.List;

public interface RbMsgConcurrentlyListener {

    void consumeMessage(final List<ConsumerMsg> consumerMsgs);
}
