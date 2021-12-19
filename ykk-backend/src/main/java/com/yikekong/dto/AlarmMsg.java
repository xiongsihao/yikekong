package com.yikekong.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : xsh
 * @create : 2021-12-19 - 22:26
 * @describe: 告警信息封装类
 */
@Data
public class AlarmMsg implements Serializable {
    private String deviceId;//设备id
    private String alarmName;//告警名称
    private String quotaName;//指标名称
    private String stringValue;//文本值
    private Double value;//指标值
    private Integer level;//告警级别
    private Boolean online; //联网状态
    private Boolean alarm;//是否告警
}
