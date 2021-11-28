package com.yikekong.vo;

import com.yikekong.dto.QuotaInfo;
import lombok.Data;

import java.util.List;

/**
 * @author : xsh
 * @create : 2021-11-29 - 2:10
 * @describe: 设备指标详情vo
 */
@Data
public class DeviceQuotaVO {

    private String deviceId;//设备编号

    private Boolean online;//在线状态

    private Integer level;//告警级别

    private List<QuotaInfo> quotaList;//指标列表

}
