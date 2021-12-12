package com.yikekong.dto;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.io.Serializable;

/**
 * @author : xsh
 * @create : 2021-12-12 - 21:53
 * @describe: 趋势指标点
 */
@Data
@Measurement(name = "quota")
public class TrendPoint implements Serializable {

    /**
     * 时间
     */
    @Column(name = "time")
    private String time;

    /**
     * 时间点数据
     */
    @Column(name = "pointValue")
    private Integer pointValue;
}
