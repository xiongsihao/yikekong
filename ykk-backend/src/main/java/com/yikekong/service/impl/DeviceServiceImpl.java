package com.yikekong.service.impl;
import com.yikekong.dto.DeviceDTO;
import com.yikekong.es.ESRepository;
import com.yikekong.service.DeviceService;
import com.yikekong.vo.Pager;
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

    @Override
    public boolean updateTags(String deviceId, String tags) {
        DeviceDTO deviceStatus = findDevice(deviceId);
        if(deviceStatus == null) return false;
        esRepository.updateDeviceTag(deviceId,tags);
        return true;
    }

    @Override
    public Pager<DeviceDTO> queryPage(Long page, Long pageSize, String sn, String tag, Integer status) {
        return  esRepository.searchDevice(page,pageSize,sn,tag,status);
    }

    @Override
    public boolean saveDeviceInfo(DeviceDTO deviceDTO) {
        //查询设备 ，判断开关状态 ，如果是关闭则不处理
        DeviceDTO device= findDevice(deviceDTO.getDeviceId());
        if( device!=null && !device.getStatus() ) return false;

        // 如果当前设备查不到，新增
        if(device==null){
            esRepository.addDevices( deviceDTO );
        }else{
            //如果可以查询到，更新告警信息
            esRepository.updateDevicesAlarm(deviceDTO);
        }
        return true;
    }
}
