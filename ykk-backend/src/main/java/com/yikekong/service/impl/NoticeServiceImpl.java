package com.yikekong.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.yikekong.common.SystemDefinition;
import com.yikekong.config.WebHookConfig;
import com.yikekong.dto.DeviceLocation;
import com.yikekong.dto.QuotaDTO;
import com.yikekong.service.NoticeService;
import com.yikekong.util.HttpUtil;
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
    }
    @Override
    public void gpsTransfer(DeviceLocation deviceLocation) {
        if(!Strings.isNullOrEmpty( webHookConfig.getGps())){
            HttpUtil.httpPost( webHookConfig.getGps(), deviceLocation);
        }
    }

}
