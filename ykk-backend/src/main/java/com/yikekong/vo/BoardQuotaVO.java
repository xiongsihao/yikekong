package com.yikekong.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author : xsh
 * @create : 2021-12-12 - 23:09
 * @describe: 面板VO对象
 */
@Data
public class BoardQuotaVO implements Serializable {

    /**
     * x轴数据
     */
    private List<String> xdata;

    /**
     * Y轴数据
     */
    private List<BoardQuotaData> series;

    /**
     * 面板名称
     */
    private String name;
}
