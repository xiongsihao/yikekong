package com.yikekong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.yikekong.dto.DeviceDTO;
import com.yikekong.dto.DeviceFullInfo;
import com.yikekong.dto.DeviceLocation;
import com.yikekong.dto.QuotaInfo;
import com.yikekong.emq.EmqClient;
import com.yikekong.entity.GPSEntity;
import com.yikekong.es.ESRepository;
import com.yikekong.mapper.GpsMapper;
import com.yikekong.service.GpsService;
import com.yikekong.service.QuotaService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GpsServiceImpl extends ServiceImpl<GpsMapper, GPSEntity> implements GpsService {

    @Autowired
    private EmqClient emqClient;
    @Autowired
    private ESRepository esRepository;
    @Autowired
    private QuotaService quotaService;

    @Override
    public boolean update(GPSEntity gpsEntity) {
        //订阅主题
        try {
            emqClient.subscribe("$queue/" + gpsEntity.getSubject());
        } catch (MqttException e) {
            log.error("subscribe error", e);
        }
        gpsEntity.setId(1);
        return this.updateById(gpsEntity);
    }


    @Override
    public GPSEntity getGps() {
        return this.getById(1);
    }

    @Override
    public DeviceLocation analysis(String topic, Map<String, Object> payloadMap) {
        //读取规则
        GPSEntity gpsEntity = getGps();
        if (gpsEntity == null) return null;
        if (Strings.isNullOrEmpty(gpsEntity.getSubject())) return null;// 主题为空
        if (!topic.equals(gpsEntity.getSubject())) return null;//如果主题不匹配

        //读取设备id
        String deviceId = "";
        deviceId = (String) payloadMap.get(gpsEntity.getSnKey());
        if (Strings.isNullOrEmpty(deviceId)) return null;

        //提取gps
        String location = "";
        if (gpsEntity.getSingleField()) { //
            String gps = (String) payloadMap.get(gpsEntity.getValueKey());
            String[] split = gps.split(gpsEntity.getSeparation());
            if(split.length == 2){
                location=split[1]+","+split[0];//维度，经度
            }
        } else { //如果是双字段

            String lat = (String) payloadMap.get(gpsEntity.getLatitude());
            String lon = (String) payloadMap.get(gpsEntity.getLongitude());
            if(!Strings.isNullOrEmpty(lat)&&!Strings.isNullOrEmpty(lon)){
                location=lat+","+lon;
            }
        }

        //封装返回结果
        if (location != null) {
            DeviceLocation deviceLocation = new DeviceLocation();
            deviceLocation.setDeviceId(deviceId);
            deviceLocation.setLocation(location);
            return deviceLocation;
        } else {
            return null;
        }
    }


    @Override
    public List<DeviceFullInfo> getDeviceFullInfo(Double lat, Double lon, Integer distance) {
        //按范围查询设备
        List<DeviceLocation> deviceLocationList = esRepository.searchDeviceLocation(lat, lon, distance);

        List<DeviceFullInfo> deviceFullInfoList= Lists.newArrayList();

        //查询设备详情
        deviceLocationList.forEach( deviceLocation -> {
            DeviceFullInfo deviceFullInfo=new DeviceFullInfo();
            deviceFullInfo.setDeviceId(deviceLocation.getDeviceId());//设备id
            deviceFullInfo.setLocation(deviceLocation.getLocation());//坐标

            //在线状态和告警状态

            DeviceDTO deviceDTO = esRepository.searchDeviceById(deviceLocation.getDeviceId());
            if(deviceDTO==null){
                deviceFullInfo.setOnline(false);
                deviceFullInfo.setAlarm(false);
            }else{
                deviceFullInfo.setOnline( deviceDTO.getOnline() );
                deviceFullInfo.setAlarm(deviceDTO.getAlarm());
            }

            //指标
            List<QuotaInfo> quotaList = quotaService.getLastQuotaList(deviceLocation.getDeviceId());
            deviceFullInfo.setQuotaList( quotaList );

            deviceFullInfoList.add(deviceFullInfo);
        } );

        return deviceFullInfoList;
    }
}
