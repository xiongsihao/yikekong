package com.yikekong.emq;

import com.yikekong.config.EmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author : xsh
 * @create : 2021-10-08 - 0:07
 * @describe: EMQ即Eclipse paho,是eclipse基金会下面的一个开源项目，基于MQTT协议的客户端
 */
@Component
@Slf4j
public class EmqClient{
    @Autowired
    private EmqConfig emqConfig;//emq配置

    private MqttClient mqttClient;

    @Autowired
    private EmqMsgProcess emqMsgProcess;

    /**
     * 连接mqtt broker
     */
    public void connect(){
        try {
            mqttClient = new MqttClient(
                    emqConfig.getMqttServerUrl(),"monitor."+ UUID.randomUUID().toString());
            //连接时指定接收消息的回调类
            mqttClient.setCallback(emqMsgProcess);
            mqttClient.connect();
        } catch (MqttException e) {
            log.error("mqtt creat error",e);
        }
    }

    /**
     * 发布消息
     * @param topic 消息主题
     * @param msg 发送的消息
     */
    public void publish(String topic,String msg){
        try {
            MqttMessage mqttMessage = new MqttMessage(msg.getBytes());
            mqttClient.getTopic(topic).publish(mqttMessage);//向某主题发送消息
        } catch (MqttException e) {
            log.error("mqtt publish msg error",e);
        }
    }

    /**
     * 订阅主题
     * @param topicName
     * @throws MqttException
     */
    public void subscribe(String topicName) throws MqttException {
        mqttClient.subscribe(topicName);
    }
}
