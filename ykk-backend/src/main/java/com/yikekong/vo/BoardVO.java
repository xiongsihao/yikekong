package com.yikekong.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BoardVO implements Serializable{
    private Integer id;
    private Integer adminId;
    private String name;
    private Integer quota;
    private String device;
    private Boolean system;
}
