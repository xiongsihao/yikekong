package com.yikekong.dto;

import lombok.Data;

import java.util.List;

/**
 * @author : xsh
 * @create : 2021-10-08 - 22:21
 * @describe:
 */
@Data
public class DeviceInfoDTO {

    private DeviceDTO device;//设备

    private List<QuotaDTO> quotaList; //指标列表

}
