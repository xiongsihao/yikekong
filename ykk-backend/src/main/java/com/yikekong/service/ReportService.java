package com.yikekong.service;

import com.yikekong.vo.PieVO;

import java.util.List;

/**
 * @author : xsh
 * @create : 2021-12-12 - 21:40
 * @describe: 报表服务
 */
public interface ReportService {


    /**
     * 设备状态分布
     * @return
     */
    List<PieVO> getStatusCollect();

}
