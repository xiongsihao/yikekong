package com.yikekong.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : xsh
 * @create : 2021-10-07 - 22:28
 * @describe: 设备DTO
 */
@Data
public class DeviceDTO implements Serializable {

    private String deviceId;//设备编号

    private Boolean alarm;// 是否告警

    private String alarmName;//告警名称

    private Integer level;//告警级别

    private Boolean online;//是否在线

    private String tag;// 标签

    private Boolean status;//开关状态

}
