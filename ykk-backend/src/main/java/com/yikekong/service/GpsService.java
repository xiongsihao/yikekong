package com.yikekong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yikekong.dto.DeviceFullInfo;
import com.yikekong.dto.DeviceLocation;
import com.yikekong.entity.GPSEntity;

import java.util.List;
import java.util.Map;

public interface GpsService extends IService<GPSEntity>{

    /**
     * 修改gps指标
     * @param gpsEntity
     * @return
     */
    boolean update(GPSEntity gpsEntity);

    GPSEntity getGps();

    /**
     * 解析报文获得GPS信息
     * @param payloadMap 报文内容
     * @return gps
     */
    DeviceLocation analysis(String topic, Map<String, Object> payloadMap);


    /**
     * 根据经纬度获取一定范围内的设备信息
     * @param lat
     * @param lon
     * @param distance
     * @return
     */
    List<DeviceFullInfo> getDeviceFullInfo(Double lat, Double lon, Integer distance);

}
