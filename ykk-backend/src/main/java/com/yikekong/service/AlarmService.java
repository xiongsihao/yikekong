package com.yikekong.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yikekong.entity.AlarmEntity;

import java.util.List;

public interface AlarmService extends IService<AlarmEntity>{
    /**
     * 分页查询告警设置
     * @param page
     * @param pageSize
     * @param alarmName
     * @param quotaId
     * @return
     */
    IPage<AlarmEntity> queryPage(Long page,Long pageSize,String alarmName,Integer quotaId);

    /**
     * 获取某一指标下的所有告警设置
     * @param quotaId 指标Id
     * @return
     */
    List<AlarmEntity> getByQuotaId(Integer quotaId);







}