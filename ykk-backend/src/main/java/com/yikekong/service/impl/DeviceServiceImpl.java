package com.yikekong.service.impl;
import com.google.common.collect.Lists;
import com.yikekong.common.SystemDefinition;
import com.yikekong.dto.DeviceDTO;
import com.yikekong.dto.QuotaInfo;
import com.yikekong.es.ESRepository;
import com.yikekong.service.DeviceService;
import com.yikekong.service.QuotaService;
import com.yikekong.vo.DeviceQuotaVO;
import com.yikekong.vo.Pager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService{

    @Autowired
    private ESRepository esRepository;
    @Autowired
    private QuotaService quotaService;
    @Autowired
    private RedisTemplate redisTemplate;

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
        boolean b = esRepository.updateStatus(deviceId, status);
        deviceDTO.setStatus(status);
        refreshDevice(deviceDTO);
        return b;
    }

    /**
     * 根据设备id查询设备
     * @param deviceId
     * @return
     */
    private DeviceDTO findDevice(String deviceId){
        DeviceDTO deviceDTO = (DeviceDTO)redisTemplate.boundHashOps(SystemDefinition.DEVICE_KEY).get(deviceId);
        if(null == deviceDTO){
            deviceDTO = esRepository.searchDeviceById(deviceId);
            refreshDevice(deviceDTO);
        }
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
        refreshDevice(deviceDTO);//刷新到缓存
        return true;
    }

    /**
     * 更新
     * @param deviceId
     * @param online
     */
    @Override
    public void updateOnline(String deviceId, Boolean online){
        //以webclient开头的client为系统前端,monitor开头的是亿可控服务端
        if(deviceId.startsWith("webclient") || deviceId.startsWith("monitor")){
            return;
        }
        //更新数据到es
        DeviceDTO deviceDTO = findDevice(deviceId);
        if(deviceDTO == null) return;

        deviceDTO.setOnline(online);
        esRepository.updateOnline(deviceId,online);
        deviceDTO.setOnline(online);
        refreshDevice(deviceDTO);
    }

    /**
     * 查询设备详情
     *
     * @return
     */
    @Override
    public Pager<DeviceQuotaVO> queryDeviceQuota(Long page, Long pageSize, String deviceId, String tag, Integer state) {
        //1.查询设备列表
        Pager<DeviceDTO> pager = esRepository.searchDevice(page, pageSize, deviceId, tag, state);

        //2.查询指标列表
        List<DeviceQuotaVO> deviceQuotaVOList= Lists.newArrayList();
        pager.getItems().forEach(deviceDTO -> {
            DeviceQuotaVO deviceQuotaVO=new DeviceQuotaVO();
            BeanUtils.copyProperties(deviceDTO, deviceQuotaVO );
            //查询指标
            List<QuotaInfo> quotaList = quotaService.getLastQuotaList(deviceDTO.getDeviceId());
            deviceQuotaVO.setQuotaList(quotaList);
            deviceQuotaVOList.add(deviceQuotaVO);
        });

        //3.封装返回结果
        Pager<DeviceQuotaVO> pageResult=new Pager(pager.getCounts(),pageSize);
        pageResult.setItems(deviceQuotaVOList);

        return pageResult;
    }

    /**
     * 刷新缓存
     * @param deviceDTO
     */
    private void refreshDevice(DeviceDTO deviceDTO) {
        if(deviceDTO == null) return;
        redisTemplate.boundHashOps(SystemDefinition.DEVICE_KEY).put(deviceDTO.getDeviceId(),deviceDTO );
    }
}
