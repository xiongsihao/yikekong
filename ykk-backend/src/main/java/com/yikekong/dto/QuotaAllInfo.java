package com.yikekong.dto;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

/**
 * @author : xsh
 * @create : 2021-11-29 - 1:29
 * @describe:
 */
@Data
@Measurement(name = "quota")
public class QuotaAllInfo extends QuotaInfo{
    @Column(name = "time")
    private String time;
}
