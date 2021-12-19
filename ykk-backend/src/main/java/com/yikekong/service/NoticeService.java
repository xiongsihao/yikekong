package com.yikekong.service;

import com.yikekong.dto.DeviceLocation;
import com.yikekong.dto.QuotaDTO;

import java.util.List;

/**
 * @author : xsh
 * @create : 2021-12-19 - 22:07
 * @describe: 通知(透传)服务
 */
public interface NoticeService {

    /**
     * 指标数据透传
     * @param quotaDTOList
     */
    void quotaTransfer(List<QuotaDTO> quotaDTOList);

    /**
     * 断连透传
     * @param deviceId
     * @param online
     */
    void onlineTransfer(String deviceId,Boolean online  );

    /**
     * gps透传
     * @param deviceLocation
     */
    void gpsTransfer(DeviceLocation deviceLocation);
}
