package com.yikekong.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import com.yikekong.dto.DeviceInfoDTO;
import com.yikekong.dto.QuotaDTO;
import com.yikekong.entity.QuotaEntity;

import java.util.List;
import java.util.Map;

public interface QuotaService extends IService<QuotaEntity>{

    IPage<QuotaEntity> queryPage(Long page, Long pageSize,String name);
    List<String> getAllSubject();


    /**
     * 解析报文
     * @param topic 主题名称
     * @param payloadMap 报文内容
     * @return 设备（含指标列表）
     */
    DeviceInfoDTO analysis(String topic, Map<String, Object> payloadMap);

    /***
     * 保存指标数据到influxDb
     * @param quotaDTOList
     */
    void saveQuotaToInflux(List<QuotaDTO> quotaDTOList);
}
