package com.yuan.springcloud.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConsumerMsg {

    private String topic;
    private String tags;
    private String body;

    public ConsumerMsg(String topic, String tags, String body) {
        this.topic = topic;
        this.tags = tags;
        this.body = body;
    }
}
