package com.yuan.springcloud.Interface.Impl;

import com.yuan.springcloud.Interface.DefSCMQPushConsumerConcurrently;
import com.yuan.springcloud.Interface.RbMsgConcurrentlyListener;
import com.yuan.springcloud.Interface.RbMsgListener;
import com.yuan.springcloud.entity.ConsumerMsg;
import com.yuan.springcloud.enums.EListenerType;
import com.yuan.springcloud.properties.RocketMQDefPushConsumerProperties;
import org.apache.log4j.Logger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

public class DefSCMQPushConsumerConcurrentlyImpl implements DefSCMQPushConsumerConcurrently{

    protected final Logger logger = Logger.getLogger(getClass());

    private RocketMQDefPushConsumerProperties rocketMQDefPushConsumerProperties;

    private DefaultMQPushConsumer consumer;

    private RbMsgConcurrentlyListener RbMsgConcurrentlyListener;

    public DefSCMQPushConsumerConcurrentlyImpl(RocketMQDefPushConsumerProperties rocketMQDefPushConsumerProperties) {
        this.rocketMQDefPushConsumerProperties = rocketMQDefPushConsumerProperties;
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        logger.info("DefSCMQPushConsumerConcurrentlyImpl init");
        this.consumer = getConsumer();
        logger.info("DefSCMQPushConsumerConcurrentlyImpl init end");
    }

    @PreDestroy
    public void destroy() throws Exception {

        if (consumer != null){
            consumer.shutdown();
            logger.info("DefaultMQPushConsumer shutdown");
        }
    }

    @Bean
    @Singleton
    private DefaultMQPushConsumer getConsumer() throws MQClientException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(rocketMQDefPushConsumerProperties.getConsumerGroup());
        consumer.setNamesrvAddr(rocketMQDefPushConsumerProperties.getNamesrvAddr());
        consumer.setInstanceName(rocketMQDefPushConsumerProperties.getInstanceName());
        consumer.subscribe(rocketMQDefPushConsumerProperties.getTopic(), rocketMQDefPushConsumerProperties.getTags());
        consumer.setConsumeMessageBatchMaxSize(Integer.parseInt(rocketMQDefPushConsumerProperties.getConsumeMessageBatchMaxSize()));
        consumer.setMaxReconsumeTimes(Integer.parseInt(rocketMQDefPushConsumerProperties.getMaxReconsumeTimes()));

        MessageModel mm = null;

        for (MessageModel messageModel:MessageModel.values()){
            if (messageModel.getModeCN().equalsIgnoreCase(rocketMQDefPushConsumerProperties.getMessageModel())){
                mm = messageModel;
                break;
            }
        }
        if (null == mm) {
            throw new MQClientException("DefaultMQPushConsumer MessageModel not define!",null);
        }
        consumer.setMessageModel(mm);
        return consumer;
    }

    public void startup(final RbMsgListener rbMsgListener) {

        if (StringUtils.isEmpty(rocketMQDefPushConsumerProperties.getListenerType())
                || EListenerType.getByDesc(rocketMQDefPushConsumerProperties.getListenerType()) == EListenerType.CONCURRENTLY) {

            consumer.registerMessageListener(new MessageListenerConcurrently() {
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

                    try {
                        List<ConsumerMsg> consumerMsgs = new ArrayList<ConsumerMsg>();
                        for (MessageExt messageExt : msgs) {
                            consumerMsgs.add(new ConsumerMsg(messageExt.getTopic(), messageExt.getTags(), new String(messageExt.getBody())));
                        }
                        if (consumerMsgs != null || consumerMsgs.size() > 0) {
                            rbMsgListener.consumeMessage(consumerMsgs);
                        }
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }

                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
        } else if (EListenerType.getByDesc(rocketMQDefPushConsumerProperties.getListenerType()) == EListenerType.ORDERLY) {
            consumer.registerMessageListener(new MessageListenerOrderly(){
                public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {

                    try {
                        List<ConsumerMsg> consumerMsgs = new ArrayList<ConsumerMsg>();
                        for (MessageExt messageExt : msgs) {
                            consumerMsgs.add(new ConsumerMsg(messageExt.getTopic(), messageExt.getTags(), new String(messageExt.getBody())));
                        }
                        if (consumerMsgs != null || consumerMsgs.size() > 0) {
                            rbMsgListener.consumeMessage(consumerMsgs);
                        }
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                        return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                    }
                    return ConsumeOrderlyStatus.SUCCESS;
                }
            });
        } else {
            logger.error("DefaultMQPushConsumer init failed, not set Listener");
            System.exit(0);
        }

        try {
            consumer.start();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            System.exit(0);
        }
        logger.info("DefaultMQPushConsumer start");
    }
}
