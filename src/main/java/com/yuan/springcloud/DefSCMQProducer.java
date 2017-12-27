package com.yuan.springcloud;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

public interface DefSCMQProducer {

    SendResult sendMsg(String key,String msg) throws Exception;
    SendResult sendMsg(String msg) throws Exception;
}
