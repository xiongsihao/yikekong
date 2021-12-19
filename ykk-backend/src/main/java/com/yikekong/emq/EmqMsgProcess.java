package com.yikekong.emq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yikekong.dto.DeviceInfoDTO;
import com.yikekong.dto.DeviceLocation;
import com.yikekong.es.ESRepository;
import com.yikekong.service.AlarmService;
import com.yikekong.service.DeviceService;
import com.yikekong.service.GpsService;
import com.yikekong.service.QuotaService;
import com.yikekong.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

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

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private GpsService gpsService;

    @Autowired
    private ESRepository esRepository;

    //连接丢失时触发
    @Override
    public void connectionLost(Throwable throwable) {
        log.info("emq connect lost");
        //当连接丢失时再次连接emq
        emqClient.connect();
        //重新订阅所有主题
        quotaService.getAllSubject().forEach(s -> {
            try {
                emqClient.subscribe("$queue/" + s);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
    }

    //接收到消息时触发
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String payload = new String(mqttMessage.getPayload());
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> payloadMap = mapper.readValue(payload, Map.class);
        log.info("接收到数据：" + payloadMap);
        //解析数据
        DeviceInfoDTO deviceInfoDTO = quotaService.analysis(topic, payloadMap);
        if (deviceInfoDTO != null) {
            //告警判断
            deviceInfoDTO = alarmService.verifyDeviceInfo(deviceInfoDTO);  //返回包含了告警判断的对象
            //保存设备信息
            deviceService.saveDeviceInfo(deviceInfoDTO.getDevice());
            //保存指标数据
            quotaService.saveQuotaToInflux(deviceInfoDTO.getQuotaList());
        }

        //处理gps数据
        DeviceLocation deviceLocation = gpsService.analysis(topic, payloadMap);//解析
        if (deviceLocation != null) {
            log.info("gps解析结果：{}", JsonUtil.serialize(deviceLocation));
            esRepository.saveLocation(deviceLocation);
        }
    }

    //传送完成后触发
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

}
