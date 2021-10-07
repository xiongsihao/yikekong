package com.yikekong.service.impl;
import com.yikekong.dto.DeviceDTO;
import com.yikekong.es.ESRepository;
import com.yikekong.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService{

    @Autowired
    private ESRepository esRepository;

    /**
     * 更改设备状态
     *
     * @param deviceId
     * @param status
     * @return
     */
    @Override
    public boolean setStatus(String deviceId, Boolean status) {
        DeviceDTO deviceDTO = findDevice(deviceId);
        if( deviceDTO==null ) return false;
        return esRepository.updateStatus(deviceId,status);
    }

    /**
     * 根据设备id查询设备
     * @param deviceId
     * @return
     */
    private DeviceDTO findDevice(String deviceId){
        DeviceDTO deviceDTO = esRepository.searchDeviceById(deviceId);
        return deviceDTO;
    }
}
