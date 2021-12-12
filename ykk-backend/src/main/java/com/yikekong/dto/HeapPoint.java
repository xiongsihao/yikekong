package com.yikekong.dto;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.io.Serializable;

/**
 * @author : xsh
 * @create : 2021-12-12 - 22:14
 * @describe: 累积指标
 */
@Data
@Measurement(name = "quota")
public class HeapPoint implements Serializable {
    @Column(name = "deviceId")
    private String deviceId;
    @Column(name = "heapValue")
    private Double heapValue;
    @Column(name = "quotaId")
    private String quotaId;
    @Column(name = "quotaName")
    private String quotaName;
}
