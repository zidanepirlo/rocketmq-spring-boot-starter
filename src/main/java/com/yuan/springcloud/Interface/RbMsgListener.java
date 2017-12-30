package com.yuan.springcloud.Interface;

import com.yuan.springcloud.entity.ConsumerMsg;

import java.util.List;

public interface RbMsgListener {

    void consumeMessage(final List<ConsumerMsg> consumerMsgs);
}
