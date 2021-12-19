package com.yikekong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yikekong.config.MybatisRedisCache;
import com.yikekong.entity.QuotaEntity;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@CacheNamespace(implementation= MybatisRedisCache.class,eviction=MybatisRedisCache.class)
public interface QuotaMapper extends BaseMapper<QuotaEntity>{

    /**
     * 根据主题查询指标配置列表
     * @param subject
     * @return
     */
    @Select("select * from tb_quota where subject=#{subject} ")
    List<QuotaEntity> selectBySubject(String subject);


}