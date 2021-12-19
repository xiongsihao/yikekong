package com.yikekong.core;

import com.yikekong.emq.EmqClient;
import com.yikekong.entity.GPSEntity;
import com.yikekong.service.GpsService;
import com.yikekong.service.QuotaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
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
    @Autowired
    private QuotaService quotaService;
    @Autowired
    private GpsService gpsService;

    @PostConstruct
    public void init(){
        log.info("初始化方法，自动订阅主题");
        emqClient.connect();
        quotaService.getAllSubject().forEach(s -> {
            try {
                //以下写法单机部署EMQ没有问题，但是如果是集群部署 则所有节点都会受影响
                //emqClient.subscribe(s);

                //使用以下写法 共享订阅模式，只有一个节点受影响
                emqClient.subscribe("$queue/"+s);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });

        //----------------订阅gps主题数据-------------------
        GPSEntity gpsEntity = gpsService.getGps();
        if(gpsEntity == null) return;
        try {
            if(Strings.isNotEmpty(gpsEntity.getSubject())){  //如果主题不为空
                emqClient.subscribe("$queue/"+gpsEntity.getSubject());
            }
        } catch (MqttException e) {
            log.error("订阅主题出错：",e);
        }
    }
}
