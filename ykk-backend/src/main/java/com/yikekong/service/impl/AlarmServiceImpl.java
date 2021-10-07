package com.yikekong.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.yikekong.entity.AlarmEntity;
import com.yikekong.mapper.AlarmMapper;
import com.yikekong.service.AlarmService;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class AlarmServiceImpl extends ServiceImpl<AlarmMapper, AlarmEntity> implements AlarmService{


    @Override
    public IPage<AlarmEntity> queryPage(Long page,Long pageSize,String alarmName, Integer quotaId) {
        LambdaQueryWrapper<AlarmEntity> wrapper = new LambdaQueryWrapper<>();
        if(!Strings.isNullOrEmpty(alarmName)){
            wrapper.like(AlarmEntity::getName,alarmName);
        }
        if(quotaId != null){
            wrapper.eq(AlarmEntity::getQuotaId,quotaId);
        }
        wrapper.orderByDesc(AlarmEntity::getId);

        //wrapper.orderByDesc(AlarmEntity::getCreateTime);

        Page<AlarmEntity> pageResult = new Page<>(page,pageSize);

        return this.page(pageResult,wrapper);
    }

    @Override
    public List<AlarmEntity> getByQuotaId(Integer quotaId) {
        QueryWrapper<AlarmEntity> wrapper = new QueryWrapper<>();
        wrapper
                .lambda()
                .eq(AlarmEntity::getQuotaId,quotaId)
                .orderByDesc(AlarmEntity::getLevel);

        return this.list(wrapper);
    }



}
