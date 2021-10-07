package com.yikekong.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuotaVO implements Serializable{
    private Integer id;
    private String name;
    private String unit;
    private String subject;
    private String valueKey;
    private String snKey;
    private String webhook;
    private String referenceValue;
    private String valueType;
}
