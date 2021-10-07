package com.yikekong.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeviceVO implements Serializable{
    private Long id;
    private String sn;
    private String tags;
    private Boolean status;
}
