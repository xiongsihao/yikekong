package com.yikekong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yikekong.config.MybatisRedisCache;
import com.yikekong.entity.GPSEntity;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@CacheNamespace(implementation= MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface GpsMapper extends BaseMapper<GPSEntity>{
}
