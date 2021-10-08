package com.yikekong.emq;

import com.yikekong.service.QuotaService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : xsh
 * @create : 2021-10-08 - 0:53
 * @describe: EMQ接收消息回调类
 */
@Component
@Slf4j
public class EmqMsgProcess implements MqttCallback {

    @Autowired
    private QuotaService quotaService;

    @Autowired
    private EmqClient emqClient;

    //连接丢失时触发
    @Override
    public void connectionLost(Throwable throwable) {
        log.info("emq connect lost");
        //当连接丢失时再次连接emq
        emqClient.connect();
        //重新订阅所有主题
        quotaService.getAllSubject().forEach(s -> {
            try {
                emqClient.subscribe("$queue/"+s);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
    }

    //接收到消息时触发
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String payload = new String(mqttMessage.getPayload());
        log.info("接收到数据："+payload);
    }

    //传送完成后触发
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

}