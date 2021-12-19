package com.yikekong.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.yikekong.common.SystemDefinition;
import com.yikekong.config.WebHookConfig;
import com.yikekong.dto.AlarmMsg;
import com.yikekong.dto.DeviceLocation;
import com.yikekong.dto.QuotaDTO;
import com.yikekong.emq.EmqClient;
import com.yikekong.service.NoticeService;
import com.yikekong.util.HttpUtil;
import com.yikekong.util.JsonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author : xsh
 * @create : 2021-12-19 - 22:07
 * @describe:
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private WebHookConfig webHookConfig;
    @Autowired
    private EmqClient emqClient;

    @Override
    public void quotaTransfer(List<QuotaDTO> quotaDTOList) {
        for (QuotaDTO quotaDTO : quotaDTOList) {
            if (!Strings.isNullOrEmpty(quotaDTO.getWebhook())) {  //如果钩子非空，则做数据透传
                HttpUtil.httpPost(quotaDTO.getWebhook(), quotaDTO);
            }
            //告警透传
            //告警有一个沉默周期的设置。例如沉默周期设置为5分钟。那么5分钟内不会为同一个指标的同一种告警进行重复告警，超过5分钟才会再次告警。这里使用redis实现
            if ("1".equals(quotaDTO.getAlarm()) && !Strings.isNullOrEmpty(quotaDTO.getAlarmWebHook())) {
                // key: XXXXX_设备id_告警名称
                String key = SystemDefinition.CYCLE_KEY + "_" + quotaDTO.getDeviceId() + "_" + quotaDTO.getAlarmName();
                if (redisTemplate.boundValueOps(key).get() == null) {
                    HttpUtil.httpPost(quotaDTO.getAlarmWebHook(), quotaDTO);
                    redisTemplate.boundValueOps(key).set(quotaDTO.getStringValue(), quotaDTO.getCycle(), TimeUnit.MINUTES);
                }
            }
            sendAlarm( quotaDTO );  //报警推送前端
        }
    }

    @Override
    public void onlineTransfer(String deviceId, Boolean online) {
        if(!Strings.isNullOrEmpty( webHookConfig.getOnline())){
            Map<String,Object> map= Maps.newHashMap();
            map.put("deviceId",deviceId);
            map.put("online",online);
            HttpUtil.httpPost(webHookConfig.getOnline() , map);
        }
        if(!online){
            this.disconnectionAlarm(deviceId);
        }
    }
    @Override
    public void gpsTransfer(DeviceLocation deviceLocation) {
        if(!Strings.isNullOrEmpty( webHookConfig.getGps())){
            HttpUtil.httpPost( webHookConfig.getGps(), deviceLocation);
        }
    }

    /**
     * 告警前端推送
     * @param quotaDTO
     */
    private void sendAlarm(QuotaDTO quotaDTO){
        if( !"1".equals( quotaDTO.getAlarm()  )){
            //不是告警 直接return不处理
            return;
        }
        AlarmMsg alarmMsg=new AlarmMsg();
        BeanUtils.copyProperties( quotaDTO,alarmMsg );
        alarmMsg.setLevel( Integer.parseInt(quotaDTO.getLevel() ) );
        alarmMsg.setOnline(true);

        //发送到emq
        try {
            emqClient.publish("/device/alarm", JsonUtil.serialize(alarmMsg)   );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断网前段推送
     * @param deviceId
     */
    public void disconnectionAlarm(String deviceId) {
        //以web开头的client为系统前端,monitor开头的是亿可控服务端；其它的才是设备，此处仅监控设备断网
        if(deviceId.startsWith("webclient") || deviceId.startsWith("monitor")){
            return;
        }
        AlarmMsg alarmMsg = new AlarmMsg();
        alarmMsg.setLevel(1);
        alarmMsg.setAlarmName("设备断网");
        alarmMsg.setDeviceId(deviceId);
        alarmMsg.setOnline(false);
        try {
            emqClient.publish("/device/alarm", JsonUtil.serialize(alarmMsg)   );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
