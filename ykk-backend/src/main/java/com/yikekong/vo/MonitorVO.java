package com.yikekong.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : xsh
 * @create : 2021-12-12 - 21:43
 * @describe: 实时监控数据
 */
@Data
public class MonitorVO implements Serializable {
    /**
     * 设备数量
     */
    private Long deviceCount;
    /**
     * 告警设备数
     */
    private Long alarmCount;
}
