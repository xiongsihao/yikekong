package com.yikekong.service;


public interface DeviceService  {


    /**
     * 更改设备状态
     * @param deviceId
     * @param status
     * @return
     */
    boolean setStatus(String deviceId, Boolean status);
}
