package com.yikekong.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AlarmVO implements Serializable{
    private Integer id;
    private String name;
    private Integer quotaId;
    private String operator;
    private Integer threshold;
    private Integer level;
    private Integer cycle;
    private String webHook;
}
