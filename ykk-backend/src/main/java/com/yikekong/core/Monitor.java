package com.yikekong.core;

import com.yikekong.emq.EmqClient;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author : xsh
 * @create : 2021-10-08 - 0:57
 * @describe: 自动监控 启动后自动订阅主题
 */
@Component
@Slf4j
public class Monitor{

    @Autowired
    private EmqClient emqClient;

    @PostConstruct
    public void init(){
        log.info("初始化方法，自动订阅主题");
        emqClient.connect();
        try {
            emqClient.subscribe("mytopic");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
