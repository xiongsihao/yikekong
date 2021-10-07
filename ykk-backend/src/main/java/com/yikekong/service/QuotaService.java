package com.yikekong.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import com.yikekong.entity.QuotaEntity;

import java.util.List;

public interface QuotaService extends IService<QuotaEntity>{

    IPage<QuotaEntity> queryPage(Long page, Long pageSize,String name);
    List<String> getAllSubject();





}
