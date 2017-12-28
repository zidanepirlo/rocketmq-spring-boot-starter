package com.yuan.springcloud.Interface;

import com.yuan.springcloud.entity.ConsumerMsg;
import org.apache.rocketmq.client.exception.MQClientException;

import java.util.List;

public interface DefSCMQPushConsumerConcurrently {

    void startup(final RbMsgConcurrentlyListener rbMsgConcurrentlyListener);
}
