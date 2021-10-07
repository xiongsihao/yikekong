package com.yikekong.vo;

import lombok.Data;

import java.util.List;

@Data
public class PreviewVO {
    private String quotaId;
    private List<String> deviceIdList;
    private Integer type;
    private String start;
    private String end;
}
