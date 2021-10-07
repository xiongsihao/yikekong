package com.yikekong.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.yikekong.entity.QuotaEntity;
import com.yikekong.mapper.QuotaMapper;
import com.yikekong.service.QuotaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuotaServiceImpl extends ServiceImpl<QuotaMapper, QuotaEntity> implements QuotaService{

    @Override
    public IPage<QuotaEntity> queryPage(Long page, Long pageSize, String name) {
        Page<QuotaEntity> pageResult = new Page<>(page,pageSize);
        LambdaQueryWrapper<QuotaEntity> wrapper = new LambdaQueryWrapper<>();
        if(!Strings.isNullOrEmpty(name)){
            wrapper.like(QuotaEntity::getName,name);
        }

        return this.page(pageResult,wrapper);
    }


    @Override
    public List<String> getAllSubject() {
        QueryWrapper<QuotaEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().select(QuotaEntity::getSubject);

        return this.list(wrapper).stream().map(q->q.getSubject()).collect(Collectors.toList());
    }










}
