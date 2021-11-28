package com.yikekong.dto;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

/**
 * @author : xsh
 * @create : 2021-11-29 - 0:53
 * @describe:
 */
@Data
@Measurement(name = "quota")
public class QuotaInfo {

    @Column(name = "deviceId",tag = true)
    private String deviceId;//设备id

    @Column(name = "quotaId",tag = true)
    private String quotaId;//指标id

    @Column(name = "quotaName",tag = true)
    private String quotaName;//指标名称

    @Column(name = "alarm" ,tag = true)
    private String alarm;//是否告警  0：不告警  1：告警

    @Column(name = "level" ,tag = true)
    private String level;//告警级别

    @Column(name = "alarmName" ,tag = true)
    private String alarmName;//告警名称

    @Column(name = "unit",tag = true)
    private String unit;//单位

    @Column(name = "referenceValue",tag = true)
    private String referenceValue;//参考值

    @Column(name = "value")
    private Double value;//数值指标

    @Column(name = "stringValue")
    private String stringValue;//非数值指标

}
