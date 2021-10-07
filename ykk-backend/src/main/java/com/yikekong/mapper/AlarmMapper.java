package com.yikekong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yikekong.entity.AlarmEntity;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AlarmMapper extends BaseMapper<AlarmEntity>{
    @Results(id="alarmMap",value = {
            @Result(property = "quota",column = "quota_id",one = @One(select = "com.yikekong.mapper.QuotaMapper.selectById")),
            @Result(property = "quotaId",column = "quota_id")
    })
    @Select("select * from tb_alarm where id=#{id}")
    Page<AlarmEntity> queryPage(Page<AlarmEntity> page, Integer id);
}
