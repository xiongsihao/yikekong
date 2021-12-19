package com.yikekong.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : xsh
 * @create : 2021-12-19 - 20:59
 * @describe:
 */
@Data
public class DeviceLocation implements Serializable {
    private String deviceId;//设备编号
    private String location;//经纬度 用"."分割，维度在前
}
